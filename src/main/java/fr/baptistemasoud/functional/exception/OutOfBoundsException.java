package fr.baptistemasoud.functional.exception;

public class OutOfBoundsException extends RuntimeException {
    public OutOfBoundsException(int height, int width, int x, int y) {
        super("x=%d and y=%d are out of map bounds (height=%d, width=%d)".formatted(height, width, x, y));
    }
}
