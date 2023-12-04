package io.github.ad417.year2017.day13;

import tk.vivas.adventofcode.AocUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day13 {

    record Layer(int depth, int range) {
        public static Layer makeLayer(String line) {
                String[] parts = line.split(": ");
                int depth = Integer.parseInt(parts[0]);
                int range = Integer.parseInt(parts[1]);
                return new Layer(depth, range);

        }

        public int actualRange() {
                return (range - 1) * 2;
        }

        public boolean isCaught(int offset) {
                return (offset + depth) % actualRange() == 0;
        }

        public int severity() {
                return range * depth;
            }
    }

    private static List<Layer> makeFirewall(String data) {
        List<Layer> firewall = new ArrayList<>();

        data.trim().lines().forEach(line -> firewall.add(Layer.makeLayer(line)));

        return firewall;
    }

    private static int partA(String data) {
        List<Layer> firewall = makeFirewall(data);
        int sum = 0;
        for (Layer layer : firewall) {
            if (layer.isCaught(0)) sum += layer.severity();
        }
        return sum;
    }

    private static int partB(String data) {
        List<Layer> firewall = makeFirewall(data);
        return IntStream.range(0, Integer.MAX_VALUE)
                .filter(x -> firewall.stream().noneMatch(layer -> layer.isCaught(x)))
                .findFirst().orElseThrow();
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();

        AocUtils.sendPuzzleAnswer(1, partA(data));
        AocUtils.sendPuzzleAnswer(2, partB(data));
    }
}
