package io.github.ad417.year2023.day04;

import tk.vivas.adventofcode.AocUtils;

import java.util.*;

public class Day04 {
    private static int partA(String[] lines) {
        int totalWinnings = 0;
        for (String line : lines) {
            String winningPart = line.split(" \\| ")[0];
            Set<String> winners = new HashSet<>(Arrays.asList(winningPart.split(" +")));

            String cardPart = line.split(" \\| ")[1];
            int won = (int) Arrays.stream(cardPart.split(" +")).filter(winners::contains).count() - 1;
            if (won == -1) continue;
            totalWinnings += 1 << won;
        }
        return totalWinnings;
    }

    private static int partB(String[] lines) {
        List<Integer> cardCount = new ArrayList<>(Arrays.stream(lines).map(x -> 1).toList());
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            String winningPart = line.split(" \\| ")[0];
            Set<String> winners = new HashSet<>(Arrays.asList(winningPart.split(" +")));

            String cardPart = line.split(" \\| ")[1];
            int won = (int) Arrays.stream(cardPart.split(" +")).filter(winners::contains).count();

            for (int k = i+1; k < i+1+won; k++) {
                cardCount.set(k, cardCount.get(k) + cardCount.get(i));
            }
        }
        return cardCount.stream().mapToInt(x -> x).sum();
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        String[] lines = data.lines().toArray(String[]::new);

        AocUtils.sendPuzzleAnswer(1, partA(lines));
        AocUtils.sendPuzzleAnswer(2, partB(lines));
    }
}
