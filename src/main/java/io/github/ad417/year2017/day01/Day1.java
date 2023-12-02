package io.github.ad417.year2017.day01;

import tk.vivas.adventofcode.AocUtils;

public class Day1 {

    private static int charToNum(char x) {
        return Integer.parseInt(String.valueOf(x));
    }

    private static int partA(String[] lines) {
        int out = 0;
        for (String line : lines) {
            int lineTotal = 0;
            for (int i = 0; i < line.length()-1; i++) {
                if (line.charAt(i) == line.charAt(i+1)) {
                    lineTotal += charToNum(line.charAt(i));
                }
            }
            if (line.charAt(line.length()-1) == line.charAt(0)) {
                lineTotal += charToNum(line.charAt(0));
            }
            out += lineTotal;
        }
        return out;
    }

    private static int partB(String[] lines) {
        int out = 0;
        for (String line : lines) {
            int lineTotal = 0;
            int halfLine = line.length() / 2;
            for (int i = 0; i < halfLine; i++) {
                if (line.charAt(i) == line.charAt(i + halfLine)) {
                    lineTotal += charToNum(line.charAt(i)) * 2;
                }
            }
            out += lineTotal;
        }
        return out;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput();
        String[] lines = data.lines().toArray(String[]::new);

        System.out.println(partA(lines));
        AocUtils.sendPuzzleAnswer(1, partA(lines));
        System.out.println(partB(lines));
        AocUtils.sendPuzzleAnswer(2, partB(lines));
    }

}
