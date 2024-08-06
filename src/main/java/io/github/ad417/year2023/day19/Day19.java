package io.github.ad417.year2023.day19;

import tk.vivas.adventofcode.AocUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day19 {
    record Range(int start, int end) {
        List<Range> split(int point, boolean less) {
            // Less < 2000
            // Means 1 - 1999, 2000 - 4000
            // Greater > 2000
            // Means 2001 - 4000, 1 - 2000
            // In the order pass : fail.
            List<Range> out = new ArrayList<>(2);
            if (less) {
                out.add(new Range(start, point-1));
                out.add(new Range(point, end));
            } else {
                out.add(new Range(point+1, end));
                out.add(new Range(start, point));
            }
            return out;
        }

        int size() {
            return end - start + 1;
        }
    }
    private static long partA(List<String[]> rules, List<List<Integer>> parts) {
        // This problem is similar in essence to Day 5, but significantly more
        // convoluted.
        // For each entry in the parts bin, check which workload it belongs to,
        // and either classify it as such or reject it outright.
        HashMap<String, List<String>> rulesMap = new HashMap<>();
        for (String[] rule : rules) {
            rulesMap.put(rule[0], Arrays.stream(rule).skip(1).toList());
        }

        Pattern ternary = Pattern.compile("([xmas])([<>])(\\d+)");

        HashMap<List<Integer>, String> ruleForPart = new HashMap<>();
        for (List<Integer> part : parts) {
            ruleForPart.put(part, "in");
        }

        long sum = 0;
        while (!ruleForPart.isEmpty()) {
            HashMap<List<Integer>, String> nextRuleForPart = new HashMap<>();
            for (List<Integer> part : ruleForPart.keySet()) {
                String ruleId = ruleForPart.get(part);

                if (ruleId.equals("R")) continue;
                if (ruleId.equals("A")) {
                    sum += part.stream().mapToLong(Integer::longValue).sum();
                    continue;
                }
                System.out.println(ruleId);
                List<String> rule = rulesMap.get(ruleId);
                int ruleIndex = 0;
                while (ruleIndex < rule.size()) {
                    String rulePart = rule.get(ruleIndex);
                    Matcher m = ternary.matcher(rulePart);
                    if (!m.find()) {
                        nextRuleForPart.put(part, rulePart);
                        break;
                    }
                    int value = part.get("xmas".indexOf(m.group(1)));
                    String cmp = m.group(2);
                    int test = Integer.parseInt(m.group(3));
                    if (cmp.equals(">")) {
                        if (value > test) ruleIndex++;
                        else ruleIndex += 2;
                    } else {
                        if (value < test) ruleIndex++;
                        else ruleIndex+= 2;
                    }
                }
            }
            ruleForPart = nextRuleForPart;
        }

        return sum;
    }

    private static long partB(List<String[]> rules) {
        // A significantly more involved version of part A, using Ranges a la
        // Day 5.
        // When a comparison is made, I take the portion of the range that
        // abides by the comparison and send it to the next workflow (or reject
        // it, etc.) and then take the remainder and process it through the
        // next comparison, if applicable.
        // Then, at the end, the total number of possible parts in any specific
        // range happens to be the product of the sizes of all the ranges.
        HashMap<String, List<String>> rulesMap = new HashMap<>();
        for (String[] rule : rules) {
            rulesMap.put(rule[0], Arrays.stream(rule).skip(1).toList());
        }
        rulesMap.forEach((k, v) -> System.out.println(k + " " + v));

        Pattern ternary = Pattern.compile("([xmas])([<>])(\\d+)");

        List<Range> initial = new LinkedList<>();
        for (int i = 0; i < 4; i++) {
            initial.add(new Range(1, 4000));
        }
        HashMap<List<Range>, String> ruleForPart = new HashMap<>();
        ruleForPart.put(initial, "in");

        long sum = 0;
        while (!ruleForPart.isEmpty()) {
            HashMap<List<Range>, String> nextRuleForPart = new HashMap<>();

            for (List<Range> part : ruleForPart.keySet()) {
                String ruleId = ruleForPart.get(part);

                if (ruleId.equals("R")) continue;
                if (ruleId.equals("A")) {
                    sum += part.stream().mapToLong(Range::size).reduce(1, (x, y) -> x * y);
                    continue;
                }

                List<String> rule = rulesMap.get(ruleId);
                int ruleIndex = 0;

                List<Range> remainder = part;

                while (ruleIndex < rule.size()) {
                    String rulePart = rule.get(ruleIndex);
                    Matcher m = ternary.matcher(rulePart);
                    if (!m.find()) {
                        nextRuleForPart.put(remainder, rulePart);
                        break;
                    }
                    int xmasIndex = "xmas".indexOf(m.group(1));
                    Range toSplit = remainder.get(xmasIndex);
                    String cmp = m.group(2);
                    int test = Integer.parseInt(m.group(3));

                    List<Range> split = toSplit.split(test, cmp.equals("<"));

                    List<Range> passes = new LinkedList<>(remainder);
                    passes.set(xmasIndex, split.get(0));

                    nextRuleForPart.put(passes, rule.get(ruleIndex+1));

                    remainder = new LinkedList<>(remainder);
                    remainder.set(xmasIndex, split.get(1));

                    ruleIndex += 2;
                }
            }
            ruleForPart = nextRuleForPart;
        }

        return sum;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        String[] components = data.split("\n\n");
        // Technically leaves a trailing "", but eh.
        List<String[]> rules = components[0].lines().map(x -> x.split("[{:,}]")).toList();
        List<List<Integer>> parts = components[1].lines()
                .map(x -> Pattern.compile("\\d+")
                        .matcher(x).results()
                        .map(val -> Integer.parseInt(val.group()))
                        .toList())
                .toList();

        System.out.println(partA(rules, parts));
        System.out.println(partB(rules));
        //AocUtils.sendPuzzleAnswer(1, partA(lines));
        //AocUtils.sendPuzzleAnswer(2, partB(lines));
    }
}
