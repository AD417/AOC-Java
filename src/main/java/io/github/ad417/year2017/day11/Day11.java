package io.github.ad417.year2017.day11;

import tk.vivas.adventofcode.AocUtils;

public class Day11 {
    public static int partA(String data) {
        String[] moves = data.split(",");
        int x = 0, y = 0, z = 0;

        for (String move : moves) {
            switch (move) {
                case "n" -> { x++; z--; }
                case "s" -> { x--; z++; }
                case "ne" -> { x++; y--; }
                case "nw" -> { y++; z--; }
                case "se" -> { y--; z++; }
                case "sw" -> { x--; y++; }
            }
        }
        System.out.println(x +" "+ y +" "+ z);
        System.out.println(x+y+z);
        x = Math.abs(x);
        y = Math.abs(y);
        z = Math.abs(z);
        return Math.max(Math.max(x, y), z);
    }

    public static int partB(String data) {
        String[] moves = data.split(",");
        int x = 0, y = 0, z = 0;
        int max = 0;

        for (String move : moves) {
            switch (move) {
                case "n" -> { x++; z--; }
                case "s" -> { x--; z++; }
                case "ne" -> { x++; y--; }
                case "nw" -> { y++; z--; }
                case "se" -> { y--; z++; }
                case "sw" -> { x--; y++; }
            }
            max = Math.max(Math.max(max, Math.abs(x)), Math.max(Math.abs(y), Math.abs(z)));
        }
        return max;
    }

    public static void main(String[] args) {
        // Fuck off, newline.
        String data = AocUtils.readPuzzleInput().trim();

        System.out.println(partA(data));
        AocUtils.sendPuzzleAnswer(1, partA(data));
        AocUtils.sendPuzzleAnswer(2, partB(data));
    }
}
