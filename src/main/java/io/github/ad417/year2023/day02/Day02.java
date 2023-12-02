package io.github.ad417.year2023.day02;

import tk.vivas.adventofcode.AocUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Day02 {
    enum Color {
        RED,
        BLUE,
        GREEN
    }
    static class Selection {
        private final Color color;
        private final int cubes;
        public Selection(Color color, int cubes) {
            this.color = color;
            this.cubes = cubes;
        }

        public static Selection createSelection(String entry) {
            String[] parts = entry.split(" ");
            int cubes = Integer.parseInt(parts[0]);
            Color color = switch (parts[1]) {
                case "red" -> Color.RED;
                case "blue" -> Color.BLUE;
                case "green"-> Color.GREEN;
                default -> throw new IllegalStateException("Unexpected value: " + parts[1]);
            };
            return new Selection(color, cubes);
        }

        public int getCubes() {
            return cubes;
        }

        public boolean isValid() {
            return cubes <= switch (color) {
                case RED -> 12;
                case GREEN -> 13;
                case BLUE -> 14;
            };
        }

        public boolean isComparable(Selection other) {
            return this.color == other.color;
        }
        public Selection greater(Selection other) {
            if (this.cubes > other.cubes) return this;
            return other;
        }
        public Selection[] minRequired(Selection[] currentMin) {
            for (int i = 0; i < currentMin.length; i++) {
                if (!isComparable(currentMin[i])) continue;
                currentMin[i] = this.greater(currentMin[i]);
            }
            return currentMin;
        }
    }
    private static List<Selection> parseGame(String game) {
        game = game.split(": ")[1];
        return Arrays.stream(game.split("[;,] "))
                .map(Selection::createSelection)
                .collect(Collectors.toList());
    }

    private static int partA(String[] lines) {
        int sum = 0;
        for (int i = 1; i <= lines.length; i++) {
            String game = lines[i-1];
            List<Selection> cubes = parseGame(game);

            if (cubes.stream().allMatch(Selection::isValid)) {
                sum+= i;
            }
        }
        return sum;
    }

    private static int partB(String[] lines) {
        int sum = 0;
        for (String game : lines) {
            List<Selection> cubes = parseGame(game);
            Selection[] min = new Selection[] {
                    new Selection(Color.RED, 0),
                    new Selection(Color.BLUE, 0),
                    new Selection(Color.GREEN, 0),
            };
            for (Selection cube : cubes) {
                min = cube.minRequired(min);
            }
            sum += min[0].getCubes() * min[1].getCubes() * min[2].getCubes();
        }
        return sum;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput();
        String[] lines = data.lines().toArray(String[]::new);

        AocUtils.sendPuzzleAnswer(1, partA(lines));
        AocUtils.sendPuzzleAnswer(2, partB(lines));
    }

}
