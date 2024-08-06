package io.github.ad417.year2023.day23;

import io.github.ad417.util.Coordinate;
import tk.vivas.adventofcode.AocUtils;

import java.time.Instant;
import java.util.*;

public class Day23 {
    private static HashMap<Coordinate, HashMap<Coordinate, Integer>> getRoutes(String input) {
        char[][] lines = Arrays.stream(input.split("\n")).map(String::toCharArray).toArray(char[][]::new);
        int maxRows = lines.length;
        int maxCols = lines[0].length;

        HashMap<Coordinate, HashMap<Coordinate, Integer>> allRoutes = new HashMap<>();
        List<Coordinate> two = new LinkedList<>();
        List<Coordinate> one = new LinkedList<>();
        Queue<Coordinate> zero = new LinkedList<>();

        // Determine all the locations where slopes are, and categorize them
        // based on number of entry points.
        for (int row = 0; row < lines.length; row++) {
            if (row == 0 || row+1 == maxRows) continue;
            for (int col = 0; col < lines[0].length; col++) {
                if (col == 0 || col+1 == maxCols) continue;
                Coordinate pos = new Coordinate(row, col);
                if (pos.orthogonal().stream().anyMatch(x -> lines[x.row()][x.col()] == '.')) continue;
                int slopeEntries = 2;
                Coordinate near = pos.add(-1, 0);
                if (lines[near.row()][near.col()] == '#') slopeEntries--;
                near = pos.add(0, -1);
                if (lines[near.row()][near.col()] == '#') slopeEntries--;

                if (slopeEntries == 2) two.add(pos);
                if (slopeEntries == 1) one.add(pos);
            }
        }

        // Add the starting point and ending point.
        zero.add(new Coordinate(0, new String(lines[0]).indexOf('.')));
        Coordinate end = new Coordinate(maxRows-1, new String(lines[maxRows-1]).indexOf('.'));
        one.add(end);
        Set<Coordinate> visited = new HashSet<>();

        // Determine the distance between two slope points (or the beginning or end).
        while (!zero.isEmpty()) {
            Coordinate slopePos = zero.poll();
            visited.add(slopePos);

            if (slopePos.equals(end)) continue;

            HashMap<Coordinate, Integer> routes = new HashMap<>();

            List<Coordinate> step = new LinkedList<>();
            Coordinate near = slopePos.add(0, 1);
            if (lines[near.row()][near.col()] != '#') step.add(near);
            near = slopePos.add(1, 0);
            if (lines[near.row()][near.col()] != '#') step.add(near);
            int steps = 1;
            while (!step.isEmpty()) {
                List<Coordinate> nextStep = new LinkedList<>();
                while (!step.isEmpty()) {
                    Coordinate pos = step.remove(0);
                    //System.out.println(pos);
                    if (visited.contains(pos)) continue;
                    if (two.contains(pos) || one.contains(pos)) {
                        if (one.remove(pos)) {
                            zero.add(pos);
                        }
                        if (two.remove(pos)) {
                            one.add(pos);
                        }
                        routes.put(pos, steps);
                        continue;
                    }
                    visited.add(pos);
                    for (Coordinate orth : pos.orthogonal()) {
                        if (visited.contains(orth)) continue;
                        if (!orth.inBounds(maxRows, maxCols) || lines[orth.row()][orth.col()] == '#') continue;
                        nextStep.add(orth);
                    }
                }
                steps++;
                step = nextStep;
            }
            allRoutes.put(slopePos, routes);
            //break;
        }
        return allRoutes;
    }

    private static HashMap<Coordinate, HashMap<Coordinate, Integer>> getCompleteRoutes(HashMap<Coordinate, HashMap<Coordinate, Integer>> routes) {
        HashMap<Coordinate, HashMap<Coordinate, Integer>> completeRoutes = new HashMap<>(routes);
        for (Coordinate here : routes.keySet()) {
            HashMap<Coordinate, Integer> routesFromHere = routes.get(here);
            if (routesFromHere == null) continue;
            for (Coordinate there : routesFromHere.keySet()) {
                HashMap<Coordinate, Integer> routesFromThere = completeRoutes.get(there);
                if (routesFromThere == null) continue;
                routesFromThere.put(here, routesFromHere.get(there));
            }
        }
        return completeRoutes;
    }

    private static int moveToSlope(HashMap<Coordinate, HashMap<Coordinate, Integer>> routes, Coordinate pos, int distance) {
        HashMap<Coordinate, Integer> routesFromHere = routes.get(pos);
        if (routesFromHere == null) return distance;
        return routesFromHere.keySet().stream()
                .mapToInt(k -> moveToSlope(routes, k, distance+routesFromHere.get(k)))
                .max().orElse(0);
    }

    private static int partA(String data) {
        HashMap<Coordinate, HashMap<Coordinate, Integer>> routes = getRoutes(data);
        Coordinate start = new Coordinate(0,1);
        return moveToSlope(routes, start, 0);
    }

    private static int moveToSlope(
            HashMap<Coordinate, HashMap<Coordinate, Integer>> routes, Set<Coordinate> visited,
            Coordinate pos, int distance
    ) {
        HashMap<Coordinate, Integer> routesFromHere = routes.get(pos);
        if (routesFromHere == null) return distance;

        int maxDist = 0;
        for (Coordinate newPos : routesFromHere.keySet()) {
            if (visited.contains(newPos)) continue;
            HashSet<Coordinate> newVisited = new HashSet<>(visited);
            newVisited.add(newPos);
            maxDist = Math.max(maxDist, moveToSlope(routes, newVisited, newPos, distance+routesFromHere.get(newPos)));
        }
        return maxDist;
    }

    private static int fastMove(HashMap<Coordinate, HashMap<Coordinate, Integer>> routes, HashSet<Coordinate> visited,
                                Coordinate pos, int distance) {
        visited.add(pos);
        HashMap<Coordinate, Integer> routesFromHere = routes.get(pos);
        if (routesFromHere == null) {
            visited.remove(pos);
            return distance;
        }
        int maxDist = 0;
        for (Coordinate newPos : routesFromHere.keySet()) {
            if (visited.contains(newPos)) continue;
            maxDist = Math.max(maxDist, fastMove(routes, visited, newPos, distance + routesFromHere.get(newPos)));
        }

        visited.remove(pos);
        return maxDist;
    }

    private static int partB(String data) {
        HashMap<Coordinate, HashMap<Coordinate, Integer>> routes = getCompleteRoutes(getRoutes(data));
        Coordinate start = new Coordinate(0,1);
        return fastMove(routes, new HashSet<>(), start, 0);
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        System.out.println(partA(data));
        System.out.println(partB(data));
    }

}
