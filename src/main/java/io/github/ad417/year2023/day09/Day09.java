package io.github.ad417.year2023.day09;

import tk.vivas.adventofcode.AocUtils;

import java.util.*;

public class Day09 {
    private static List<List<Integer>> getHistories(String[] lines) {
        List<List<Integer>> histories  = new ArrayList<>(lines.length);
        for (String line : lines) {
            List<Integer> history = Arrays.stream(line.split(" "))
                    .map(Integer::parseInt)
                    .toList();
            histories.add(history);
        }
        return histories;
    }
    private static int partA(List<List<Integer>> histories) {
        int sum = 0;
        for (List<Integer> history : histories) {
            Stack<List<Integer>> layers = new Stack<>();
            List<Integer> layer = history;
            layers.push(layer);

            boolean allZero = false;
            while (!allZero) {
                allZero = true;
                List<Integer> nextLayer = new ArrayList<>(layer.size() - 1);
                for (int i = 0; i < layer.size()-1; i++) {
                    int val = layer.get(i+1) - layer.get(i);
                    if (val != 0) allZero = false;
                    nextLayer.add(val);
                }
                layer = nextLayer;
                layers.push(nextLayer);
            }
            int nextVal = 0;
            while (!layers.isEmpty()) {
                layer = layers.pop();
                nextVal += layer.get(layer.size() - 1);
            }
            sum += nextVal;
        }
        return sum;
    }

    private static long partB(List<List<Integer>> histories) {
        int sum = 0;
        for (List<Integer> history : histories) {
            Stack<List<Integer>> layers = new Stack<>();
            List<Integer> layer = history;
            layers.push(layer);

            boolean allZero = false;
            while (!allZero) {
                allZero = true;
                List<Integer> nextLayer = new ArrayList<>(layer.size() - 1);
                for (int i = 0; i < layer.size()-1; i++) {
                    int val = layer.get(i+1) - layer.get(i);
                    if (val != 0) allZero = false;
                    nextLayer.add(val);
                }
                layer = nextLayer;
                layers.push(nextLayer);
            }
            int nextVal = 0;
            while (!layers.isEmpty()) {
                layer = layers.pop();
                nextVal = layer.get(0) - nextVal;
            }
            sum += nextVal;
        }
        return sum;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        String[] lines = data.split("\n");
        List<List<Integer>> histories = getHistories(lines);

        AocUtils.sendPuzzleAnswer(1, partA(histories));
        AocUtils.sendPuzzleAnswer(2, partB(histories));
    }
}
