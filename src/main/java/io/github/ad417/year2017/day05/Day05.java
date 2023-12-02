package io.github.ad417.year2017.day05;

import tk.vivas.adventofcode.AocUtils;

import java.util.Arrays;

public class Day05 {
    private static int partA(Integer[] nums) {
        Integer[] tape = nums.clone();
        int count = 0;
        try {
            int index = 0;
            while (true) {
                int val = nums[index];
                nums[index]++;
                index += val;
                count++;
            }
        } catch (IndexOutOfBoundsException boundsException) {
            // Nothing
        } finally {
            return count;
        }
    }

    private static int partB(Integer[] nums) {
        Integer[] tape = nums.clone();
        int count = 0;
        try {
            int index = 0;
            while (true) {
                int val = nums[index];
                if (nums[index] >= 3) nums[index]--;
                else nums[index]++;
                index += val;
                count++;
            }
        } catch (IndexOutOfBoundsException boundsException) {
            // Nothing
        } finally {
            return count;
        }
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput();
        Integer[] numbers = data.lines().map(Integer::parseInt).toArray(Integer[]::new);

        AocUtils.sendPuzzleAnswer(1, partA(numbers));
        // Hooray, pointer bs. How is there no Arrays.deepcopy()?
        numbers = data.lines().map(Integer::parseInt).toArray(Integer[]::new);
        AocUtils.sendPuzzleAnswer(2, partB(numbers));

    }

}
