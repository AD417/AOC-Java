package io.github.ad417.year2023.day05;

import tk.vivas.adventofcode.AocUtils;

import java.util.*;
import java.util.stream.Collectors;

public class Day05 {
    /**
     * A range of integer values, starting at some number, and containing
     * range numbers.
     * @param start the first number in the range.
     * @param range the number of numbers in the range.
     */
    record Range(long start, long range) {
        /** A range with nothing in it. */
        public static Range EMPTY = new Range(-1,-1);
        /**
         * Determine the last number in this range.
         * @return the last number included in this range.
         */
        long end() {
            return start + range - 1;
        }
    }

    /**
     * A range of integer values, starting at `sourceStart` containing
     * `range` numbers, that maps onto an identical range starting at `destStart`
     * @param destStart where the destination range begins.
     * @param sourceStart where the source range begins.
     * @param range the number of values in both ranges. Includes the starting number.
     */
    private record RangeMap(long destStart, long sourceStart, long range) {
        /**
         * RangeMap factory for the input to Day 5.
         * @param line a string containing 3 space-separated numbers.
         * @return the RangeMap defined by those numbers.
         */
        public static RangeMap create(String line) {
            String[] parts = line.split(" ");
            long destStart = Long.parseLong(parts[0]);
            long sourceStart = Long.parseLong(parts[1]);
            long range = Long.parseLong(parts[2]);
            return new RangeMap(destStart, sourceStart, range);
        }

        /**
         * Determine the last number in the range.
         * @return the last number included in the range.
         */
        public long sourceEnd() {
            // e = s + r - 1
            // r = e - s + 1
            return sourceStart + range - 1;
        }

        /**
         * Determine if a value lies within the source range of this map.
         * @param value the value to check for inclusion.
         * @return true if in the range, false otherwise.
         */
        public boolean inRange(long value) {
            return (value >= sourceStart && value < sourceStart + range);
        }

        /**
         * Determine what a value maps to according to this range.
         * If the input value falls outside the range, then return -1.
         * @param value the value to map to the destination range.
         * @return the value after mapping iff it's in the source range;
         * -1 otherwise.
         */
        public long getDestination(long value) {
            if (!inRange(value)) return -1L;
            return value - sourceStart + destStart;
        }

        /**
         * Determine if the source range overlaps with the provided range,
         * and compute exactly what the intersection range is.
         * @param range the range to check for intersection.
         * @return the entirety of the range where an intersection occurs.
         * If there is no intersection, returns {@code Range.EMPTY}.
         */
        public Range getIntersection(Range range) {
            long start = Math.max(range.start, sourceStart);
            long end = Math.min(range.end(), sourceEnd());
            if (start > end) return Range.EMPTY;

            return new Range(start, end - start + 1);
        }

        /**
         * Determine what part of the input range maps to the destination, and
         * split off the destination range. If there is no overlap between
         * source and the given range, returns {@code Range.EMPTY}.
         * @param range the range to map from the source to the destination.
         * @return the destination range, iff the range intersects the source
         * range; {@code Range.EMPTY} otherwise.
         */
        public Range getDestinationRange(Range range) {
            Range includedFragment = getIntersection(range);
            if (includedFragment.equals(Range.EMPTY)) return Range.EMPTY;
            long start = getDestination(includedFragment.start);

            return new Range(start, includedFragment.range);
        }
    }

    /**
     * Determine the numbers used as Seeds in the input data.
     * @param data the input data for this problem.
     * @return an array containing all the seeds.
     */
    private static long[] getSeeds(String data) {
        String[] seedStr = data.split("\n")[0].split(" ");
        long[] seeds = new long[seedStr.length-1];
        for (int i = 0; i < seedStr.length-1; i++) {
            seeds[i] = Long.parseLong(seedStr[i+1]);
        }
        return seeds;
    }

    /**
     * Get all the maps used for all the entries in the "Almanac" for this
     * problem.
     * @param data the input data for this problem.
     * @return a list containing lists of RangeMaps. Each sublist comprises
     * the entirety of the positive {@link Long} space, and is one component of
     * the input almanac data. A seed needs to be mapped through all of them to
     * get its position, the answer.
     */
    private static List<List<RangeMap>> getAlmanac(String data) {
        String[] groups = data.split("\n\n");
        List<List<RangeMap>> almanac = new ArrayList<>(groups.length - 1);

        for (int i = 1; i < groups.length; i++) {
            String numbers = groups[i].split("\n", 2)[1];

            List<RangeMap> map = numbers.lines()
                    .map(RangeMap::create)
                    .sorted(Comparator.comparing(RangeMap::sourceStart))
                    .collect(Collectors.toCollection(LinkedList::new));

            map.addAll(padPiecewiseMap(map));

            almanac.add(map);
        }
        return almanac;
    }

    /**
     * Pad out a RangeMap list to deal with any value in the set of Integers.
     * @param map a partially complete almanac entry detailing all explicit
     *            mappings.
     * @return a closure for the provided map that
     */
    private static List<RangeMap> padPiecewiseMap(List<RangeMap> map) {
        // The mappings are basically just a very long-winded piecewise
        // function of the following form:
        // f(x) = {
        //      a1 <= x < (a1 + r1) --> x - a1 + d1
        //      a2 <= x < (a2 + r2) --> x - a2 + d2
        //      ...
        //      an <= x < (an + rn) --> x - an + dn
        //      else --> x
        // }
        // Where a = source starting, r = range, and d = destination starting.
        //
        // This function serves to remove the "else" block and replace it with
        // even more piecewise components, like so:
        // f(x) = {
        //      0 <= x < a1 --> x
        //      a1 <= x < (a1 + r1) --> x - a1 + d1
        //      (a1 + r1) <= x < a2 --> x
        //      a2 <= x < (a2 + r2) --> x - a2 + d2
        //      ...
        //      an <= x < (an + rn) --> x - an + dn
        //      (an + rn) <= x [< Long.MAX_VALUE] --> x
        // }
        //
        // This process simplifies a bunch of range calculations in the actual
        // solving part, as the ranges no longer need to determine what
        // components, if any, are not mapped.
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
        return missing;
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
