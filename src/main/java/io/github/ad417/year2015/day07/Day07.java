package io.github.ad417.year2015.day07;

import tk.vivas.adventofcode.AocUtils;

import java.util.Arrays;
import java.util.HashMap;

public class Day07 {
    private static boolean isInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
    private static HashMap<String, String[]> getLogic(String[] lines) {
        HashMap<String, String[]> logic = new HashMap<>(lines.length);
        for (String line : lines) {
            String[] components = line.split(" ");
            String[] gate = new String[components.length - 2];
            String output = components[components.length - 1];
            if (components.length == 5) {
                gate[0] = components[1];
                gate[1] = components[0];
                gate[2] = components[2];
            } else {
                System.arraycopy(components, 0, gate, 0, gate.length);
            }
            logic.put(output, gate);
        }
        return logic;
    }

    private static int valueOf(String wire, HashMap<String, Integer> wireValues, HashMap<String, String[]> logic) {
        if (isInt(wire)) return Integer.parseInt(wire);
        if (wireValues.containsKey(wire)) return wireValues.get(wire);

        String[] gate = logic.get(wire);
        String operation = gate[0];
        if (gate.length == 1) {
            wireValues.put(wire, valueOf(operation, wireValues, logic));
            return wireValues.get(wire);
        }

        int param1, param2 = 0;
        param1 = valueOf(gate[1], wireValues, logic);
        if (gate.length > 2) {
            param2 = valueOf(gate[2], wireValues, logic);
        }

        int out = 0;
        switch (operation) {
            case "AND" -> out = param1 & param2;
            case "OR" -> out = param1 | param2;
            case "NOT" -> out = ~param1;
            case "LSHIFT" -> out = param1 << param2;
            case "RSHIFT" -> out = param1 >> param2;
        }
        out &= 0xFFFF;
        wireValues.put(wire, out);
        return out;
    }

    private static int partA(HashMap<String, String[]> logic) {
        HashMap<String, Integer> wireValues = new HashMap<>();
        return valueOf("a", wireValues, logic);
    }

    private static int partB(HashMap<String, String[]> logic) {
        HashMap<String, Integer> wireValues = new HashMap<>();
        int a = valueOf("a", wireValues, logic);
        logic.put("b", new String[] { String.valueOf(a) });
        wireValues = new HashMap<>();
        return valueOf("a", wireValues, logic);
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        String[] lines = data.split("\n");
        HashMap<String, String[]> logic = getLogic(lines);

        AocUtils.sendPuzzleAnswer(1, partA(logic));
        AocUtils.sendPuzzleAnswer(2, partB(logic));
    }
}
