package io.github.ad417.year2017.day20;

import tk.vivas.adventofcode.AocUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day20 {

    record Vector3(int x, int y, int z) {
        Vector3 add(Vector3 other) {
            return new Vector3(
                    this.x + other.x,
                    this.y + other.y,
                    this.z + other.z
            );
        }
        boolean sameSigns(Vector3 other) {
            boolean sameX = !(this.x < 0) ^ (other.x < 0);
            boolean sameY = !(this.y < 0) ^ (other.y < 0);
            boolean sameZ = !(this.z < 0) ^ (other.z < 0);
            return sameX && sameY && sameZ;
        }
        int distance() {
            return Math.abs(x) + Math.abs(y) + Math.abs(z);
        }
    }

    private static HashMap<Integer, List<Vector3>> getParticles(String data) {
        Pattern pattern = Pattern.compile("(-?\\d+),(-?\\d+),(-?\\d+)");

        HashMap<Integer, List<Vector3>> particles = new HashMap<>();
        String[] lines = data.split("\n");
        for (int i = 0; i < lines.length; i++) {
            Matcher m = pattern.matcher(lines[i]);
            List<Vector3> particle = new ArrayList<>();
            while (m.find()) {
                particle.add(new Vector3(
                        Integer.parseInt(m.group(1)),
                        Integer.parseInt(m.group(2)),
                        Integer.parseInt(m.group(3))
                ));
            }
            particles.put(i, particle);
        }

        return particles;
    }

    private static int partA(HashMap<Integer, List<Vector3>> particles) {
        int lowestAcc = particles.values().stream()
                .map(x -> x.get(2))
                .min(Comparator.comparingInt(Vector3::distance))
                .orElse(new Vector3(0,0,0))
                .distance();

        HashMap<Integer, List<Vector3>> slowest = new HashMap<>();

        particles.forEach((k,v) -> {
            if (v.get(2).distance() != lowestAcc) return;
            slowest.put(k, v);
        });

        return slowest.keySet().stream().findFirst().orElseThrow();
    }

    private static int partB(HashMap<Integer, List<Vector3>> particles) {
        // There probably exists an actually intelligent way to do this.
        while (particles.values().stream().anyMatch(x -> x.get(0).distance() < 100000)) {
            final HashMap<Vector3, Integer> positions = new HashMap<>();
            final HashSet<Integer> collisions = new HashSet<>();
            particles.forEach((k, v) -> {
                v.set(1, v.get(1).add(v.get(2)));
                v.set(0, v.get(0).add(v.get(1)));

                if (positions.containsKey(v.get(0))) {
                    collisions.add(positions.get(v.get(0)));
                    collisions.add(k);
                }
                positions.put(v.get(0), k);
            });
            collisions.forEach(particles::remove);
        }
        return particles.size();
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        HashMap<Integer, List<Vector3>> particles = getParticles(data);

        AocUtils.sendPuzzleAnswer(1, partA(particles));
        AocUtils.sendPuzzleAnswer(2, partB(particles));
    }
}
