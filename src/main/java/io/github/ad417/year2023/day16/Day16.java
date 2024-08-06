package io.github.ad417.year2023.day16;

import io.github.ad417.util.Coordinate;
import tk.vivas.adventofcode.AocUtils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Day16 {
    record Beam(Coordinate pos, Coordinate vel) {
        public Beam tick() {
            return new Beam(pos.add(vel), vel);
        }
        public List<Beam> interactWith(char tile) {
            List<Beam> DEFAULT = List.of(this);
            switch (tile) {
                case '-' -> {
                    if (vel.row() == 0) return DEFAULT;
                    return List.of(
                            new Beam(pos, new Coordinate(0,1)),
                            new Beam(pos, new Coordinate(0,-1))
                    );
                }
                case '|' -> {
                    if (vel.col() == 0) return DEFAULT;
                    return List.of(
                            new Beam(pos, new Coordinate(1, 0)),
                            new Beam(pos, new Coordinate(-1, 0))
                    );
                }
                case '/' -> {
                    return List.of(new Beam(pos, new Coordinate(-vel.col(), -vel.row())));
                }
                case '\\' -> {
                    return List.of(new Beam(pos, new Coordinate(vel.col(), vel.row())));
                }
                case '.' -> {
                    return DEFAULT;
                }
                default -> throw new RuntimeException("Invalid character: "+tile);
            }
        }
    }

    private static int simulate(char[][] grid, Beam initialBeam) {
        // This is effectively part A.
        // The beam starts on the edge and moves in some direction. It reflects
        // off of the tiles and whatnot, and eventually either ends up in a
        // loop or flying off the edge of the grid.
        // This simulates all the locations touched by the initial beam, and
        // determines how many tiles are touched by it.
        int maxRows = grid.length;
        int maxCols = grid[0].length;

        List<Beam> beams = List.of(initialBeam);
        Set<Beam> identicalBeams = new HashSet<>();
        Set<Coordinate> visited = new HashSet<>();

        while (!beams.isEmpty()) {
            List<Beam> nextBeams = new LinkedList<>();
            for (Beam beam : beams) {
                if (identicalBeams.contains(beam)) continue;
                identicalBeams.add(beam);
                Coordinate pos = beam.pos();
                if (!pos.inBounds(maxRows, maxCols)) continue;
                visited.add(pos);

                char currentPosition = grid[pos.row()][pos.col()];

                List<Beam> nextBeam = beam.interactWith(currentPosition);
                nextBeams.addAll(nextBeam.stream().map(Beam::tick).toList());
            }
            beams = nextBeams;
        }
        return visited.size();
    }

    private static int partA(char[][] grid) {
        return simulate(grid, new Beam(new Coordinate(0,0), new Coordinate(0,1)));
    }

    private static int partB(char[][] grid) {
        // Similar to Part A, except we find the entry point where the number
        // of tiles hit is largest. Just simple iteration.
        int maxRows = grid.length;
        int maxCols = grid[0].length;

        int maxHeat = 0;
        for (int row = 0; row < maxRows; row++) {
            Coordinate pos = new Coordinate(row, 0);
            Coordinate vel = new Coordinate(0, 1);
            Beam beam = new Beam(pos, vel);
            int heat = simulate(grid, beam);
            if (maxHeat < heat) maxHeat = heat;


            pos = new Coordinate(row, maxCols-1);
            vel = new Coordinate(0, -1);
            beam = new Beam(pos, vel);
            heat = simulate(grid, beam);
            if (maxHeat < heat) maxHeat = heat;
        }
        for (int col = 0; col < maxCols; col++) {
            Coordinate pos = new Coordinate(0, col);
            Coordinate vel = new Coordinate(1, 0);
            Beam beam = new Beam(pos, vel);
            int heat = simulate(grid, beam);
            if (maxHeat < heat) maxHeat = heat;


            pos = new Coordinate(maxRows-1, col);
            vel = new Coordinate(-1, 0);
            beam = new Beam(pos, vel);
            heat = simulate(grid, beam);
            if (maxHeat < heat) maxHeat = heat;
        }
        return maxHeat;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        char[][] grid = data.lines().map(String::toCharArray).toArray(char[][]::new);

        AocUtils.sendPuzzleAnswer(1, partA(grid));
        AocUtils.sendPuzzleAnswer(2, partB(grid));
    }
}
