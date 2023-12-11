package io.github.ad417.year2015.day19;

import tk.vivas.adventofcode.AocUtils;

import java.util.*;
import java.util.stream.Collectors;

public class Day19 {
    private static HashMap<String, List<List<String>>> getReplacements(String data) {
        HashMap<String, List<List<String>>> replacements = new HashMap<>();

        data.lines().forEach(line -> {
            String[] parts = line.split(" => ");
            String start = parts[0];
            // Split before capital letters
            List<String> result = Arrays.stream(parts[1].split("(?<=[A-Za-z])(?=[A-Z])")).toList();

            List<List<String>> replacementsFromStart;
            if (replacements.containsKey(start)) {
                replacementsFromStart = replacements.get(start);
            } else {
                replacementsFromStart = new LinkedList<>();
                replacements.put(start, replacementsFromStart);
            }
            replacementsFromStart.add(result);
        });
        return replacements;
    }
    private static int partA(HashMap<String, List<List<String>>> replacements, List<String> medicine) {
        HashSet<List<String>> results = new HashSet<>();
        for (int i = 0; i < medicine.size(); i++) {
            String reactant = medicine.get(i);
            if (!replacements.containsKey(reactant)) continue;

            List<List<String>> potentialProducts = replacements.get(reactant);
            for (List<String> product : potentialProducts) {
                List<String> futureMolecule = new LinkedList<>(medicine.subList(0, i));
                futureMolecule.addAll(product);
                if (i != medicine.size()-1) futureMolecule.addAll(medicine.subList(i+1, medicine.size()));
                results.add(futureMolecule);
            }
        }
        return results.size();
    }

    private static int partBButHeapSpace(HashMap<String, List<List<String>>> replacements, List<String> medicine) {
        int maxLen = medicine.size();
        int iterations = 0;

        Set<List<String>> layer = new HashSet<>();
        List<String> initial = new LinkedList<>();
        initial.add("e");
        layer.add(initial);

        while (!layer.contains(medicine)) {
            iterations++;
            Set<List<String>> nextLayer = new HashSet<>();
            layer.forEach(molecule -> {
                for (int i = 0; i < molecule.size(); i++) {
                    String reactant = molecule.get(i);
                    if (!replacements.containsKey(reactant)) continue;

                    List<List<String>> potentialProducts = replacements.get(reactant);
                    for (List<String> product : potentialProducts) {
                        List<String> futureMolecule = new LinkedList<>(molecule.subList(0, i));
                        futureMolecule.addAll(product);
                        if (i != molecule.size()-1) futureMolecule.addAll(molecule.subList(i+1, molecule.size()));
                        if (futureMolecule.size() <= maxLen) nextLayer.add(futureMolecule);
                    }
                }
            });
            System.out.println(nextLayer.size());
            layer = nextLayer;
        }
        return iterations;
    }

    private static HashMap<List<String>, String> reverseReplacements(HashMap<String, List<List<String>>> replacements) {
        HashMap<List<String>, String> inversion = new HashMap<>();
        replacements.forEach((reactant, products) -> products.forEach(product -> inversion.put(product, reactant)));
        return inversion;
    }

    private static Set<List<String>> getPrecursorsTo(
            List<String> molecule, HashMap<List<String>, String> inversions, Set<Integer> inversionSizes
    ) {
        Set<List<String>> precursors = new HashSet<>();
        for (int i = 0; i < molecule.size(); i++) {
            for (int size : inversionSizes) {
                if (i + size > molecule.size()) continue;
                List<String> product = molecule.subList(i, i+size);
                if (!inversions.containsKey(product)) continue;

                String reactant = inversions.get(product);
                if (reactant.equals("e") && !molecule.equals(product)) continue;

                List<String> precursor = new LinkedList<>(molecule.subList(0, i));
                precursor.add(reactant);
                precursor.addAll(molecule.subList(i+size, molecule.size()));
                precursors.add(precursor);
            }
        }
        return precursors;
    }

    private static int partBAlsoHasNoHeapSpace(HashMap<String, List<List<String>>> replacements, List<String> medicine) {
        HashMap<List<String>, String> inversions = reverseReplacements(replacements);
        Set<Integer> inversionSizes = inversions.keySet().stream().map(List::size).collect(Collectors.toSet());

        List<String> solution = new LinkedList<>();
        solution.add("e");

        // Take the medicine, and work backwards to create the problem!
        Set<List<String>> precursors = new HashSet<>();
        precursors.add(medicine);

        int iterations = 0;
        while (!precursors.contains(solution)) {
            iterations++;
            Set<List<String>> previousPrecursors = new HashSet<>();
            precursors.forEach(molecule -> {
                previousPrecursors.addAll(getPrecursorsTo(molecule, inversions, inversionSizes));
            });
            precursors = previousPrecursors;
            System.out.println(precursors.size());
        }
        return iterations;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        String[] parts = data.split("\n\n");
        HashMap<String, List<List<String>>> replacements = getReplacements(parts[0]);
        List<String> medicine = Arrays.stream(parts[1].split("(?<=[A-Za-z])(?=[A-Z])")).toList();
        System.out.println(medicine.size());

        // System.out.println(partB(replacements, medicine));
        AocUtils.sendPuzzleAnswer(1, partA(replacements, medicine));
        // I give up.
        // AocUtils.sendPuzzleAnswer(2, partB(replacements, medicine));
    }
}
