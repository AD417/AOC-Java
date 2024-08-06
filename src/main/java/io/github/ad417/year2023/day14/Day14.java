package io.github.ad417.year2023.day14;

import io.github.ad417.util.Coordinate;
import tk.vivas.adventofcode.AocUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Day14 {
    private record MoveData(Coordinate position, boolean doesThing) {}
    private static class AoCConfig {
        // Code is adopted from a BFS solver I wrote for my Computer Science
        // class. Sadly, it was not useful, but allowed me to copy-paste a lot
        // of methods.
        private static char ROLLER = 'O';
        private static char EMPTY = '.';
        int size;
        final char[][] world;

        public AoCConfig(String map) {
            String[] rows = map.split("\n");
            size = rows.length;
            world = new char[size][size];
            for (int i = 0; i < size; i++) world[i] = rows[i].toCharArray();
        }
        public AoCConfig(AoCConfig other, Coordinate dir) {

            // Clone a 2D charArray
            this.size = other.size;
            this.world = Arrays.stream(other.world)
                    .map(char[]::clone)
                    .toArray(char[][]::new);

            for (Coordinate cell : traversals(dir)) {
                //System.out.println("Something happened");
                int row = cell.row();
                int col = cell.col();
                char blockType = getVal(cell);
                if (blockType != ROLLER) continue;

                world[col][row] = EMPTY;
                MoveData result = moveTile(cell, dir);

                row = result.position.row();
                col = result.position.col();
                world[col][row] = blockType;
            }

        }
        private MoveData moveTile(Coordinate position, Coordinate direction) {
            Coordinate previous;
            do {
                previous = position;
                position = new Coordinate(position.row() + direction.row(), position.col() + direction.col());
            } while (!isNotTile(position) && getVal(position) == EMPTY);

            return new MoveData(previous, false);
        }
        public char getVal(Coordinate pos) { return this.world[pos.col()][pos.row()]; }
        public boolean isNotTile(Coordinate pos) {
            return Math.max(pos.row(), pos.col()) >= size || Math.min(pos.row(), pos.col()) < 0;
        }
        private Iterable<Coordinate> traversals(Coordinate direction) {
            // This bit of code is stolen, ultimately, from 2048. It has a long
            // lineage through several programs I've written.
            LinkedList<Coordinate> output = new LinkedList<>();
            int[] rowOrder = new int[size];
            int[] colOrder = new int[size];

            for (int i = 0; i < size; i++) {
                rowOrder[i] = direction.row() == 1 ? (size - 1 - i) : i;
                colOrder[i] = direction.col() == 1 ? (size - 1 - i) : i;
            }
            for (int row : rowOrder) {
                for (int col : colOrder) {
                    output.add(new Coordinate(row, col));
                }
            }
            return output;
        }
        public int dangerLevel() {
            int sum = 0;
            for (Coordinate pos : traversals(new Coordinate(0,0))) {
                if (getVal(pos) != ROLLER) continue;
                sum += size - pos.col();
            }
            return sum;
        }
        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (other == null || getClass() != other.getClass()) return false;
            AoCConfig ac = (AoCConfig) other;
            return Arrays.deepEquals(world, ac.world);
        }

        @Override
        public int hashCode() {
            return Arrays.deepHashCode(world);
        }
        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            for (int col = 0; col < size; ++col) {
                for (int row = 0; row < size; ++row) {
                    result.append(getVal(new Coordinate(row, col))).append(" ");
                }
                if (col != size-1) {
                    result.append(System.lineSeparator());
                }
            }
            return result.toString();
        }
    }
    private static int partA(AoCConfig initial) {
        // Big whoop, moving all the tiles up.
        AoCConfig north = new AoCConfig(initial, new Coordinate(0,-1));
        //System.out.println(north);
        return north.dangerLevel();
    }

    private static int partB(AoCConfig initial) {
        // Move up, left, down, right, 1 billion times.
        // The secret is that, after some number of moves, we end up in a
        // cycle. Once we determine the length of the cycle, we can "fast-
        // forward" the cycling to the end.
        AoCConfig current = initial;
        Coordinate[] moves = new Coordinate[] {
                new Coordinate(0, -1),
                new Coordinate(-1, 0),
                new Coordinate(0, 1),
                new Coordinate(1, 0)
        };
        Set<AoCConfig> seenPreviously = new HashSet<>();
        boolean inLoop = false;

        // 1e9 cycles = 4e9 moves. I did a stupid.
        long times = 4_000_000_000L;

        for (long i = 0; i < times; i++) {
            current = new AoCConfig(current, moves[(int)(i % 4)]);

            if (!seenPreviously.contains(current)) {
                seenPreviously.add(current);
                continue;
            }
            if (!inLoop) {
                seenPreviously = new HashSet<>();
                inLoop = true;
                continue;
            }
            inLoop = false;
            i += (times - i) / seenPreviously.size() * seenPreviously.size();
        }
        return current.dangerLevel();
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        AoCConfig initial = new AoCConfig(data);


        AocUtils.sendPuzzleAnswer(1, partA(initial));
        AocUtils.sendPuzzleAnswer(2, partB(initial));
    }
}
