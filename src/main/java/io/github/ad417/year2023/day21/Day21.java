package io.github.ad417.year2023.day21;

import io.github.ad417.util.Coordinate;
import tk.vivas.adventofcode.AocUtils;

import java.util.*;
import java.util.stream.Collectors;

public class Day21 {
    private static Coordinate getStart(String input) {
        String[] lines = input.split("\n");
        for (int row = 0; row < lines.length; row++) {
            char[] line = lines[row].toCharArray();
            for (int col = 0; col < line.length; col++) {
                if (line[col] == 'S') {
                    return new Coordinate(row, col);
                }
            }
        }
        throw new RuntimeException("Whoops!");
    }
    private static HashSet<Coordinate> getBlocks(String input) {
        HashSet<Coordinate> blocks = new HashSet<>();
        String[] lines = input.split("\n");
        for (int row = 0; row < lines.length; row++) {
            char[] line = lines[row].toCharArray();
            for (int col = 0; col < line.length; col++) {
                if (line[col] == '#') {
                    blocks.add(new Coordinate(row, col));
                }
            }
        }
        return blocks;
    }
    private static int partA(HashSet<Coordinate> blocks, Coordinate start) {
        // A nonstandard usage of caches to determine the number of steps.
        // 64 steps is even, so in 64 steps the "even" set will contain all of
        // the tiles that the walker can reach.
        HashSet<Coordinate> odd = new HashSet<>();
        HashSet<Coordinate> even = new HashSet<>();

        int STEPS = 64;
        Set<Coordinate> frontier = new HashSet<>();
        frontier.add(start);
        for (int i = 0; i < STEPS/2; i++) {
            Set<Coordinate> nextFrontier = new HashSet<>();
            for (Coordinate pos : frontier) {
                even.add(pos);
                for (Coordinate orth : pos.orthogonal()) {
                    if (blocks.contains(orth)) continue;
                    if (odd.contains(orth)) continue;
                    nextFrontier.add(orth);
                }
            }
            frontier = new HashSet<>();
            for (Coordinate pos : nextFrontier) {
                odd.add(pos);
                for (Coordinate orth : pos.orthogonal()) {
                    if (blocks.contains(orth)) continue;
                    if (even.contains(orth)) continue;
                    frontier.add(orth);
                }
            }
        }
        even.addAll(frontier);
        return even.size();
    }

    private static boolean formsHistory(List<Integer> sequence) {
        List<Integer> layer = sequence;
        boolean allZero;
        for (int i = 0; i < sequence.size() - 1; i++) {
            allZero = true;
            List<Integer> nextLayer = new ArrayList<>(layer.size() - 1);
            for (int k = 0; k < layer.size() - 1; k++) {
                int val = layer.get(k + 1) - layer.get(k);
                if (val != 0) allZero = false;
                nextLayer.add(val);
            }
            // System.out.println(nextLayer);
            layer = nextLayer;
            if (allZero) return true;
        }
        return false;
    }

    private static List<List<Long>> createHistory(List<Integer> sequence) {
        List<Long> layer = sequence.stream().map(Integer::longValue).collect(Collectors.toCollection(LinkedList::new));
        boolean allZero;
        List<List<Long>> layers = new LinkedList<>();
        for (int i = 0; i < sequence.size() - 1; i++) {
            allZero = true;
            List<Long> nextLayer = new LinkedList<>();
            for (int k = 0; k < layer.size() - 1; k++) {
                long val = layer.get(k + 1) - layer.get(k);
                if (val != 0) allZero = false;
                nextLayer.add(val);
            }
            // System.out.println(nextLayer);
            layers.add(layer);
            layer = nextLayer;
            if (allZero) {
                layers.add(nextLayer);
                return new ArrayList<>(layers);
            }
        }
        throw new IllegalArgumentException("Argument does not form history!");
    }

    private static long partB(HashSet<Coordinate> blocks, Coordinate start) {
        // This part involves a few interesting properties with the input.
        // First, the grid is always 131 x 131, so the center point is, with
        // indexing from 0, (65,65).
        // Second, the value used, 26,501,365, is exactly
        //      (2023 * 100 * 131) + 65
        // which is an exact number of map wrappings, plus the distance to
        // reach the edge of the map.
        // Third, the map has a diamond cut out around 60-65 units from the
        // center of the grid. This is useful because it ensures that, no
        // matter how dense the rocks are in the input, you can get to any
        // point past the diamond.
        //
        // So, how does the math work here?
        // Since the grid is 131 units wide, going to the "Parallel Universe"
        // to the left results in us changing parity, so we end up visiting
        Coordinate wrapPoint = start.mult(2).add(1,1);
        /*System.out.println(blocks);
        System.out.println("WRAP: " + wrapPoint);
        System.out.println(new Coordinate(-3, 2).wrap(wrapPoint));*/

        Set<Coordinate> frontier = new HashSet<>();
        frontier.add(start);

        HashSet<Coordinate> odd = new HashSet<>();
        HashSet<Coordinate> even = new HashSet<>();

        List<Integer> sequence = new LinkedList<>();

        for (int i = 1; i <= 26501365; i++) {
            HashSet<Coordinate> currentPos;
            HashSet<Coordinate> adjacentPos;
            if (i % 2 == 1) {
                currentPos = even;
                adjacentPos = odd;
            } else {
                currentPos = odd;
                adjacentPos = even;
            }
            HashSet<Coordinate> nextFrontier = new HashSet<>();

            for (Coordinate pos : frontier) {
                currentPos.add(pos);
                for (Coordinate orth : pos.orthogonal()) {
                    if (blocks.contains(orth.wrap(wrapPoint))) continue;
                    if (adjacentPos.contains(orth)) continue;
                    nextFrontier.add(orth);
                }
            }
            frontier = nextFrontier;

            if (i % 262 == 66) {
                System.out.println(i + ": " + currentPos.size());
                sequence.add(currentPos.size());
                System.out.println(sequence);
                if (formsHistory(sequence)) break;
                // System.out.println(formsHistory(sequence));
            }
        }
        // TODO: Actually interpolate the sequence.
        List<List<Long>> history = createHistory(sequence);

        int effectiveSteps = 65 + 262 * (sequence.size() - 1);
        while (effectiveSteps < 26501365) {
            history.get(history.size()-1).add(0L);
            for (int i = history.size()-2; i >= 0; i--) {
                List<Long> currentLayer = history.get(i);
                List<Long> lastLayer = history.get(i+1);
                currentLayer.add(currentLayer.get(currentLayer.size()-1) + lastLayer.get(lastLayer.size()-1));
            }
            effectiveSteps += 262;
        }
        // history.forEach(x -> System.out.println(x.get(x.size()-1)));

        return history.get(0).get(history.get(0).size() - 1);
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        /*data = """
                ...........
                .....###.#.
                .###.##..#.
                ..#.#...#..
                ....#.#....
                .##..S####.
                .##..#...#.
                .......##..
                .##.#.####.
                .##..##.##.
                ...........""";*/
        Coordinate start = getStart(data);
        HashSet<Coordinate> blocks = getBlocks(data);

        System.out.println(partA(blocks, start));
        System.out.println(partB(blocks, start));
    }

}
