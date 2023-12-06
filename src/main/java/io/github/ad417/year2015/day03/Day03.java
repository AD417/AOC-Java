package io.github.ad417.year2015.day03;

import tk.vivas.adventofcode.AocUtils;

import java.util.HashSet;
import java.util.Set;

public class Day03 {
    record Coordinate(int row, int col) {}
    private static int partA(String moves) {
        Set<Coordinate> delivered = new HashSet<>();
        Coordinate pos = new Coordinate(0,0);
        for (char c : moves.toCharArray()) {
            delivered.add(pos);
            int dx = 0, dy = 0;
            switch (c) {
                case '^' -> dy++;
                case 'v' -> dy--;
                case '<' -> dx--;
                case '>' -> dx++;
            }
            pos = new Coordinate(pos.row + dx, pos.col + dy);
            delivered.add(pos);
        }
        return delivered.size();
    }

    private static int partB(String moves) {
        Set<Coordinate> delivered = new HashSet<>();
        Coordinate realPos = new Coordinate(0,0);
        Coordinate roboPos = new Coordinate(0,0);
        boolean roboMove = false;
        for (char c : moves.toCharArray()) {
            if (roboMove) {
                delivered.add(roboPos);
                int dx = 0, dy = 0;
                switch (c) {
                    case '^' -> dy++;
                    case 'v' -> dy--;
                    case '<' -> dx--;
                    case '>' -> dx++;
                }
                roboPos = new Coordinate(roboPos.row + dx, roboPos.col + dy);
                delivered.add(roboPos);
            } else {
                delivered.add(realPos);
                int dx = 0, dy = 0;
                switch (c) {
                    case '^' -> dy++;
                    case 'v' -> dy--;
                    case '<' -> dx--;
                    case '>' -> dx++;
                }
                realPos = new Coordinate(realPos.row + dx, realPos.col + dy);
                delivered.add(realPos);
            }
            roboMove = !roboMove;
        }
        return delivered.size();
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();

        AocUtils.sendPuzzleAnswer(1, partA(data));
        AocUtils.sendPuzzleAnswer(2, partB(data));
    }
}
