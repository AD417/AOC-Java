package io.github.ad417.year2017.day03;

import tk.vivas.adventofcode.AocUtils;

import java.util.Arrays;

public class Day03 {
    private enum Direction {
        LEFT,
        UP,
        RIGHT,
        DOWN
    }

    private static int partA(int data) {
        // Min distance possible -- side length, I guess.
        int min = (int) (Math.sqrt(data) + 1) / 2;
        // System.out.println(min);

        int topLeft = 4 * min * min + 1;
        int topRight = 4 * min * min - 2 * min + 1;
        int lowLeft = 4 * min * min + 2 * min + 1;
        int lowRight = (2 * min - 1) * (2 * min - 1);
        // System.out.println(topLeft + " " + topRight + "\n" + lowLeft + " " + lowRight);

        int mid;
        if (data < topRight) {
            mid = (topRight + lowRight) / 2;
        } else if (data < topLeft) {
            mid = (topLeft + topRight) / 2;
        } else if (data < lowLeft) {
            mid = (lowLeft + topLeft) / 2;
        } else {
            lowRight = (2 * min + 1) * (2 * min + 1);
            mid = (lowLeft + lowRight) / 2;
        }
        return min + Math.abs(data - mid);
    }

    private static int partB(int data) {
        int[][] grid = new int[15][15];
        int x = 7;
        int y = 7;
        Direction dir = Direction.LEFT;
        grid[7][7] = 1;
        int tileValue = 0;
        while (grid[x][y] < data) {
            switch (dir) {
                case UP -> {
                    y++;
                    if (grid[x-1][y] == 0) dir = Direction.LEFT;
                }
                case LEFT -> {
                    x--;
                    if (grid[x][y-1] == 0) dir = Direction.DOWN;
                }
                case DOWN -> {
                    y--;
                    if (grid[x+1][y] == 0) dir = Direction.RIGHT;
                }
                case RIGHT -> {
                    x++;
                    if (grid[x][y+1] == 0) dir = Direction.UP;
                }
            }
            tileValue = 0;
            for (int dx = x-1; dx <= x+1; dx++) {
                for (int dy = y-1; dy <= y+1; dy++) {
                    tileValue += grid[dx][dy];
                }
            }
            grid[x][y] = tileValue;
        }
        return tileValue;
    }

    public static void main(String[] args) {
        String input = AocUtils.readPuzzleInput().strip();
        int data = Integer.parseInt(input);

        AocUtils.sendPuzzleAnswer(1, partA(data));
        AocUtils.sendPuzzleAnswer(2, partB(data));
    }

}

