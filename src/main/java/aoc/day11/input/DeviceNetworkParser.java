package aoc.day11.input;

import aoc.day11.reactor.DeviceNetwork;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class DeviceNetworkParser {
    public DeviceNetwork parse(List<String> lines) {
        Map<String, List<String>> outputsByDevice = new LinkedHashMap<>();

        lines.stream()
                .filter(line -> !line.isBlank())
                .forEach(line -> parseLine(line, outputsByDevice));

        if (outputsByDevice.isEmpty()) {
            throw new IllegalArgumentException("Device network cannot be empty");
        }

        return new DeviceNetwork(outputsByDevice);
    }

    private void parseLine(String line, Map<String, List<String>> outputsByDevice) {
        String[] parts = line.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Device line must contain one colon: " + line);
        }

        String device = parts[0].trim();
        if (device.isEmpty()) {
            throw new IllegalArgumentException("Device name cannot be empty");
        }
        if (outputsByDevice.containsKey(device)) {
            throw new IllegalArgumentException("Device is defined more than once: " + device);
        }

        outputsByDevice.put(device, parseOutputs(parts[1]));
    }

    private List<String> parseOutputs(String text) {
        List<String> outputs = new ArrayList<>();
        for (String output : text.trim().split("\\s+")) {
            if (!output.isBlank()) {
                outputs.add(output);
            }
        }

        if (outputs.isEmpty()) {
            throw new IllegalArgumentException("Device must have at least one output");
        }

        return outputs;
    }
}
