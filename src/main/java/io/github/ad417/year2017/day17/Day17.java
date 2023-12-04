package io.github.ad417.year2017.day17;

import tk.vivas.adventofcode.AocUtils;

import java.util.ArrayList;

public class Day17 {
    private static int partA(int stepSize) {
        ArrayList<Integer> ints = new ArrayList<>();
        ints.add(0);
        int pointer = 0;
        for (int i = 1; i <= 2017; i++) {
            pointer += stepSize;
            pointer %= ints.size();
            ints.add(pointer+1, i);
            pointer++;
        }
        pointer++;
        if (pointer >= ints.size()) pointer = 0;
        return ints.get(pointer);
    }

    private static int partB(int stepSize) {
        int pointer = 0;
        int size = 1;

        int afterZero = 0;
        for (int i = 1; i < 50_000_000; i++) {
            pointer += stepSize;
            pointer %= size;
            if (pointer == 0) afterZero = i;
            size++;
            pointer++;
        }
        return afterZero;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        int stepSize = Integer.parseInt(data);

        AocUtils.sendPuzzleAnswer(1, partA(stepSize));
        AocUtils.sendPuzzleAnswer(2, partB(stepSize));
    }

}
