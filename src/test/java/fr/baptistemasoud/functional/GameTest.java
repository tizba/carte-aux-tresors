package fr.baptistemasoud.functional;

import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class GameTest {

    private Map.MapBuilder builder;

    @BeforeEach
    void beforeEach() {
        builder = new Map.MapBuilder(4, 3);
    }

    @Test
    void play() {
        builder.placeMountain(0, 1);
        builder.placeMountain(1, 2);
        builder.placeTreasure(3, 0, 2);
        builder.placeTreasure(3, 1, 3);
        Map map = builder.createMap();

        List<Triple<Adventurer, Point, Game.Movement[]>> adventurers = new ArrayList<>();
        Adventurer adventurer = new Adventurer(Adventurer.Orientation.NORTH, "Lara");
        adventurers.add(Triple.of(
                adventurer,
                new Point(1, 1),
                new Game.Movement[]{Game.Movement.FORWARD, Game.Movement.ROTATE_LEFT, Game.Movement.FORWARD, Game.Movement.ROTATE_LEFT, Game.Movement.FORWARD, Game.Movement.FORWARD, Game.Movement.ROTATE_RIGHT, Game.Movement.FORWARD, Game.Movement.FORWARD, Game.Movement.ROTATE_RIGHT, Game.Movement.FORWARD, Game.Movement.ROTATE_LEFT, Game.Movement.ROTATE_LEFT, Game.Movement.FORWARD, null}));

        adventurers.add(Triple.of(
                new Adventurer(Adventurer.Orientation.NORTH, "Blocker"),
                new Point(1, 0),
                new Game.Movement[]{}
        ));

        Game game = new Game(map, adventurers.toArray(Triple[]::new));

        game.play();

        map = game.getMap();
        Assertions.assertEquals(3, game.getCollectedTreasures(adventurer));
        Assertions.assertEquals(adventurer, map.getCell(3, 0).getAdventurer());
        Assertions.assertFalse(map.getCell(3, 0).hasTreasures());
        Assertions.assertEquals(2, map.getCell(3, 1).getTreasures());
    }

    @Test
    void getCollectedTreasures() {
        builder.placeTreasure(1, 0, 1);
        builder.placeTreasure(2, 0, 1);
        Map map = builder.createMap();

        List<Triple<Adventurer, Point, Game.Movement[]>> adventurers = new ArrayList<>();
        Adventurer adventurer = new Adventurer(Adventurer.Orientation.SOUTH, "Lara");
        adventurers.add(Triple.of(
                adventurer,
                new Point(0, 0),
                new Game.Movement[]{Game.Movement.FORWARD, Game.Movement.FORWARD}));

        Game game = new Game(map, adventurers.toArray(Triple[]::new));

        game.play();

        Assertions.assertEquals(2, game.getCollectedTreasures(adventurer));
    }
}