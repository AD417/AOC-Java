package io.github.ad417.year2015.day18;

import tk.vivas.adventofcode.AocUtils;

import java.util.HashSet;
import java.util.Set;

public class Day18 {
    record Coordinate(int row, int col) {}

    private static Set<Coordinate> getGrid(String data) {
        String[] lines = data.split("\n");
        HashSet<Coordinate> grid = new HashSet<>();
        for (int row = 0; row < lines.length; row++) {
            char[] line = lines[row].toCharArray();
            for (int col = 0; col < line.length; col++) {
                if (line[col] == '#') grid.add(new Coordinate(row, col));
            }
        }
        return grid;
    }

    private static boolean becomesAlive(Coordinate pos, Set<Coordinate> grid) {
        int amount = 0;
        for (int row = pos.row-1; row <= pos.row+1; row++) {
            for (int col = pos.col-1; col <= pos.col+1; col++) {
                Coordinate adj = new Coordinate(row, col);
                if (adj.equals(pos)) continue;
                if (grid.contains(adj)) amount++;
            }
        }
        return amount == 3;
    }

    private static boolean staysAlive(Coordinate pos, Set<Coordinate> grid) {
        int amount = 0;
        for (int row = pos.row-1; row <= pos.row+1; row++) {
            for (int col = pos.col-1; col <= pos.col+1; col++) {
                Coordinate adj = new Coordinate(row, col);
                if (adj.equals(pos)) continue;
                if (grid.contains(adj)) amount++;
            }
        }
        return amount == 3 || amount == 2;

    }

    private static int partA(Set<Coordinate> initialConfig) {
        Set<Coordinate> config = initialConfig;
        for (int i = 0; i < 100; i++) {
            Set<Coordinate> nextConfig = new HashSet<>();
            for (int row = 0; row < 100; row++) {
                for (int col = 0; col < 100; col++) {
                    Coordinate pos = new Coordinate(row, col);
                    if (config.contains(pos) && staysAlive(pos, config)) nextConfig.add(pos);
                    if (!config.contains(pos) && becomesAlive(pos, config)) nextConfig.add(pos);
                }
            }
            config = nextConfig;
        }
        return config.size();
    }

    private static int partB(Set<Coordinate> initialConfig) {
        Set<Coordinate> config = initialConfig;
        for (int i = 0; i < 100; i++) {
            Set<Coordinate> nextConfig = new HashSet<>();
            for (int row = 0; row < 100; row++) {
                for (int col = 0; col < 100; col++) {
                    Coordinate pos = new Coordinate(row, col);
                    if (config.contains(pos) && staysAlive(pos, config)) nextConfig.add(pos);
                    if (!config.contains(pos) && becomesAlive(pos, config)) nextConfig.add(pos);
                }
            }
            nextConfig.add(new Coordinate(0,0));
            nextConfig.add(new Coordinate(0,99));
            nextConfig.add(new Coordinate(99,0));
            nextConfig.add(new Coordinate(99,99));
            config = nextConfig;
        }
        return config.size();
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        Set<Coordinate> grid = getGrid(data);

        AocUtils.sendPuzzleAnswer(1, partA(grid));
        AocUtils.sendPuzzleAnswer(2, partB(grid));
    }

}
