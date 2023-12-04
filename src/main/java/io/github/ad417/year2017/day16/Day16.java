package io.github.ad417.year2017.day16;

import tk.vivas.adventofcode.AocUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day16 {

    private static void dance(HashMap<Character, Integer> programs, String[] moves) {
        Pattern exchanger = Pattern.compile("\\d+");

        for (String move : moves) {
            switch (move.charAt(0)) {
                case 's' -> { // Swap
                    int delta = Integer.parseInt(move.substring(1));
                    for (Character key : programs.keySet()) {
                        int newPos = (programs.get(key) + delta) % 16;
                        programs.put(key, newPos);
                    }
                }
                case 'p' -> { // Partner
                    char first = move.charAt(1);
                    char second = move.charAt(3);
                    int swap = programs.get(first);
                    programs.put(first, programs.get(second));
                    programs.put(second, swap);
                }
                case 'x' -> {
                    Matcher m = exchanger.matcher(move);
                    int firstIndex, secondIndex;
                    if (m.find()) firstIndex = Integer.parseInt(m.group());
                    else firstIndex = 0;
                    if (m.find()) secondIndex = Integer.parseInt(m.group());
                    else secondIndex = 0;
                    AtomicReference<Character> first = new AtomicReference<>('\0');
                    AtomicReference<Character> second = new AtomicReference<>('\0');
                    programs.forEach((k, v) -> {
                        if (v == firstIndex) first.set(k);
                        if (v == secondIndex) second.set(k);
                    });
                    int swap = programs.get(first.get());
                    programs.put(first.get(), programs.get(second.get()));
                    programs.put(second.get(), swap);
                }
            }
        }
    }

    private static String partA(String[] moves) {
        HashMap<Character, Integer> programs = new HashMap<>();
        for (int i = 'a'; i <= 'p'; i++) {
            programs.put((char)i, i - 'a');
        }

        dance(programs, moves);

        char[] out = new char[16];
        for (Character c : programs.keySet()) {
            int i = programs.get(c);
            out[i] = c;
        }
        return new String(out);
    }

    private static String partB(String[] moves) {
        HashSet<String> seen = new HashSet<>();
        boolean inLoop = false;

        HashMap<Character, Integer> programs = new HashMap<>();
        for (int i = 'a'; i <= 'p'; i++) {
            programs.put((char) i, i - 'a');
        }

        String config = "";
        for (int i = 0; i < 1_000_000_000; i++) {

            dance(programs, moves);

            char[] out = new char[16];
            for (Character c : programs.keySet()) {
                int index = programs.get(c);
                out[index] = c;
            }

            config = new String(out);
            if (seen.contains(config)) {
                if (inLoop) {
                    int remaining = 1_000_000_000 - i;
                    remaining -= remaining % seen.size();
                    i += remaining;
                    inLoop = false;
                } else {
                    inLoop = true;
                }
                seen = new HashSet<>();
            }
            seen.add(config);
        }
        return config;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput();
        String[] moves = data.split(",");

        System.out.println(partB(moves));
        // AocUtils.sendPuzzleAnswer(1, partA(moves));
        AocUtils.sendPuzzleAnswer(2, partB(moves));
    }

}
