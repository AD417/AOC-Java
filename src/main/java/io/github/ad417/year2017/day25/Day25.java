package io.github.ad417.year2017.day25;

import tk.vivas.adventofcode.AocUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day25 {
    record TuringState(String name, boolean oneToZero, boolean leftAsOne, boolean zeroToOne, boolean leftAsZero, String nextIfZero, String nextIfOne) {
        public static TuringState createState(String data) {
            Pattern leftRight = Pattern.compile("left|right");
            Pattern nextState = Pattern.compile("(?<=state )\\w+");
            Pattern valueWritten = Pattern.compile("(?<=value )[01]");
            String[] lines = data.lines().map(String::trim).toArray(String[]::new);

            String name = lines[0].substring(9, lines[0].length()-1);

            boolean oneToZero = false;
            boolean zeroToOne = false;
            boolean leftAsZero = false;
            boolean leftAsOne = false;
            String nextIfZero = "";
            String nextIfOne = "";
            boolean zero = true;

            for (int i = 2; i < lines.length; i++) {
                String line = lines[i];
                if (line.startsWith("If the")) {
                    zero = false;
                    continue;
                }
                Matcher m;
                m = leftRight.matcher(line);
                if (m.find()) {
                    if (zero) leftAsZero = m.group().equals("left");
                    else leftAsOne = m.group().equals("left");
                }

                m = nextState.matcher(line);
                if (m.find()) {
                    if (zero) nextIfZero = m.group();
                    else nextIfOne = m.group();
                }

                m = valueWritten.matcher(line);
                if (m.find()) {
                    String val = m.group();
                    if (zero) {
                        if (val.equals("1")) zeroToOne = true;
                    }
                    else {
                        if (val.equals("0")) oneToZero = true;
                    }
                }
            }
            return new TuringState(name, oneToZero, leftAsOne, zeroToOne, leftAsZero, nextIfZero, nextIfOne);
        }

        public TuringOperation tick(Set<Integer> onePositions, int headPos) {
            if (onePositions.contains(headPos)) {
                // ONE!
                if (oneToZero) onePositions.remove(headPos);
                if (leftAsOne) headPos--;
                else headPos++;
                return new TuringOperation(nextIfOne, headPos);
            } else {
                // ZERO!
                if (zeroToOne) onePositions.add(headPos);
                if (leftAsZero) headPos--;
                else headPos++;
                return new TuringOperation(nextIfZero, headPos);
            }
        }
    }

    record TuringOperation(String nextState, int headPos) {}

    public static HashMap<String, TuringState> createMachines(String data) {
        String[] states = data.split("\n\n");
        HashMap<String, TuringState> machines = new HashMap<>();
        for (int i = 1; i < states.length; i++) {
            String name = states[i].substring(9, 10);
            machines.put(name, TuringState.createState(states[i]));
        }
        return machines;
    }

    public static TuringOperation getStartingInfo(String data) {
        String header = data.split("\n\n")[0];
        String startingState = Pattern.compile("(?<=state )\\w+")
                .matcher(header).results().findFirst().orElseThrow().group();
        String iterations = Pattern.compile("(?<=after )\\d+")
                .matcher(header).results().findFirst().orElseThrow().group();
        return new TuringOperation(startingState, Integer.parseInt(iterations));
    }

    private static int partA(HashMap<String, TuringState> machines, TuringOperation initial) {
        int checkSumTime = initial.headPos;
        TuringOperation current = new TuringOperation(initial.nextState, 0);

        HashSet<Integer> ones = new HashSet<>();

        int headPos = 0;
        for (int i = 0; i < checkSumTime; i++) {
            headPos = current.headPos;
            TuringState currentState = machines.get(current.nextState);
            current = currentState.tick(ones, headPos);
        }
        return ones.size();
    }

    private static int partB(HashMap<String, TuringState> machines, TuringOperation initial) {
        System.out.println("You did it!");
        return Integer.MAX_VALUE;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();

        HashMap<String, TuringState> machines = createMachines(data);
        TuringOperation initial = getStartingInfo(data);

        AocUtils.sendPuzzleAnswer(1, partA(machines, initial));
        AocUtils.sendPuzzleAnswer(2, partB(machines, initial));
    }
}
