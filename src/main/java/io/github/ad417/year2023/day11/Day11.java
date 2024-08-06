package io.github.ad417.year2023.day11;

import io.github.ad417.util.Coordinate;
import tk.vivas.adventofcode.AocUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day11 {
    private static Set<Coordinate> getInitialGalaxies(String data) {
        String[] lines = data.split("\n");
        int rows = lines.length;
        int cols = lines[0].length();
        HashSet<Coordinate> initialGalaxies = new HashSet<>();
        for (int row = 0; row < rows; row++) {
            String line = lines[row];
            for (int col = 0; col < cols; col++) {
                if (line.charAt(col) != '#') continue;
                initialGalaxies.add(new Coordinate(row, col));
            }
        };
        return initialGalaxies;
    }

    private static Set<Coordinate> getExpansion(Set<Coordinate> initialGalaxies, int expansionFactor) {
        int rows = initialGalaxies.stream().mapToInt(Coordinate::row).max().orElseThrow()+1;
        int cols = initialGalaxies.stream().mapToInt(Coordinate::col).max().orElseThrow()+1;

        Set<Integer> emptyRows = IntStream.range(0, rows)
                .filter(x -> initialGalaxies.stream().noneMatch(pos -> pos.row() == x))
                .boxed()
                .collect(Collectors.toSet());

        Set<Integer> emptyCols = IntStream.range(0, cols)
                .filter(x -> initialGalaxies.stream().noneMatch(pos -> pos.col() == x))
                .boxed()
                .collect(Collectors.toSet());

        List<Integer> rowExpansion = new ArrayList<>(rows);
        int expansion = 0;
        for (int i = 0; i < rows; i++) {
            if (emptyRows.contains(i)) expansion += expansionFactor;
            rowExpansion.add(expansion);
        }
        List<Integer> colExpansion = new ArrayList<>(cols);
        expansion = 0;
        for (int i = 0; i < cols; i++) {
            if (emptyCols.contains(i)) expansion += expansionFactor;
            colExpansion.add(expansion);
        }

        Set<Coordinate> finalGalaxies = new HashSet<>();
        for (Coordinate pos : initialGalaxies) {
            int row = pos.row();
            int col = pos.col();
            finalGalaxies.add(new Coordinate(row + rowExpansion.get(row), col + colExpansion.get(col)));
        }
        return finalGalaxies;
    }

    private static int partA(Set<Coordinate> galaxies) {
        // The shortest path, as given by the problem, is just the Manhattan
        // distance between the points.

        // Due to a fencepost error in my expansion math, the expansion factor
        // is 1 lower than it should be. In part A, a single empty row is
        // replaced by 2.
        galaxies = getExpansion(galaxies, 1);
        int sum = 0;
        for (Coordinate galaxy1 : galaxies) {
            for (Coordinate galaxy2 : galaxies) {
                sum += galaxy1.distanceTo(galaxy2);
            }
        }
        return sum / 2;
    }

    private static long partB(Set<Coordinate> galaxies) {
        // Same as part 1, but with a larger expansion factor.
        //
        // An expansion of a single row to 1,000,000 is equal to
        // adding 999,999 empty rows.
        galaxies = getExpansion(galaxies, 1_000_000 - 1);
        long sum = 0;
        for (Coordinate galaxy1 : galaxies) {
            for (Coordinate galaxy2 : galaxies) {
                sum += galaxy1.distanceTo(galaxy2);
            }
        }
        return sum / 2;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        Set<Coordinate> galaxies = getInitialGalaxies(data);

        AocUtils.sendPuzzleAnswer(1, partA(galaxies));
        AocUtils.sendPuzzleAnswer(2, partB(galaxies));
    }
}
