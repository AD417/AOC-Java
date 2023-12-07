package io.github.ad417.year2023.day07;

import tk.vivas.adventofcode.AocUtils;

import java.util.*;

public class Day07 {
    private static final List<Character> CARDS = "23456789TJQKA".chars().mapToObj(x -> (char)x).toList();
    private static final List<Character> CARDS_JOKER = "J23456789TQKA".chars().mapToObj(x -> (char)x).toList();
    private static final Map<Character, Integer> emptyHand;
    static {
        HashMap<Character, Integer> hand = new HashMap<>();
        for (char c : CARDS) hand.put(c, 0);
        emptyHand = Collections.unmodifiableMap(hand);
    }
    static class Hand implements Comparable<Hand> {
        String cards;
        int bid;
        int type;

        public Hand(String data) {
            this.cards = data.split(" ")[0];
            this.bid = Integer.parseInt(data.split(" ")[1]);

            HashMap<Character, Integer> typeMap = new HashMap<>(emptyHand);
            for (char c : cards.toCharArray()) {
                typeMap.put(c, typeMap.get(c)+1);
            }
            Collection<Integer> vals = typeMap.values();
            if (vals.contains(5)) type = 6;         // Five of a kind!  (I really hope we don't get jokers.)
            else if (vals.contains(4)) type = 5;    // Four of a kind.
            else if (vals.contains(3)) {
                if (vals.contains(2)) type = 4;     // Full House
                else type = 3;                      // Three of a kind
            } else if (vals.contains(2)) {
                vals.remove(2);
                if (vals.contains(2)) type = 2;     // Two pair
                else type = 1;                      // One pair
            } else type = 0;                        // High Card
        }

        @Override
        public int compareTo(Hand other) {
            int out = this.type - other.type;
            for (int i = 0; i < cards.length() && out == 0; i++) {
                out = CARDS.indexOf(this.cards.charAt(i)) - CARDS.indexOf(other.cards.charAt(i));
            }
            return out;
        }
    }
    static class WildHand implements Comparable<WildHand> {
        String cards;
        int bid;
        int type;

        public WildHand(String data) {
            this.cards = data.split(" ")[0];
            this.bid = Integer.parseInt(data.split(" ")[1]);

            HashMap<Character, Integer> typeMap = new HashMap<>(emptyHand);
            for (char c : cards.toCharArray()) {
                typeMap.put(c, typeMap.get(c)+1);
            }
            int maxType = 0;
            for (Character c : CARDS_JOKER) {
                if (c == 'J') continue;

                HashMap<Character, Integer> jokerMap = new HashMap<>(typeMap);
                jokerMap.put(c, jokerMap.get(c) + jokerMap.remove('J'));
                int jokerType;

                Collection<Integer> vals = jokerMap.values();
                if (vals.contains(5)) jokerType = 6;         // Five of a kind!  (aaaaaaaaaaaaaa)
                else if (vals.contains(4)) jokerType = 5;    // Four of a kind.
                else if (vals.contains(3)) {
                    if (vals.contains(2)) jokerType = 4;     // Full House
                    else jokerType = 3;                      // Three of a kind
                } else if (vals.contains(2)) {
                    vals.remove(2);
                    if (vals.contains(2)) jokerType = 2;     // Two pair
                    else jokerType = 1;                      // One pair
                } else jokerType = 0;                        // High Card

                if (maxType < jokerType) maxType = jokerType;
            }
            type = maxType;
        }

        @Override
        public int compareTo(WildHand other) {
            int out = this.type - other.type;
            for (int i = 0; i < cards.length() && out == 0; i++) {
                out = CARDS_JOKER.indexOf(this.cards.charAt(i)) - CARDS_JOKER.indexOf(other.cards.charAt(i));
            }
            return out;
        }
    }
    private static int partA(String data) {
        List<Hand> handsSorted = data.lines().map(Hand::new).sorted().toList();
        int total = 0;
        for (int i = 0; i < handsSorted.size(); i++) {
            total += (i + 1) * handsSorted.get(i).bid;
        }
        return total;
    }

    private static int partB(String data) {
        List<WildHand> handsSorted = data.lines().map(WildHand::new).sorted().toList();
        int total = 0;
        for (int i = 0; i < handsSorted.size(); i++) {
            total += (i + 1) * handsSorted.get(i).bid;
        }
        return total;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();

        AocUtils.sendPuzzleAnswer(1, partA(data));
        AocUtils.sendPuzzleAnswer(2, partB(data));
    }
}
