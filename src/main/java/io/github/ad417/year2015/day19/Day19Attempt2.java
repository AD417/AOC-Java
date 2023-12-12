package io.github.ad417.year2015.day19;

import tk.vivas.adventofcode.AocUtils;

import java.util.*;
import java.util.regex.Pattern;

public class Day19Attempt2 {
    record Replacement(String first, String second) {
        private static int INTERMEDIATE_INT = 0;
        public static String getUniqueId() {
            return String.format("int_%d", INTERMEDIATE_INT++);
        }

        @Override
        public String toString() {
            return "[" + first + second + "]";
        }
        public List<String> unpack() {
            return List.of(first, second);
        }
    }

    private static Map<String, List<Replacement>> getGrammar(String data) {
        HashMap<String, List<Replacement>> grammar = new HashMap<>();
        data.lines().forEach(line -> {
            String[] components = line.split(" => ");
            String reactant = components[0];
            List<String> product = List.of(components[1].split("(?<=[A-Za-z])(?=[A-Z])"));
            List<String> rules = new LinkedList<>(product);
            while (rules.size() > 2) {
                List<String> ruleLayer = new LinkedList<>();
                for (int index = 0; index < rules.size()-1; index += 2) {
                    Replacement newRule = new Replacement(rules.get(index), rules.get(index+1));
                    String existingRuleId = grammar.keySet().stream()
                            .filter(x -> grammar.get(x).get(0).equals(newRule))
                            .findFirst().orElse("");
                    if (existingRuleId.isEmpty()) {
                        existingRuleId = Replacement.getUniqueId();
                        grammar.put(existingRuleId, List.of(newRule));
                    }
                    ruleLayer.add(existingRuleId);
                }
                if (rules.size() % 2 == 1) ruleLayer.add(rules.get(rules.size() - 1));
                rules = ruleLayer;
            }
            Replacement newRule = new Replacement(rules.get(0), rules.get(1));
            List<Replacement> rulesForReactant;
            if (grammar.containsKey(reactant)) {
                rulesForReactant = grammar.get(reactant);
            } else {
                rulesForReactant = new LinkedList<>();
                grammar.put(reactant, rulesForReactant);
            }
            rulesForReactant.add(newRule);
        });

        return grammar;
    }

    private static Map<Replacement, String> invertGrammar(Map<String, List<Replacement>> grammar) {
        Map<Replacement, String> inversion = new HashMap<>();
        for (String reactant : grammar.keySet()) {
            List<Replacement> products = grammar.get(reactant);
            for (Replacement rep : products) inversion.put(rep, reactant);
        }
        return inversion;
    }

    private static int partA(Map<String, List<Replacement>> grammar, List<String> molecule) {
        Set<List<String>> products = new HashSet<>();
        for (int i = 0; i < molecule.size(); i++) {
            String reactant = molecule.get(i);
            if (!grammar.containsKey(reactant)) continue;
            for (Replacement rep : grammar.get(reactant)) {
                List<String> result = rep.unpack();
                boolean hasIntermediate = result.stream().anyMatch(x -> x.startsWith("int_"));
                while (hasIntermediate) {
                    List<String> nextResult = new LinkedList<>();
                    for (String element : result) {
                        if (element.startsWith("int_")) nextResult.addAll(grammar.get(element).get(0).unpack());
                        else nextResult.add(element);
                    }
                    result = nextResult;
                    hasIntermediate = result.stream().anyMatch(x -> x.startsWith("int_"));
                }
                List<String> product = new LinkedList<>(molecule.subList(0, i));
                product.addAll(result);
                product.addAll(molecule.subList(i+1, molecule.size()));
                products.add(product);
            }
        }
        return products.size();
    }

    private static int partB(String[] lines) {
        return 0;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        String[] components = data.split("\n\n");

        Map<String, List<Replacement>> grammar = getGrammar(components[0]);
        List<String> molecule = List.of(components[1].split("(?<=[A-Za-z])(?=[A-Z])"));
        Map<Replacement, String> grammarInverted = invertGrammar(grammar);


        AocUtils.sendPuzzleAnswer(1, partA(grammar, molecule));
        //AocUtils.sendPuzzleAnswer(2, partB(lines));
    }
}
