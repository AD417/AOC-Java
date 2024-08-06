package io.github.ad417.year2015.day23;

import tk.vivas.adventofcode.AocUtils;

import java.util.HashMap;
import java.util.List;

public class Day23 {
    private static int partA(List<String[]> instructions) {
        HashMap<String, Integer> register = new HashMap<>();
        register.put("a", 0);
        register.put("b", 0);
        int ins = 0;
        while (ins < instructions.size()) {
            String[] instruction = instructions.get(ins);
            switch (instruction[0]) {
                case "hlf" -> register.put(instruction[1], register.get(instruction[1]) / 2);
                case "tpl" -> register.put(instruction[1], register.get(instruction[1]) * 3);
                case "inc" -> register.put(instruction[1], register.get(instruction[1]) + 1);
                case "jmp" -> {
                    ins += Integer.parseInt(instruction[1]);
                    continue;
                }
                case "jie" -> {
                    if (register.get(instruction[1]) % 2 == 0) {
                        ins += Integer.parseInt(instruction[2]);
                        continue;
                    }
                }
                case "jio" -> {
                    if (register.get(instruction[1]) == 1) {
                        ins += Integer.parseInt(instruction[2]);
                        continue;
                    }
                }
            }
            ins++;
        }
        return register.get("b");
    }

    private static int partB(List<String[]> instructions) {
        HashMap<String, Integer> register = new HashMap<>();
        register.put("a", 1);
        register.put("b", 0);
        int ins = 0;
        while (ins < instructions.size()) {
            String[] instruction = instructions.get(ins);
            switch (instruction[0]) {
                case "hlf" -> register.put(instruction[1], register.get(instruction[1]) / 2);
                case "tpl" -> register.put(instruction[1], register.get(instruction[1]) * 3);
                case "inc" -> register.put(instruction[1], register.get(instruction[1]) + 1);
                case "jmp" -> {
                    ins += Integer.parseInt(instruction[1]);
                    continue;
                }
                case "jie" -> {
                    if (register.get(instruction[1]) % 2 == 0) {
                        ins += Integer.parseInt(instruction[2]);
                        continue;
                    }
                }
                case "jio" -> {
                    if (register.get(instruction[1]) == 1) {
                        ins += Integer.parseInt(instruction[2]);
                        continue;
                    }
                }
            }
            ins++;
        }
        return register.get("b");
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        List<String[]> instructions = data.lines().map(x -> x.split("[, ]+")).toList();

        AocUtils.sendPuzzleAnswer(1, partA(instructions));
        AocUtils.sendPuzzleAnswer(2, partB(instructions));
    }
}
