package io.github.ad417.year2017.day23;

import tk.vivas.adventofcode.AocUtils;

import java.util.HashMap;

public class Day23 {
    private static long valueOf(String val, HashMap<String, Long> register) {
        if (register.containsKey(val)) return register.get(val);
        return Integer.parseInt(val);
    }

    private static int partA(String[] lines) {
        HashMap<String, Long> register = new HashMap<>();
        for (int i = 'a'; i <= 'h'; i++) {
            register.put(String.valueOf((char)i), 0L);
        }

        int instruction = 0;
        int mults = 0;
        while (instruction < lines.length) {
            String[] op = lines[instruction].split(" ");
            switch (op[0]) {
                case "set" -> register.put(op[1], valueOf(op[2], register));
                case "sub" -> register.put(op[1], valueOf(op[1], register) - valueOf(op[2], register));
                case "mul" -> {
                    register.put(op[1], valueOf(op[1], register) * valueOf(op[2], register));
                    mults++;
                }
                case "jnz" -> {
                    if (valueOf(op[1], register) != 0) {
                        instruction += (int) valueOf(op[2], register);
                        continue;
                    }
                }
            }
            instruction++;
        }
        return mults;
    }

    private static boolean isComposite(int value) {
        int root = (int) Math.sqrt(value) + 1;
        for (int i = 2; i < root; i++) {
            if (value % i == 0) return true;
        }
        return false;
    }

    private static int partB(String[] lines) {
        HashMap<String, Long> register = new HashMap<>();
        for (int i = 'a'; i <= 'h'; i++) {
            register.put(String.valueOf((char)i), 0L);
        }
        register.put("a", 1L);

        int instruction = 0;
        int mults = 0;
        while (instruction < 8) {
            String[] op = lines[instruction].split(" ");
            switch (op[0]) {
                case "set" -> register.put(op[1], valueOf(op[2], register));
                case "sub" -> register.put(op[1], valueOf(op[1], register) - valueOf(op[2], register));
                case "mul" -> {
                    register.put(op[1], valueOf(op[1], register) * valueOf(op[2], register));
                    mults++;
                }
                case "jnz" -> {
                    if (valueOf(op[1], register) != 0) {
                        instruction += (int) valueOf(op[2], register);
                        continue;
                    }
                }
            }
            instruction++;
        }
        int a = 1;
        int b = register.get("b").intValue();
        int c = register.get("c").intValue();
        int h = 0;

        for (int num = b; num <= c; num += 17) {
            int d = 2;
            int e = 2;
            int f = 1;
            if (isComposite(num)) h++;
        }
        return h;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        String[] lines = data.lines().toArray(String[]::new);

        AocUtils.sendPuzzleAnswer(1, partA(lines));
        AocUtils.sendPuzzleAnswer(2, partB(lines));
    }
}
