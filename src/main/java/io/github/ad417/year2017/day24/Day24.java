package io.github.ad417.year2017.day24;

import tk.vivas.adventofcode.AocUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Day24 {

    static class Connector {
        final int first;
        final int second;
        int connectedBy = -1;
        public Connector(int first, int second) {
            this.first = first;
            this.second = second;
        }
        public static Connector create(String line) {
            int first = Integer.parseInt(line.split("/")[0]);
            int second = Integer.parseInt(line.split("/")[1]);
            return new Connector(first, second);
        }
        int strength() {
            return first + second;
        }

        void connectTo(Connector other) {
            if (this.connectedBy == this.first) {
                other.connectedBy = this.second;
            } else {
                other.connectedBy = this.first;
            }
        }

        void makeStart() {
            this.connectedBy = 0;
        }

        boolean connectsTo(Connector other) {
            int free = (connectedBy == first ? second : first);
            if (free == other.first) return true;
            return free == other.second;
        }

        @Override
        public String toString() {
            return connectedBy + "/" + first + "/" + second;
        }
    }

    record BridgeData(int length, int strength) implements Comparable<BridgeData> {

        @Override
        public int compareTo(BridgeData other) {
            int out = this.length - other.length;
            if (out == 0) out = this.strength - other.strength;
            return out;
        }

        public boolean equals(BridgeData other) {
            return this.length == other.length && this.strength == other.strength;
        }
    }

    private static int partA(List<Connector> ports ) {
        LinkedList<Connector> bridge = new LinkedList<>();
        Connector c = new Connector(0,0);
        c.makeStart();
        bridge.add(c);
        class MaxStrengthGetter {
            static int get(List<Connector> bridge, List<Connector> remaining) {
                Connector last = bridge.get(bridge.size() - 1);
                int max = -1;
                for (Connector connector : remaining) {
                    if (!last.connectsTo(connector)) continue;
                    last.connectTo(connector);
                    LinkedList<Connector> newBridge = new LinkedList<>(bridge);
                    newBridge.add(connector);
                    LinkedList<Connector> newRemaining = new LinkedList<>(remaining);
                    newRemaining.remove(connector);
                    int maxWithConnector = get(newBridge, newRemaining);
                    if (max < maxWithConnector) max = maxWithConnector;
                }
                if (max == -1) {
                    int str = bridge.stream().mapToInt(Connector::strength).sum();
                    // System.out.println(str + " " + bridge);
                    return str;
                }
                return max;
            }
        }
        return MaxStrengthGetter.get(bridge, ports);
    }

    private static int partB(List<Connector> ports) {
        LinkedList<Connector> bridge = new LinkedList<>();
        Connector c = new Connector(0,0);
        c.makeStart();
        bridge.add(c);
        class MaxStrengthGetter {
            static BridgeData get(List<Connector> bridge, List<Connector> remaining) {
                Connector last = bridge.get(bridge.size() - 1);
                BridgeData best = new BridgeData(0,0);
                for (Connector connector : remaining) {
                    if (!last.connectsTo(connector)) continue;

                    last.connectTo(connector);
                    LinkedList<Connector> newBridge = new LinkedList<>(bridge);
                    newBridge.add(connector);
                    LinkedList<Connector> newRemaining = new LinkedList<>(remaining);
                    newRemaining.remove(connector);

                    BridgeData maxWithConnector = get(newBridge, newRemaining);
                    if (best.compareTo(maxWithConnector) < 0) best = maxWithConnector;
                }
                if (best.equals(new BridgeData(0,0))) {
                    int length = bridge.size();
                    int strength = bridge.stream().mapToInt(Connector::strength).sum();
                    // System.out.println(str + " " + bridge);
                    return new BridgeData(length, strength);
                }
                return best;
            }
        }
        return MaxStrengthGetter.get(bridge, ports).strength;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        List<Connector> ports = data.lines().map(Connector::create).collect(Collectors.toCollection(ArrayList::new));

        AocUtils.sendPuzzleAnswer(1, partA(ports));
        AocUtils.sendPuzzleAnswer(2, partB(ports));
    }
}
