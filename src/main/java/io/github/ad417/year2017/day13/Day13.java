package io.github.ad417.year2017.day13;

import tk.vivas.adventofcode.AocUtils;

import java.util.Collections;
import java.util.HashMap;

public class Day13 {

    static class Layer {
        int range;
        int scanner = 0;
        boolean goingDown = true;
        public Layer(int depth) {
            this.range = depth;
        }
        public int getRange() {
            return range;
        }
        public void tick() {
            if (goingDown) {
                scanner++;
                if (scanner + 1 == range) goingDown = false;
            } else {
                scanner--;
                if (scanner == 0) goingDown = true;
            }
        }
        public boolean isCaught() {
            return scanner == 0;
        }
        public String toString() {
            return "{range="+range+" scanner="+scanner+" goingDown="+goingDown+"}";
        }
    }

    private static HashMap<Integer, Layer> makeFirewall(String data) {
        HashMap<Integer, Layer> firewall = new HashMap<>();

        data.trim().lines().forEach(line ->{
            String[] parts = line.split(": ");
            int depth = Integer.parseInt(parts[0]);
            int range = Integer.parseInt(parts[1]);
            firewall.put(depth, new Layer(range));
        });

        return firewall;
    }

    private static int severityOfTravel(HashMap<Integer, Layer> firewall, int offset) {
        for (int i = 0; i < offset; i++) {
            firewall.forEach((key, value) -> value.tick());
        }
        int severity = 0;

        int finalWall = Collections.max(firewall.keySet());
        for (int pos = 0; pos <= finalWall; pos++) {
            if (firewall.containsKey(pos)) {
                Layer layer = firewall.get(pos);
                if (layer.isCaught()) {
                    severity += layer.getRange() * pos;
                }
            }
            firewall.forEach((wallPos, layer) -> layer.tick());
        }
        return severity;
    }

    private static boolean getsCaught(HashMap<Integer, Layer> firewall, int offset) {
        for (int i = 0; i < offset; i++) {
            firewall.forEach((key, value) -> value.tick());
        }

        int finalWall = Collections.max(firewall.keySet());
        for (int pos = 0; pos <= finalWall; pos++) {
            if (firewall.containsKey(pos) && firewall.get(pos).isCaught()) return true;
            firewall.forEach((wallPos, layer) -> layer.tick());
        }
        return false;

    }

    private static int partA(String data) {
        return severityOfTravel(makeFirewall(data), 0);
    }

    private static int partB(String data) {
        int offset = 0;
        while (getsCaught(makeFirewall(data), offset)) {
            offset++;
            System.out.println(offset);
        }
        return offset;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput();

        //AocUtils.sendPuzzleAnswer(1, partA(data));
        System.out.println(partB(data));
        //AocUtils.sendPuzzleAnswer(2, partB(data));
    }
}
