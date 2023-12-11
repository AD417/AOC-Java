package io.github.ad417.year2015.day21;

import tk.vivas.adventofcode.AocUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day21 {
    private static final String ITEM_SHOP = """
            Weapons:    Cost  Damage  Armor
            Dagger        8     4       0
            Shortsword   10     5       0
            Warhammer    25     6       0
            Longsword    40     7       0
            Greataxe     74     8       0
                        
            Armor:      Cost  Damage  Armor
            Leather      13     0       1
            Chainmail    31     0       2
            Splintmail   53     0       3
            Bandedmail   75     0       4
            Platemail   102     0       5
                        
            Rings:      Cost  Damage  Armor
            Damage +1    25     1       0
            Damage +2    50     2       0
            Damage +3   100     3       0
            Defense +1   20     0       1
            Defense +2   40     0       2
            Defense +3   80     0       3""";

    private static List<List<Integer>> parseItemShop(String data) {
        List<List<Integer>> shop = new LinkedList<>();
        String[] categories = data.split("\n\n");
        Pattern numbers = Pattern.compile("\\d+");

        for (String category : categories) {
            category.lines().forEach(line -> {
                Matcher m = numbers.matcher(line);
                List<Integer> details = new ArrayList<>(3);
                while (m.find()) details.add(Integer.parseInt(m.group()));
                if (details.isEmpty()) return;

            });
        }
        return shop;
    }
    private static int partA(String[] lines) {
        return 0;
    }

    private static int partB(String[] lines) {
        return 0;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        String[] lines = data.split("\n");

        AocUtils.sendPuzzleAnswer(1, partA(lines));
        AocUtils.sendPuzzleAnswer(2, partB(lines));
    }
}
