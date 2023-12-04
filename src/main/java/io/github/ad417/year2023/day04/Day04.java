package io.github.ad417.year2023.day04;

import tk.vivas.adventofcode.AocUtils;

import java.util.*;

public class Day04 {
    private static int partA(String[] lines) {
        int totalWinnings = 0;
        for (String line : lines) {
            Set<String> winners = new HashSet<>();
            String winningPart = line.split(" \\| ")[0];
            winners.addAll(Arrays.asList(winningPart.split(" +")));

            int won = 1;
            boolean actuallyWon = false;
            String cardPart = line.split(" \\| ")[1];
            for (String num : cardPart.split(" +")) {
                if (!winners.contains(num)) continue;
                won *= 2;
                actuallyWon = true;
            }
            if (!actuallyWon) continue;
            won /= 2;
            totalWinnings += won;
        }
        return totalWinnings;
    }

    private static int partB(String[] lines) {
        List<Integer> cardCount = new ArrayList<>(Arrays.stream(lines).map(x -> 1).toList());
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            String winningPart = line.split(" \\| ")[0];
            Set<String> winners = new HashSet<>(Arrays.asList(winningPart.split(" +")));

            int won = 0;
            String cardPart = line.split(" \\| ")[1];
            for (String num : cardPart.split(" +")) {
                if (!winners.contains(num)) continue;
                won++;
            }

            for (int k = i+1; k < i+1+won; k++) {
                cardCount.set(k, cardCount.get(k) + cardCount.get(i));
            }
        }
        int total = 0;
        for (int i : cardCount) {
            total += i;
        }
        return total;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        String[] lines = data.lines().toArray(String[]::new);

        AocUtils.sendPuzzleAnswer(1, partA(lines)); 
        AocUtils.sendPuzzleAnswer(2, partB(lines));
    }
}
