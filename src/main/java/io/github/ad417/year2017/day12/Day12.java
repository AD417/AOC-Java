package io.github.ad417.year2017.day12;

import tk.vivas.adventofcode.AocUtils;

import java.util.*;

public class Day12 {
    private static HashMap<Integer, List<Integer>> createGraph(String data) {
        HashMap<Integer, List<Integer>> graph = new HashMap<>();

        data.lines().forEach(line -> {
            int id = Integer.parseInt(line.split(" <-> ")[0]);
            List<Integer> neighbors = Arrays.stream(line.split(" <-> ")[1].split(", "))
                    .map(Integer::parseInt)
                    .toList();

            graph.put(id, neighbors);
        });

        return graph;
    }
    private static int partA(HashMap<Integer, List<Integer>> graph) {
        Set<Integer> seen = new HashSet<>();
        Queue<Integer> toVisit = new LinkedList<>();

        toVisit.add(0);
        while (!toVisit.isEmpty()) {
            int id = toVisit.poll();
            if (seen.contains(id)) continue;
            seen.add(id);

            toVisit.addAll(graph.get(id));
        }

        return seen.size();
    }

    private static int partB(HashMap<Integer, List<Integer>> graph) {
        int groups = 0;

        Set<Integer> seen = new HashSet<>();
        Queue<Integer> toVisit = new LinkedList<>();

        for (int process = 0; graph.containsKey(process); process++) {
            if (seen.contains(process)) continue;
            groups++;

            toVisit.add(process);
            while (!toVisit.isEmpty()) {
                int id = toVisit.poll();
                if (seen.contains(id)) continue;
                seen.add(id);

                toVisit.addAll(graph.get(id));
            }
        }
        return groups;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput();
        HashMap<Integer, List<Integer>> graph = createGraph(data);

        AocUtils.sendPuzzleAnswer(1, partA(graph));
        AocUtils.sendPuzzleAnswer(2, partB(graph));
    }
}
