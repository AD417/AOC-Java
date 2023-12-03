package io.github.ad417.year2023.day03;

import tk.vivas.adventofcode.AocUtils;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day03 {
    private record Position(int row, int col) {}

    private static boolean isNumber(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static HashMap<Position, String> createGrid(String data) {
        String[] lines = data.split("\n");

        HashMap<Position, String> map = new HashMap<>();
        // Match all numbers in full, or anything that's not a period.
        Pattern pattern = Pattern.compile("(?<!\\d)\\d+|[^.]");
        for (int row = 0; row < lines.length; row++) {
            Matcher matches = pattern.matcher(lines[row]);
            while (matches.find()) {
                int col = matches.start();
                String val = matches.group();
                map.put(new Position(row, col), val);
            }
        }
        return map;
    }

    private static int partA(HashMap<Position, String> map) {
        int sum = 0;
        for (Position pos : map.keySet()) {
            String num = map.get(pos);
            if (!isNumber(map.get(pos))) continue;

            for (int row = pos.row-1; row <= pos.row+1; row++) {
                for (int col = pos.col-1; col <= pos.col+num.length(); col++) {
                    Position adj = new Position(row, col);
                    if (!map.containsKey(adj)) continue;
                    String symbol = map.get(adj);
                    if (isNumber(symbol)) continue;

                    sum += Integer.parseInt(num);
                }
            }
        }
        return sum;
    }

    private static int partB(HashMap<Position, String> map) {
        int sum = 0;
        HashMap<Position, Integer> seenGears = new HashMap<>();
        for (Position pos : map.keySet()) {
            String num = map.get(pos);
            if (!isNumber(map.get(pos))) continue;

            for (int row = pos.row-1; row <= pos.row+1; row++) {
                for (int col = pos.col-1; col <= pos.col+num.length(); col++) {
                    Position adj = new Position(row, col);
                    if (!map.containsKey(adj)) continue;
                    String gear = map.get(adj);
                    if (!gear.equals("*")) continue;

                    if (seenGears.containsKey(adj)) {
                        assert  seenGears.get(adj) != 0;
                        sum += seenGears.get(adj) * Integer.parseInt(num);
                        seenGears.put(adj, 0);
                    } else {
                        seenGears.put(adj, Integer.parseInt(num));
                    }
                }
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        HashMap<Position, String> map = createGrid(data);

        AocUtils.sendPuzzleAnswer(1, partA(map));
        AocUtils.sendPuzzleAnswer(2, partB(map));
    }

}
