package io.github.ad417.year2017.day09;

import tk.vivas.adventofcode.AocUtils;

public class Day09 {
    private static int partA(String data) {
        int score = 0;
        int layers = 0;
        boolean isGarbage = false;
        boolean isIgnored = false;
        for (char c : data.toCharArray()) {
            if (isIgnored) {
                isIgnored = false;
                continue;
            }
            switch (c) {
                case '<' -> isGarbage = true;
                case '>' -> isGarbage = false;
                case '!' -> isIgnored = true;
                case '{' -> {
                    if (isGarbage) break;
                    layers++;
                    score += layers;
                }
                case '}' -> {
                    if (isGarbage) break;
                    layers--;
                }
            }
        }
        return score;
    }

    private static int partB(String data) {
        int score = 0;
        boolean isGarbage = false;
        boolean isIgnored = false;
        for (char c : data.toCharArray()) {
            if (isIgnored) {
                isIgnored = false;
                continue;
            }
            switch (c) {
                case '<' -> {
                    if (isGarbage) score++;
                    isGarbage = true;
                }
                case '>' -> isGarbage = false;
                case '!' -> isIgnored = true;
                default -> score += isGarbage ? 1 : 0;
            }
        }
        return score;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput();

        AocUtils.sendPuzzleAnswer(1, partA(data));
        AocUtils.sendPuzzleAnswer(2, partB(data));
    }
}
