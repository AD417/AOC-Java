package io.github.ad417.year2015.day04;

import tk.vivas.adventofcode.AocUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Day04 {
    private static int partA(String data) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsae) { throw new RuntimeException(); }

        int answer = 0;
        byte[] messageBytes = data.getBytes(StandardCharsets.UTF_8);
        byte[] hash = md5.digest(messageBytes);
        while ((hash[0] | hash[1] | hash[2] & 0xF0) != 0) {
            answer++;
            String check = data + answer;
            messageBytes = check.getBytes(StandardCharsets.UTF_8);
            hash = md5.digest(messageBytes);
        }
        return answer;
    }

    private static int partB(String data) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsae) { throw new RuntimeException(); }

        int answer = 0;
        byte[] messageBytes = data.getBytes(StandardCharsets.UTF_8);
        byte[] hash = md5.digest(messageBytes);
        while ((hash[0] | hash[1] | hash[2]) != 0) {
            answer++;
            String check = data + answer;
            messageBytes = check.getBytes(StandardCharsets.UTF_8);
            hash = md5.digest(messageBytes);
        }
        return answer;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();

        AocUtils.sendPuzzleAnswer(1, partA(data));
        AocUtils.sendPuzzleAnswer(2, partB(data));
    }
}
