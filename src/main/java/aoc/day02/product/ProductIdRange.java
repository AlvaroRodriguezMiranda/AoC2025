package aoc.day02.product;

public record ProductIdRange(long firstId, long lastId) {
    public ProductIdRange {
        if (firstId <= 0 || lastId <= 0) {
            throw new IllegalArgumentException("Product IDs must be positive");
        }
        if (firstId > lastId) {
            throw new IllegalArgumentException("Range start cannot be greater than range end");
        }
    }

    public boolean contains(long productId) {
        return productId >= firstId && productId <= lastId;
    }
}
