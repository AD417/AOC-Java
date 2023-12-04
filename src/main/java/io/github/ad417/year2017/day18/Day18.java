package io.github.ad417.year2017.day18;

import tk.vivas.adventofcode.AocUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Day18 {
    private static long valueOf(String val, HashMap<String, Long> register) {
        if (register.containsKey(val)) return register.get(val);
        return Integer.parseInt(val);
    }

    private static boolean isInt(String val) {
        try {
            Integer.parseInt(val);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static int partA(String[] lines) {
        HashMap<String, Long> register = new HashMap<>();

        for (String s : lines) {
            String reg = s.split(" ")[1];
            if (!isInt(reg)) register.put(reg, 0L);
        }
        int lastPlayed = -11111;

        int instruction = 0;
        while (true) {
            String[] op = lines[instruction].split(" ");
            switch (op[0]) {
                case "snd" -> {
                    lastPlayed = (int) valueOf(op[1], register);
                }
                case "set" -> register.put(op[1], valueOf(op[2], register));
                case "add" -> register.put(op[1], valueOf(op[1], register) + valueOf(op[2], register));
                case "mul" -> register.put(op[1], valueOf(op[1], register) * valueOf(op[2], register));
                case "mod" -> register.put(op[1], valueOf(op[1], register) % valueOf(op[2], register));
                case "rcv" -> {
                    System.out.println(register);
                    if (valueOf(op[1], register) != 0) return lastPlayed;
                }
                case "jgz" -> {
                    if (valueOf(op[1], register) > 0) {
                        instruction += (int) valueOf(op[2], register);
                        continue;
                    }
                }
            }
            instruction++;
        }
    }

    private static Queue<Long> runUntilWaiting(String[] lines, HashMap<String, Long> register, Queue<Long> received) {
        Queue<Long> sent = new LinkedList<>();
        while (register.get("ins") < lines.length) {
            int instruction = register.get("ins").intValue();
            String[] op = lines[instruction].split(" ");
            switch (op[0]) {
                case "snd" -> {
                    sent.offer(valueOf(op[1], register));
                    register.put("sent", register.get("sent")+1);
                }
                case "set" -> register.put(op[1], valueOf(op[2], register));
                case "add" -> register.put(op[1], valueOf(op[1], register) + valueOf(op[2], register));
                case "mul" -> register.put(op[1], valueOf(op[1], register) * valueOf(op[2], register));
                case "mod" -> register.put(op[1], valueOf(op[1], register) % valueOf(op[2], register));
                case "rcv" -> {
                    if (received.isEmpty()) return sent;
                    register.put(op[1], received.poll());
                }
                case "jgz" -> {
                    if (valueOf(op[1], register) > 0) {
                        register.put("ins", instruction + valueOf(op[2], register));
                        continue;
                    }
                }
            }
            register.put("ins", (long) instruction + 1);
        }
        return sent;
    }

    private static int partB(String[] lines) {
        HashMap<String, Long> register0 = new HashMap<>();
        HashMap<String, Long> register1 = new HashMap<>();

        // Instruction pointer.
        register1.put("ins", 0L);
        // Number of sent things.
        register1.put("sent", 0L);
        register0.put("ins", 0L);
        register0.put("sent", 0L);

        for (String s : lines) {
            String reg = s.split(" ")[1];
            if (!isInt(reg)) register0.put(reg, 0L);
            if (!isInt(reg)) register1.put(reg, 0L);
        }
        register1.put("p", 1L);
        register0.put("p", 0L);

        Queue<Long> send0 = new LinkedList<>();
        Queue<Long> send1 = new LinkedList<>();

        do {
            send0.addAll(runUntilWaiting(lines, register1, send1));
            send1.addAll(runUntilWaiting(lines, register0, send0));
        } while (!send0.isEmpty() || !send1.isEmpty());

        return register1.get("sent").intValue();
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        String[] lines = data.lines().toArray(String[]::new);

        AocUtils.sendPuzzleAnswer(1, partA(lines));
        AocUtils.sendPuzzleAnswer(2, partB(lines));
    }

}
