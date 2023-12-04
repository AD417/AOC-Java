package io.github.ad417.year2017;

public class KnotHash {
    private static class Hasher {
        private static final int SIZE = 256;
        int pos = 0;
        int skipSize = 0;

        int[] steps;
        int[] sparse;
        public Hasher(String input) {
            steps = new int[input.length()+5];
            for (int i = 0; i < input.length(); i++) {
                steps[i] = input.charAt(i);
            }
            steps[steps.length-5] = 17;
            steps[steps.length-4] = 31;
            steps[steps.length-3] = 73;
            steps[steps.length-2] = 47;
            steps[steps.length-1] = 23;

            sparse = new int[SIZE];
            for (int i = 0; i < SIZE; i++) {
                sparse[i] = i;
            }

            for (int i = 0; i < 64; i++) knotHashStep();
        }

        private void knotHashStep() {

            for (int step : steps) {
                int circleIndex = pos;
                int[] subList = new int[step];
                for (int i = 0; i < step; i++) {
                    subList[i] = sparse[circleIndex];
                    circleIndex++;
                    if (circleIndex == SIZE) circleIndex = 0;
                }

                circleIndex--;
                if (circleIndex == -1) circleIndex = SIZE - 1;

                for (int i = 0; i < step; i++) {
                    sparse[circleIndex] = subList[i];
                    circleIndex--;
                    if (circleIndex == -1) circleIndex = SIZE-1;
                }

                pos += step + skipSize;
                pos %= SIZE;
                skipSize++;
            }
        }

        private int[] getDense() {
            int[] dense = new int[16];
            for (int i = 0; i < SIZE; i++) {
                dense[i/16] ^= sparse[i];
            }
            return dense;
        }

        public String getHexHash() {
            int[] dense = getDense();

            StringBuilder sb = new StringBuilder();
            for (int value : dense) {
                String a = Integer.toHexString(value);
                if (a.length() == 1) sb.append("0");
                sb.append(a);
            }
            return sb.toString();
        }

        public String getBinaryHash() {
            int[] dense = getDense();
            StringBuilder sb = new StringBuilder();
            for (int value : dense) {
                String a = Integer.toBinaryString(value);
                sb.append("00000000".substring(a.length()));
                sb.append(a);
            }
            return sb.toString();
        }
    }

    public static String hexHash(String s) {
        return new Hasher(s).getHexHash();
    }

    public static String binaryHash(String s) {
        return new Hasher(s).getBinaryHash();
    }
}
