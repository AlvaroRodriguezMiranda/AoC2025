package aoc.day11.reactor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class PathCounter {
    public long countPaths(DeviceNetwork network, String startDevice, String targetDevice) {
        if (!network.contains(startDevice)) {
            throw new IllegalArgumentException("Start device is not defined: " + startDevice);
        }

        return countPathsFrom(network, startDevice, targetDevice, new HashMap<>(), new HashSet<>());
    }

    public long countPathsVisitingBoth(
            DeviceNetwork network,
            String startDevice,
            String targetDevice,
            String firstRequiredDevice,
            String secondRequiredDevice
    ) {
        if (!network.contains(startDevice)) {
            throw new IllegalArgumentException("Start device is not defined: " + startDevice);
        }

        RequiredVisitState initialState = RequiredVisitState.from(
                startDevice,
                firstRequiredDevice,
                secondRequiredDevice
        );
        return countRequiredPathsFrom(
                network,
                startDevice,
                targetDevice,
                firstRequiredDevice,
                secondRequiredDevice,
                initialState,
                new HashMap<>(),
                new HashSet<>()
        );
    }

    private long countPathsFrom(
            DeviceNetwork network,
            String currentDevice,
            String targetDevice,
            Map<String, Long> pathsByDevice,
            Set<String> currentPath
    ) {
        if (currentDevice.equals(targetDevice)) {
            return 1;
        }

        Long cachedPaths = pathsByDevice.get(currentDevice);
        if (cachedPaths != null) {
            return cachedPaths;
        }

        if (!currentPath.add(currentDevice)) {
            throw new IllegalStateException("Device network contains a cycle involving: " + currentDevice);
        }

        long paths = network.outputsFrom(currentDevice).stream()
                .mapToLong(output -> countPathsFrom(network, output, targetDevice, pathsByDevice, currentPath))
                .sum();

        currentPath.remove(currentDevice);
        pathsByDevice.put(currentDevice, paths);
        return paths;
    }

    private long countRequiredPathsFrom(
            DeviceNetwork network,
            String currentDevice,
            String targetDevice,
            String firstRequiredDevice,
            String secondRequiredDevice,
            RequiredVisitState requiredVisitState,
            Map<RequiredPathKey, Long> pathsByState,
            Set<RequiredPathKey> currentPath
    ) {
        if (currentDevice.equals(targetDevice)) {
            return requiredVisitState.hasVisitedBoth() ? 1 : 0;
        }

        RequiredPathKey key = new RequiredPathKey(currentDevice, requiredVisitState);
        Long cachedPaths = pathsByState.get(key);
        if (cachedPaths != null) {
            return cachedPaths;
        }

        if (!currentPath.add(key)) {
            throw new IllegalStateException("Device network contains a cycle involving: " + currentDevice);
        }

        long paths = network.outputsFrom(currentDevice).stream()
                .mapToLong(output -> countRequiredPathsFrom(
                        network,
                        output,
                        targetDevice,
                        firstRequiredDevice,
                        secondRequiredDevice,
                        requiredVisitState.afterVisiting(output, firstRequiredDevice, secondRequiredDevice),
                        pathsByState,
                        currentPath
                ))
                .sum();

        currentPath.remove(key);
        pathsByState.put(key, paths);
        return paths;
    }

    private record RequiredPathKey(String device, RequiredVisitState requiredVisitState) {
    }

    private record RequiredVisitState(boolean visitedFirstRequiredDevice, boolean visitedSecondRequiredDevice) {
        private static RequiredVisitState from(String device, String firstRequiredDevice, String secondRequiredDevice) {
            return new RequiredVisitState(
                    device.equals(firstRequiredDevice),
                    device.equals(secondRequiredDevice)
            );
        }

        private RequiredVisitState afterVisiting(String device, String firstRequiredDevice, String secondRequiredDevice) {
            return new RequiredVisitState(
                    visitedFirstRequiredDevice || device.equals(firstRequiredDevice),
                    visitedSecondRequiredDevice || device.equals(secondRequiredDevice)
            );
        }

        private boolean hasVisitedBoth() {
            return visitedFirstRequiredDevice && visitedSecondRequiredDevice;
        }
    }
}
