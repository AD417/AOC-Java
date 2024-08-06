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
            // Classic factory method, converts the lines into the relevant data structure.
            String[] components = line.split(" ");
            List<Character> status = components[0].chars().mapToObj(x -> (char)x).toList();
            List<Integer> groups = Arrays.stream(components[1].split(",")).map(Integer::parseInt).toList();
            return new SpringConfig(status, groups, 0, 0);
        }

        SpringConfig unfold() {
            // "Unfolds" the data for part B.
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
            // Helper method so I can write out less complex if statements.
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
            // Analyze the next group and next tiles, and determine where, if
            // anywhere, we can actually put the next group.
            // For example, given the following:
            //      .#.?????#?.#??. 1,3,2
            // Assuming the first group is complete, we can have ant of the
            // following:
            //      .#.###??#?.#??.
            //      .#.?###?#?.#??.
            //      .#.???###?.#??.
            //      .#.????###.#??.
            // This creates the list of future configurations.
            // We stop as soon as we get past some existing tile. If we didn't,
            // then we would end up with a config matching 1,1,3,2, which
            // would not be valid.
            // If there are no valid configs from the half-done config, then
            // we return nothing.
            // We could just store the data structure as 2 numbers, but this
            // made visualization and debugging significantly harder while I
            // was working on it.
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
            // We only care about the groups made and the end position of the
            // last group. ideally, we should never compare two springConfigs
            // that come from different lines, but the last bit is just a
            // sanity check.
            // If two configs match, then that means that their futures are
            // identical and we can aggregate them together.
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
            // A comparison is necessary to ensure that we complete the first
            // parts of ALL other sequences before we parse this one. As such,
            // by the time we process this one, we know that there are no more
            // ways that this configuration can be made.
            int out = this.lastGroupPos - o.lastGroupPos;
            if (out == 0) return this.groupsMade - o.groupsMade;
            return out;
        }
    }
    private static long partA(List<SpringConfig> initialConfigs) {
        // Another Dynamic Programming solution, using a similar memoization
        // trick to Day 04 with the scratchcards.
        //
        // Instead of going pixel by pixel in the damaged image, we go region
        // by region, removing groups as we go.
        //
        // Consider the following:
        //      ###....#.????? 3,1,1,1
        //      .###...#.????? 3,1,1,1
        //      ..###..#.????? 3,1,1,1
        //      ...###.#.????? 3,1,1,1
        // At this point, the remainder is identical, so we can aggregate them
        // into one possibility that appears several times.
        // Through memoization and sorting the input by tiles left to uncover,
        // We can ensure that by the time we process some configuration we have
        // ensured no more copies of that configuration can be made.
        // This results in effectively optimal solving.
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
        System.out.println("Things looked at: " + steps);
        return possibilities;
    }

    private static long partB(List<SpringConfig> initialConfigs) {
        // Same as part A, except we actually unfold the list this time.
        // Only checks some 130,000 configurations. Effectively instant for
        // machines.
        List<SpringConfig> actualConfigs = initialConfigs.stream().map(SpringConfig::unfold).toList();
        return partA(actualConfigs);
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        // data = "??????????????????????? 3,1,2,3,2";

        List<SpringConfig> configs = data.lines().map(SpringConfig::makeConfig).toList();

        AocUtils.sendPuzzleAnswer(1, partA(configs));
        AocUtils.sendPuzzleAnswer(2, partB(configs));
    }
}
