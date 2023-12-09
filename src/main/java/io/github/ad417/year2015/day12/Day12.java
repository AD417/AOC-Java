package io.github.ad417.year2015.day12;

import tk.vivas.adventofcode.AocUtils;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class Day12 {
    private static int partA(String json) {
        return Pattern.compile("-?\\d+")
                .matcher(json)
                .results()
                .map(MatchResult::group)
                .mapToInt(Integer::parseInt)
                .sum();
    }

    private static int partB(String[] lines) {
        return 0;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();

        AocUtils.sendPuzzleAnswer(1, partA(data));
        // AocUtils.sendPuzzleAnswer(2, partB(lines));
    }
}
