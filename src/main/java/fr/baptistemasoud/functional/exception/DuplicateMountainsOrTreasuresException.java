package fr.baptistemasoud.functional.exception;

public class DuplicateMountainsOrTreasuresException extends RuntimeException {
    public DuplicateMountainsOrTreasuresException() {
        super("Two mountains/treasures have the same coordinates");
    }
}
