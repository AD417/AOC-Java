package io.github.ad417.year2015.day13;

import tk.vivas.adventofcode.AocUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day13 {
    private static HashMap<String, HashMap<String, Integer>> getHappyMap(String data) {
        HashMap<String, HashMap<String, Integer>> happyMap = new HashMap<>();
        Pattern parser = Pattern.compile("(\\w+) would (gain|lose) (\\d+) happiness units by sitting next to (\\w+)");

        data.lines().forEach(line -> {
            Matcher m = parser.matcher(line);
            if (!m.find()) return;
            String firstName = m.group(1);
            String secondName = m.group(4);
            int happiness = Integer.parseInt(m.group(3));
            if (m.group(2).equals("lose")) happiness *= -1;

            HashMap<String, Integer> personalMap;
            if (happyMap.containsKey(firstName)) {
                personalMap = happyMap.get(firstName);
            } else {
                personalMap = new HashMap<>();
                happyMap.put(firstName, personalMap);
            }

            personalMap.put(secondName, happiness);
        });

        return happyMap;
    }

    private static Set<List<String>> getAllArrangementsFrom(List<String> partialArrangement, Set<String> allNames) {
        Set<List<String>> completeArrangements = new HashSet<>();

        if (partialArrangement.size() == allNames.size()) {
            completeArrangements.add(partialArrangement);
            return completeArrangements;
        }

        allNames.forEach(name -> {
            if (partialArrangement.contains(name)) return;
            List<String> nextPartialArrangement = new ArrayList<>(allNames.size());
            nextPartialArrangement.addAll(partialArrangement);
            nextPartialArrangement.add(name);

            completeArrangements.addAll(getAllArrangementsFrom(nextPartialArrangement, allNames));
        });
        return completeArrangements;
    }

    private static int partA(HashMap<String, HashMap<String, Integer>> happyMap) {
        Set<String> allNames = happyMap.keySet();
        List<String> fixedStartPointList = new ArrayList<>(allNames.size());
        fixedStartPointList.add(allNames.stream().findFirst().orElseThrow());

        Set<List<String>> allArrangements = getAllArrangementsFrom(fixedStartPointList, allNames);

        return allArrangements.stream().mapToInt(arrangement -> {
            int totalHappiness = 0;
            String firstName;
            String secondName;
            for (int i = 0; i < arrangement.size()-1; i++) {
                firstName = arrangement.get(i);
                secondName = arrangement.get(i+1);
                totalHappiness += happyMap.get(firstName).get(secondName);
                totalHappiness += happyMap.get(secondName).get(firstName);
            }
            firstName = arrangement.get(arrangement.size()-1);
            secondName = arrangement.get(0);
            totalHappiness += happyMap.get(firstName).get(secondName);
            totalHappiness += happyMap.get(secondName).get(firstName);

            return totalHappiness;
        }).max().orElseThrow();
    }

    private static int partB(HashMap<String, HashMap<String, Integer>> happyMap) {
        HashMap<String, Integer> myMap = new HashMap<>();
        for (String otherPerson : happyMap.keySet()) {
            myMap.put(otherPerson, 0);
            HashMap<String, Integer> theirMap = happyMap.get(otherPerson);
            theirMap.put("Me", 0);
        }
        happyMap.put("Me", myMap);

        return partA(happyMap);
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        HashMap<String, HashMap<String, Integer>> happyMap = getHappyMap(data);

        AocUtils.sendPuzzleAnswer(1, partA(happyMap));
        AocUtils.sendPuzzleAnswer(2, partB(happyMap));
    }

}
