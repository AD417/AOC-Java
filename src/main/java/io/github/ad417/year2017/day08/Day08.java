package io.github.ad417.year2017.day08;

import tk.vivas.adventofcode.AocUtils;

import java.util.Collections;
import java.util.HashMap;

public class Day08 {
    private static final String INCREMENT = "inc";

    private static HashMap<String, Integer> getRegisters(String[] lines) {
        // Assumes all registers are touched at some point in the program.
        // I could edit it to
        HashMap<String, Integer> registers = new HashMap<>();
        for (String line : lines) {
            String name = line.split(" ", 2)[0];
            registers.put(name, 0);
        }
        return registers;
    }

    private static int partA(String[] lines) {
        HashMap<String, Integer> registers = getRegisters(lines);

        for (String line : lines) {
            String[] code = line.split(" ");

            int delta = Integer.parseInt(code[2]);
            if (!code[1].equals(INCREMENT)) delta *= -1;

            boolean execute = switch (code[5]) {
                case "==" -> registers.get(code[4]) == Integer.parseInt(code[6]);
                case "!=" -> registers.get(code[4]) != Integer.parseInt(code[6]);
                case ">" -> registers.get(code[4]) > Integer.parseInt(code[6]);
                case "<" -> registers.get(code[4]) < Integer.parseInt(code[6]);
                case ">=" -> registers.get(code[4]) >= Integer.parseInt(code[6]);
                case "<=" -> registers.get(code[4]) <= Integer.parseInt(code[6]);
                default -> throw new IllegalStateException("Unexpected value: " + code[5]);
            };

            if (execute) {
                registers.put(code[0], registers.get(code[0]) + delta);
            }
        }
        return Collections.max(registers.values());
    }

    private static int partB(String[] lines) {
        HashMap<String, Integer> registers = getRegisters(lines);
        int max = 0;

        for (String line : lines) {
            String[] code = line.split(" ");

            int delta = Integer.parseInt(code[2]);
            if (!code[1].equals(INCREMENT)) delta *= -1;

            boolean execute = switch (code[5]) {
                case "==" -> registers.get(code[4]) == Integer.parseInt(code[6]);
                case "!=" -> registers.get(code[4]) != Integer.parseInt(code[6]);
                case ">" -> registers.get(code[4]) > Integer.parseInt(code[6]);
                case "<" -> registers.get(code[4]) < Integer.parseInt(code[6]);
                case ">=" -> registers.get(code[4]) >= Integer.parseInt(code[6]);
                case "<=" -> registers.get(code[4]) <= Integer.parseInt(code[6]);
                default -> throw new IllegalStateException("Unexpected value: " + code[5]);
            };

            if (execute) {
                int newValue = registers.get(code[0]) + delta;
                if (max < newValue) max = newValue;
                registers.put(code[0], newValue);
            }
        }
        return max;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput();
        String[] lines = data.lines().toArray(String[]::new);

        AocUtils.sendPuzzleAnswer(1, partA(lines));
        AocUtils.sendPuzzleAnswer(2, partB(lines));
    }

}
