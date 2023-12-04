package io.github.ad417.year2017.day15;

import javafx.css.Match;
import tk.vivas.adventofcode.AocUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day15 {
    private static int partA(String data) {
        int matches = 0;
        Matcher m = Pattern.compile("\\d+").matcher(data);
        long genA = 0, genB = 0;
        if (m.find()) genA = Integer.parseInt(m.group());
        if (m.find()) genB = Integer.parseInt(m.group());

        for (int i = 0; i < 40_000_000; i++) {
            genA *= 16807;
            genA %= Integer.MAX_VALUE;
            genB *= 48271;
            genB %= Integer.MAX_VALUE;

            if ((genA & 0xFFFF) == (genB & 0xFFFF)) matches++;
        }
        return matches;
    }

    private static int partB(String data) {
        int matches = 0;
        Matcher m = Pattern.compile("\\d+").matcher(data);
        long genA = 0, genB = 0;
        if (m.find()) genA = Integer.parseInt(m.group());
        if (m.find()) genB = Integer.parseInt(m.group());

        for (int i = 0; i < 5_000_000; i++) {
            do {
                genA *= 16807;
                genA %= Integer.MAX_VALUE;
            } while ((genA & 0b11) != 0);
            do {
                genB *= 48271;
                genB %= Integer.MAX_VALUE;
            } while ((genB & 0b111) != 0);

            if ((genA & 0xFFFF) == (genB & 0xFFFF)) matches++;
        }
        return matches;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput();

        AocUtils.sendPuzzleAnswer(1, partA(data));
        AocUtils.sendPuzzleAnswer(2, partB(data));
    }

}
