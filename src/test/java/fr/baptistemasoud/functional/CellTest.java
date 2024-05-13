package fr.baptistemasoud.functional;

import fr.baptistemasoud.functional.exception.AdventurerOnAdventurerException;
import fr.baptistemasoud.functional.exception.AdventurerOnMountainException;
import fr.baptistemasoud.functional.exception.NoTreasureOnCellException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {

    @Test
    void createMountainCell_CorrectProperties() {
        Cell cell = Cell.createMountainCell();

        assertTrue(cell.isMountain());
        assertFalse(cell.isPlain());
        assertFalse(cell.hasAdventurer());
        assertFalse(cell.hasTreasures());
        assertEquals(0, cell.getTreasures());
        assertNull(cell.getAdventurer());
    }

    @Test
    void createPlainCell_CorrectProperties() {
        Cell cell = Cell.createPlainCell();

        assertTrue(cell.isPlain());
        assertFalse(cell.hasAdventurer());
        assertFalse(cell.isMountain());
        assertFalse(cell.hasTreasures());
        assertEquals(0, cell.getTreasures());
        assertNull(cell.getAdventurer());
    }

    @Test
    void createTreasuresCell_CorrectProperties() {
        int treasures = 5;
        Cell cell = Cell.createTreasuresCell(treasures);

        assertTrue(cell.hasTreasures());
        assertEquals(treasures, cell.getTreasures());
        assertFalse(cell.isPlain());
        assertFalse(cell.hasAdventurer());
        assertFalse(cell.isMountain());
        assertNull(cell.getAdventurer());
    }

    @Test
    void createTreasuresCell_WithNegativeAmount_ThrowsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> Cell.createTreasuresCell(-5));
    }

    @Test
    void createTreasuresCell_WithZeroAmount_ThrowsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> Cell.createTreasuresCell(0));
    }

    @Nested
    class removeAdventurer {
        @Test
        void removeAdventurer_OnCellWithAdventurer_RemovesAdventurer() {
            Cell cell = Cell.createPlainCell();
            cell.placeAdventurer(new Adventurer(Adventurer.Orientation.NORTH, "name"));

            cell.removeAdventurer();

            assertFalse(cell.hasAdventurer());
            assertNull(cell.getAdventurer());
        }

        @Test
        void removeAdventurer_OnCellWithAdventurerAndTreasures_RemovesAdventurer() {
            Cell cell = Cell.createTreasuresCell(5);
            cell.placeAdventurer(new Adventurer(Adventurer.Orientation.NORTH, "name"));

            cell.removeAdventurer();

            assertFalse(cell.hasAdventurer());
            assertNull(cell.getAdventurer());
        }
    }

    @Nested
    class placeAdventurer {
        @Test
        void placeAdventurer_OnPlainCell_PlacesCorrectAdventurer() {
            Cell cell = Cell.createPlainCell();

            Adventurer adventurer = new Adventurer(Adventurer.Orientation.NORTH, "name");
            cell.placeAdventurer(adventurer);

            assertTrue(cell.hasAdventurer());
            assertEquals(adventurer, cell.getAdventurer());
            assertEquals(0, cell.getTreasures());
        }

        @Test
        void placeAdventurer_OnCellWithTreasures_PlacesCorrectAdventurer() {
            Cell cell = Cell.createTreasuresCell(5);

            Adventurer adventurer = new Adventurer(Adventurer.Orientation.NORTH, "name");
            cell.placeAdventurer(adventurer);

            assertTrue(cell.hasAdventurer());
        }

        @Test
        void placeAdventurer_OnMountainCell_ThrowsCorrectException() {
            Cell cell = Cell.createMountainCell();

            Adventurer adventurer = new Adventurer(Adventurer.Orientation.NORTH, "name");
            assertThrows(AdventurerOnMountainException.class, () -> cell.placeAdventurer(adventurer));
        }

        @Test
        void placeAdventurer_OnCellContainingAdventurer_ThrowsCorrectException() {
            Cell cell = Cell.createPlainCell();
            cell.placeAdventurer(new Adventurer(Adventurer.Orientation.NORTH, "name"));

            Adventurer adventurer = new Adventurer(Adventurer.Orientation.NORTH, "name");
            assertThrows(AdventurerOnAdventurerException.class, () -> cell.placeAdventurer(adventurer));
        }
    }

    @Nested
    class getTreasures {
        @Test
        void getTreasures_ForPlainCell_ReturnsZero() {
            Cell cell = Cell.createPlainCell();
            assertEquals(0, cell.getTreasures());
        }

        @Test
        void getTreasures_ForCellWithTreasures_ReturnsCorrectAmount() {
            int treasures = 5;
            Cell cell = Cell.createTreasuresCell(treasures);
            assertEquals(treasures, cell.getTreasures());
        }

        @Test
        void getTreasures_ForCellWithNoTreasuresButWithAdventurer_ReturnsZero() {
            Cell cell = Cell.createPlainCell();
            cell.placeAdventurer(new Adventurer(Adventurer.Orientation.NORTH, "name"));
            assertEquals(0, cell.getTreasures());
        }
    }

    @Nested
    class getAdventurer {
        @Test
        void getAdventurer_ForPlainCell_ReturnsNull() {
            Cell cell = Cell.createPlainCell();
            assertNull(cell.getAdventurer());
        }

        @Test
        void getAdventurer_ForCellWithAdventurer_ReturnsAdventurer() {
            Cell cell = Cell.createPlainCell();
            Adventurer adventurer = new Adventurer(Adventurer.Orientation.NORTH, "name");
            cell.placeAdventurer(adventurer);

            assertEquals(adventurer, cell.getAdventurer());
        }

        @Test
        void getAdventurer_ForCellWithNoAdventurer_ReturnsNull() {
            Cell cell = Cell.createPlainCell();
            assertNull(cell.getAdventurer());
        }

        @Test
        void getAdventurer_ForCellWithNoAdventurerAndTreasures_ReturnsNull() {
            Cell cell = Cell.createTreasuresCell(1);
            assertNull(cell.getAdventurer());
        }

        @Test
        void getAdventurer_ForCellWithAdventurerAndTreasures_ReturnsAdventurer() {
            Cell cell = Cell.createTreasuresCell(1);
            Adventurer adventurer = new Adventurer(Adventurer.Orientation.NORTH, "name");
            cell.placeAdventurer(adventurer);

            assertEquals(adventurer, cell.getAdventurer());
        }
    }

    @Nested
    class isMountain {
        @Test
        void isMountain_ForPlainCell_IsFalse() {
            Cell cell = Cell.createPlainCell();
            assertFalse(cell.isMountain());
        }

        @Test
        void isMountain_ForCellWithAdventurer_IsFalse() {
            Cell cell = Cell.createPlainCell();
            cell.placeAdventurer(new Adventurer(Adventurer.Orientation.NORTH, "name"));
            assertFalse(cell.isMountain());
        }

        @Test
        void isMountain_ForCellWithTreasures_IsFalse() {
            Cell cell = Cell.createTreasuresCell(1);
            assertFalse(cell.isMountain());
        }

        @Test
        void isMountain_ForMountainCell_IsTrue() {
            Cell cell = Cell.createMountainCell();
            assertTrue(cell.isMountain());
        }

        @Test
        void isMountain_ForCellWithAdventurerAndTreasures_IsFalse() {
            Cell cell = Cell.createTreasuresCell(1);
            cell.placeAdventurer(new Adventurer(Adventurer.Orientation.NORTH, "name"));
            assertFalse(cell.isMountain());
        }
    }

    @Nested
    class hasTreasures {
        @Test
        void hasTreasures_ForPlainCell_IsFalse() {
            Cell cell = Cell.createPlainCell();
            assertFalse(cell.hasTreasures());
        }

        @Test
        void hasTreasures_ForCellWithAdventurer_IsFalse() {
            Cell cell = Cell.createPlainCell();
            cell.placeAdventurer(new Adventurer(Adventurer.Orientation.NORTH, "name"));
            assertFalse(cell.hasTreasures());
        }

        @Test
        void hasTreasures_ForCellWithTreasures_IsTrue() {
            Cell cell = Cell.createTreasuresCell(1);
            assertTrue(cell.hasTreasures());
        }

        @Test
        void hasTreasures_ForMountainCell_IsFalse() {
            Cell cell = Cell.createMountainCell();
            assertFalse(cell.hasTreasures());
        }

        @Test
        void hasTreasures_ForCellWithTreasuresAndAdventurer_IsTrue() {
            Cell cell = Cell.createTreasuresCell(5);
            cell.placeAdventurer(new Adventurer(Adventurer.Orientation.NORTH, "name"));
            assertTrue(cell.hasTreasures());
        }
    }

    @Nested
    class hasAdventurer {
        @Test
        void hasAdventurer_ForPlainCell_IsFalse() {
            Cell cell = Cell.createPlainCell();
            assertFalse(cell.hasAdventurer());
        }

        @Test
        void hasAdventurer_ForCellWithAdventurer_IsTrue() {
            Cell cell = Cell.createPlainCell();
            cell.placeAdventurer(new Adventurer(Adventurer.Orientation.NORTH, "name"));
            assertTrue(cell.hasAdventurer());
        }

        @Test
        void hasAdventurer_ForCellWithTreasures_IsFalse() {
            Cell cell = Cell.createTreasuresCell(1);
            assertFalse(cell.hasAdventurer());
        }

        @Test
        void hasAdventurer_ForMountainCell_IsFalse() {
            Cell cell = Cell.createMountainCell();
            assertFalse(cell.hasAdventurer());
        }

        @Test
        void hasAdventurer_ForCellWithAdventurerAndTreasures_IsTrue() {
            Cell cell = Cell.createTreasuresCell(1);
            cell.placeAdventurer(new Adventurer(Adventurer.Orientation.NORTH, "name"));
            assertTrue(cell.hasAdventurer());
        }
    }

    @Nested
    class isPlain {
        @Test
        void isPlain_ForPlainCell_IsTrue() {
            Cell cell = Cell.createPlainCell();
            assertTrue(cell.isPlain());
        }

        @Test
        void isPlain_ForCellWithAdventurer_IsFalse() {
            Cell cell = Cell.createPlainCell();
            cell.placeAdventurer(new Adventurer(Adventurer.Orientation.NORTH, "name"));

            assertTrue(cell.isPlain());
        }

        @Test
        void isPlain_ForCellWithTreasures_IsFalse() {
            Cell cell = Cell.createTreasuresCell(1);
            assertFalse(cell.isPlain());
        }

        @Test
        void isPlain_ForCellWithAdventurerAndTreasures_IsFalse() {
            Cell cell = Cell.createTreasuresCell(5);
            cell.placeAdventurer(new Adventurer(Adventurer.Orientation.NORTH, "name"));
            assertFalse(cell.isPlain());
        }

        @Test
        void isPlain_ForMountainCell_IsFalse() {
            Cell cell = Cell.createMountainCell();
            assertFalse(cell.isPlain());
        }
    }

    @Nested
    class removeOneTreasure {
        @Test
        void removeOneTreasure_onPainCell_throwsNoTreasureOnCell() {
            Cell plainCell = Cell.createPlainCell();

            assertThrows(NoTreasureOnCellException.class, plainCell::removeOneTreasure);
        }

        @Test
        void removeOneTreasure_onMountainCell_throwsNoTreasureOnCell() {
            Cell mountainCell = Cell.createMountainCell();

            assertThrows(NoTreasureOnCellException.class, mountainCell::removeOneTreasure);
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 3, 100})
        void removeOneTreasure_onTreasuresCell_removesOneTreasure(int treasures) {
            Cell treasuresCell = Cell.createTreasuresCell(treasures);

            treasuresCell.removeOneTreasure();

            assertEquals(treasures - 1, treasuresCell.getTreasures());
        }
    }
}