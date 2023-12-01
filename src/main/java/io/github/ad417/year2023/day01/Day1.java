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

    public static void main(String[] args) {
        String a = AocUtils.readPuzzleInput();
        String[] lines = a.split("\n");


        AocUtils.sendPuzzleAnswer(1, partA(lines));
        AocUtils.sendPuzzleAnswer(2, partB(lines));
    }
}
