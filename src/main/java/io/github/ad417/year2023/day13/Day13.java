package io.github.ad417.year2023.day13;

import tk.vivas.adventofcode.AocUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Day13 {
    static class Matrix extends ArrayList<List<Character>> {
        // I really only did this because writing out `List<List<Character>>`
        // was bound to get annoying.
        public Matrix(int size) {
            super(size);
        }
        public Matrix(List<List<Character>> old) {
            super(old);
        }
        public Matrix transpose() {
            // Swap the X and Y axes. The main reason this structure exists.
            Matrix transposition = new Matrix(get(0).size());
            for (int rowT = 0; rowT < get(0).size(); rowT++) {
                List<Character> transRow = new ArrayList<>(size());
                for (List<Character> characters : this) {
                    transRow.add(characters.get(rowT));
                }
                transposition.add(transRow);
            }
            return transposition;
        }
        public int findReflection(List<Integer> possiblePoints) {
            // In retrospect, I should've just copied the function below.
            // Takes a list, and validates that some reflection point is
            // actually a reflection point.
            for (int point : possiblePoints) {
                if (reflectsAt(point)) return point;
            }
            return -1;
        }
        public int findAlmostReflection() {
            // Should've done this from the start.
            // For every row, check if it "almost reflects" -- that is, the sum
            // of the number of flaws in the reflection is exactly 1.
            for (int row = 0; row < size(); row++) {
                if (almostReflectsAt(row)) return row;
            }
            return -1;
        }
        private boolean reflectsAt(int index) {
            int other = index + 1;
            while (index > 0 && other < size()) {
                if (!get(index--).equals(get(other++))) return false;
            }
            return true;
        }
        private boolean almostReflectsAt(int index) {
            int differs = 0;
            int other = index + 1;
            while (index >= 0 && other < size()) {
                differs += rowDifference(index--, other++);
            }
            return differs == 1;
        }

        private int rowDifference(int row1, int row2) {
            List<Character> firstRow = get(row1);
            List<Character> secondRow = get(row2);
            int differs = 0;
            for (int i = 0; i < firstRow.size(); i++) {
                if (firstRow.get(i) != secondRow.get(i)) differs++;
            }
            return differs;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (List<Character> line : this) {
                sb.append(line);
                sb.append("\n");
            }
            return sb.toString();
        }
    }
    private static int partA(List<Matrix> grids) {
        // My most hideous code, in retrospect.
        // Checks if a pair of rows are identical. If so, then it is a
        // candidate for being the reflection line. Since one line is not
        // enough, I then run a more comprehensive test later on.
        // If there are no rows, then I transpose the matrix and check the
        // rows again, because it's less typing on paper.
        // (It was not less typing.)
        int sum = 0;
        for (Matrix grid : grids) {
            int mult = 100;
            int reflection;
            List<Integer> reflections = new LinkedList<>();
            for (int row = 1; row < grid.size(); row++) {
                if (grid.get(0).equals(grid.get(row))) {
                    reflections.add(row / 2);
                }
            }
            reflection = grid.findReflection(reflections);
            if (reflection != -1) sum += mult * (reflection + 1);

            reflections = new LinkedList<>();
            for (int row = grid.size() - 1; row >= 0; row--) {
                if (grid.get(grid.size()-1).equals(grid.get(row))) {
                    reflections.add((row + grid.size()) / 2 - 1);
                }
            }
            reflection = grid.findReflection(reflections);
            if (reflection != -1) sum += mult * (reflection + 1);

            grid = grid.transpose();
            mult = 1;
            reflections = new LinkedList<>();
            for (int row = 1; row < grid.size(); row++) {
                if (grid.get(0).equals(grid.get(row))) {
                    reflections.add(row / 2);
                }
            }
            reflection = grid.findReflection(reflections);
            if (reflection != -1) sum += mult * (reflection + 1);

            reflections = new LinkedList<>();
            for (int row = grid.size() - 1; row >= 0; row--) {
                if (grid.get(grid.size()-1).equals(grid.get(row))) {
                    reflections.add((row + grid.size()) / 2 - 1);
                }
            }
            reflection = grid.findReflection(reflections);
            if (reflection != -1) sum += mult * (reflection + 1);
        }
        return sum;
    }

    private static int partB(List<Matrix> grids) {
        // Same as part A, except less stupid and checking for the singular
        // blemish on the mirror.
        int sum = 0;
        for (Matrix grid : grids) {
            int row = grid.findAlmostReflection();
            if (row != -1) {
                sum += 100 * (row + 1);
                continue;
            }
            grid = grid.transpose();
            int col = grid.findAlmostReflection();
            if (col != -1) {
                sum += col + 1;
            } else {
                System.out.println("Mirror machine broke");
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        List<Matrix> grids = Arrays.stream(data.split("\n\n"))
                .map(section -> section.lines().map(line -> line.chars().mapToObj(c -> (char) c).toList()).toList())
                .map(Matrix::new)
                .toList();

        AocUtils.sendPuzzleAnswer(1, partA(grids));
        AocUtils.sendPuzzleAnswer(2, partB(grids));
    }
}
