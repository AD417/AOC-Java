package io.github.ad417.year2017.day04;

import tk.vivas.adventofcode.AocUtils;

import java.util.*;

public class Day04 {
    private static final HashMap<Character, Integer> emptyAnagrm;
    static {
        emptyAnagrm = new HashMap<>();
        for (int i = 'a'; i <= 'z'; i++) {
            emptyAnagrm.put((char)i, 0);
        }
    }

    private static int partA(String[] lines) {
        int count = 0;
        for (String line : lines) {
            HashSet<String> words = new HashSet<>();
            boolean duplicate = false;
            for (String word : line.split(" ")) {
                if (words.contains(word)) {
                    duplicate = true;
                    break;
                }
                words.add(word);
            }
            if (!duplicate) count++;
        }
        return count;
    }

    private static int partB(String[] lines) {
        int count = 0;
        for (String line : lines) {
            boolean duplicates = false;
            List<HashMap<Character, Integer>> anagrams = new ArrayList<>();
            for (String word : line.split(" ")) {

                HashMap<Character, Integer> anagram = (HashMap<Character, Integer>) emptyAnagrm.clone();
                for (char c : word.toCharArray()) anagram.put(c, anagram.get(c) + 1);

                if (anagrams.stream().anyMatch(x -> x.equals(anagram))) {
                    duplicates = true;
                    break;
                }
                anagrams.add(anagram);
            }
            if (!duplicates) count++;
        }
        return count;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput();
        String[] lines = data.lines().toArray(String[]::new);

        AocUtils.sendPuzzleAnswer(1, partA(lines));
        AocUtils.sendPuzzleAnswer(2, partB(lines));
    }

}
