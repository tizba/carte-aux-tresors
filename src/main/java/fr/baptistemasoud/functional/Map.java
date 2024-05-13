package fr.baptistemasoud.functional;

import fr.baptistemasoud.functional.exception.DuplicateMountainsOrTreasuresException;
import fr.baptistemasoud.functional.exception.OutOfBoundsException;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Map {
    private final Cell[][] cells;
    private final int height;
    private final int width;

    /**
     * @throws DuplicateMountainsOrTreasuresException if 2 treasure/mountain have the same coordinates
     */
    private Map(int height, int width, Point[] mountainsCoords, Pair<Point, Integer>[] treasuresCoordsAndAmounts) {
        this.cells = new Cell[height][width];
        this.height = height;
        this.width = width;

        // validate that all coordinates are distinct
        Stream<Point> treasuresPoints = Arrays.stream(treasuresCoordsAndAmounts).map((Pair::getLeft));
        Stream<Point> mountainsPoints = Arrays.stream(mountainsCoords);
        Set<Point> pointsSet = Stream.concat(mountainsPoints, treasuresPoints).collect(Collectors.toSet());
        if (pointsSet.size() != mountainsCoords.length + treasuresCoordsAndAmounts.length) {
            throw new DuplicateMountainsOrTreasuresException();
        }

        // init cells (plain, treasures or mountain)
        initCells(mountainsCoords, treasuresCoordsAndAmounts);
    }

    /**
     * @param height the height of the map, must be strictly positive
     * @param width  the width of the map, must be strictly positive
     * @param x      x coordinate
     * @param y      y coordinate
     * @return true if the coordinates are out of bounds, false otherwise
     * @throws IllegalArgumentException if height or width are not strictly positive
     */
    public static boolean areCoordsOutOfBounds(int height, int width, int x, int y) {
        if (height <= 0) throw new IllegalArgumentException("height=%d is not strictly positive".formatted(height));
        if (width <= 0) throw new IllegalArgumentException("width=%d is not strictly positive".formatted(width));
        return x < 0 || x >= height || y < 0 || y >= width;
    }

    private void initCells(Point[] mountainsCoords, Pair<Point, Integer>[] treasuresCoordsAndAmounts) {
        // create all mountains
        for (Point mountainCoords : mountainsCoords) {
            cells[mountainCoords.x][mountainCoords.y] = Cell.createMountainCell();
        }

        // create all treasures
        for (Pair<Point, Integer> treasureCoordsAndAmount : treasuresCoordsAndAmounts) {
            Point treasureCoords = treasureCoordsAndAmount.getLeft();
            int treasureAmount = treasureCoordsAndAmount.getRight();
            cells[treasureCoords.x][treasureCoords.y] = Cell.createTreasuresCell(treasureAmount);
        }

        // fill remaining empty cells with plains
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (cells[i][j] == null) {
                    cells[i][j] = Cell.createPlainCell();
                }
            }
        }
    }

    /**
     * @param x x coordinate
     * @param y y coordinate
     * @return the Cell at the coordinate
     * @throws OutOfBoundsException if coordinates are out of map bounds
     */
    public Cell getCell(int x, int y) {
        if (areCoordsOutOfBounds(height, width, x, y)) {
            throw new OutOfBoundsException(height, width, x, y);
        }
        return cells[x][y];
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public static class MapBuilder {
        private final int height;
        private final int width;
        private final List<Point> mountainsCoords = new ArrayList<>();
        private final List<Pair<Point, Integer>> treasuresCoordsAndAmounts = new ArrayList<>();

        /**
         * @param height the height of the map, must be strictly positive
         * @param width  the width of the map, must be strictly positive
         * @throws IllegalArgumentException if height or width are not strictly positive
         */
        public MapBuilder(int height, int width) {
            if (height <= 0) throw new IllegalArgumentException("height=%d is not strictly positive".formatted(height));
            if (width <= 0) throw new IllegalArgumentException("width=%d is not strictly positive".formatted(width));
            this.height = height;
            this.width = width;
        }

        /**
         * @param x coordinate
         * @param y coordinate
         * @return this MapBuilder
         * @throws OutOfBoundsException if coordinates are out of map bounds
         */
        public MapBuilder placeMountain(int x, int y) {
            if (areCoordsOutOfBounds(height, width, x, y)) {
                throw new OutOfBoundsException(height, width, x, y);
            }
            mountainsCoords.add(new Point(x, y));
            return this;
        }

        /**
         * @param x      coordinate
         * @param y      coordinate
         * @param amount of treasures to be placed, must be strictly positive
         * @return this MapBuilder/
         * @throws OutOfBoundsException     if coordinates are out of map bounds
         * @throws IllegalArgumentException if amount is not strictly positive
         */
        public MapBuilder placeTreasure(int x, int y, int amount) {
            if (amount <= 0) throw new IllegalArgumentException("amount=%d is not strictly positive".formatted(amount));
            if (areCoordsOutOfBounds(height, width, x, y)) throw new OutOfBoundsException(height, width, x, y);

            treasuresCoordsAndAmounts.add(Pair.of(new Point(x, y), amount));
            return this;
        }

        /**
         * @return the created Map
         * @throws IllegalArgumentException               if height, width are not strictly positive
         * @throws OutOfBoundsException                   if a mountain or a treasure is out of bounds
         * @throws DuplicateMountainsOrTreasuresException if 2 mountain/treasure have the same coordinates
         */
        public Map createMap() {
            return new Map(
                    height,
                    width,
                    mountainsCoords.toArray(Point[]::new),
                    treasuresCoordsAndAmounts.toArray(Pair[]::new));
        }
    }
}