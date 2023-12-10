package io.github.ad417.year2015.day16;

import tk.vivas.adventofcode.AocUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day16 {
    record Sue(
            int id,
            int children, int cats, int samoyeds, int pomeranians,
            int akitas, int vizslas, int goldfish, int trees,
            int cars, int perfumes
    ) {
        static Sue makeSue(String data) {
            int id = -1;
            int children = -1;
            int cats = -1;
            int samoyeds = -1;
            int pomeranians = -1;
            int akitas = -1;
            int vizslas = -1;
            int goldfish = -1;
            int trees = -1;
            int cars = -1;
            int perfumes = -1;
            Matcher m = Pattern.compile("(\\w+): (\\d+)", Pattern.MULTILINE).matcher(data);

            while (m.find()) {
                String property = m.group(1);
                int value = Integer.parseInt(m.group(2));
                switch (property) {
                    case "children" -> children = value;
                    case "cats" -> cats = value;
                    case "samoyeds" -> samoyeds = value;
                    case "pomeranians" -> pomeranians = value;
                    case "akitas" -> akitas = value;
                    case "vizslas" -> vizslas = value;
                    case "goldfish" -> goldfish = value;
                    case "trees" -> trees = value;
                    case "cars" -> cars = value;
                    case "perfumes" -> perfumes = value;
                }
            }
            if (data.startsWith("Sue ")) id = Integer.parseInt(data.split("[ :]", 3)[1]);
            return new Sue(id, children, cats, samoyeds, pomeranians, akitas, vizslas, goldfish, trees, cars, perfumes);
        }

        public boolean equals(Sue other) {
            if (children != -1 && children != other.children) return false;
            if (cats != -1 && cats != other.cats) return false;
            if (samoyeds != -1 && samoyeds != other.samoyeds) return false;
            if (pomeranians != -1 && pomeranians != other.pomeranians) return false;
            if (akitas != -1 && akitas != other.akitas) return false;
            if (vizslas != -1 && vizslas != other.vizslas) return false;
            if (goldfish != -1 && goldfish != other.goldfish) return false;
            if (trees != -1 && trees != other.trees) return false;
            if (cars != -1 && cars != other.cars) return false;
            if (perfumes != -1 && perfumes != other.perfumes) return false;
            return true;
        }

        public boolean actuallyEquals(Sue other) {
            if (children != -1 && children != other.children) return false;
            if (cats != -1 && cats <= other.cats) return false;
            if (samoyeds != -1 && samoyeds != other.samoyeds) return false;
            if (pomeranians != -1 && pomeranians >= other.pomeranians) return false;
            if (akitas != -1 && akitas != other.akitas) return false;
            if (vizslas != -1 && vizslas != other.vizslas) return false;
            if (goldfish != -1 && goldfish >= other.goldfish) return false;
            if (trees != -1 && trees <= other.trees) return false;
            if (cars != -1 && cars != other.cars) return false;
            if (perfumes != -1 && perfumes != other.perfumes) return false;
            return true;
        }
    }
    private static int partA(List<Sue> sues) {
        Sue theOriginal = Sue.makeSue("""
                children: 3
                cats: 7
                samoyeds: 2
                pomeranians: 3
                akitas: 0
                vizslas: 0
                goldfish: 5
                trees: 3
                cars: 2
                perfumes: 1""");

        return sues.stream().filter(sue -> sue.equals(theOriginal)).findFirst().orElseThrow().id();
    }

    private static int partB(List<Sue> sues) {
        Sue theOriginal = Sue.makeSue("""
                children: 3
                cats: 7
                samoyeds: 2
                pomeranians: 3
                akitas: 0
                vizslas: 0
                goldfish: 5
                trees: 3
                cars: 2
                perfumes: 1""");

        return sues.stream().filter(sue -> sue.actuallyEquals(theOriginal)).findFirst().orElseThrow().id();
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        List<Sue> sues = data.lines().map(Sue::makeSue).toList();

        AocUtils.sendPuzzleAnswer(1, partA(sues));
        AocUtils.sendPuzzleAnswer(2, partB(sues));
    }

}
