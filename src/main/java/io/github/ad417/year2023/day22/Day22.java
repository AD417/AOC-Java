package io.github.ad417.year2023.day22;

import tk.vivas.adventofcode.AocUtils;

import java.util.*;
import java.util.stream.Collectors;

public class Day22 {
    record Point(int x, int y, int z) { }
    record Box(char id, int xMin, int yMin, int zMin, int xMax, int yMax, int zMax) implements Comparable<Box> {
        static char ID = 'A';
        private static Box makeBox(String line) {
            String[] parts = line.split("~");
            int[] min = Arrays.stream(parts[0].split(",")).mapToInt(Integer::parseInt).toArray();
            int[] max = Arrays.stream(parts[1].split(",")).mapToInt(Integer::parseInt).toArray();
            return new Box(ID++, min[0], min[1], min[2], max[0], max[1], max[2]);
        }
        private boolean intersects(Box other) {
            int xCross = Math.min(this.xMax, other.xMax) - Math.max(this.xMin, other.xMin);
            int yCross = Math.min(this.yMax, other.yMax) - Math.max(this.yMin, other.yMin);
            int zCross = Math.min(this.zMax, other.zMax) - Math.max(this.zMin, other.zMin);
            return xCross >= 0 && yCross >= 0 && zCross >= 0;
        }

        public Box fall(int amount) {
            return new Box(id, xMin, yMin, zMin-amount, xMax, yMax, zMax-amount);
        }

        @Override
        public int compareTo(Box o) {
            int out = this.zMin - o.zMin;
            if (out == 0) return this.zMax - o.zMax;
            return out;
        }
    }

    private static List<Box> getBoxes(String data) {
        List<Box> boxesInOrder = data.lines().map(Box::makeBox).toList();
        boxesInOrder = boxesInOrder.stream().sorted().toList();
        return new ArrayList<>(boxesInOrder);
    }

    private static List<Box> collapse(List<Box> initial) {
        List<Box> collapsed = new ArrayList<>(initial.size());
        for (Box box : initial) {
            while (box.zMin > 1) {
                Box fallenBox = box.fall(1);
                boolean hitsSomething = false;
                for (Box settled : collapsed) {
                    if (fallenBox.intersects(settled)) {
                        hitsSomething = true;
                        break;
                    }
                }
                if (hitsSomething) break;
                box = fallenBox;
            }
            collapsed.add(box);
        }
        return collapsed;
    }

    private static HashMap<Box, List<Box>> getSupportStructure(List<Box> boxes) {
        HashMap<Box, List<Box>> structure = new HashMap<>();
        for (Box box : boxes) {
            List<Box> supportedBy = new LinkedList<>();
            Box fallen = box.fall(1);
            for (Box checkIntersect : boxes) {
                if (checkIntersect.equals(box)) continue;
                if (checkIntersect.intersects(fallen)) supportedBy.add(checkIntersect);
            }
            structure.put(box, supportedBy);
        }
        return structure;
    }

    private static HashMap<Box, List<Box>> completePoset(HashMap<Box, List<Box>> structure) {
        Queue<Box> toAdd = new LinkedList<>();
        HashMap<Box, List<Box>> poset = new HashMap<>();
        for (Box box : structure.keySet()) {
            List<Box> completeSupportedBy = new LinkedList<>();
            toAdd.addAll(structure.get(box));
            while (!toAdd.isEmpty()) {
                Box addable = toAdd.poll();
                toAdd.addAll(structure.get(addable));
                completeSupportedBy.add(addable);
            }
            poset.put(box, completeSupportedBy);
        }
        return poset;
    }

    private static int partA(List<Box> boxes) {
        List<Box> collapsed = collapse(boxes);
        HashMap<Box, List<Box>> structure = getSupportStructure(collapsed);
        Set<Box> safeToRemove = new HashSet<>(collapsed);
        for (Box box : structure.keySet()) {
            if (box.zMin == 1) continue;
            if (structure.get(box).size() != 1) continue;
            safeToRemove.remove(structure.get(box).get(0));
        }
        return safeToRemove.size();
    }

    private static int partB(List<Box> boxes) {
        List<Box> collapsed = collapse(boxes);
        Set<Box> boxSet = new HashSet<>(collapsed);

        int collapseTotal = 0;
        for (Box box : collapsed) {
            List<Box> withoutBox = new LinkedList<>(collapsed);
            withoutBox.remove(box);

            Set<Box> newBoxSet = new HashSet<>(collapse(withoutBox));
            Set<Box> boxSetCopy = new HashSet<>(boxSet);
            boxSetCopy.removeAll(newBoxSet);
            collapseTotal += boxSetCopy.size() - 1;
            // System.out.println(box + " " + (boxSetCopy.size()-1));
        }
        return collapseTotal;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
       /*data = """
               1,0,1~1,2,1
               0,0,2~2,0,2
               0,2,3~2,2,3
               0,0,4~0,2,4
               2,0,5~2,2,5
               0,1,6~2,1,6
               1,1,8~1,1,9""";*/
        List<Box> boxes = getBoxes(data);

        System.out.println(partA(boxes));
        System.out.println(partB(boxes));

    }

}
