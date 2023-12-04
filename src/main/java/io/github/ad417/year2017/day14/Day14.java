package io.github.ad417.year2017.day14;

import io.github.ad417.year2017.KnotHash;
import tk.vivas.adventofcode.AocUtils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day14 {
    record Position(int row, int col) {}

    private static int partA(String data) {
        int sum = 0;
        Pattern pattern = Pattern.compile("1");

        for (int i = 0; i < 128; i++) {
            String code = data + "-" + i;

            Matcher matches = pattern.matcher(KnotHash.binaryHash(code));
            while (matches.find()) sum++;
        }
        return sum;
    }

    private static int partB(String data) {
        HashSet<Position> ones = new HashSet<>();
        Queue<Position> toVisit = new LinkedList<>();

        Pattern pattern = Pattern.compile("1");
        for (int row = 0; row < 128; row++) {
            String code = data + "-" + row;

            Matcher matches = pattern.matcher(KnotHash.binaryHash(code));
            while (matches.find()) {
                int col = matches.start();
                ones.add(new Position(row, col));
            }
        }

        int groups = 0;
        while (!ones.isEmpty()) {
            groups++;
            toVisit.offer(ones.iterator().next());

            while (!toVisit.isEmpty()) {
                Position pos = toVisit.poll();
                if (!ones.contains(pos)) continue;
                ones.remove(pos);

                toVisit.add(new Position(pos.row+1, pos.col));
                toVisit.add(new Position(pos.row-1, pos.col));
                toVisit.add(new Position(pos.row, pos.col+1));
                toVisit.add(new Position(pos.row, pos.col-1));
            }
        }
        return groups;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();

        AocUtils.sendPuzzleAnswer(1, partA(data));
        AocUtils.sendPuzzleAnswer(2, partB(data));
    }

}
