package io.github.ad417.year2017.day06;

import tk.vivas.adventofcode.AocUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day06 {
    private static int partA(List<Integer> boxes) {
        int steps = 0;
        HashSet<List<Integer>> configs = new HashSet<>();
        while (!configs.contains(boxes)) {
            configs.add(boxes);
            int largest = IntStream.range(0, boxes.size()).boxed()
                    .max(Comparator.comparing(boxes::get)).orElse(-1);

            int toMove = boxes.get(largest);
            int boxIndex = largest;
            boxes.set(largest, 0);
            // "Count down to" operator.
            while (toMove --> 0) {
                boxIndex++;
                if (boxIndex >= boxes.size()) boxIndex = 0;
                boxes.set(boxIndex, boxes.get(boxIndex) + 1);
            }
            steps++;
        }
        return steps;
    }

    private static int partB(List<Integer> boxes) {
        // NOTE: boxes has been edited by PartA
        List<Integer> original = boxes.stream().map(Integer::new).collect(Collectors.toList());
        int steps = 0;
        do {
            int largest = IntStream.range(0, boxes.size()).boxed()
                    .max(Comparator.comparing(boxes::get)).orElse(-1);

            int toMove = boxes.get(largest);
            int boxIndex = largest;
            boxes.set(largest, 0);
            // "Count down to" operator.
            while (toMove --> 0) {
                boxIndex++;
                if (boxIndex >= boxes.size()) boxIndex = 0;
                boxes.set(boxIndex, boxes.get(boxIndex) + 1);
            }
            steps++;
        } while (!original.equals(boxes));

        return steps;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput();

        List<Integer> boxes;
        boxes = Arrays.stream(data.trim().split("\t")).map(Integer::parseInt).collect(Collectors.toList());
        // System.out.println(partA(boxes));
        AocUtils.sendPuzzleAnswer(1, partA(boxes));

        AocUtils.sendPuzzleAnswer(2, partB(boxes));
    }

}
