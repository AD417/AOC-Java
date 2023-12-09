package io.github.ad417.year2015.day10;

import tk.vivas.adventofcode.AocUtils;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Day10 {
    private static int runFor(String data, int times) {
        String iteration = data;
        for (int i = 0; i < times; i++) {
            StringBuilder sb = new StringBuilder();
            char last = iteration.charAt(0);
            int count = 1;
            for (int k = 1; k < iteration.length(); k++) {
                char c = iteration.charAt(k);
                if (c != last) {
                    sb.append(count);
                    count = 0;
                    sb.append(last);
                    last = c;
                }
                count++;
            }
            sb.append(count);
            sb.append(last);
            iteration = sb.toString();
        }
        return iteration.length();
    }
    private static int partA(String data) {
        return runFor(data, 40);
    }

    private static int partB(String data) {
        return runFor(data, 50);
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();

        AocUtils.sendPuzzleAnswer(1, partA(data));
        AocUtils.sendPuzzleAnswer(2, partB(data));
    }
}
