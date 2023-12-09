package io.github.ad417.year2015.day09;

import tk.vivas.adventofcode.AocUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Day09 {
    private static HashMap<String, HashMap<String, Integer>> getMap(String data) {
        HashMap<String, HashMap<String, Integer>> map = new HashMap<>();
        data.lines().forEach(line -> {
            String[] components = line.split(" ");
            HashMap<String, Integer> subMap;
            if (map.containsKey(components[0])) subMap = map.get(components[0]);
            else {
                subMap = new HashMap<>();
                map.put(components[0], subMap);
            }
            subMap.put(components[2], Integer.valueOf(components[4]));
            if (map.containsKey(components[2])) subMap = map.get(components[2]);
            else {
                subMap = new HashMap<>();
                map.put(components[2], subMap);
            }
            subMap.put(components[0], Integer.valueOf(components[4]));
        });
        return map;
    }

    private static int findShortestPath(String currentPos, int currentDistance, HashSet<String> visited, HashMap<String, HashMap<String, Integer>> map) {
        if (visited.size() == map.size()) return currentDistance;
        HashMap<String, Integer> toVisit = map.get(currentPos);

        int shortestPath = Integer.MAX_VALUE;
        for (String place : toVisit.keySet()) {
            if (visited.contains(place)) continue;
            int distance = toVisit.get(place);
            HashSet<String> nowVisited = new HashSet<>(visited);
            nowVisited.add(place);
            int shortestFromPlace = findShortestPath(place, currentDistance + distance, nowVisited, map);
            if (shortestPath > shortestFromPlace) shortestPath = shortestFromPlace;
        }
        return shortestPath;
    }

    private static int partA(HashMap<String, HashMap<String, Integer>> map) {
        return map.keySet().stream()
                .mapToInt(place -> {
                    HashSet<String> visited = new HashSet<>();
                    visited.add(place);
                    return findShortestPath(place, 0, visited, map);
                })
                .min()
                .orElseThrow();
    }

    private static int findLongestPath(String currentPos, int currentDistance, HashSet<String> visited, HashMap<String, HashMap<String, Integer>> map) {
        if (visited.size() == map.size()) return currentDistance;
        HashMap<String, Integer> toVisit = map.get(currentPos);

        int shortestPath = Integer.MIN_VALUE;
        for (String place : toVisit.keySet()) {
            if (visited.contains(place)) continue;
            int distance = toVisit.get(place);
            HashSet<String> nowVisited = new HashSet<>(visited);
            nowVisited.add(place);
            int shortestFromPlace = findLongestPath(place, currentDistance + distance, nowVisited, map);
            if (shortestPath < shortestFromPlace) shortestPath = shortestFromPlace;
        }
        return shortestPath;
    }

    private static int partB(HashMap<String, HashMap<String, Integer>> map) {
        return map.keySet().stream()
                .mapToInt(place -> {
                    HashSet<String> visited = new HashSet<>();
                    visited.add(place);
                    return findLongestPath(place, 0, visited, map);
                })
                .max()
                .orElseThrow();
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        HashMap<String, HashMap<String, Integer>> map = getMap(data);

        AocUtils.sendPuzzleAnswer(1, partA(map));
        AocUtils.sendPuzzleAnswer(2, partB(map));
    }
}
