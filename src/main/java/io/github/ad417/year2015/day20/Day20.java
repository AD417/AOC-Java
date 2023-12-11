package io.github.ad417.year2015.day20;

import tk.vivas.adventofcode.AocUtils;

import java.util.HashSet;
import java.util.Set;

public class Day20 {
    record Factor(int prime, int times) {
        public int getProduct() {
            int product = 1;
            for (int i = 0; i <= times; i++) {
                product *= prime;
            }
            product--;
            return product / (prime - 1);
        }
    }

    private static Set<Factor> factorize(int value) {
        Set<Factor> factors = new HashSet<>();
        int root = (int) Math.sqrt(value) + 1;
        for (int i = 2; i <= root; i++) {
            int times = 0;
            while (value % i == 0) {
                times++;
                value /= i;
            }
            if (times > 0) {
                factors.add(new Factor(i, times));
                root = (int) Math.sqrt(value) + 1;
            }
        }
        if (value != 1) factors.add(new Factor(value, 1));
        return factors;
    }

    private static int partA(int num) {
        int factorSumGoal = num / 10;
        for (int i = 2; i < num; i++) {
            Set<Factor> factors = factorize(i);
            // System.out.println(factors);
            int presents = factors.stream().mapToInt(Factor::getProduct).reduce(1, (x, y) -> x * y);
            // System.out.println(presents);
            if (presents >= factorSumGoal) return i;
        }
        return -1;
    }

    private static int partB(int num) {
        int factorGoal = num / 11 + 1;
        for (int i = 2; i < factorGoal; i++) {
            int presents = 0;
            for (int housesVisited = 1; housesVisited <= 50; housesVisited++) {
                if (i % housesVisited != 0) continue;
                presents += i / housesVisited;
            }
            if (presents > factorGoal) return i;
        }
        return -1;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        int num = Integer.parseInt(data);

        AocUtils.sendPuzzleAnswer(1, partA(num));
        AocUtils.sendPuzzleAnswer(2, partB(num));
    }
}
