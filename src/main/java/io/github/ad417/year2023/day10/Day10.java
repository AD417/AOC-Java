package io.github.ad417.year2023.day10;

import tk.vivas.adventofcode.AocUtils;

import java.util.*;

public class Day10 {
    static Coordinate origin = null;
    record Coordinate(int row, int col) {
        public Coordinate upscale() {
            return new Coordinate(3 * row + 1, 3 * col + 1);
        }
    }

    private static HashMap<Coordinate, List<Coordinate>> getConnections(String data) {
        HashMap<Coordinate, List<Coordinate>> connections = new HashMap<>();
        String[] lines = data.split("\n");
        for (int row = 0; row < lines.length; row++) {
            char[] line = lines[row].toCharArray();
            for (int col = 0; col < line.length; col++) {
                Coordinate pos = new Coordinate(row, col);
                Coordinate[] adj = new Coordinate[2];
                switch (line[col]) {
                    case '|' -> adj = new Coordinate[] { new Coordinate(row-1, col), new Coordinate(row+1, col) };
                    case '-' -> adj = new Coordinate[] { new Coordinate(row, col-1), new Coordinate(row, col+1) };
                    case 'L' -> adj = new Coordinate[] { new Coordinate(row-1, col), new Coordinate(row, col+1) };
                    case 'J' -> adj = new Coordinate[] { new Coordinate(row-1, col), new Coordinate(row, col-1) };
                    case 'F' -> adj = new Coordinate[] { new Coordinate(row+1, col), new Coordinate(row, col+1) };
                    case '7' -> adj = new Coordinate[] { new Coordinate(row+1, col), new Coordinate(row, col-1) };
                    case '.' -> { continue; }
                    case 'S' -> {
                        origin = pos;
                        continue;
                    }
                }
                connections.put(pos, List.of(adj));
            }
        }
        List<Coordinate> adjList = new LinkedList<>();
        Coordinate adj;
        adj = new Coordinate(origin.row-1, origin.col);
        if (connections.containsKey(adj) && connections.get(adj).contains(origin)) adjList.add(adj);
        adj = new Coordinate(origin.row+1, origin.col);
        if (connections.containsKey(adj) && connections.get(adj).contains(origin)) adjList.add(adj);
        adj = new Coordinate(origin.row, origin.col+1);
        if (connections.containsKey(adj) && connections.get(adj).contains(origin)) adjList.add(adj);
        adj = new Coordinate(origin.row, origin.col-1);
        if (connections.containsKey(adj) && connections.get(adj).contains(origin)) adjList.add(adj);
        connections.put(origin, adjList);
        return connections;
    }

    private static Set<Coordinate> getUpscaledConnections(
            HashMap<Coordinate, List<Coordinate>> currentConnections, List<Coordinate> loopConnections) {
        Set<Coordinate> upscaled = new HashSet<>();

        for (Coordinate c : loopConnections) {
            Coordinate upscale = c.upscale();
            upscaled.add(upscale);
            Coordinate adj;
            adj = new Coordinate(c.row+1, c.col);
            if (currentConnections.get(c).contains(adj)) {
                upscaled.add(new Coordinate(upscale.row+1, upscale.col));
            }
            adj = new Coordinate(c.row-1, c.col);
            if (currentConnections.get(c).contains(adj)) {
                upscaled.add(new Coordinate(upscale.row-1, upscale.col));
            }
            adj = new Coordinate(c.row, c.col+1);
            if (currentConnections.get(c).contains(adj)) {
                upscaled.add(new Coordinate(upscale.row, upscale.col+1));
            }
            adj = new Coordinate(c.row, c.col-1);
            if (currentConnections.get(c).contains(adj)) {
                upscaled.add(new Coordinate(upscale.row, upscale.col-1));
            }
        }
        return upscaled;
    }
    private static int partA(HashMap<Coordinate, List<Coordinate>> connections) {
        // Use a one-way BFS to determine all the tiles connected to the start
        // point. The loop will always be an even number of tiles long, and the
        // furthest point is always (length / 2) tiles from the start.
        List<Coordinate> seen = new LinkedList<>();
        seen.add(origin);
        Coordinate current = connections.get(origin).get(0);

        while (!current.equals(origin)) {
            seen.add(current);
            Coordinate next = connections.get(current).get(0);
            if (next.equals(seen.get(seen.size()-2)))
                next = connections.get(current).get(1);
            current = next;
        }
        return seen.size() / 2;
    }

    private static int partB(HashMap<Coordinate, List<Coordinate>> connections) {
        // There are more elegant solutions with Pick's Theorem, but I used
        // the Ray Casting algorithm. Start at your point, go in an arbitrary
        // direction (north), and check how many times you cross the wall. If
        // odd, then you are within the shape. Else, you are outside.
        //
        // Some map expansion has to be done to address the fact that hitting
        // a wall dead-on doesn't tell you if the beam would cross it. Taking
        // Each wall tile and expanding it to a 3x3 is enough for this.
        List<Coordinate> inLoop = new LinkedList<>();
        inLoop.add(origin);
        Coordinate current = connections.get(origin).get(0);

        while (!current.equals(origin)) {
            inLoop.add(current);
            Coordinate next = connections.get(current).get(0);
            if (next.equals(inLoop.get(inLoop.size()-2)))
                next = connections.get(current).get(1);
            current = next;
        }

        int enclosed = 0;
        Set<Coordinate> upscaledInLoop = getUpscaledConnections(connections, inLoop);

        int maxRow = connections.keySet().stream().mapToInt(Coordinate::row).max().orElseThrow();
        int maxCol = connections.keySet().stream().mapToInt(Coordinate::col).max().orElseThrow();

        Queue<Coordinate> bfsQueue = new LinkedList<>();
        for (int row = 0; row <= maxRow; row++) {
            for (int col = 0; col <= maxCol; col++) {
                Coordinate pos = new Coordinate(row, col).upscale();
                if (upscaledInLoop.contains(pos)) continue;
                pos = new Coordinate(pos.row-1, pos.col-1);

                int crossings = 0;
                Coordinate tracer;
                for (int traceRow = pos.row; traceRow >= 0; traceRow--) {
                    tracer = new Coordinate(traceRow, pos.col);
                    if (upscaledInLoop.contains(tracer)) crossings++;
                }
                if (crossings % 2 == 1) enclosed++;
            }
        }
        return enclosed;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        HashMap<Coordinate, List<Coordinate>> connections = getConnections(data);

        AocUtils.sendPuzzleAnswer(1, partA(connections));
        AocUtils.sendPuzzleAnswer(2, partB(connections));
    }

}
