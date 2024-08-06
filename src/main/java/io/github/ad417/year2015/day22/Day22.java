package io.github.ad417.year2015.day22;

import tk.vivas.adventofcode.AocUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day22 {
    record Wizard(int hp, int dmg, int mana, boolean playerTurn, int shieldTime, int poisonTime, int chargeTime, int manaSpent) implements Comparable<Wizard> {
        static final int MISSILE_COST = 53;
        static final int MISSILE_DMG = 4;
        static final int DRAIN_COST = 73;
        static final int DRAIN_EFFECT = 2;
        static final int SHIELD_COST = 113;
        static final int SHIELD_STRENGTH = 7;
        static final int SHIELD_TIME = 6;
        static final int POISON_COST = 173;
        static final int POISON_STRENGTH = 3;
        static final int POISON_TIME = 6;
        static final int CHARGE_COST = 229;
        static final int CHARGE_STRENGTH = 101;
        static final int CHARGE_TIME = 5;
        public Wizard() {
            this(50, 0, 500, true, 0, 0, 0, 0);
        }

        public boolean hasLost() {
            return !canCastMissile() || hp <= 0;
        }

        public Wizard tickSpells() {
            int newShield = Math.max(shieldTime-1, 0);
            int newPoison = Math.max(poisonTime-1, 0);
            int newCharge = Math.max(chargeTime-1, 0);
            int newMana = mana;
            if (chargeTime > 0) newMana += CHARGE_STRENGTH;
            int newDmg = dmg;
            if (poisonTime > 0) newDmg += POISON_STRENGTH;
            return new Wizard(hp, newDmg, newMana, playerTurn, newShield, newPoison, newCharge, manaSpent);
        }

        public boolean canCastMissile() {
            return mana >= MISSILE_COST;
        }

        public boolean canCastDrain() {
            return mana >= DRAIN_COST;
        }

        public boolean canCastShield() {
            return shieldTime == 0 && mana >= SHIELD_COST;
        }
        public boolean canCastPoison() {
            return poisonTime == 0 && mana >= POISON_COST;
        }
        public boolean canCastCharge() {
            return chargeTime == 0 && mana >= CHARGE_COST;
        }

        public Wizard hitFor(int damage) {
            int actualDamage = damage;
            if (shieldTime > 0) actualDamage = Math.max(actualDamage - SHIELD_STRENGTH, 1);
            return new Wizard(
                    hp-actualDamage, dmg, mana, true, shieldTime,
                    poisonTime, chargeTime, manaSpent
            );
        }

        public Wizard castMissile() {
            return new Wizard(
                    hp, dmg+MISSILE_DMG, mana-MISSILE_COST, false,
                    shieldTime, poisonTime, chargeTime, manaSpent+MISSILE_COST
            );
        }

        public Wizard castDrain() {
            return new Wizard(
                    hp+DRAIN_EFFECT, dmg+DRAIN_EFFECT, mana-DRAIN_COST, false,
                    shieldTime, poisonTime, chargeTime, manaSpent+DRAIN_COST
            );
        }

        public Wizard castShield() {
            return new Wizard(
                    hp, dmg, mana-SHIELD_COST, false, SHIELD_TIME,
                    poisonTime, chargeTime, manaSpent+SHIELD_COST
            );
        }

        public Wizard castPoison() {
            return new Wizard(
                    hp, dmg, mana-POISON_COST, false, shieldTime,
                    POISON_TIME, chargeTime, manaSpent+POISON_COST
            );
        }

        public Wizard castCharge() {
            return new Wizard(
                    hp, dmg, mana-CHARGE_COST, false, shieldTime,
                    poisonTime, CHARGE_TIME, manaSpent+CHARGE_COST
            );
        }

        @Override
        public int compareTo(Wizard o) {
            return this.manaSpent - o.manaSpent;
        }
    }

    private static int partA(List<Integer> values) {
        int hp = values.get(0);
        int atk = values.get(1);
        Queue<Wizard> gameState = new PriorityQueue<>();
        gameState.add(new Wizard());

        while (!gameState.isEmpty()) {
            Wizard player = gameState.poll();
            player = player.tickSpells();
            if (player.dmg() >= hp) return player.manaSpent;
            if (player.playerTurn()) {
                if (!player.canCastMissile()) continue;
                gameState.add(player.castMissile());
                if (player.canCastDrain()) gameState.add(player.castDrain());
                if (player.canCastShield()) gameState.add(player.castShield());
                if (player.canCastPoison()) gameState.add(player.castPoison());
                if (player.canCastCharge()) gameState.add(player.castCharge());
            } else {
                Wizard nextPlayer = player.hitFor(atk);
                if (nextPlayer.hp() <= 0) continue;
                gameState.add(nextPlayer);
            }
        }
        throw new IllegalStateException("Impossible to win the battle!");
    }

    private static int partB(List<Integer> values) {
        int hp = values.get(0);
        int atk = values.get(1);
        Queue<Wizard> gameState = new PriorityQueue<>();
        gameState.add(new Wizard());

        while (!gameState.isEmpty()) {
            Wizard player = gameState.poll();
            player = player.tickSpells();
            if (player.dmg() >= hp) return player.manaSpent;
            if (player.playerTurn()) {
                player = player.hitFor(1);
                if (player.hasLost()) continue;
                gameState.add(player.castMissile());
                if (player.canCastDrain()) gameState.add(player.castDrain());
                if (player.canCastShield()) gameState.add(player.castShield());
                if (player.canCastPoison()) gameState.add(player.castPoison());
                if (player.canCastCharge()) gameState.add(player.castCharge());
            } else {
                Wizard nextPlayer = player.hitFor(atk);
                if (nextPlayer.hp() <= 0) continue;
                gameState.add(nextPlayer);
            }
        }
        throw new IllegalStateException("Impossible to win the battle!");
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        /*data = """
                Hit Points: 14
                Damage: 8""";*/
        List<Integer> values = Pattern.compile("\\d+")
                .matcher(data).results()
                .map(x -> Integer.parseInt(x.group()))
                .toList();

        System.out.println(partB(values));
        //AocUtils.sendPuzzleAnswer(1, partA(values));
        //AocUtils.sendPuzzleAnswer(2, partB(values));
    }
}
