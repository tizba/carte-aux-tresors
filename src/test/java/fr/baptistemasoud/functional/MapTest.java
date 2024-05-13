package fr.baptistemasoud.functional;

import fr.baptistemasoud.functional.exception.OutOfBoundsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class MapTest {
    @Test
    void areCoordsOutOfBounds_heightNotPositive_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Map.areCoordsOutOfBounds(0, 5, 1, 1));
    }

    @Test
    void areCoordsOutOfBounds_widthNotPositive_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Map.areCoordsOutOfBounds(5, 0, 1, 1));
    }

    @ParameterizedTest
    @CsvSource({"0, 0", "0, 1", "1, 0", "1, 1"})
    void areCoordsOutOfBounds_inBounds_returnsFalse(int x, int y) {
        assertFalse(Map.areCoordsOutOfBounds(2, 2, x, y));
    }

    @ParameterizedTest
    @CsvSource({"-1, 0", "0, -1", "-1, -1", "2, 0", "0, 2", "2, 2"})
    void areCoordsOutOfBounds_outOfBounds_returnsTrue(int x, int y) {
        assertTrue(Map.areCoordsOutOfBounds(2, 2, x, y));
    }

    @ParameterizedTest
    @CsvSource({"-2, 2", "2, -2", "15, 2", "2, 15"})
    void getCell_outOfBounds_throwsOutOfBounds(int x, int y) {
        Map map = new Map.MapBuilder(3, 3).createMap();

        assertThrows(OutOfBoundsException.class, () -> map.getCell(x, y));
    }
}