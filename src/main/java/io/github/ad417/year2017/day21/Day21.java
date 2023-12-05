package io.github.ad417.year2017.day21;

import tk.vivas.adventofcode.AocUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day21 {
    /**
     * Standard class for representing 2x2, 3x3, and 4x4 configurations.
     */
    public static class Configuration {
        /** A square of #s and .s, representing a part of the picture. */
        private final boolean[][] lights;
        /* Internal size stuff */
        private final int dim;
        public Configuration(boolean[][] lights, int dim) {
            this.lights = lights;
            this.dim = dim;
        }

        /**
         * Generate configurations from the initial input data, by cutting up all the slashes.
         * @param code a slash-separated array of dots and hashes.
         * @return the configuration represented by the compressed string.
         */
        public static Configuration makeConfig(String code) {
            String[] pieces = code.split("/");
            int dim = pieces.length;

            boolean[][] lights = new boolean[dim][dim];
            for (int row = 0; row < dim; row++) {
                for (int col = 0; col < dim; col++) {
                    lights[row][col] = pieces[row].charAt(col) == '#';
                }
            }
            return new Configuration(lights, dim);
        }

        /**
         * Rotate the config 90 degrees clockwise some number of times.
         * @param times How many times to do it. Must be at least 0.
         * @return a copy of this config, rotated.
         */
        public Configuration rotate(int times) {
            int dimRot = dim - 1;
            boolean[][] now = Arrays.stream(lights).map(x -> Arrays.copyOf(x, x.length)).toArray(boolean[][]::new);
            boolean[][] next = new boolean[dim][dim];

            for (int i = 0; i < times; i++) {
                for (int row = 0; row < dim; row++) {
                    for (int col = 0; col < dim; col++) {
                        next[col][dimRot-row] = now[row][col];
                    }
                }
                //System.out.println(new Configuration(next, dim));
                now = Arrays.stream(next).map(x -> Arrays.copyOf(x, x.length)).toArray(boolean[][]::new);
                next = new boolean[dim][dim];
            }
            return new Configuration(now, dim);
        }

        /**
         * Flip this config.
         * @return a vertically flipped version of this config.
         */
        public Configuration flip() {
            boolean[][] next = new boolean[dim][dim];
            for (int i = 0; i < dim; i++) {
                next[i] = lights[dim-i-1];
            }
            return new Configuration(next, dim);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (boolean[] line : lights) {
                sb.append("\n");
                for (boolean bool : line) {
                    sb.append(bool ? "#" : ".");
                }
            }
            return sb.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Configuration that = (Configuration) o;
            return equals(that);
        }

        /**
         * Determine if any rotation or flipping of this config can generate
         * the other config, as part of equality checking.
         * @param other the config to compare this one against.
         * @return if any transformation matches the other config exactly.
         */
        public boolean equals(Configuration other) {
            if (this.dim != other.dim) return false;
            for (int i = 0; i < 4; i++) {
                if (Arrays.deepEquals(this.rotate(i).lights, other.lights)) return true;
            }

            Configuration flip = flip();
            for (int i = 0; i < 4; i++) {
                if (Arrays.deepEquals(flip.rotate(i).lights, other.lights)) return true;
            }
            return false;
        }
    }

    /**
     * Extract a section of a 2D boolean array into a config.
     * @param picture the 2D boolean array. Allegedly a picture in this problem.
     * @param dim the size of the configs generated, usually 2 or 3.
     * @param row the row of the resulting config.
     * @param col the column of the resulting config.
     * @return the config found from extracting part of the picture.
     */
    private static Configuration extract(boolean[][] picture, int dim, int row, int col) {
        boolean[][] lights = new boolean[dim][dim];
        for (int x = 0; x < dim; x++) {
            System.arraycopy(picture[row * dim + x], col * dim, lights[x], 0, dim);
        }
        return new Configuration(lights, dim);
    }

    /**
     * Cut up this boolean array into a 2D array of configs.
     * @param picture the 2D boolean array
     * @return a 2D array of configurations.
     */
    private static List<List<Configuration>> divide(boolean[][] picture) {
        int dim = 3;
        if (picture.length % 2 == 0) dim = 2;

        int squares = picture.length / dim;
        List<List<Configuration>> out = new ArrayList<>(squares);

        for (int row = 0; row < squares; row++) {
            List<Configuration> configRow = new ArrayList<>(squares);
            for (int col = 0; col < squares; col++) {
                configRow.add(extract(picture, dim, row, col));
            }
            out.add(configRow);
        }
        return out;
    }

    /**
     * Insert the given config into this picture.
     * @param picture a 2D boolean array containing all the pixels. Should be initialized to be the correct size.
     * @param config the config to insert.
     * @param row the row to insert the config at, scaled by the size of the config.
     * @param col the column to insert the config at, scaled by the size of the config.
     * @return the picture, having been modified.
     */
    private static boolean[][] insert(boolean[][] picture, Configuration config, int row, int col) {
        int dim = config.dim;
        for (int x = 0; x < dim; x++) {
            System.arraycopy(config.lights[x], 0, picture[row * dim + x], col * dim, dim);
        }
        return picture;
    }

    private static boolean[][] recombine(List<List<Configuration>> picture) {

        int largeDim = picture.size();
        int smallDim = picture.get(0).get(0).dim;
        int totalDim = largeDim * smallDim;

        boolean[][] out = new boolean[totalDim][totalDim];
        for (int row = 0; row < largeDim; row++) {
            for (int col = 0; col < largeDim; col++) {
                insert(out, picture.get(row).get(col), row, col);
            }
        }
        return out;
    }



    private static List<Configuration> parseInitialRules(String data) {
        return data.lines()
                .map(x -> Configuration.makeConfig(x.split(" => ")[0]))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static List<Configuration> parseResultRules(String data) {
        return data.lines()
                .map(x -> Configuration.makeConfig(x.split(" => ")[1]))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static int partA(String data) {
        List<Configuration> initial = parseInitialRules(data);
        List<Configuration> result = parseResultRules(data);

        boolean[][] picture = Configuration.makeConfig(".#./..#/###").lights;

        for (int i = 0; i < 5; i++) {
            List<List<Configuration>> asConfig = divide(picture);

            List<List<Configuration>> transformation = asConfig.stream()
                    .map(row -> row.stream().map(x -> result.get(initial.indexOf(x))).toList())
                    .toList();

            picture = recombine(transformation);
        }
        // System.out.println(new Configuration(picture, picture.length));

        return Arrays.stream(picture)
                .mapToInt(x -> {
                    int count = 0;
                    for (boolean b : x) if (b) count++;
                    return count;
                })
                .sum();
    }

    private static int partB(String data) {
        List<Configuration> initial = parseInitialRules(data);
        List<Configuration> result = parseResultRules(data);

        boolean[][] picture = Configuration.makeConfig(".#./..#/###").lights;

        for (int i = 0; i < 18; i++) {
            List<List<Configuration>> asConfig = divide(picture);

            List<List<Configuration>> transformation = asConfig.stream()
                    .map(row -> row.stream().map(x -> result.get(initial.indexOf(x))).toList())
                    .toList();

            picture = recombine(transformation);
        }
        // System.out.println(new Configuration(picture, picture.length));

        return Arrays.stream(picture)
                .mapToInt(x -> {
                    int count = 0;
                    for (boolean b : x) if (b) count++;
                    return count;
                })
                .sum();
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();

        AocUtils.sendPuzzleAnswer(1, partA(data));
        AocUtils.sendPuzzleAnswer(2, partB(data));
    }
}
