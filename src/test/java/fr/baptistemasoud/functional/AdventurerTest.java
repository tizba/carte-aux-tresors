package fr.baptistemasoud.functional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AdventurerTest {
    @Test
    void constructor_NameNull_ThrowsNullPointer() {
        assertThrows(NullPointerException.class, () -> new Adventurer(Adventurer.Orientation.EAST, null));
    }

    @Test
    void constructor_OrientationNull_ThrowsNullPointer() {
        assertThrows(NullPointerException.class, () -> new Adventurer(null, "name"));
    }

    @ParameterizedTest
    @EnumSource(Adventurer.Orientation.class)
    void constructor_CorrectProperties(Adventurer.Orientation orientation) {
        String name = "testName";
        Adventurer adventurer = new Adventurer(orientation, name);

        assertEquals(orientation, adventurer.getOrientation());
        assertEquals(name, adventurer.getName());
    }

    @Test
    void getName() {
        String name = "testName";
        Adventurer adventurer = new Adventurer(Adventurer.Orientation.EAST, name);
        assertEquals(name, adventurer.getName());
    }

    @Nested
    class RotateRight {
        @Test
        void rotateRight_WhenNorth_FaceEast() {
            Adventurer adventurer = new Adventurer(Adventurer.Orientation.NORTH, "name");
            adventurer.rotateRight();
            assertEquals(Adventurer.Orientation.EAST, adventurer.getOrientation());
        }

        @Test
        void rotateRight_WhenWest_FaceNorth() {
            Adventurer adventurer = new Adventurer(Adventurer.Orientation.WEST, "name");
            adventurer.rotateRight();
            assertEquals(Adventurer.Orientation.NORTH, adventurer.getOrientation());
        }

        @Test
        void rotateRight_WhenSouth_FaceWest() {
            Adventurer adventurer = new Adventurer(Adventurer.Orientation.SOUTH, "name");
            adventurer.rotateRight();
            assertEquals(Adventurer.Orientation.WEST, adventurer.getOrientation());
        }

        @Test
        void rotateRight_WhenEast_FaceSouth() {
            Adventurer adventurer = new Adventurer(Adventurer.Orientation.EAST, "name");
            adventurer.rotateRight();
            assertEquals(Adventurer.Orientation.SOUTH, adventurer.getOrientation());
        }
    }

    @Nested
    class RotateLeft {
        @Test
        void rotateLeft_WhenNorth_FaceWest() {
            Adventurer adventurer = new Adventurer(Adventurer.Orientation.NORTH, "name");
            adventurer.rotateLeft();
            assertEquals(Adventurer.Orientation.WEST, adventurer.getOrientation());
        }

        @Test
        void rotateLeft_WhenWest_FaceSouth() {
            Adventurer adventurer = new Adventurer(Adventurer.Orientation.WEST, "name");
            adventurer.rotateLeft();
            assertEquals(Adventurer.Orientation.SOUTH, adventurer.getOrientation());
        }

        @Test
        void rotateLeft_WhenSouth_FaceEast() {
            Adventurer adventurer = new Adventurer(Adventurer.Orientation.SOUTH, "name");
            adventurer.rotateLeft();
            assertEquals(Adventurer.Orientation.EAST, adventurer.getOrientation());
        }

        @Test
        void rotateLeft_WhenEast_FaceNorth() {
            Adventurer adventurer = new Adventurer(Adventurer.Orientation.EAST, "name");
            adventurer.rotateLeft();
            assertEquals(Adventurer.Orientation.NORTH, adventurer.getOrientation());
        }
    }
}