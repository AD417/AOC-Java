package io.github.ad417.year2023.day17;

import io.github.ad417.util.Coordinate;
import tk.vivas.adventofcode.AocUtils;

import java.util.*;

public class Day17 {
    static class Movement implements Comparable<Movement> {
        // Not a record, because I wanted to inherit from it.
        final Coordinate current;
        final Coordinate direction;
        final int directionTimes;
        final int heatLoss;
        public Movement(Coordinate current, Coordinate direction, int directionTimes, int heatLoss) {
            this.current = current;
            this.direction = direction;
            this.directionTimes = directionTimes;
            this.heatLoss = heatLoss;
        }
        public static int getVal(char c) {
            return Integer.parseInt(String.valueOf(c));
        }
        public List<Movement> tick(char[][] grid) {
            List<Movement> nextMoves = new LinkedList<>();
            int maxRow = grid.length;
            int maxCol = grid[0].length;
            Coordinate nextDir;
            Coordinate nextPos;

            // Turn right
            nextDir = new Coordinate(direction.col(), direction.row());
            nextPos = current.add(nextDir);
            if (nextPos.inBounds(maxRow, maxCol)) {
                int dHeat = getVal(grid[nextPos.row()][nextPos.col()]);
                nextMoves.add(new Movement(nextPos, nextDir, 1, heatLoss + dHeat));
            }

            // Turn left
            nextDir = new Coordinate(-direction.col(), -direction.row());
            nextPos = current.add(nextDir);
            if (nextPos.inBounds(maxRow, maxCol)) {
                int dHeat = getVal(grid[nextPos.row()][nextPos.col()]);
                nextMoves.add(new Movement(nextPos, nextDir, 1, heatLoss + dHeat));
            }

            if (!canMoveForward()) return nextMoves;
            // Go forward.
            nextDir = direction;
            nextPos = current.add(nextDir);
            if (nextPos.inBounds(maxRow, maxCol)) {
                int dHeat = getVal(grid[nextPos.row()][nextPos.col()]);
                nextMoves.add(new Movement(nextPos, nextDir, directionTimes + 1, heatLoss + dHeat));
            }

            return nextMoves;
        }
        public boolean canMoveForward() {
            return directionTimes <= 2;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Movement movement = (Movement) o;
            return directionTimes == movement.directionTimes && Objects.equals(current, movement.current) && Objects.equals(direction, movement.direction);
        }

        @Override
        public int hashCode() {
            return Objects.hash(current, direction, directionTimes);
        }

        @Override
        public int compareTo(Movement o) {
            return this.heatLoss - o.heatLoss;
        }
        public String toString() {
            return "" + heatLoss;
        }
    }
    static class UltraMovement extends Movement {
        UltraMovement(Coordinate current, Coordinate direction, int directionTimes, int heatLoss) {
            super(current, direction, directionTimes, heatLoss);
        }

        public List<Movement> tick(char[][] grid) {
            // Determine the positions
            List<Movement> nextMoves = new LinkedList<>();
            int maxRow = grid.length;
            int maxCol = grid[0].length;
            Coordinate nextDir;
            Coordinate nextPos;

            if (this.directionTimes >= 4) {
                // Turn right
                nextDir = new Coordinate(direction.col(), direction.row());
                nextPos = current.add(nextDir);
                if (nextPos.inBounds(maxRow, maxCol)) {
                    int dHeat = getVal(grid[nextPos.row()][nextPos.col()]);
                    nextMoves.add(new UltraMovement(nextPos, nextDir, 1, heatLoss + dHeat));
                }

                // Turn left
                nextDir = new Coordinate(-direction.col(), -direction.row());
                nextPos = current.add(nextDir);
                if (nextPos.inBounds(maxRow, maxCol)) {
                    int dHeat = getVal(grid[nextPos.row()][nextPos.col()]);
                    nextMoves.add(new UltraMovement(nextPos, nextDir, 1, heatLoss + dHeat));
                }
            }

            if (this.directionTimes >= 10) return nextMoves;
            // Go forward.
            nextDir = direction;
            nextPos = current.add(nextDir);
            if (nextPos.inBounds(maxRow, maxCol)) {
                int dHeat = getVal(grid[nextPos.row()][nextPos.col()]);
                nextMoves.add(new UltraMovement(nextPos, nextDir, directionTimes + 1, heatLoss + dHeat));
            }

            return nextMoves;
        }
    }
    private static int partA(char[][] grid) {
        // This is just Djikstra's algorithm. Sort cars based on heat value.
        // First crucible to make it to the factory by default has the lowest
        // heat value.
        // The hard part was caching. It's not enough to know the first
        // crucible to make it to some location. You also need to consider how
        // many moves it has made in a straight line, as that is relevant with
        // future moves it might not be able to make.
        int maxRow = grid.length;
        int maxCol = grid[0].length;
        PriorityQueue<Movement> queue = new PriorityQueue<>();
        HashSet<Movement> visited = new HashSet<>();

        Movement movement = new Movement(new Coordinate(0, 0), new Coordinate(0, 1), 0, 0);
        queue.offer(movement);
        while (!queue.isEmpty()) {
            movement = queue.poll();
            Coordinate pos = movement.current;
            if (pos.row() == maxRow-1 && pos.col() == maxCol-1) {
                break;
                /*System.out.println(movement.heatLoss);
                if (movement.heatLoss() > minHeat) minHeat = movement.heatLoss;
                continue;*/
                //break;
            }
            if (visited.contains(movement)) continue;
            visited.add(movement);
            queue.addAll(movement.tick(grid));
        }
        // Remainder is debug...
        /*Coordinate pos = movement.current;
        visited.remove(new Coordinate(0,0));
        grid = """
                .............
                .............
                .............
                .............
                .............
                .............
                .............
                .............
                .............
                .............
                .............
                .............
                .............""".lines().map(String::toCharArray).toArray(char[][]::new);
        while (pos != null) {
            System.out.println(pos);
            grid[pos.row()][pos.col()] = '#';
            pos = visited.get(pos);
        }
        Arrays.stream(grid).forEach(x -> System.out.println(new String(x)));*/
        return movement.heatLoss;
    }

    private static int partB(char[][] grid) {
        // Same as part A, except the crucible movement logic is slightly
        // different.
        // Notably, the crucible must have been moving in a straight line for
        // at least 4 steps in order for the solution to be valid, which I
        // completely forgot to implement when doing this the first time.
        int maxRow = grid.length;
        int maxCol = grid[0].length;
        PriorityQueue<Movement> queue = new PriorityQueue<>();
        HashMap<UltraMovement, Integer> visited = new HashMap<>();

        UltraMovement movement = new UltraMovement(new Coordinate(0, 0), new Coordinate(0, 1), 0, 0);
        queue.offer(movement);
        movement = new UltraMovement(new Coordinate(0, 0), new Coordinate(1, 0), 0, 0);
        queue.offer(movement);
        while (!queue.isEmpty()) {
            movement = (UltraMovement) queue.poll();
            Coordinate pos = movement.current;
            if (pos.row() == maxRow-1 && pos.col() == maxCol-1) {
                // Aaaaaaaaaaaaaaaa
                if (movement.directionTimes < 4) break;
            }
            if (visited.containsKey(movement)) {
                if (visited.get(movement) < movement.heatLoss) continue;
            }
            visited.put(movement, movement.heatLoss);
            queue.addAll(movement.tick(grid));
        }
        return movement.heatLoss;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        char[][] grid = data.lines().map(String::toCharArray).toArray(char[][]::new);

        System.out.println(partA(grid));
        System.out.println(partB(grid));

        //System.out.println(partA(lines));
        //AocUtils.sendPuzzleAnswer(1, partA(lines));
        //AocUtils.sendPuzzleAnswer(2, partB(lines));
    }
}
