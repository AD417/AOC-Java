package io.github.ad417.year2023.day08;

import tk.vivas.adventofcode.AocUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day08 {
    private static HashMap<String, String[]> getMap(String data) {
        HashMap<String, String[]> map = new HashMap<>();
        Pattern encoding = Pattern.compile("(\\w+) = \\((\\w+), (\\w+)\\)");
        data.lines().forEach(line -> {
            Matcher m = encoding.matcher(line);
            if (!m.find()) return;
            String[] leftRight = new String[] { m.group(2), m.group(3) };
            map.put(m.group(1), leftRight);
        });
        return map;
    }
    private static int partA(String moves, HashMap<String, String[]> map) {
        // Just step through the provided instructions until we reach the
        // destination. Nothing special here.
        int moveCount = 0;
        String currentPos = "AAA";
        while (!currentPos.equals("ZZZ")) {
            char move = moves.charAt(moveCount % moves.length());
            if (move == 'L') currentPos = map.get(currentPos)[0];
            else currentPos = map.get(currentPos)[1];
            moveCount++;
        }
        return moveCount;
    }

    private static int partBNaive(String moves, HashMap<String, String[]> map) {
        Set<String> currentPos = map.keySet().stream()
                .filter(x -> x.lastIndexOf('A') == 2)
                .collect(Collectors.toSet());
        System.out.println(currentPos);
        boolean someNotYetAtZ = true;
        int moveCount = 0;
        while (someNotYetAtZ) {
            char move = moves.charAt(moveCount % moves.length());
            if (move == 'L') {
                currentPos = currentPos.stream().map(x -> map.get(x)[0]).collect(Collectors.toSet());
            } else {
                currentPos = currentPos.stream().map(x -> map.get(x)[1]).collect(Collectors.toSet());
            }
            moveCount++;
            someNotYetAtZ = currentPos.stream().anyMatch(x -> x.lastIndexOf('Z') != 2);
            if (moveCount % 10_000_000 == 0) System.out.println(currentPos);
        }
        return moveCount;
    }

    private static String smallestIn(HashMap<String, Long> map) {
        long smallest = Long.MAX_VALUE;
        String smallestStr = null;
        for (String k : map.keySet()) {
            if (map.get(k) < smallest) {
                smallest = map.get(k);
                smallestStr = k;
            }
        }
        return smallestStr;
    }

    private static long partBYetStillNaive(String moves, HashMap<String, String[]> map) {
        HashMap<String, Long> AtoZSteps = new HashMap<>();
        HashMap<String, String> AtoZ = new HashMap<>();

        HashMap<String, Long> ZtoZSteps = new HashMap<>();
        HashMap<String, String> ZtoZ = new HashMap<>();
        map.keySet().stream()
                .filter(x -> x.endsWith("A"))
                .forEach(point -> {
                    String start = point;
                    long moveCount = 0;
                    while (!point.endsWith("Z")) {
                        char move = moves.charAt((int) moveCount++ % moves.length());
                        if (move == 'L') point = map.get(point)[0];
                        else point = map.get(point)[1];
                    }
                    AtoZ.put(start, point);
                    AtoZSteps.put(start, moveCount);

                    start = point;
                    moveCount = 0;
                    do {
                        char move = moves.charAt((int) moveCount++ % moves.length());
                        if (move == 'L') point = map.get(point)[0];
                        else point = map.get(point)[1];
                    } while (!point.endsWith("Z"));
                    ZtoZ.put(start, point);
                    ZtoZSteps.put(start, moveCount);
                });
        System.out.println(ZtoZ);
        boolean notEqual = true;
        int runs = 0;
        while (notEqual) {
            if (runs++ % 1000000 == 0) System.out.println(AtoZSteps);
            String next = smallestIn(AtoZSteps);
            String currentPos = AtoZ.get(next);

            AtoZSteps.put(next, AtoZSteps.get(next) + ZtoZSteps.get(currentPos));
            AtoZ.put(next, ZtoZ.get(currentPos));

            notEqual = AtoZSteps.values().stream().anyMatch(x -> !Objects.equals(x, AtoZSteps.get("AAA")));
        }
        return AtoZSteps.get("AAA");
    }

    private static long LCM(long a, long b) {
        long aFactor = a, bFactor = b;
        while (bFactor > 0) {
            long c = aFactor;
            aFactor = bFactor;
            bFactor = c % aFactor;
        }
        return a / aFactor * b;
    }

    private static long partB(String moves, HashMap<String, String[]> map) {
        // This is the first input-specific problem.
        // It turns out that the input has 2 properties we can exploit.
        //
        // 1.   The distance from any XXA tile to XXZ is always evenly
        //      divisible by the length of the movement instructions. This
        //      means the Instructions are irrelevant after you compute them
        //      the first time.
        //
        // 2.   The number of steps required to go from XXA to XXZ is the same
        //      As going from XXZ to XXZ, because XXZ's "neighbors" are the
        //      same as XXA's neighbors. This means that there is a cycle of
        //      A known length for each XXA-XXZ input.
        //
        // By leveraging both of these properties, we find that the answer is
        // going to be whenever all the XXZs arrive at the same time. This
        // takes too long to do by hand, so we instead take the LCM of all
        // the cycle lengths.
        Stack<Long> AtoZ = new Stack<>();
        Stack<Long> ZtoZ = new Stack<>();
        map.keySet().stream()
                .filter(x -> x.endsWith("A"))
                .forEach(point -> {
                    String start = point;
                    long moveCount = 0;
                    while (!point.endsWith("Z")) {
                        char move = moves.charAt((int) moveCount++ % moves.length());
                        if (move == 'L') point = map.get(point)[0];
                        else point = map.get(point)[1];
                    }
                    AtoZ.push(moveCount);
                    moveCount = 0;
                    do {
                        char move = moves.charAt((int) moveCount++ % moves.length());
                        if (move == 'L') point = map.get(point)[0];
                        else point = map.get(point)[1];
                    } while (!point.endsWith("Z"));
                    ZtoZ.push(moveCount);
                });
        // ZtoZ == AtoZ, somehow.
        long steps = AtoZ.pop();
        /////// aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
        while (!AtoZ.isEmpty()) steps = LCM(steps, AtoZ.pop());
        return steps;
        /*long steps = AtoZ.pop();
        long factor = 1;
        while (!ZtoZ.isEmpty()) {
            long mod = ZtoZ.pop();
            long remainder = AtoZ.pop();
            while (steps % mod != remainder) {
                steps += factor;
                System.out.println(steps);
            }
            factor = LCM(factor, mod);
        }
        return steps;*/
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        String[] parts = data.split("\n\n");

        HashMap<String, String[]> map = getMap(parts[1]);

        AocUtils.sendPuzzleAnswer(1, partA(parts[0], map));
        AocUtils.sendPuzzleAnswer(2, partB(parts[0], map));
    }
}
