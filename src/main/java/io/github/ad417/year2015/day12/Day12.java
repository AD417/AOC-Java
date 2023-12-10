package io.github.ad417.year2015.day12;

import tk.vivas.adventofcode.AocUtils;

import java.util.regex.Matcher;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class Day12 {

    static class BadJavaParser {
        static Pattern numberHere = Pattern.compile("^-?\\d+");
        int cursor = 0;
        String data;
        public BadJavaParser(String data) {
            this.data = data;
        }

        public int getNumberSum(boolean isObject) {
            int sum = 0;
            boolean hasRed = false;
            while (true) {
                cursor++;
                char c = data.charAt(cursor);
                if (c == ']' || c == '}') break;
                if (c == '[') {
                    sum += getNumberSum(false);
                    continue;
                }
                if (c == '{') {
                    sum += getNumberSum(true);
                    continue;
                }
                String partialData = data.substring(cursor);
                if (isObject && partialData.startsWith("red")) {
                    hasRed = true;
                }
                Matcher m = numberHere.matcher(partialData);
                if (m.find()) {
                    sum += Integer.parseInt(m.group());
                    cursor += m.group().length() - 1;
                }
            }
            if (hasRed) return 0;
            return sum;
        }
    }

    private static int partA(String json) {
        return Pattern.compile("-?\\d+")
                .matcher(json)
                .results()
                .map(MatchResult::group)
                .mapToInt(Integer::parseInt)
                .sum();
    }

    private static int partB(String data) {
        BadJavaParser parser = new BadJavaParser(data);
        boolean isObject = data.charAt(0) == '{';
        return parser.getNumberSum(isObject);
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();

        AocUtils.sendPuzzleAnswer(1, partA(data));
        AocUtils.sendPuzzleAnswer(2, partB(data));
    }
}
