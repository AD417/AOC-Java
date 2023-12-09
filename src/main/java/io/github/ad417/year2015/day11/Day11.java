package io.github.ad417.year2015.day11;

import tk.vivas.adventofcode.AocUtils;

import java.util.regex.Pattern;

public class Day11 {
    private static String sanitize(String current) {
        char[] letters = current.toCharArray();
        boolean illegalFound = false;
        for (int i = 0; i < letters.length; i++) {
            if (illegalFound) {
                letters[i] = 'a';
                continue;
            }
            char c = letters[i];
            if (c == 'i' || c == 'o' || c == 'l') {
                illegalFound = true;
                letters[i]++;
            }
        }
        return new String(letters);
    }
    private static String increment(String current) {
        char[] letters = current.toCharArray();
        for (int i = letters.length - 1; i >= 0; i--) {
            char c = letters[i];
            c++;
            if (c == 'i' || c == 'o' || c == 'l') c++;
            letters[i] = c;
            if (letters[i] > 'z') letters[i] = 'a';
            else break;
        }
        return new String(letters);
    }
    private static boolean isAscending(String letters) {
        char[] c = letters.toCharArray();
        return c[0] + 1 == c[1] && c[1] + 1 == c[2];
    }
    private static String partA(String password) {
        String nextPassword = sanitize(password);
        boolean isValid = false;
        while (!isValid) {
            nextPassword = increment(nextPassword);

            boolean hasAscending = false;
            for (int i = 0; i < nextPassword.length()-2; i++) {
                if (isAscending(nextPassword.substring(i, i + 3))) {
                    hasAscending = true;
                    break;
                }
            }
            if (!hasAscending) continue;
            boolean hasOneDouble = false;
            for (int i = 0; i < nextPassword.length()-1; i++) {
                if (nextPassword.charAt(i) == nextPassword.charAt(i+1)) {
                    if (hasOneDouble) {
                        isValid = true;
                        break;
                    }
                    hasOneDouble = true;
                    i++;
                }
            }
        }

        return nextPassword;
    }

    private static String partB(String data) {
        return partA(partA(data));
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();

        AocUtils.sendPuzzleAnswer(1, partA(data));
        AocUtils.sendPuzzleAnswer(2, partB(data));
    }
}
