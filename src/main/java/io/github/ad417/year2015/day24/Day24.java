package io.github.ad417.year2015.day24;

import tk.vivas.adventofcode.AocUtils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Day24 {
    private static long compartmentalize(List<Integer> presents, int compartments) {
        int balanced = presents.stream().mapToInt(Integer::intValue).sum() / compartments;
        HashSet<List<Integer>> valid = new HashSet<>();

        List<List<Integer>> partialConfigs = new LinkedList<>();
        presents.stream().map(List::of).forEach(partialConfigs::add);
        while (valid.isEmpty()) {
            List<List<Integer>> nextPartialConfigs = new LinkedList<>();
            for (List<Integer> config : partialConfigs) {
                int total = config.stream().mapToInt(Integer::intValue).sum();
                if (total == balanced) valid.add(config);
                if (total >= balanced) continue;

                int largest = config.get(config.size() - 1);
                for (int present : presents) {
                    if (present <= largest) continue;
                    ;
                    List<Integer> nextConfig = new LinkedList<>(config);
                    nextConfig.add(present);
                    nextPartialConfigs.add(nextConfig);
                }
            }
            partialConfigs = nextPartialConfigs;
        }
        return valid.stream()
                .mapToLong(config -> config.stream().mapToLong(Integer::longValue).reduce(1, (x, y) -> x * y))
                .min().orElseThrow();
    }
    private static long partA(List<Integer> presents) {
        return compartmentalize(presents, 3);
    }

    private static long partB(List<Integer> presents) {
        return compartmentalize(presents, 4);
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        /*data = """
                1
                2
                3
                4
                5
                7
                8
                9
                10
                11""";*/
        List<Integer> presents = data.lines().map(Integer::parseInt).toList();

        AocUtils.sendPuzzleAnswer(1, partA(presents));
        AocUtils.sendPuzzleAnswer(2, partB(presents));
    }
}
