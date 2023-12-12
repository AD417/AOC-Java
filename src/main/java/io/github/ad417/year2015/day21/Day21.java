package io.github.ad417.year2015.day21;

import tk.vivas.adventofcode.AocUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Day21 {
    record Item(int cost, int atk, int def) {
        static final Item DEFAULT = new Item(0,0,0);
        static Item makeItem(String line) {
            int[] values = new int[3];
            Matcher m = numbers.matcher(line);
            for (int i = 0; m.find(); i++) {
                values[i] = Integer.parseInt(m.group());
            }
            return new Item(values[0], values[1], values[2]);
        }
    }

    record Creature(int hp, int atk, int def) {
        static Creature makeCreature(String data) {
            int[] values = new int[3];
            Matcher m = numbers.matcher(data);
            for (int i = 0; m.find(); i++) {
                values[i] = Integer.parseInt(m.group());
            }
            return new Creature(values[0], values[1], values[2]);
        }

        boolean isAlive() {
            return hp > 0;
        }

        Creature hitFor(int damage) {
            int damageTaken = damage - def;
            if (damageTaken < 1) damageTaken = 1;
            return new Creature(hp-damageTaken, atk, def);
        }
    }

    private static final Pattern numbers = Pattern.compile("(?<= )\\d+", Pattern.MULTILINE);

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

    private static List<List<Item>> parseItemShop(String data) {
        List<List<Item>> shop = new LinkedList<>();
        String[] categories = data.split("\n\n");

        for (String category : categories) {
            List<Item> shopSection = new LinkedList<>();
            category.lines().forEach(line -> {
                if (!numbers.matcher(line).find()) return;
                shopSection.add(Item.makeItem(line));
            });
            shop.add(shopSection);
        }
        return shop;
    }

    private static boolean playerWins(Creature player, Creature boss) {
        while (player.isAlive() && boss.isAlive()) {
            boss = boss.hitFor(player.atk);
            if (boss.isAlive()) player = player.hitFor(boss.atk);
        }
        return player.isAlive();
    }

    private static int partA(List<List<Item>> shop, Creature boss) {
        int totalConfigs = shop.get(0).size() *
                (shop.get(1).size() + 1) *
                (shop.get(2).size() + 1) *
                (shop.get(2).size() + 1);
        return IntStream.range(0, totalConfigs).map(x -> {
            List<Integer> config = new ArrayList<>(4);
            int weaponId = x % shop.get(0).size();
            x /= shop.get(0).size();
            int armorId = x % shop.get(1).size() - 1;
            x /= shop.get(1).size();
            int ring1Id = x % shop.get(2).size() - 1;
            x /= shop.get(2).size();
            int ring2Id = x % shop.get(2).size() - 1;

            if (ring1Id == ring2Id && ring2Id != -1) return Integer.MAX_VALUE;

            Item weapon = Item.DEFAULT;
            weapon = shop.get(0).get(weaponId);
            Item armor = Item.DEFAULT;
            if (armorId != -1) armor = shop.get(1).get(armorId);
            Item ring1 = Item.DEFAULT;
            if (ring1Id != -1) ring1 = shop.get(2).get(ring1Id);
            Item ring2 = Item.DEFAULT;
            if (ring2Id != -1) ring2 = shop.get(2).get(ring2Id);

            int atk = weapon.atk + ring1.atk + ring2.atk;
            int def = armor.def + ring1.def + ring2.def;

            Creature player = new Creature(100, atk, def);
            if (!playerWins(player, boss)) return Integer.MAX_VALUE;

            return weapon.cost + armor.cost + ring1.cost + ring2.cost;
        }).min().orElseThrow();
    }

    private static int partB(List<List<Item>> shop, Creature boss) {
        int totalConfigs = shop.get(0).size() *
                (shop.get(1).size() + 1) *
                (shop.get(2).size() + 1) *
                (shop.get(2).size() + 1);
        return IntStream.range(0, totalConfigs).map(x -> {
            List<Integer> config = new ArrayList<>(4);
            int weaponId = x % shop.get(0).size();
            x /= shop.get(0).size();
            int armorId = x % shop.get(1).size() - 1;
            x /= shop.get(1).size();
            int ring1Id = x % shop.get(2).size() - 1;
            x /= shop.get(2).size();
            int ring2Id = x % shop.get(2).size() - 1;

            if (ring1Id == ring2Id && ring2Id != -1) return Integer.MIN_VALUE;

            Item weapon = Item.DEFAULT;
            weapon = shop.get(0).get(weaponId);
            Item armor = Item.DEFAULT;
            if (armorId != -1) armor = shop.get(1).get(armorId);
            Item ring1 = Item.DEFAULT;
            if (ring1Id != -1) ring1 = shop.get(2).get(ring1Id);
            Item ring2 = Item.DEFAULT;
            if (ring2Id != -1) ring2 = shop.get(2).get(ring2Id);

            int atk = weapon.atk + ring1.atk + ring2.atk;
            int def = armor.def + ring1.def + ring2.def;

            Creature player = new Creature(100, atk, def);
            if (playerWins(player, boss)) return Integer.MIN_VALUE;

            return weapon.cost + armor.cost + ring1.cost + ring2.cost;
        }).max().orElseThrow();
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        List<List<Item>> shop = parseItemShop(ITEM_SHOP);
        Creature boss = Creature.makeCreature(data);

        AocUtils.sendPuzzleAnswer(1, partA(shop, boss));
        AocUtils.sendPuzzleAnswer(2, partB(shop, boss));
    }
}
