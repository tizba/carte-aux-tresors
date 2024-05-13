package fr.baptistemasoud.functional;

import java.util.Objects;

public class Adventurer {
    private final String name;
    private Orientation orientation;

    /**
     * Create a new adventurer
     * @param orientation the initial orientation. Must not be null
     * @param name        the name
     * @throws NullPointerException if orientation or name is null
     */
    public Adventurer(Orientation orientation, String name) {
        this.orientation = Objects.requireNonNull(orientation, "orientation must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
    }

    /**
     * Rotate the adventurer to the left
     */
    public void rotateLeft() {
        switch (this.orientation) {
            case NORTH -> this.orientation = Orientation.WEST;
            case EAST -> this.orientation = Orientation.NORTH;
            case SOUTH -> this.orientation = Orientation.EAST;
            case WEST -> this.orientation = Orientation.SOUTH;
        }
    }

    /**
     * Rotate the adventurer to the right
     */
    public void rotateRight() {
        switch (this.orientation) {
            case NORTH -> this.orientation = Orientation.EAST;
            case EAST -> this.orientation = Orientation.SOUTH;
            case SOUTH -> this.orientation = Orientation.WEST;
            case WEST -> this.orientation = Orientation.NORTH;
        }
    }

    /**
     * Get the name of the adventurer
     * @return the name of the adventurer
     */
    public String getName() {
        return name;
    }

    /**
     * Get the orientation of the adventurer
     * @return the orientation of the adventurer
     */
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * The orientation of an adventurer
     */
    public enum Orientation {
        NORTH, EAST, SOUTH, WEST
    }
}
