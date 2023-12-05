package io.github.ad417.year2023.day05;

import tk.vivas.adventofcode.AocUtils;

import java.util.*;
import java.util.stream.Collectors;

public class Day05 {
    private record Range(long start, long range) {
        static Range EMPTY = new Range(-1,-1);
        long end() {
            return start + range - 1;
        }
    }
    private record RangeMap(long destStart, long sourceStart, long range) {
        public static RangeMap create(String line) {
            String[] parts = line.split(" ");
            long destStart = Long.parseLong(parts[0]);
            long sourceStart = Long.parseLong(parts[1]);
            long range = Long.parseLong(parts[2]);
            return new RangeMap(destStart, sourceStart, range);
        }

        public long sourceEnd() {
            // e = s + r - 1
            // r = e - s + 1
            return sourceStart + range - 1;
        }

        public boolean inRange(long value) {
            return (value >= sourceStart && value < sourceStart + range);
        }

        public long getDestination(long value) {
            if (!inRange(value)) return -1L;
            return value - sourceStart + destStart;
        }

        public Range getIntersection(Range range) {
            long start = Math.max(range.start, sourceStart);
            long end = Math.min(range.end(), sourceEnd());
            if (start > end) return Range.EMPTY;

            return new Range(start, end - start + 1);
        }

        public Range getDestinationRange(Range range) {
            Range includedFragment = getIntersection(range);
            if (includedFragment.equals(Range.EMPTY)) return Range.EMPTY;
            long start = getDestination(includedFragment.start);

            return new Range(start, includedFragment.range);
        }
    }

    private static long[] getSeeds(String data) {
        String[] seedStr = data.split("\n")[0].split(" ");
        long[] seeds = new long[seedStr.length-1];
        for (int i = 0; i < seedStr.length-1; i++) {
            seeds[i] = Long.parseLong(seedStr[i+1]);
        }
        return seeds;
    }

    private static List<List<RangeMap>> getAlmanac(String data) {
        String[] groups = data.split("\n\n");
        List<List<RangeMap>> almanac = new ArrayList<>(groups.length - 1);

        for (int i = 1; i < groups.length; i++) {
            String numbers = groups[i].split("\n", 2)[1];
            List<RangeMap> map = numbers.lines()
                    .map(RangeMap::create)
                    .sorted(Comparator.comparing(RangeMap::sourceStart))
                    .collect(Collectors.toCollection(LinkedList::new));

            long start = 0;
            long end;
            List<RangeMap> missing = new LinkedList<>();
            for (RangeMap rm : map) {
                end = rm.sourceStart-1;
                missing.add(new RangeMap(start, start, end - start));
                start = rm.sourceEnd() + 1;
            }
            end = Long.MAX_VALUE;
            missing.add(new RangeMap(start, start, end - start));
            map.addAll(missing);

            almanac.add(map);
        }
        return almanac;
    }

    private static long partA(List<List<RangeMap>> almanac, long[] seeds) {
        for (List<RangeMap> entry : almanac) {
            for (int i = 0; i < seeds.length; i++) {
                final int index = i;
                long genericMap = seeds[i];
                seeds[i] = entry.stream()
                        .mapToLong(rm -> rm.getDestination(seeds[index]))
                        .max()
                        .orElseThrow();
                // if (seeds[i] == -1) seeds[i] = genericMap;
            }
            System.out.println(Arrays.toString(seeds));
        }
        return Arrays.stream(seeds).min().orElseThrow();
    }

    private static long partB(List<List<RangeMap>> almanac, long[] seeds) {
        List<Range> actualSeeds = new LinkedList<>();
        for (int i = 0; i < seeds.length; i+= 2) {
            actualSeeds.add(new Range(seeds[i], seeds[i+1]));
        }
        List<Range> layer = actualSeeds;
        for (List<RangeMap> entry : almanac) {
            List<Range> nextLayer = new LinkedList<>();
            for (RangeMap rm : entry) {
                nextLayer.addAll(layer.stream()
                        .map(rm::getDestinationRange)
                        .filter(x -> !x.equals(Range.EMPTY))
                        .toList());
            }
            layer = nextLayer;
        }
        return layer.stream()
                .mapToLong(Range::start)
                .min()
                .orElseThrow();
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();

        List<List<RangeMap>> almanac = getAlmanac(data);
        long[] seeds = getSeeds(data);

        AocUtils.sendPuzzleAnswer(1, partA(almanac, seeds));
        seeds = getSeeds(data);
        AocUtils.sendPuzzleAnswer(2, partB(almanac, seeds));
    }
}
