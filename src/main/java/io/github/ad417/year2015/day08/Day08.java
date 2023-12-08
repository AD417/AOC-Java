package io.github.ad417.year2015.day08;

import tk.vivas.adventofcode.AocUtils;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class Day08 {
    private static int partA(String[] lines) {
        Pattern escaped = Pattern.compile("(?<=\\\\)(x\\d\\d|\"|'|\\\\)");
        int escapedCount = 0;
        for (String line : lines) {
            escapedCount += 2;
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == '\\') {
                    escapedCount++;
                    if (line.charAt(++i) != 'x') continue;
                    escapedCount += 2;
                    i += 2;
                }
            }
        }
        return escapedCount;
    }

    private static int partB(String[] lines) {
        int added = 0;
        for (String line : lines) {
            added += 2;
            for (char c : line.toCharArray()) {
                if (c == '\\' || c == '\'' || c == '"') added++;
            }
        }
        return added;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        String[] lines = data.split("\n");

        AocUtils.sendPuzzleAnswer(1, partA(lines));
        AocUtils.sendPuzzleAnswer(2, partB(lines));
    }
}
