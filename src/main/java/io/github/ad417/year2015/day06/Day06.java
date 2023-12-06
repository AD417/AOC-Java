package io.github.ad417.year2015.day06;

import tk.vivas.adventofcode.AocUtils;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day06 {
    private static void setAll(boolean[][] world, int rowStart, int colStart, int rowEnd, int colEnd, boolean value) {
        for (int row = rowStart; row <= rowEnd; row++) {
            for (int col = colStart; col <= colEnd; col++) {
                world[row][col] = value;
            }
        }
    }

    private static void toggle(boolean[][] world, int rowStart, int colStart, int rowEnd, int colEnd) {
        for (int row = rowStart; row <= rowEnd; row++) {
            for (int col = colStart; col <= colEnd; col++) {
                world[row][col] = !world[row][col];
            }
        }
    }

    private static int partA(String[] lines) {
        Pattern format = Pattern.compile("(turn on|turn off|toggle) (\\d+),(\\d+) through (\\d+),(\\d+)");
        boolean[][] world = new boolean[1000][1000];
        for (String line : lines) {
            Matcher m = format.matcher(line);
            if (!m.find()) continue;
            int rowStart = Integer.parseInt(m.group(2));
            int colStart = Integer.parseInt(m.group(3));
            int rowEnd = Integer.parseInt(m.group(4));
            int colEnd = Integer.parseInt(m.group(5));
            switch (m.group(1)) {
                case "turn on" -> setAll(world, rowStart, colStart, rowEnd, colEnd, true);
                case "turn off" -> setAll(world, rowStart, colStart, rowEnd, colEnd, false);
                case "toggle" -> toggle(world, rowStart, colStart, rowEnd, colEnd);
            }
        }
        int count = 0;
        for (boolean[] row : world) {
            for (boolean pixel : row) {
                if (pixel) count++;
            }
        }
        return count;
    }

    private static void incAll(int[][] world, int rowStart, int colStart, int rowEnd, int colEnd) {
        for (int row = rowStart; row <= rowEnd; row++) {
            for (int col = colStart; col <= colEnd; col++) {
                world[row][col]++;
            }
        }
    }
    private static void decAll(int[][] world, int rowStart, int colStart, int rowEnd, int colEnd) {
        for (int row = rowStart; row <= rowEnd; row++) {
            for (int col = colStart; col <= colEnd; col++) {
                world[row][col]--;
                if (world[row][col] < 0) world[row][col] = 0;
            }
        }
    }
    private static void toggleAll(int[][] world, int rowStart, int colStart, int rowEnd, int colEnd) {
        for (int row = rowStart; row <= rowEnd; row++) {
            for (int col = colStart; col <= colEnd; col++) {
                world[row][col] += 2;
            }
        }
    }

    private static int partB(String[] lines) {
        Pattern format = Pattern.compile("(turn on|turn off|toggle) (\\d+),(\\d+) through (\\d+),(\\d+)");
        int[][] world = new int[1000][1000];
        for (String line : lines) {
            Matcher m = format.matcher(line);
            if (!m.find()) continue;
            int rowStart = Integer.parseInt(m.group(2));
            int colStart = Integer.parseInt(m.group(3));
            int rowEnd = Integer.parseInt(m.group(4));
            int colEnd = Integer.parseInt(m.group(5));
            switch (m.group(1)) {
                case "turn on" -> incAll(world, rowStart, colStart, rowEnd, colEnd);
                case "turn off" -> decAll(world, rowStart, colStart, rowEnd, colEnd);
                case "toggle" -> toggleAll(world, rowStart, colStart, rowEnd, colEnd);
            }
        }
        int count = 0;
        for (int[] row : world) {
            for (int pixel : row) {
                count += pixel;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        String[] lines = data.split("\n");

        AocUtils.sendPuzzleAnswer(1, partA(lines));
        AocUtils.sendPuzzleAnswer(2, partB(lines));
    }
}
