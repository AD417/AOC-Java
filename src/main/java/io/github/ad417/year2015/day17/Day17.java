package io.github.ad417.year2015.day17;

import tk.vivas.adventofcode.AocUtils;

import java.util.*;

public class Day17 {
    private static int partA(List<Integer> containers) {
        int ways = 0;
        int maxMask = 2 << containers.size() - 1;
        for (int i = 0; i < maxMask; i++) {
            int sum = 0;
            for (int k = 0; k < containers.size(); k++) {
                if ((i & (1 << k)) == 0) continue;
                sum += containers.get(k);
            }
            if (sum == 150) ways++;
        }
        return ways;
    }

    private static int partB(List<Integer> containers) {
        int minAmount = Integer.MAX_VALUE;
        int ways = 0;
        int maxMask = 2 << containers.size() - 1;
        for (int i = 0; i < maxMask; i++) {
            int sum = 0;
            int amount = 0;
            for (int k = 0; k < containers.size(); k++) {
                if ((i & (1 << k)) == 0) continue;
                sum += containers.get(k);
                amount++;
            }
            if (sum == 150) {
                if (minAmount > amount) {
                    minAmount = amount;
                    ways = 0;
                }
                if (minAmount == amount) ways++;
            }
        }
        return ways;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        List<Integer> containers = data.lines().map(Integer::valueOf).toList();

        AocUtils.sendPuzzleAnswer(1, partA(containers));
        AocUtils.sendPuzzleAnswer(2, partB(containers));
    }

}
