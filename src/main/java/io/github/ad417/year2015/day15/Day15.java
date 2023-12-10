package io.github.ad417.year2015.day15;

import tk.vivas.adventofcode.AocUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Day15 {
    record Ingredient(int capacity, int durability, int flavor, int texture, int calories) {
        static Ingredient makeIngredient(String line) {
            Matcher m = Pattern.compile("(\\w+): capacity (-?\\d+), durability (-?\\d+), flavor (-?\\d+), texture (-?\\d+), calories (-?\\d+)")
                    .matcher(line);

            if (!m.find()) throw new RuntimeException("Invalid line: '"+line+"'");

            int capacity = Integer.parseInt(m.group(2));
            int durability = Integer.parseInt(m.group(3));
            int flavor = Integer.parseInt(m.group(4));
            int texture = Integer.parseInt(m.group(5));
            int calories = Integer.parseInt(m.group(6));

            return new Ingredient(capacity, durability, flavor, texture, calories);
        }
    }

    private static Set<List<Integer>> makeCombos(List<Integer> partialCombo, int amountLeft, int layersLeft) {
        Set<List<Integer>> fullCombos = new HashSet<>();

        if (layersLeft == 1) {
            List<Integer> fullCombo = new ArrayList<>(partialCombo.size()+1);
            fullCombo.addAll(partialCombo);
            fullCombo.add(amountLeft);
            fullCombos.add(fullCombo);
            return fullCombos;
        }

        IntStream.range(0, amountLeft+1).forEach(amount -> {
            List<Integer> nextPartialCombo = new ArrayList<>(partialCombo.size()+layersLeft);
            nextPartialCombo.addAll(partialCombo);
            nextPartialCombo.add(amount);
            fullCombos.addAll(makeCombos(nextPartialCombo, amountLeft-amount, layersLeft-1));
        });
        return fullCombos;
    }

    private static int partA(List<Ingredient> ingredients) {
        return makeCombos(new ArrayList<>(4), 100, 4).stream().mapToInt(amounts -> {
            int capacity = 0;
            int durability = 0;
            int flavor = 0;
            int texture = 0;
            for (int i = 0; i < 4; i++) {
                Ingredient ingredient = ingredients.get(i);
                int amount = amounts.get(i);
                capacity += ingredient.capacity * amount;
                durability += ingredient.durability * amount;
                flavor += ingredient.flavor * amount;
                texture += ingredient.texture * amount;
            }
            if (capacity < 0 || durability < 0 || flavor < 0 || texture < 0) return 0;
            return capacity * durability * flavor * texture;
        }).max().orElseThrow();
    }

    private static int partB(List<Ingredient> ingredients) {
        return makeCombos(new ArrayList<>(4), 100, 4).stream().mapToInt(amounts -> {
            int capacity = 0;
            int durability = 0;
            int flavor = 0;
            int texture = 0;
            int calories = 0;
            for (int i = 0; i < 4; i++) {
                Ingredient ingredient = ingredients.get(i);
                int amount = amounts.get(i);
                capacity += ingredient.capacity * amount;
                durability += ingredient.durability * amount;
                flavor += ingredient.flavor * amount;
                texture += ingredient.texture * amount;
                calories += ingredient.calories * amount;
            }
            if (calories != 500) return -1;
            if (capacity < 0 || durability < 0 || flavor < 0 || texture < 0) return 0;
            return capacity * durability * flavor * texture;
        }).max().orElseThrow();
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        List<Ingredient> ingredients = data.lines().map(Ingredient::makeIngredient).toList();

        AocUtils.sendPuzzleAnswer(1, partA(ingredients));
        AocUtils.sendPuzzleAnswer(2, partB(ingredients));
    }

}
