package aoc.day01.dial;

import java.util.Iterator;
import java.util.List;

public final class RotationProgram implements Iterable<DialRotation> {
    private final List<DialRotation> rotations;

    public RotationProgram(List<DialRotation> rotations) {
        this.rotations = List.copyOf(rotations);
    }

    @Override
    public Iterator<DialRotation> iterator() {
        return rotations.iterator();
    }

    public List<DialRotation> asList() {
        return rotations;
    }

    public int size() {
        return rotations.size();
    }
}
