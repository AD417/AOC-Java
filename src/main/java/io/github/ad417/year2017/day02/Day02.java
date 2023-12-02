package io.github.ad417.year2017.day02;

import tk.vivas.adventofcode.AocUtils;

import java.util.Arrays;

public class Day02 {
    private static int partA(Integer[][] data) {
        int checksum = 0;
        for (Integer[] line : data) {
            Integer max = Arrays.stream(line).max(Integer::compare).get();
            Integer min = Arrays.stream(line).min(Integer::compare).orElse(0);
            checksum += max - min;
        }
        return checksum;
    }

    private static int partB(Integer[][] data) {
        int checksum = 0;
        for (Integer[] line : data) {
            for (int i = 0; i < line.length; i++) {
                for (int k = 0; k < line.length; k++) {
                    if (i == k) continue;
                    if (line[i] % line[k] != 0) continue;
                    checksum += line[i] / line[k];
                    break;
                }
            }
        }
        return checksum;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput();
        Integer[][] lines = data.lines()
                .map(line -> Arrays.stream(line.split("[ \t]")).map(Integer::parseInt).toArray(Integer[]::new))
                .toArray(Integer[][]::new);

        AocUtils.sendPuzzleAnswer(1, partA(lines));
        AocUtils.sendPuzzleAnswer(2, partB(lines));
    }

}

