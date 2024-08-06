package io.github.ad417.year2023.day12;

import tk.vivas.adventofcode.AocUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Day12 {
    record SpringConfig(List<Character> status, List<Integer> groups) {
        static final char UNKNOWN = '?';
        static final char SPRING = '#';
        static final char EMPTY = '.';

        static SpringConfig makeConfig(String line) {
            String[] components = line.split(" ");
            List<Character> status = components[0].chars().mapToObj(x -> (char)x).toList();
            List<Integer> groups = Arrays.stream(components[1].split(",")).map(Integer::parseInt).toList();
            return new SpringConfig(status, groups);
        }

        SpringConfig unfold() {
            List<Character> unfoldedStatus = new LinkedList<>();
            List<Integer> unfoldedGroups = new LinkedList<>();
            for (int i = 0; i < 5; i++) {
                unfoldedGroups.addAll(groups);
                unfoldedStatus.addAll(status);
            }
            return new SpringConfig(unfoldedStatus, unfoldedGroups);
        }

        boolean isComplete() {
            return !status.contains(UNKNOWN);
        }

        boolean isValid() {
            int continuous = 0;
            int groupsSeen = 0;
            int pos;
            for (pos = 0; pos < status.size() && status.get(pos) != UNKNOWN; pos++) {
                if (status.get(pos) == SPRING) {
                    continuous++;
                    if (groupsSeen == groups.size()) return false;
                    if (continuous > groups.get(groupsSeen)) return false;
                }
                else {
                    if (continuous == 0) continue;
                    if (continuous < groups.get(groupsSeen)) return false;
                    continuous = 0;
                    groupsSeen++;
                }
            }
            // Jank logic alert.
            if (pos != status.size()) return true;
            if (groupsSeen == groups.size()) return true;
            if (groupsSeen == groups.size() - 1) {
                return continuous == groups.get(groupsSeen);
            }
            return false;
        }

        List<SpringConfig> branch() {
            List<SpringConfig> potentialConfigs = new LinkedList<>();
            int pos = status.indexOf(UNKNOWN);
            if (pos == -1) return potentialConfigs;

            List<Character> potentialStatus = new LinkedList<>(status);
            potentialStatus.set(pos, SPRING);
            potentialConfigs.add(new SpringConfig(potentialStatus, groups));

            potentialStatus = new LinkedList<>(status);
            potentialStatus.set(pos, EMPTY);
            potentialConfigs.add(new SpringConfig(potentialStatus, groups));

            return potentialConfigs;
        }
    }
    static int partA(List<SpringConfig> initialConfigs) {
        // Brute force searching, using DFS with Backtracking.
        // Needless to say, it doesn't work for part B.
        List<SpringConfig> configs = initialConfigs;
        int possibilities = 0;
        while (!configs.isEmpty()) {
            List<SpringConfig> futureConfigs = new LinkedList<>();
            for (SpringConfig config : configs) {
                //System.out.println(config);
                if (!config.isValid()) continue;
                //System.out.println(config.isComplete());
                if (config.isComplete()) {
                    //System.out.println(config);
                    possibilities++;
                    continue;
                }
                futureConfigs.addAll(config.branch());
            }
            //System.out.println(futureConfigs.size());
            configs = futureConfigs;
        }

        return possibilities;
    }

    private static int partB(List<SpringConfig> initialConfigs) {
        // Very naive implementation of Part B.
        List<SpringConfig> configs = initialConfigs.stream().map(SpringConfig::unfold).toList();
        int possibilities = 0;
        while (!configs.isEmpty()) {
            List<SpringConfig> futureConfigs = new LinkedList<>();
            for (SpringConfig config : configs) {
                //System.out.println(config);
                if (!config.isValid()) continue;
                //System.out.println(config.isComplete());
                if (config.isComplete()) {
                    //System.out.println(config);
                    possibilities++;
                    continue;
                }
                futureConfigs.addAll(config.branch());
            }
            System.out.println(futureConfigs.size());
            configs = futureConfigs;
        }

        return possibilities;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        List<SpringConfig> configs = data.lines().map(SpringConfig::makeConfig).toList();

        // System.out.println(partA(configs));

        AocUtils.sendPuzzleAnswer(1, partA(configs));
        AocUtils.sendPuzzleAnswer(2, partB(configs));
    }
}
