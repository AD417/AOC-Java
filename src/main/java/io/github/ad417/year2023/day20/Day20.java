package io.github.ad417.year2023.day20;

import tk.vivas.adventofcode.AocUtils;

import java.util.*;

public class Day20 {
    record Pulse(String origin, String target, boolean high) {}
    static class Module {
        // As you can tell, speed was not something I cared about while solving
        // this problem. As a result, I employed polymorphism.
        String name;
        List<String> outputs;

        public Module(String name, List<String> outputs) {
            this.name = name;
            this.outputs = outputs;
        }
        public boolean isOn() {
            return false;
        }

        public List<Pulse> emit(Pulse pulse) {
            boolean isOn = isOn();
            return outputs.stream().map(x -> new Pulse(name, x, isOn)).toList();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Module module = (Module) o;
            return Objects.equals(name, module.name) && this.isOn() == module.isOn();
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, isOn());
        }
    }
    static class FlipFlop extends Module {
        private boolean on = false;

        public FlipFlop(String name, List<String> outputs) {
            super(name, outputs);
        }

        @Override
        public boolean isOn() {
            return on;
        }

        @Override
        public List<Pulse> emit(Pulse pulse) {
            if (pulse.high) return new LinkedList<>();
            on = !on;
            return super.emit(pulse);
        }
    }

    static class Conjunction extends Module {
        HashMap<String, Boolean> inputs = new HashMap<>();

        public Conjunction(String name, List<String> outputs, List<String> inputs) {
            super(name, outputs);
            inputs.forEach(x -> this.inputs.put(x, false));
        }

        @Override
        public boolean isOn() {
            // NAND ALL the hashmap values.
            return !inputs.values().stream().reduce(true, (x, y) -> x & y);
        }

        @Override
        public List<Pulse> emit(Pulse pulse) {
            inputs.put(pulse.origin, pulse.high);
            return super.emit(pulse);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            Conjunction that = (Conjunction) o;
            return Objects.equals(inputs, that.inputs);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), inputs);
        }
    }

    private static HashMap<String, Module> getModules(String input) {
        // Since the Conjunction modules need to know EVERY module that goes
        // into them, I had to iterate twice.
        HashMap<String, Module> modules = new HashMap<>();
        HashMap<String, List<String>> allInputs = new HashMap<>();
        for (String line : input.split("\n")) {
            String[] parts = line.substring(1).split(" -> ");
            String name = parts[0];
            List<String> outputs = Arrays.stream(parts[1].split(", ")).toList();

            for (String output : outputs) {
                List<String> inputs = allInputs.computeIfAbsent(output, k -> new LinkedList<>());
                inputs.add(name);
            }

            if (line.startsWith("%")) modules.put(name, new FlipFlop(name, outputs));
            else if (!line.startsWith("&")) modules.put("broadcaster", new Module("broadcaster", outputs));
        }
        for (String line : input.split("\n")) {
            if (!line.startsWith("&")) continue;
            String[] parts = line.substring(1).split(" -> ");
            String name = parts[0];
            List<String> outputs = Arrays.stream(parts[1].split(", ")).toList();

            modules.put(name, new Conjunction(name, outputs, allInputs.get(name)));
        }

        return modules;
    }
    private static long partA(String input) {
        // For 1000 steps, determine how many modules send high or low pulses,
        // and send the product out at the end.
        HashMap<String, Module> modules = getModules(input);
        int high = 0;
        int low = 0;

        Queue<Pulse> pulses = new LinkedList<>();
        for (int i = 0; i < 1000; i++) {
            pulses.addAll(modules.get("broadcaster").emit(null));
            low++;
            while (!pulses.isEmpty()) {
                Pulse pulse = pulses.poll();
                Module mod = modules.get(pulse.target);
                if (mod != null) {
                    // System.out.println(pulse.name());
                    pulses.addAll(mod.emit(pulse));
                }
                if (pulse.high) high++;
                else low++;
            }
        }

        return (long) low * high;
    }

    private static long partB(String input) {
        // This one is a more involved version of Day 8.
        // The input has another unique property that needs to be used to find
        // the solution in a reasonable amount of time. When the broadcaster
        // sends signals out to various modules, each module informed is part
        // of its own, unique loop. These loops repeat after a prime number of
        // iterations.
        // Each one fires after a large (Several thousand) number of
        // iterations, so the question is how long it will take for all the
        // cycles to sync up and send a signal to RX. This is the LCM (or
        // product) of all the cycles' lengths. The hard part is determining
        // the cycles, which is just the frequency of pulses from the modules
        // that transmit to the grandparent module of RX, because of how all
        // the cycles are tied together. Conjunction is effectively NAND, so
        // NAND( NAND (stuff) ) is AND(stuff).
        HashMap<String, Module> modules = getModules(input);

        /*Conjunction toRX = (Conjunction) modules.values().stream()
                .filter(x -> x.outputs.contains("rx"))
                .findFirst().orElseThrow();
        Set<String> rxReliant = new HashSet<>(toRX.inputs.keySet());
        HashMap<String, Integer> rxRequirements = new HashMap<>();

        for (int i = 1; rxReliant.size() != rxRequirements.size(); i++) {*/

        HashMap<String, Integer> first = new HashMap<>();
        HashMap<String, Integer> second = new HashMap<>();
        for (int presses = 1; second.size() + 1 < modules.size(); presses++) {
            Queue<Pulse> pulses = new LinkedList<>(modules.get("broadcaster").emit(null));
            while (!pulses.isEmpty()) {
                Pulse pulse = pulses.poll();
                Module mod = modules.get(pulse.target);
                if (mod != null) {
                    pulses.addAll(mod.emit(pulse));
                }
                // If we send a high pulse to the NAND gate running
                if (pulse.high) continue;
                String target = pulse.target;
                if (!first.containsKey(target)) {
                    first.put(target, presses);
                } else if (!second.containsKey(target)) {
                    second.put(target, presses - first.get(target));
                }
            }
        }

        long prod = 1;
        String rxPrecursor = modules.values().stream()
                .filter(x -> x.outputs.contains("rx"))
                .findFirst().orElseThrow()
                .name;

        for (String entry : second.keySet()) {
            Module m = modules.get(entry);
            if (m == null) {
                System.out.println(entry);
                continue;
            }
            if (!m.outputs.contains(rxPrecursor)) continue;
            prod *= second.get(entry);
        }
        // second.keySet().stream().filter(x -> modules.get(x).outputs.contains("sq")).forEach(x -> System.out.println(second.get(x)));
        //return second.values().stream().mapToLong(Integer::longValue).filter(Day20::isNotPow2).reduce(1L, Day20::LCM);
        return prod;
    }

    private static boolean isNotPow2(long x) {
        while (x > 1) {
            if ((x & 1) == 1) return true;
            x >>= 1;
        }
        return false;
    }

    private static long LCM(long a, long b) {
        if (b == 0) return a;
        long aFactor = a, bFactor = b;
        while (bFactor > 0) {
            long c = aFactor;
            aFactor = bFactor;
            bFactor = c % aFactor;
        }
        return a / aFactor * b;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        /*data = """
                broadcaster -> a
                %a -> inv, con
                &inv -> b
                %b -> con
                &con -> output""";*/

        System.out.println(partA(data));
        System.out.println(partB(data));

        //AocUtils.sendPuzzleAnswer(1, partA(lines));
        //AocUtils.sendPuzzleAnswer(2, partB(lines));
    }

}
