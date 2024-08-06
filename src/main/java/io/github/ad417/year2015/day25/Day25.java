package io.github.ad417.year2015.day25;

import tk.vivas.adventofcode.AocUtils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Day25 {
    private static enum Direction {
        DOWN_RIGHT,
        UP_LEFT,
    }
    private static int partA(int[] values) {
        System.out.println(Arrays.toString(values));
        int targetRow = values[0];
        int targetCol = values[1];

        int row = 1;
        System.out.println(row == targetRow);
        int col = 1;
        System.out.println(col == targetCol);
        Direction dir = Direction.DOWN_RIGHT;
        long code = 20151125;

        while (row != targetRow || col != targetCol) {
            if (dir == Direction.DOWN_RIGHT) {
                row++;
                if (col == 1) dir = Direction.UP_LEFT;
                else col--;
            } else {
                col++;
                if (row == 1) dir = Direction.DOWN_RIGHT;
                else row--;
            }
            code *= 252533;
            code %= 33554393;
        }
        return (int) code;
    }

    private static int partB(int[] values) {
        return 49;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        int[] values = Pattern.compile("\\d+")
                .matcher(data).results()
                .mapToInt(m -> Integer.parseInt(m.group()))
                .toArray();

        AocUtils.sendPuzzleAnswer(1, partA(values));
        AocUtils.sendPuzzleAnswer(2, partB(values));
    }
}
