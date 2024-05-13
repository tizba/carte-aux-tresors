package fr.baptistemasoud.functional.exception;

public class AdventurerOnAdventurerException extends RuntimeException {
    public AdventurerOnAdventurerException() {
        super("A Cell cannot contain multiple Adventurers");
    }
}
