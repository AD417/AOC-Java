package io.github.ad417.year2017.generic;

import tk.vivas.adventofcode.AocUtils;

public class BlankDay {
    private static int partA(String[] lines) {
        return 0;
    }

    private static int partB(String[] lines) {
        return 0;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput();
        String[] lines = data.lines().toArray(String[]::new);

        AocUtils.sendPuzzleAnswer(1, partA(lines));
        AocUtils.sendPuzzleAnswer(2, partB(lines));
    }

}
