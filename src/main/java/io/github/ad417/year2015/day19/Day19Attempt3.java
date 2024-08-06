package io.github.ad417.year2015.day19;

import java.util.*;

public class Day19Attempt3 {
    public static final String VAL = "HAHAH";
    private static HashMap<String, List<String>> getReplacements(String data) {
        HashMap<String, List<String>> replacements = new HashMap<>();

        data.lines().forEach(line -> {
            String[] parts = line.split(" => ");
            System.out.println(VAL);
            String start = parts[0];
            // Split before capital letters
            String result = parts[1]; // .split("(?<=[A-Za-z])(?=[A-Z])");

            List<String> replacementsFromStart;
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
    private static int partA(HashMap<String, List<String>> replacements, List<String> medicine) {
        HashSet<List<String>> results = new HashSet<>();
        for (int i = 0; i < medicine.size(); i++) {
            String reactant = medicine.get(i);
            if (!replacements.containsKey(reactant)) continue;

            List<String> potentialProducts = replacements.get(reactant);
            for (String product : potentialProducts) {
                List<String> futureMolecule = new LinkedList<>(medicine.subList(0, i));
                futureMolecule.add(product);
                if (i != medicine.size()-1) futureMolecule.addAll(medicine.subList(i+1, medicine.size()));
                results.add(futureMolecule);
            }
        }
        return results.size();
    }
}
