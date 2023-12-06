package io.github.ad417.year2015.day05;

import tk.vivas.adventofcode.AocUtils;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day05 {
    private static int partA(String[] lines) {
        Pattern repeats = Pattern.compile("(\\w)\\1");
        Pattern bad = Pattern.compile("ab|cd|pq|xy");
        Pattern vowel = Pattern.compile("[aeiou]");

        return (int) Arrays.stream(lines)
                .filter(line -> repeats.matcher(line).find())
                .filter(line -> !bad.matcher(line).find())
                .filter(line -> {
                    Matcher m = vowel.matcher(line);
                    return m.find() && m.find() && m.find();
                })
                .count();
    }

    private static int partB(String[] lines) {
        Pattern twoRepeats = Pattern.compile("(\\w\\w).*\\1");
        Pattern repeatBetween = Pattern.compile("(\\w)\\w\\1");

        return (int) Arrays.stream(lines)
                .filter(line -> twoRepeats.matcher(line).find())
                .filter(line -> repeatBetween.matcher(line).find())
                .count();
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        String[] lines = data.lines().toArray(String[]::new);

        AocUtils.sendPuzzleAnswer(1, partA(lines));
        AocUtils.sendPuzzleAnswer(2, partB(lines));
    }
}
