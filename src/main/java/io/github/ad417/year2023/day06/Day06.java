package io.github.ad417.year2023.day06;

import tk.vivas.adventofcode.AocUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day06 {
    private static int[] parseInput(String data) {
        Pattern nums = Pattern.compile("\\d+");
        String[] lines = data.split("\n");

        Matcher time = nums.matcher(lines[0]);
        Matcher distance = nums.matcher(lines[1]);

        List<Integer> out = new LinkedList<>();
        while (time.find() && distance.find()) {
            out.add(Integer.parseInt(time.group()));
            out.add(Integer.parseInt(distance.group()));
        }
        return out.stream().mapToInt(Integer::intValue).toArray();
    }
    private static long[] parseLegitInput(String data) {
        Pattern nums = Pattern.compile("\\d+");
        String[] lines = data.split("\n");

        Matcher time = nums.matcher(lines[0]);
        Matcher distance = nums.matcher(lines[1]);

        StringBuilder trueTime = new StringBuilder();
        StringBuilder trueDistance = new StringBuilder();


        while (time.find() && distance.find()) {
            trueTime.append(time.group());
            trueDistance.append(distance.group());
        }
        return new long[] {
                Long.parseLong(trueTime.toString()),
                Long.parseLong(trueDistance.toString())
        };
    }
    private static int partA(int[] values) {
        int product = 1;
        for (int i = 0; i < values.length; i+=2) {
            int time = values[i];
            int distance = values[i+1];

            int wins = 0;
            int holdTime = time / 2;
            int runTime = time - holdTime;
            while (runTime * holdTime > distance) {
                wins += 2;
                holdTime--;
                runTime++;
            }
            if (time % 2 == 0) wins--;
            product *= wins;
        }
        return product;
    }

    private static long partB(long[] values) {
        long time = values[0];
        long distance = values[1];

        long min = 0;
        long max = time / 2;
        long mid = (min + max) / 2;
        // Fuck yeah, binary search
        while (max - min > 1) {
            if (mid * (time - mid) > distance) {
                max = mid;
            } else {
                min = mid;
            }
            mid = (min + max) / 2;
        }
        return time - mid * 2 - 1;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        int[] vals = parseInput(data);

        AocUtils.sendPuzzleAnswer(1, partA(vals));
        long[] trueVals = parseLegitInput(data);
        AocUtils.sendPuzzleAnswer(2, partB(trueVals));
    }
}
