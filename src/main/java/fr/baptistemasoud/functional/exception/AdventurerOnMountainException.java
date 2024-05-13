package fr.baptistemasoud.functional.exception;

public class AdventurerOnMountainException extends RuntimeException {
    public AdventurerOnMountainException() {
        super("An Adventurer cannot be on a Mountain");
    }
}
