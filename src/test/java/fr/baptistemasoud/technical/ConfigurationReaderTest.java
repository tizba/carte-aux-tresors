package fr.baptistemasoud.technical;

import fr.baptistemasoud.functional.Game;
import fr.baptistemasoud.technical.exception.ConfigurationFormatException;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static fr.baptistemasoud.functional.Adventurer.Orientation;
import static org.junit.jupiter.api.Assertions.*;

class ConfigurationReaderTest {

    private static ConfigurationReader getConfigurationFromString() throws IOException {
        StringReader reader = new StringReader("""
                # This is a comment
                C - 3 - 4
                M - 1 - 1
                M - 2 - 2
                T - 0 - 3 - 2
                T - 1 - 3 - 3
                A - Lara - 1 - 1 - S - AADADAGGA
                A - Indiana - 2 - 2 - N - AADADAGGA
                A - Oriane - 0 - 0 - W - AADADAGGA
                A - Baptiste - 0 - 1 - E - AADADAGGA
                """);

        return new ConfigurationReader(new BufferedReader(reader));
    }

    @Test
    void getMapConfiguration() throws IOException {
        MapConfiguration mapConfiguration = getConfigurationFromString().getMapConfiguration();

        assertEquals(3, mapConfiguration.width());
        assertEquals(4, mapConfiguration.height());
        assertTrue(mapConfiguration.mountainsCoords().contains(new Point(1, 1)));
        assertTrue(mapConfiguration.mountainsCoords().contains(new Point(2, 2)));
        assertTrue(mapConfiguration.treasuresCoordsAndAmount().contains(Pair.of(new Point(3, 0), 2)));
        assertTrue(mapConfiguration.treasuresCoordsAndAmount().contains(Pair.of(new Point(3, 1), 3)));
    }

    @Test
    void getAdventurersConfigurations() throws IOException {
        AdventurerConfiguration[] adventurersConfigurations = getConfigurationFromString().getAdventurersConfigurations();

        assertEquals(4, adventurersConfigurations.length);

        assertEquals("Lara", adventurersConfigurations[0].name());
        assertEquals(new Point(1, 1), adventurersConfigurations[0].coords());
        assertEquals(Orientation.SOUTH, adventurersConfigurations[0].orientation());
        Game.Movement[] expectedMovements = new Game.Movement[]{
                Game.Movement.FORWARD,
                Game.Movement.FORWARD,
                Game.Movement.ROTATE_RIGHT,
                Game.Movement.FORWARD,
                Game.Movement.ROTATE_RIGHT,
                Game.Movement.FORWARD,
                Game.Movement.ROTATE_LEFT,
                Game.Movement.ROTATE_LEFT,
                Game.Movement.FORWARD
        };
        assertArrayEquals(expectedMovements, adventurersConfigurations[0].movements());

        assertEquals("Indiana", adventurersConfigurations[1].name());
        assertEquals(new Point(2, 2), adventurersConfigurations[1].coords());
        assertEquals(Orientation.NORTH, adventurersConfigurations[1].orientation());
        assertArrayEquals(expectedMovements, adventurersConfigurations[1].movements());

        assertEquals("Oriane", adventurersConfigurations[2].name());
        assertEquals(new Point(0, 0), adventurersConfigurations[2].coords());
        assertEquals(Orientation.WEST, adventurersConfigurations[2].orientation());
        assertArrayEquals(expectedMovements, adventurersConfigurations[2].movements());

        assertEquals("Baptiste", adventurersConfigurations[3].name());
        assertEquals(new Point(1, 0), adventurersConfigurations[3].coords());
        assertEquals(Orientation.EAST, adventurersConfigurations[3].orientation());
        assertArrayEquals(expectedMovements, adventurersConfigurations[3].movements());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            // line starting with invalid char
            "X - 3 - 4",

            // missing map dimensions
            "M - 1 - 1",

            // treasure line with invalid format
            """
                    C - 3 - 4
                    T - 0 - 3
                    """,

            // mountain line with invalid format
            """
                    C - 3 - 4
                    M - 1
                    """,

            // adventurer line with invalid format
            """
                    C - 3 - 4
                    A - Lara - 1 - 1
                    """,

            // map dimensions defined multiple times
            """
                    C - 3 - 4
                    C - 3 - 4
                    """,

            // map dimensions line with invalid format
            """
                    C - 3
                    """,

            // adventurer line with invalid orientation
            """
                    C - 3 - 4
                    A - Lara - 1 - 1 - X - AADADAGGA
                    """,

            // adventurer line with invalid movement
            """
                    C - 3 - 4
                    A - Lara - 1 - 1 - S - AX
                    """

    })
    void constructor_invalidConfigurationFormat_throwsConfigurationFormat(String configurationAsString) {
        StringReader reader = new StringReader(configurationAsString);
        BufferedReader bufferedReader = new BufferedReader(reader);

        assertThrows(ConfigurationFormatException.class, () -> new ConfigurationReader(bufferedReader));
    }
}