package io.github.ad417.year2023.day01;

import tk.vivas.adventofcode.AocUtils;

public class Day1 {
    private static String[] NUMBERS = new String[] {
            "one", "two", "three", "four", "five",
            "six", "seven", "eight", "nine"
    };
    private static int partA(String[] lines) {
        int result = 0;
        for (String line : lines) {
            int lineNum = 0;
            char[] lineAsChar = line.toCharArray();
            for (int i = 0; i < lineAsChar.length; i++) {
                int ascii = lineAsChar[i] - '0';
                if (0 <= ascii && ascii < 10) {
                    lineNum += 10 * ascii;
                    break;
                }
            }
            for (int i = lineAsChar.length - 1; i >= 0; i--) {
                int ascii = lineAsChar[i] - '0';
                if (0 <= ascii && ascii < 10) {
                    lineNum += ascii;
                    break;
                }
            }
            result += lineNum;
        }
        return result;
    }

    private static int partAPostmortem(String[] lines) {
        int result = 0;
        for (String line : lines) {
            int first = 0;
            int last = 0;
            for (int i = 0; i < line.length(); i++) {
                int numberVal = line.charAt(i) - '0';
                if (0 < numberVal && numberVal < 10) {
                    if (first == 0) first = numberVal;
                    last = numberVal;
                }
            }
            result += first * 10 + last;
        }
        return result;
    }

    private static int partB(String[] lines) {
        int result = 0;
        for (String line : lines) {
            int lineNum = 0;
            char[] lineAsChar = line.toCharArray();
            for (int i = 0; i < lineAsChar.length; i++) {
                int ascii = lineAsChar[i] - '0';
                if (0 <= ascii && ascii < 10) {
                    lineNum += 10 * ascii;
                    break;
                }
                boolean writtenOut = false;
                for (int k = 0; k < 9; k++) {
                    if (line.substring(i).startsWith(NUMBERS[k])) {
                        lineNum += 10 * (k+1);
                        writtenOut = true;
                        break;
                    }
                }
                if (writtenOut) break;
            }
            for (int i = lineAsChar.length - 1; i >= 0; i--) {
                int ascii = lineAsChar[i] - '0';
                if (0 <= ascii && ascii < 10) {
                    lineNum += ascii;
                    break;
                }
                boolean writtenOut = false;
                for (int k = 0; k < 9; k++) {
                    if (line.substring(i).startsWith(NUMBERS[k])) {
                        lineNum += k+1;
                        writtenOut = true;
                        break;
                    }
                }
                if (writtenOut) break;
            }
            result += lineNum;
        }
        return result;
    }

    private static int partBPostmortem(String[] lines) {
        int result = 0;
        for (String line : lines) {
            int first = 0;
            int last = 0;
            for (int i = 0; i < line.length(); i++) {
                int numberVal = line.charAt(i) - '0';
                if (0 < numberVal && numberVal < 10) {
                    if (first == 0) first = numberVal;
                    last = numberVal;
                }
                for (int k = 0; k < 9; k++) {
                    if (!line.substring(i).startsWith(NUMBERS[k])) continue;

                    if (first == 0) first = k+1;
                    last = k+1;
                }
            }
            result += first * 10 + last;
        }
        return result;
    }

    public static void main(String[] args) {
        String a = AocUtils.readPuzzleInput();
        String[] lines = a.split("\n");


        AocUtils.sendPuzzleAnswer(1, partA(lines));
        AocUtils.sendPuzzleAnswer(2, partB(lines));

        System.out.println(partA(lines));
        System.out.println(partAPostmortem(lines));
        System.out.println(partB(lines));
        System.out.println(partBPostmortem(lines));
    }
}
