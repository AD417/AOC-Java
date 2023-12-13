package io.github.ad417.year2023.day12;

import tk.vivas.adventofcode.AocUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day12ButMaybeDP {
    record SpringConfig(List<Character> status, List<Integer> groups, int groupsMade, int lastGroupPos) implements Comparable<SpringConfig> {
        static final char UNKNOWN = '?';
        static final char SPRING = '#';
        static final char EMPTY = '.';

        static SpringConfig makeConfig(String line) {
            String[] components = line.split(" ");
            List<Character> status = components[0].chars().mapToObj(x -> (char)x).toList();
            List<Integer> groups = Arrays.stream(components[1].split(",")).map(Integer::parseInt).toList();
            return new SpringConfig(status, groups, 0, 0);
        }

        SpringConfig unfold() {
            List<Character> unfoldedStatus = new LinkedList<>();
            List<Integer> unfoldedGroups = new LinkedList<>();
            for (int i = 0; i < 5; i++) {
                if (i != 0) unfoldedStatus.add(UNKNOWN);
                unfoldedGroups.addAll(groups);
                unfoldedStatus.addAll(status);
            }
            return new SpringConfig(unfoldedStatus, unfoldedGroups, 0, 0);
        }

        char safeGet(int index) {
            if (index < 0 || index >= status.size()) return EMPTY;
            return status.get(index);
        }

        boolean isComplete() {
            return groupsMade == groups.size();
        }

        boolean isValid() {
            if (lastGroupPos >= status.size()) return true;
            return !status.subList(lastGroupPos, status.size()).contains(SPRING);
        }

        List<SpringConfig> createNextValidGroups() {
            List<SpringConfig> validGroups = new LinkedList<>();

            int groupSize = groups.get(groupsMade);
            for (int pos = lastGroupPos; pos <= status.size()-groupSize; pos++) {
                List<Character> springLocation = status.subList(pos, pos+groupSize);
                if (springLocation.contains(EMPTY)) {
                    if (status.get(pos) == SPRING) break;
                    continue;
                }
                if (safeGet(pos+groupSize) == SPRING || safeGet(pos-1) == SPRING) {
                    if (status.get(pos) == SPRING) break;
                    continue;
                }
                // There was some list stuff here to make the printed out configs comprehensible, but I deleted it.

                validGroups.add(new SpringConfig(status, groups, groupsMade+1, pos+groupSize+1));

                if (status.get(pos) == SPRING) break;
            }
            return validGroups;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SpringConfig that = (SpringConfig) o;
            return groupsMade == that.groupsMade && lastGroupPos == that.lastGroupPos && Objects.equals(groups, that.groups);
        }

        @Override
        public int hashCode() {
            return Objects.hash(groups, groupsMade, lastGroupPos);
        }

        @Override
        public int compareTo(SpringConfig o) {
            int out = this.lastGroupPos - o.lastGroupPos;
            if (out == 0) return this.groupsMade - o.groupsMade;
            return out;
        }
    }
    private static long partA(List<SpringConfig> initialConfigs) {
        long possibilities = 0;
        int steps = 0;
        for (SpringConfig initialConfig : initialConfigs) {
            HashMap<SpringConfig, Long> multiplier = new HashMap<>();
            multiplier.put(initialConfig, 1L);

            PriorityQueue<SpringConfig> queue = new PriorityQueue<>();
            queue.add(initialConfig);

            while (!queue.isEmpty()) {
                SpringConfig config = queue.poll();
                steps++;
                long mult = multiplier.get(config);
                if (config.isComplete()) {
                    if (config.isValid()) {
                        possibilities += mult;
                        if (possibilities < 0) System.out.println("OVERFLOW!");
                    }
                    continue;
                }
                for (SpringConfig futureConfig : config.createNextValidGroups()) {
                    if (multiplier.containsKey(futureConfig)) {
                        multiplier.put(futureConfig, multiplier.get(futureConfig) + mult);
                    } else {
                        multiplier.put(futureConfig, mult);
                        queue.offer(futureConfig);
                    }
                }
            }
        }
        //System.out.println("Things looked at: " + steps);
        return possibilities;
    }

    private static long partB(List<SpringConfig> initialConfigs) {
        List<SpringConfig> actualConfigs = initialConfigs.stream().map(SpringConfig::unfold).toList();
        return partA(actualConfigs);
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();


        List<SpringConfig> configs = data.lines().map(SpringConfig::makeConfig).toList();

        AocUtils.sendPuzzleAnswer(1, partA(configs));
        AocUtils.sendPuzzleAnswer(2, partB(configs));
    }
}
