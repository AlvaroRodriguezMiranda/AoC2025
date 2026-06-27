package aoc.day11.reactor;

import java.util.List;
import java.util.Map;

public final class DeviceNetwork {
    private final Map<String, List<String>> outputsByDevice;

    public DeviceNetwork(Map<String, List<String>> outputsByDevice) {
        this.outputsByDevice = outputsByDevice.entrySet().stream()
                .collect(java.util.stream.Collectors.toUnmodifiableMap(
                        Map.Entry::getKey,
                        entry -> List.copyOf(entry.getValue())
                ));
    }

    public List<String> outputsFrom(String device) {
        return outputsByDevice.getOrDefault(device, List.of());
    }

    public boolean contains(String device) {
        return outputsByDevice.containsKey(device);
    }
}
