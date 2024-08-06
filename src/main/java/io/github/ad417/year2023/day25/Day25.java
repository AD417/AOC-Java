package io.github.ad417.year2023.day25;

import tk.vivas.adventofcode.AocUtils;

import java.util.*;

public class Day25 {
    private static final Random RNG = new Random();

    static class Connections extends HashMap<HashSet<String>, Integer> {
        public Connections() {
            super();
        }
        public Connections(String name) {
            super();
            HashSet<String> connection = new HashSet<>();
            connection.add(name);
            this.put(connection, 1);
        }
        public Connections(Connections other) {
            super();
            putAll(other);
        }
        public void combineWith(Connections other) {
            for (HashSet<String> node : other.keySet()) {
                int val = this.computeIfAbsent(node, k -> 0);
                this.put(node, val + other.get(node));
            }
        }

        public void updateConnection(HashSet<String> first, HashSet<String> second, HashSet<String> next) {
            Integer val1 = this.remove(first);
            Integer val2 = this.remove(second);
            if (val1 == null && val2 == null) return;
            val1 = (val1 == null) ? 0 : val1;
            val2 = (val2 == null) ? 0 : val2;
            this.put(next, val1 + val2);
        }

        public HashSet<String> randomConnection() {
            return this.keySet().stream().skip(RNG.nextInt(size())).findFirst().orElseThrow();
        }
    }

    private static HashSet<String> makeSet(String value) {
        HashSet<String> out = new HashSet<>();
        out.add(value);
        return out;
    }
    private static HashMap<HashSet<String>, Connections> getWiring(String data) {
        // This is the creation of a very involved graph -- probably too
        // complicated for its intended purpose.
        // Effectively, it's a multigraph, since multiple "Nodes" can have
        // several edges between them, but as edges are contracted together,
        // even the nodes will be multiple nodes. "Yo dawg..."
        //
        // The reason all the keys are HashSets of Strings is because they
        // are actually sets containing a bunch of nodes. The edges within
        // those nodes are not shown, but there may be multiple edges between
        // various clusters.
        //
        // The graph is too sparse to make an adjacency matrix worthwhile.
        // I guess a LinkedList with an implicit occurrence count might be
        // better?
        String[] lines = data.split("\n");

        HashMap<HashSet<String>, Connections> wiring = new HashMap<>();

        for (String line : lines) {
            // Use Regex on the predictable format of these lines.
            List<String> components = Arrays.stream(line.split(":? ")).toList();
            // The stuff before the colon maps to the stuff after, and vice versa.
            HashSet<String> main = makeSet(components.get(0));
            Connections mainAsConnector = new Connections(components.get(0));
            Connections mainCon = wiring.computeIfAbsent(main, k -> new Connections());
            for (int i = 1; i < components.size(); i++) {
                HashSet<String> later = makeSet(components.get(i));
                mainCon.combineWith(new Connections(components.get(i)));
                Connections subCon = wiring.computeIfAbsent(later, k -> new Connections());
                subCon.combineWith(mainAsConnector);
            }
        }
        return wiring;
    }

    private static HashMap<HashSet<String>, Connections> copyWiring(HashMap<HashSet<String>, Connections> wiring) {
        HashMap<HashSet<String>, Connections> copy = new HashMap<>();
        for (HashSet<String> component : wiring.keySet()) {
            copy.put(component, new Connections(wiring.get(component)));
        }
        return copy;
    }

    private static void contractEdge(
            HashMap<HashSet<String>, Connections> wiring,
            HashSet<String> first, HashSet<String> second
    ) {
        // Take two nodes and combine them into a single "meta-node" in the
        // provided wiring diagram. This works in-place on the wiring map.
        // We merge all the edge data for the two merged nodes together.
        // Then, we remove all references to the pre-merged nodes. We then
        // insert this new combo node in-place in the wiring map. Hopefully
        // it's a cop,y and we're not messing up the original map.
        HashSet<String> combo = new HashSet<>(first);
        combo.addAll(second);
        Connections comboConnection = new Connections(wiring.remove(first));
        comboConnection.remove(second);
        comboConnection.combineWith(wiring.remove(second));
        comboConnection.remove(first);

        for (HashSet<String> connector : comboConnection.keySet()) {
            wiring.get(connector).updateConnection(first, second, combo);
        }
        wiring.put(combo, comboConnection);
    }

    private static void contract(HashMap<HashSet<String>, Connections> wiring, int vertexCount) {
        // Randomly pick nodes to contract until we reduce the graph to having
        // only `vertexCount` nodes.
        // The RNG is done by just taking the list of nodes, skipping through
        // many of them, and then taking the next one after the skip.
        // We then choose an arbitrary node connected to this one. The odds
        // that we happened to choose one of the three wires we need to cut is
        // relatively low (<0.1% initially), so we can safely do this for a
        // while.
        // Eventually, when we reach the end, we have two groups, separated by
        // some number of cuttable wires (edges). We return this data.

        // See also: https://en.wikipedia.org/wiki/Karger%27s_algorithm
        while (wiring.size() > vertexCount) {
            HashSet<String> first = wiring.keySet().stream()
                    .skip(RNG.nextInt(wiring.size()))
                    .findFirst().orElseThrow();
            HashSet<String> second = wiring.get(first).randomConnection();

            contractEdge(wiring, first, second);
        }
    }

    private static int partA(HashMap<HashSet<String>, Connections> wiring) {
        // Baby's first RNG algorithm. Specifically, Karger's Algorithm, since
        // I was too lazy to implement the Karger-Stein optimization.
        //
        // See `contract()` for more information.
        for (int tries = 1; true; tries++) {
            tries++;
            HashMap<HashSet<String>, Connections> wireSim = copyWiring(wiring);
            contract(wireSim, 2);

            Iterator<HashSet<String>> groupGetter = wireSim.keySet().iterator();
            HashSet<String> first = groupGetter.next();
            HashSet<String> second = groupGetter.next();
            // We basically keep throwing the wiring map at the contractor
            // until it gives us the correct answer.
            // In the original algorithm this can take N^2 times for N nodes,
            // but we know exactly when we are done because of the constants
            // of the problem.
            if (wireSim.get(first).get(second) == 3) {
                System.out.println("Solution took " + tries + " tries!");
                return first.size() * second.size();
            }
        }
    }

    private static int partB(String[] lines) {
        // The last problem. Jokingly, I return 49, because that's how many
        // stars I should have.
        return 49;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        /*data = """
                jqt: rhn xhk nvd
                rsh: frs pzl lsr
                xhk: hfx
                cmg: qnr nvd lhk bvb
                rhn: xhk bvb hfx
                bvb: xhk hfx
                pzl: lsr hfx nvd
                qnr: nvd
                ntq: jqt hfx bvb xhk
                nvd: lhk
                lsr: lhk
                rzs: qnr cmg lsr rsh
                frs: qnr lhk lsr""";*/
        HashMap<HashSet<String>, Connections> wiring = getWiring(data);

        System.out.println(partA(wiring));
    }

}
