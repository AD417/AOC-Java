package io.github.ad417.year2017.day10;

import tk.vivas.adventofcode.AocUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day10 {
    private static final int SIZE = 256;

    private static int pos = 0;
    private static int skipSize = 0;

    private static int[] initialArray() {
        int[] array = new int[SIZE];
        for (int i = 0; i < SIZE; i++) {
            array[i] = i;
        }
        return array;
    }

    private static int[] knotHashStep(int[] numbers, Integer[] knots) {

        for (int knot : knots) {
            int circleIndex = pos;
            int[] subList = new int[knot];
            for (int i = 0; i < knot; i++) {
                subList[i] = numbers[circleIndex];
                circleIndex++;
                if (circleIndex == SIZE) circleIndex = 0;
            }

            circleIndex--;
            if (circleIndex == -1) circleIndex = SIZE - 1;

            for (int i = 0; i < knot; i++) {
                numbers[circleIndex] = subList[i];
                circleIndex--;
                if (circleIndex == -1) circleIndex = SIZE-1;
            }

            pos += knot + skipSize;
            if (pos >= SIZE) pos %= SIZE;
            skipSize++;
        }

        return numbers;
    }

    private static int partA(String data) {
        Integer[] knots = Arrays.stream(data.trim().split(",")).map(Integer::parseInt).toArray(Integer[]::new);
        int[] array = initialArray();
        pos = 0;
        skipSize = 0;

        int[] result = knotHashStep(array, knots);
        return result[0] * result[1];
    }

    private static String partB(String data) {
        ArrayList<Integer> numbers = Arrays.stream(data.trim().split(""))
                .map(x -> (int)x.charAt(0))
                .collect(Collectors.toCollection(ArrayList::new));
        numbers.addAll(List.of(17, 31, 73, 47, 23));
        Integer[] knots = numbers.toArray(Integer[]::new);

        pos = 0;
        skipSize = 0;

        int[] hash = initialArray();
        for (int i = 0; i < 64; i++) {
            hash = knotHashStep(hash, knots);
        }

        int[] dense = new int[16];
        for (int i = 0; i < SIZE; i++) {
            dense[i/16] ^= hash[i];
        }
        System.out.println(Arrays.toString(dense));
        StringBuilder sb = new StringBuilder();
        for (int value : dense) {
            String a = Integer.toHexString(value);
            if (a.length() == 1) sb.append("0");
            sb.append(a);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput();

        AocUtils.sendPuzzleAnswer(1, partA(data));
        AocUtils.sendPuzzleAnswer(2, partB(data));
    }
}
