package io.github.ad417.year2015.day02;

import tk.vivas.adventofcode.AocUtils;

public class Day02 {
    record Present(int x, int y, int z) {
        public static Present create(String line) {
            String[] size = line.split("x");
            return new Present(
                    Integer.parseInt(size[0]),
                    Integer.parseInt(size[1]),
                    Integer.parseInt(size[2])
            );
        }

        public int paperNeeded() {
            int boxSize = 2 * (x*y + y*z + x*z);
            int slack = Math.min(Math.min(x*y, y*z), x*z);
            return boxSize + slack;
        }

        public int ribbonNeeded() {
            int wrapSize = 2 * (x + y + z - Math.max(Math.max(x, y), z));
            int bowSize = x * y * z;
            return wrapSize + bowSize;
        }
    }
    private static int partA(String data) {
        return data.lines().map(Present::create).mapToInt(Present::paperNeeded).sum();
    }

    private static int partB(String data) {
        return data.lines().map(Present::create).mapToInt(Present::ribbonNeeded).sum();
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();

        AocUtils.sendPuzzleAnswer(1, partA(data));
        AocUtils.sendPuzzleAnswer(2, partB(data));
    }
}
