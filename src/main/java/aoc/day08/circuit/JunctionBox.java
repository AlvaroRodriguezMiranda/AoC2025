package aoc.day08.circuit;

public record JunctionBox(long x, long y, long z) {
    public long squaredDistanceTo(JunctionBox other) {
        long xDifference = x - other.x;
        long yDifference = y - other.y;
        long zDifference = z - other.z;
        return xDifference * xDifference + yDifference * yDifference + zDifference * zDifference;
    }
}
