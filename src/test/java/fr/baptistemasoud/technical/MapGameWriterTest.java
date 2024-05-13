package fr.baptistemasoud.technical;

import fr.baptistemasoud.functional.Adventurer;
import fr.baptistemasoud.functional.Game;
import fr.baptistemasoud.functional.Map;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MapGameWriterTest {

    @Test
    void printGameDetails() {
        // arrange
        Map map = new Map.MapBuilder(10, 10)
                .placeMountain(1, 1)
                .placeMountain(2, 2)
                .placeTreasure(3, 0, 2)
                .placeTreasure(3, 1, 3)
                .createMap();

        Triple<Adventurer, Point, Game.Movement[]>[] adventurersCoordsMovements = new Triple[1];
        adventurersCoordsMovements[0] = Triple.of(
                new Adventurer(Adventurer.Orientation.SOUTH, "Lara"),
                new Point(2, 0),
                new Game.Movement[]{
                        Game.Movement.FORWARD
                });

        Game game = new Game(map, adventurersCoordsMovements);
        game.play();

        // act
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(out);
        MapGameWriter.printGameDetails(game, stream);

        // assert
        String expected = """
                C - 10 - 10
                M - 1 - 1
                M - 2 - 2
                T - 0 - 3 - 1
                T - 1 - 3 - 3
                A - Lara - 0 - 3 - S - 1
                """;

        assertEquals(expected, out.toString());
    }
}