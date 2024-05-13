package fr.baptistemasoud.functional;

import fr.baptistemasoud.functional.exception.DuplicateMountainsOrTreasuresException;
import fr.baptistemasoud.functional.exception.OutOfBoundsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.awt.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MapBuilderTest {
    @Test
    void constructor_HeightZero_ThrowsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Map.MapBuilder(0, 5));
    }

    @Test
    void constructor_HeightNegative_ThrowsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Map.MapBuilder(-5, 5));
    }

    @Test
    void constructor_WidthZero_ThrowsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Map.MapBuilder(5, 0));
    }

    @Test
    void constructor_WidthNegative_ThrowsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Map.MapBuilder(0, -5));
    }

    @Test
    void placeTreasure_ZeroTreasureAmounts_ThrowsIllegalArgument() {
        Map.MapBuilder builder = new Map.MapBuilder(5, 5);
        assertThrows(IllegalArgumentException.class, () -> builder.placeTreasure(3, 3, 0));
    }

    @Test
    void placeTreasure_NegativeTreasureAmounts_ThrowsIllegalArgument() {
        Map.MapBuilder builder = new Map.MapBuilder(5, 5);
        assertThrows(IllegalArgumentException.class, () -> builder.placeTreasure(3, 3, -5));
    }

    @ParameterizedTest
    @CsvSource({"-2, 2", "2, -2", "15, 2", "2, 15"})
    void placeMountain_MountainOutOfBounds_ThrowsOutOfBoundsPoint(int x, int y) {
        Map.MapBuilder builder = new Map.MapBuilder(5, 5);
        assertThrows(OutOfBoundsException.class, () -> builder.placeMountain(x, y));
    }

    @ParameterizedTest
    @CsvSource({"-2, 2", "2, -2", "15, 2", "2, 15"})
    void placeTreasure_TreasureOutOfBounds_ThrowsOutOfBoundsPoint(int x, int y) {
        Map.MapBuilder builder = new Map.MapBuilder(5, 5);
        assertThrows(OutOfBoundsException.class, () -> builder.placeTreasure(x, y, 1));
    }

    @Test
    void createMap_MountainsAndTreasuresCoordsNotDistinct_ThrowsDuplicateMountainsOrTreasures() {
        Map.MapBuilder builder = new Map.MapBuilder(5, 5);
        builder.placeMountain(2, 2);
        builder.placeTreasure(2, 2, 1);

        assertThrows(DuplicateMountainsOrTreasuresException.class, builder::createMap);
    }

    @Test
    void createMap_MountainsCoordsNotDistinct_ThrowsDuplicateMountainsOrTreasures() {
        Map.MapBuilder builder = new Map.MapBuilder(5, 5);
        builder.placeMountain(2, 2);
        builder.placeMountain(2, 2);

        assertThrows(DuplicateMountainsOrTreasuresException.class, builder::createMap);
    }

    @Test
    void createMap_TreasuresCoordsNotDistinct_ThrowsDuplicateMountainsOrTreasures() {
        Map.MapBuilder builder = new Map.MapBuilder(5, 5);
        builder.placeTreasure(2, 2, 4);
        builder.placeTreasure(2, 2, 1);

        assertThrows(DuplicateMountainsOrTreasuresException.class, builder::createMap);
    }

    @Test
    void createMap_noTreasureNorMountain_AllPlainsCellsAndCorrectDimensions() {
        int height = 5;
        int width = 10;
        Map map = new Map.MapBuilder(height, width).createMap();

        assertEquals(height, map.getHeight());
        assertEquals(width, map.getWidth());

        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                assertTrue(map.getCell(x, y).isPlain());
            }
        }
    }

    @Test
    void createMap_withTreasuresAndMountains_correctCells() {
        // arrange
        int height = 5;
        int width = 10;
        Point[] mountainsCoords = {new Point(3, 4), new Point(2, 1)};
        Point[] treasuresCoords = {new Point(1, 1), new Point(4, 1)};
        Map.MapBuilder builder = new Map.MapBuilder(height, width);
        for (Point mountainCoords : mountainsCoords) {
            builder.placeMountain(mountainCoords.x, mountainCoords.y);
        }
        for (Point treasureCoords : treasuresCoords) {
            builder.placeTreasure(treasureCoords.x, treasureCoords.y, 1);
        }

        // act
        Map map = builder.createMap();

        // assert
        assertEquals(height, map.getHeight());
        assertEquals(width, map.getWidth());
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                Cell cell = map.getCell(x, y);

                if (Arrays.stream(mountainsCoords).toList().contains(new Point(x, y))) {
                    assertTrue(cell.isMountain());
                    continue;
                }
                if (Arrays.stream(treasuresCoords).toList().contains(new Point(x, y))) {
                    assertTrue(cell.hasTreasures());
                    continue;
                }
                assertTrue(cell.isPlain());
            }
        }
    }
}
