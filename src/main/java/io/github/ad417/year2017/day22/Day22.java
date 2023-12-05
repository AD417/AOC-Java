package io.github.ad417.year2017.day22;

import tk.vivas.adventofcode.AocUtils;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;

public class Day22 {
    record Position(int row, int col) {
        public Position move(Direction dir) {
            return switch (dir) {
                case UP -> new Position(row-1, col);
                case DOWN -> new Position(row+1, col);
                case LEFT -> new Position(row, col-1);
                case RIGHT -> new Position(row, col+1);
            };
        }
    }
    enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT,
    }

    private static HashSet<Position> getInfected(String[] lines) {
        HashSet<Position> infected = new HashSet<>();
        for (int row = 0; row < lines.length; row++) {
            String line = lines[row];
            for (int col = 0; col < line.length(); col++) {
                if (line.charAt(col) == '.') continue;
                infected.add(new Position(row, col));
            }
        }
        return infected;
    }

    private static int partA(String data) {
        String[] lines = data.split("\n");
        HashSet<Position> infected = getInfected(lines);
        int centerRow = lines.length / 2;
        int centerCol = lines[0].length() / 2;
        Position pos = new Position(centerRow, centerCol);
        Direction dir = Direction.UP;

        int infections = 0;

        for (int i = 0; i < 10000; i++) {
            if (infected.contains(pos)) {
                infected.remove(pos);
                dir = switch (dir) {
                    case UP -> Direction.RIGHT;
                    case RIGHT -> Direction.DOWN;
                    case DOWN -> Direction.LEFT;
                    case LEFT -> Direction.UP;
                };
            } else {
                infected.add(pos);
                infections++;
                dir = switch (dir) {
                    case UP -> Direction.LEFT;
                    case LEFT -> Direction.DOWN;
                    case DOWN -> Direction.RIGHT;
                    case RIGHT -> Direction.UP;
                };
            }
            pos = pos.move(dir);
        }

        /*StringBuilder sb = new StringBuilder();
        for (int row = -3; row < 5; row++) {
            for (int col = -3; col < 6; col++) {
                if (infected.contains(new Position(row, col))) sb.append('#');
                else sb.append('.');
            }
            sb.append('\n');
        }
        System.out.println(sb);*/

        return infections;
    }

    private static int partB(String data) {
        String[] lines = data.split("\n");
        HashSet<Position> infected = getInfected(lines);
        HashSet<Position> flagged = new HashSet<>();
        HashSet<Position> weakened = new HashSet<>();

        int centerRow = lines.length / 2;
        int centerCol = lines[0].length() / 2;
        Position pos = new Position(centerRow, centerCol);
        Direction dir = Direction.UP;

        int infections = 0;

        for (int i = 0; i < 10000000; i++) {
            if (infected.contains(pos)) {
                infected.remove(pos);
                flagged.add(pos);
                dir = switch (dir) {
                    case UP -> Direction.RIGHT;
                    case RIGHT -> Direction.DOWN;
                    case DOWN -> Direction.LEFT;
                    case LEFT -> Direction.UP;
                };
            } else if (flagged.contains(pos)) {
                flagged.remove(pos);
                dir = switch (dir) {
                    case UP -> Direction.DOWN;
                    case RIGHT -> Direction.LEFT;
                    case DOWN -> Direction.UP;
                    case LEFT -> Direction.RIGHT;
                };
            } else if (weakened.contains(pos)) {
                weakened.remove(pos);
                infected.add(pos);
                infections++;
            } else {
                weakened.add(pos);
                dir = switch (dir) {
                    case UP -> Direction.LEFT;
                    case LEFT -> Direction.DOWN;
                    case DOWN -> Direction.RIGHT;
                    case RIGHT -> Direction.UP;
                };
            }
            pos = pos.move(dir);
        }

        return infections;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();

        AocUtils.sendPuzzleAnswer(1, partA(data));
        AocUtils.sendPuzzleAnswer(2, partB(data));
    }
}
