package io.github.ad417.year2017.day07;

import tk.vivas.adventofcode.AocUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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

    private static HashMap<String, Disk> parseData(String data) {
        HashMap<String, Disk> map = new HashMap<>();

        String[] lines = data.split("\n");
        for (String line : lines) {
            String name = line.split(" ")[0];
            int weight = Integer.parseInt(line.split("[()]")[1]);
            Disk disk = new Disk(weight);
            map.put(name, disk);

            if (!line.contains(" -> ")) continue;
            String[] children = line.split(" -> ")[1].split(", ");
            disk.addAll(Arrays.asList(children));
        }
        return map;
    }

    private static String partA(HashMap<String, Disk> map) {
        List<String> allChildren = new LinkedList<>();
        map.forEach((_key, list) -> allChildren.addAll(list));
        for(String name : map.keySet()) {
            if (!allChildren.contains(name)) return name;
        }
        return null;
    }

    private static int getWeightOn(String name, HashMap<String, Disk> map) {
        Disk disk = map.get(name);
        int totalWeight = disk.getWeight();
        for (String child : disk) {
            totalWeight += getWeightOn(child, map);
        }
        return totalWeight;
    }

    private static int partB(HashMap<String, Disk> map) {
        String unbalancedParent = "";
        String unbalanced = partA(map);
        Disk disk = map.get(unbalanced);

        while (!disk.isEmpty()) {
            // Get the total weights of the children of this disk.
            List<Integer> weights = disk.stream().map(x -> getWeightOn(x, map)).toList();
            int unbalancedIndex = -1;
            // If any are unbalanced, this will find it.
            for (int i = 1; i < weights.size(); i++) {
                if (!Objects.equals(weights.get(i), weights.get(0))) {
                    if (unbalancedIndex != -1) {
                        unbalancedIndex = 0;
                        break;
                    }
                    unbalancedIndex = i;
                }
            }

            if (unbalancedIndex == -1) {
                // This disk is unbalanced, yet its children are.
                // We found the wrong disk.
                break;
            }
            // Else, check the unbalanced child now.
            unbalancedParent = unbalanced;
            unbalanced = disk.get(unbalancedIndex);
            disk = map.get(unbalanced);
        }

        // Find a correctly weighted disk from the other disks the unbalanced parent is holding.
        final String finalUnbalanced = unbalanced;
        int correctWeight = map.get(unbalancedParent)
                .stream()
                .filter(x -> !x.equals(finalUnbalanced))
                .findFirst()
                .map(x -> getWeightOn(x, map)).orElse(-1);

        // Determine how far off the unbalanced disk is, and correct for it.
        int delta = correctWeight - getWeightOn(unbalanced, map);
        return delta + map.get(unbalanced).getWeight();
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput();

        HashMap<String, Disk> map =  parseData(data);

        AocUtils.sendPuzzleAnswer(1, partA(map));
        AocUtils.sendPuzzleAnswer(2, partB(map));
    }
}