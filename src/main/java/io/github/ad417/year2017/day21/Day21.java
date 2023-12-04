package io.github.ad417.year2017.day21;

import tk.vivas.adventofcode.AocUtils;

import java.util.Arrays;

public class Day21 {
    private static class Configuration {
        boolean[][] lights;
        int dim;
        private Configuration(boolean[][] lights, int dim) {
            this.lights = lights;
            this.dim = dim;
        }

        private Configuration makeConfig(String code) {
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

        public Configuration rotate(int times) {
            int dimRot = dim - 1;
            boolean[][] now = Arrays.stream(lights).map(x -> Arrays.copyOf(x, x.length)).toArray(boolean[][]::new);
            boolean[][] next = new boolean[dim][dim];

            for (int i = 0; i < times; i++) {
                for (int row = 0; row < dim; row++) {
                    for (int col = 0; col < dim; col++) {

                    }
                }
            }
            return this;
        }
    }
    private static int partA(String[] lines) {
        return 0;
    }

    private static int partB(String[] lines) {
        return 0;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        String[] lines = data.lines().toArray(String[]::new);

        AocUtils.sendPuzzleAnswer(1, partA(lines));
        AocUtils.sendPuzzleAnswer(2, partB(lines));
    }
}
