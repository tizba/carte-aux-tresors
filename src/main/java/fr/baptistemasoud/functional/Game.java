package fr.baptistemasoud.functional;

import org.apache.commons.lang3.tuple.Triple;

import java.awt.*;
import java.util.List;
import java.util.Queue;
import java.util.*;

public class Game {
    private final Map map;
    private final java.util.Map<Adventurer, Queue<Movement>> adventurersMovements = new HashMap<>();
    private final java.util.Map<Adventurer, Point> adventurersCoords = new HashMap<>();
    private final java.util.Map<Adventurer, Integer> adventurersCollectedTreasures = new HashMap<>();
    private final List<Adventurer> adventurers = new ArrayList<>();

    /**
     * @param map the map for which the game will be played on
     * @param adventurersCoordsMovements an array of Triple containing the adventurer, its initial coordinates and its movements
     */
    public Game(Map map, Triple<Adventurer, Point, Movement[]>[] adventurersCoordsMovements) {
        this.map = map;
        for (Triple<Adventurer, Point, Movement[]> adventurerCoordsMovements : adventurersCoordsMovements) {
            Adventurer adventurer = adventurerCoordsMovements.getLeft();
            adventurers.add(adventurer);
            adventurersCollectedTreasures.put(adventurer, 0);
        }
        initAdventurersPlacement(adventurersCoordsMovements);
        initAdventurersMovements(adventurersCoordsMovements);
    }

    private void initAdventurersPlacement(Triple<Adventurer, Point, Movement[]>[] adventurersCoordsMovements) {
        for (Triple<Adventurer, Point, Movement[]> adventurerCoordsMovements : adventurersCoordsMovements) {
            Adventurer adventurer = adventurerCoordsMovements.getLeft();
            Point point = adventurerCoordsMovements.getMiddle();
            this.adventurersCoords.put(adventurer, point);
        }
    }

    private void initAdventurersMovements(Triple<Adventurer, Point, Movement[]>[] adventurersCoordsMovements) {
        for (Triple<Adventurer, Point, Movement[]> adventurerCoordsMovements : adventurersCoordsMovements) {
            // add all movements in a queue
            Queue<Movement> movementsQueue = new LinkedList<>(Arrays.asList(adventurerCoordsMovements.getRight()));
            Adventurer adventurer = adventurerCoordsMovements.getLeft();
            this.adventurersMovements.put(adventurer, movementsQueue);
        }
    }

    /**
     * Play the game. Place all adventurers on the map and play their movements.
     */
    public void play() {
        // place all adventurers on the map
        for (java.util.Map.Entry<Adventurer, Point> adventurerCoords : adventurersCoords.entrySet()) {
            Point coords = adventurerCoords.getValue();
            Cell cell = this.map.getCell(coords.x, coords.y);
            cell.placeAdventurer(adventurerCoords.getKey());
        }

        // play their movements
        while (!isFinished()) {
            for (Adventurer adventurer : adventurers) {
                Movement movement = getNextMovement(adventurer);
                if (movement == null) continue;
                switch (movement) {
                    case FORWARD -> moveAdventurerForward(adventurer);
                    case ROTATE_LEFT -> adventurer.rotateLeft();
                    case ROTATE_RIGHT -> adventurer.rotateRight();
                }
            }
        }
    }

    public Map getMap() {
        return map;
    }

    /**
     * @param adventurer the adventurer to get the amount of collected treasures
     * @return the amount of collected treasures
     */
    public int getCollectedTreasures(Adventurer adventurer) {
        return adventurersCollectedTreasures.get(adventurer);
    }

    private Movement getNextMovement(Adventurer adventurer) {
        return adventurersMovements.get(adventurer).poll();
    }

    private void moveAdventurerForward(Adventurer adventurer) {
        Point coords = adventurersCoords.get(adventurer);
        switch (adventurer.getOrientation()) {
            case NORTH -> moveAdventurerToCoords(adventurer, coords.x, coords.y, coords.x - 1, coords.y);
            case EAST -> moveAdventurerToCoords(adventurer, coords.x, coords.y, coords.x, coords.y + 1);
            case SOUTH -> moveAdventurerToCoords(adventurer, coords.x, coords.y, coords.x + 1, coords.y);
            case WEST -> moveAdventurerToCoords(adventurer, coords.x, coords.y, coords.x, coords.y - 1);
        }
    }

    private void moveAdventurerToCoords(Adventurer adventurer, int fromX, int fromY, int toX, int toY) {
        if (Map.areCoordsOutOfBounds(map.getHeight(), map.getHeight(), toX, toY)) {
            return;
        }
        Cell sourceCell = this.map.getCell(fromX, fromY);
        Cell destinationCell = this.map.getCell(toX, toY);
        if (!destinationCell.isMountain() && !destinationCell.hasAdventurer()) {
            sourceCell.removeAdventurer();
            destinationCell.placeAdventurer(adventurer);
            adventurersCoords.put(adventurer, new Point(toX, toY));
            if (destinationCell.hasTreasures()) {
                destinationCell.removeOneTreasure();
                adventurersCollectedTreasures.put(adventurer, adventurersCollectedTreasures.get(adventurer) + 1);
            }
        }
    }

    private boolean isFinished() {
        for (Queue<Movement> movements : adventurersMovements.values()) {
            if (!movements.isEmpty()) return false;
        }
        return true;
    }

    public enum Movement {
        FORWARD,
        ROTATE_LEFT,
        ROTATE_RIGHT
    }
}
