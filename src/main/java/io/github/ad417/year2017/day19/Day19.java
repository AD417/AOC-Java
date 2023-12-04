package io.github.ad417.year2017.day19;

import tk.vivas.adventofcode.AocUtils;

import java.util.HashMap;
import java.util.Set;

public class Day19 {
    enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT,
    }
    record Coordinate(int row, int col) {
        Coordinate add(Coordinate other) {
            return new Coordinate(this.row + other.row, this.col + other.col);
        }
    }


    private static String partA(char[][] grid) {
        Coordinate pos = new Coordinate(0,0);
        for (int i = 0; i < grid[0].length; i++) {
            if (grid[0][i] == '|') {
                pos = new Coordinate(0, i);
                break;
            }
        }
        Direction dir = Direction.DOWN;

        StringBuilder sb = new StringBuilder();

        while (true) {
            switch (grid[pos.row][pos.col]) {
                case ' ' -> {
                    return sb.toString();
                }
                case '+' -> {
                    if (dir == Direction.DOWN || dir == Direction.UP) {
                        if (grid[pos.row][pos.col-1] == ' ') dir = Direction.RIGHT;
                        else dir = Direction.LEFT;
                    } else {
                        if (grid[pos.row-1][pos.col] == ' ') dir = Direction.DOWN;
                        else dir = Direction.UP;
                    }
                }
                case '|', '-' -> {}
                default -> sb.append(grid[pos.row][pos.col]);
            }
            switch (dir) {
                case UP -> pos = pos.add(new Coordinate(-1, 0));
                case DOWN -> pos = pos.add(new Coordinate(1, 0));
                case LEFT -> pos = pos.add(new Coordinate(0, -1));
                case RIGHT -> pos = pos.add(new Coordinate(0, 1));
            }
        }
    }

    private static int partB(char[][] grid) {
        Coordinate pos = new Coordinate(0,0);
        for (int i = 0; i < grid[0].length; i++) {
            if (grid[0][i] == '|') {
                pos = new Coordinate(0, i);
                break;
            }
        }
        Direction dir = Direction.DOWN;

        int distance = 0;

        while (true) {
            switch (grid[pos.row][pos.col]) {
                case ' ' -> {
                    return distance;
                }
                case '+' -> {
                    if (dir == Direction.DOWN || dir == Direction.UP) {
                        if (grid[pos.row][pos.col-1] == ' ') dir = Direction.RIGHT;
                        else dir = Direction.LEFT;
                    } else {
                        if (grid[pos.row-1][pos.col] == ' ') dir = Direction.DOWN;
                        else dir = Direction.UP;
                    }
                }
            }
            switch (dir) {
                case UP -> pos = pos.add(new Coordinate(-1, 0));
                case DOWN -> pos = pos.add(new Coordinate(1, 0));
                case LEFT -> pos = pos.add(new Coordinate(0, -1));
                case RIGHT -> pos = pos.add(new Coordinate(0, 1));
            }
            distance++;
        }
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput();
        char[][] grid = data.lines().map(String::toCharArray).toArray(char[][]::new);

        AocUtils.sendPuzzleAnswer(1, partA(grid));
        AocUtils.sendPuzzleAnswer(2, partB(grid));
    }

}
