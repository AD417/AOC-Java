package io.github.ad417.year2015.day14;

import tk.vivas.adventofcode.AocUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day14 {
    static class Reindeer {
        int speed;
        int runTime;
        int restTime;

        boolean running = true;
        int distance = 0;
        int stateTimeRemaining;
        int points = 0;

        public Reindeer(int speed, int runTime, int restTime) {
            this.speed = speed;
            this.runTime = runTime;
            this.restTime = restTime;

            this.stateTimeRemaining = runTime;
        }

        public void award() {
            points++;
        }

        public int getDistance() {
            return distance;
        }

        public int getPoints() {
            return points;
        }

        public int getStateTimeRemaining() {
            return stateTimeRemaining;
        }

        static Reindeer makeDeer(String line) {
            Matcher deerData = Pattern.compile(
                    "(\\w+) can fly (\\d+) km/s for (\\d+) seconds, but then must rest for (\\d+) seconds."
            ).matcher(line);
            if (!deerData.find()) throw new RuntimeException("Bad data: '"+line+"'");

            int speed = Integer.parseInt(deerData.group(2));
            int runTime = Integer.parseInt(deerData.group(3));
            int restTime = Integer.parseInt(deerData.group(4));

            return new Reindeer(speed, runTime, restTime);
        }

        void tick() {
            if (running) distance += speed;
            stateTimeRemaining--;
            if (stateTimeRemaining == 0) {
                running = !running;
                stateTimeRemaining = running ? runTime : restTime;
            }
        }
    }
    private static int partA(String data) {
        List<Reindeer> deer = data.lines().map(Reindeer::makeDeer).toList();

        for (int i = 0; i < 2503; i++) deer.forEach(Reindeer::tick);

        return deer.stream().mapToInt(Reindeer::getDistance).max().orElseThrow();
    }

    private static int partB(String data) {
        List<Reindeer> deer = data.lines().map(Reindeer::makeDeer).toList();

        for (int i = 0; i < 2503; i++) {
            deer.forEach(Reindeer::tick);
            int furthest = deer.stream().mapToInt(Reindeer::getDistance).max().orElseThrow();
            deer.stream().filter(r -> r.getDistance() == furthest).forEach(Reindeer::award);
        }
        return deer.stream().mapToInt(Reindeer::getPoints).max().orElseThrow();
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();

        AocUtils.sendPuzzleAnswer(1, partA(data));
        AocUtils.sendPuzzleAnswer(2, partB(data));
    }

}
