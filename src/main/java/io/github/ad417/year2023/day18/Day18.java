package io.github.ad417.year2023.day18;

import io.github.ad417.util.Coordinate;
import tk.vivas.adventofcode.AocUtils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day18 {
    private static final Coordinate[] MOVES = new Coordinate[] {
            new Coordinate(-1, 0),
            new Coordinate(1, 0),
            new Coordinate(0, 1),
            new Coordinate(0, -1),
    };
    private static int partA(String[] lines) {
        // A naive implementation of an area calculating algorithm, using
        // BFS to determine all the tiles within the region.
        HashSet<Coordinate> edge = new HashSet<>();
        Pattern number = Pattern.compile("\\d+");
        Coordinate diggerPos = new Coordinate(0,0);
        for (String line : lines) {
            Coordinate delta = switch (line.charAt(0)) {
                case 'U' -> MOVES[0];
                case 'D' -> MOVES[1];
                case 'R' -> MOVES[2];
                case 'L' -> MOVES[3];
                default -> throw new IllegalStateException("Unexpected value: " + line.charAt(0));
            };
            Matcher m = number.matcher(line);
            if (!m.find()) {
                System.out.println("erm what the spruce");
                continue;
            }
            int moves = Integer.parseInt(m.group());
            for (int i = 0; i < moves; i++) {
                edge.add(diggerPos);
                diggerPos = diggerPos.add(delta);
            }
        }

        Queue<Coordinate> toVisit = new LinkedList<>();
        HashSet<Coordinate> inside = new HashSet<>();
        toVisit.add(new Coordinate(1,1));
        while (!toVisit.isEmpty()) {
            // NB: assumes that the basin isn't "pinched" (two nonconsecutive adjacent edge tiles)
            // Also that (1,1) is inside the basin.
            Coordinate pos = toVisit.poll();
            if (edge.contains(pos)) continue;
            if (inside.contains(pos)) continue;
            inside.add(pos);
            for (Coordinate move : MOVES) {
                toVisit.add(pos.add(move));
            }
        }

        return edge.size() + inside.size();
    }

    private static long partB(String[] lines) {
        // A far more intelligent implementation of an area computing
        // algorithm using "Pick's Theorem", a modification to the shoelace
        // algorithm that could've been used on Day 11. I don't fully
        // understand it.
        // Based on my understanding, you create a bunch of triangles, that
        // may or may not add to or subtract from the area of the polygon as
        // you go clockwise or counterclockwise around (0,0). When you finish,
        // (and include the outer perimeter for some reason) and add 1 (for
        // some other reason), you get the total area of the shape.
        //
        // I did not deserve rank 120 for this. At least I didn't make the
        // leaderboard.
        // My Regex character arc is coming around -- that expression for
        // extracting the number and direction code is majestic. The conversion
        // to the MOVES[] index is less so, because I didn't know how it would
        // go.
        Pattern hex = Pattern.compile("(?<=#)([0-9a-f]{5})([0-9a-f])");
        Coordinate diggerPos = new Coordinate(1,1);
        Coordinate lastDiggerPos;
        long sum = 0;
        long totalDistance = 0;
        for (String line : lines) {
            Matcher m = hex.matcher(line);
            if (!m.find()) {
                System.out.println("erm what the spruce");
                continue;
            }
            int distance = Integer.parseInt(m.group(1), 16);
            totalDistance += distance;
            Coordinate delta = switch (m.group(2)) {
                // Fucking lmao
                case "0" -> MOVES[2];
                case "1" -> MOVES[1];
                case "2" -> MOVES[3];
                case "3" -> MOVES[0];
                default -> throw new IllegalStateException("Unexpected value: " + m.group(2));
            };
            delta = delta.mult(distance);

            lastDiggerPos = diggerPos;
            diggerPos = diggerPos.add(delta);
            sum += (long) lastDiggerPos.row() * diggerPos.col() - (long) lastDiggerPos.col() * diggerPos.row();
        }
        return (Math.abs(sum) + totalDistance) / 2 + 1;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        String[] lines = data.split("\n");

        System.out.println(partA(lines));
        System.out.println(partB(lines));
    }
}
