package io.github.ad417.year2023.day15;

import tk.vivas.adventofcode.AocUtils;

import java.util.*;

public class Day15 {
    record Lens(String cause, int focus) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Lens lens = (Lens) o;
            return Objects.equals(cause, lens.cause);
        }

        @Override
        public int hashCode() {
            return Objects.hash(cause);
        }

        @Override
        public String toString() {
            return "["+cause+" "+focus+"]";
        }
    }
    private static int hash(String data) {
        // I tried to be funny and use a byte for this, but
        // it turns out Java doesn't have an unsigned byte. How sad.
        byte x = 0;
        for (char c : data.toCharArray()) {
            x += (byte) c;
            x *= 17;
        }
        if (x >= 0) return x;
        // Damn you, twos compliment.
        return x + 256;
    }
    private static int partA(String[] lines) {
        // Implement a basic hashing function, and add up the sum of the hashes
        // of all the numbers.
        int sum = 0;
        for (String line : lines) {
            sum += hash(line);
        }
        return sum;
    }

    private static long partB(String[] lines) {
        // Literally just implement a Map<String, Integer> that uses a
        // Linked list address instead of open addressing.
        // The actual index we end up putting items at is determined
        // based on the hash we are given in part A.
        List<List<Lens>> boxes = new ArrayList<>(256);
        for (int i = 0; i < 256; i++) boxes.add(new LinkedList<>());

        for (String line : lines) {
            String[] components = line.split("[-=]");
            Lens lens;
            if (components.length == 2) {
                lens = new Lens(components[0], Integer.parseInt(components[1]));
            } else {
                lens = new Lens(components[0], 0);
            }
            List<Lens> box = boxes.get(hash(components[0]));
            if (lens.focus == 0) {
                box.remove(lens);
            } else {
                int updateIndex = box.indexOf(lens);
                if (updateIndex != -1) box.set(updateIndex, lens);
                else box.add(lens);
            }
        }

        long sum = 0;
        for (int i = 0; i < 256; i++) {
            int boxMult = i + 1;
            List<Lens> box = boxes.get(i);
            if (box.isEmpty()) continue;


            for (int k = 0; k < box.size(); k++) {
                int posMult = k + 1;
                Lens lens = box.get(k);
                sum += (long) boxMult * posMult * lens.focus;
            }
        }

        return sum;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        //data = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7";
        String[] lines = data.split(",");

        //System.out.println(partB(lines));
        AocUtils.sendPuzzleAnswer(1, partA(lines));
        AocUtils.sendPuzzleAnswer(2, partB(lines));
    }
}
