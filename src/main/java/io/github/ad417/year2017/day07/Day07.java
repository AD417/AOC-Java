package io.github.ad417.year2017.day07;

import tk.vivas.adventofcode.AocUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Day07 {
    static class Disk extends ArrayList<String> {
        private final int weight;
        public Disk(int weight) {
            super();
            this.weight = weight;
        }
        public int getWeight() {
            return weight;
        }
    }

    private static HashMap<String, Disk> predecessorMap(String data) {
        HashMap<String, Disk> map = new HashMap<>();

        String[] lines = data.split("\n");
        for (String line : lines) {
            String name = line.split(" ")[0];
            int weight = Integer.parseInt(line.split("\\(")[1]);
            map.put(name, new Disk(weight));
        }
        for (String line : lines) {
            if (!line.contains(" -> ")) continue;
            String parent = line.split(" ")[0];
            String[] children = line.split(" -> ")[1].split(", ");
            for (String child : children) {
                map.get(child).add(parent);
            }
        }
        return map;
    }

    private static String partA(HashMap<String, Disk> map) {
        for (String parent : map.keySet()) {
            if (map.get(parent).isEmpty()) return parent;
        }
        return null;
    }

    private static int partB(HashMap<String, Disk> map) {
        throw new RuntimeException("Not implemented");
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput();

        HashMap<String, Disk> map =  predecessorMap(data);

        AocUtils.sendPuzzleAnswer(1, partA(map));
    }
}