package io.github.ad417.year2015.day01;

import tk.vivas.adventofcode.AocUtils;

public class Day01 {
    private static int partA(String data) {
        int count = 0;
        for (char c : data.toCharArray()) {
            count += c == '(' ? 1 : -1;
        }
        return count;
    }

    private static int partB(String data) {
        char[] moves = data.toCharArray();
        int floor = 0;
        int i = 0;
        for (i = 0; floor >= 0; i++) {
            floor += moves[i] == '(' ? 1 : -1;
        }
        return i;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();

        AocUtils.sendPuzzleAnswer(1, partA(data));
        AocUtils.sendPuzzleAnswer(2, partB(data));
    }
}
