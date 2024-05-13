package fr.baptistemasoud.functional;

import fr.baptistemasoud.functional.exception.AdventurerOnAdventurerException;
import fr.baptistemasoud.functional.exception.AdventurerOnMountainException;
import fr.baptistemasoud.functional.exception.NoTreasureOnCellException;

public class Cell {
    private Adventurer adventurer;
    private final boolean isMountain;
    private int treasures;

    private Cell(boolean isMountain, int treasures) {
        this.isMountain = isMountain;
        this.treasures = treasures;
        this.adventurer = null;
    }

    /**
     * Create a new Mountain cell
     * @return a new Mountain cell
     */
    public static Cell createMountainCell() {
        return new Cell(true, 0);
    }

    /**
     * Create a new Plain cell
     * @return a new Plain cell
     */
    public static Cell createPlainCell() {
        return new Cell(false, 0);
    }

    /**
     * Create a new Cell containing the amount of treasures
     * @param treasures the amount of treasures on the cell. Must be strictly positive
     * @throws IllegalArgumentException if treasures is negative or equal to 0
     * @return a new Cell containing the amount of treasures
     */
    public static Cell createTreasuresCell(int treasures) {
        if (treasures <= 0) {
            throw new IllegalArgumentException("\"treasures\" parameter must be strictly positive");
        }
        return new Cell(false, treasures);
    }

    /**
     * Check if the cell is a plain cell, a cell is plain if it does not contain any treasures and is not a mountain
     * @return true if the cell is a plain cell, false otherwise
     */
    public boolean isPlain() {
        return !hasTreasures() && !isMountain();
    }

    /**
     * Check if the cell has an adventurer
     * @return true if the cell has an adventurer, false otherwise
     */
    public boolean hasAdventurer() {
        return (adventurer != null);
    }

    /**
     * Check if the cell has treasures
     * @return true if the cell has treasures, false otherwise
     */
    public boolean hasTreasures() {
        return treasures > 0;
    }

    /**
     * Remove one treasure
     * @throws NoTreasureOnCellException if cell does not contain any treasure
     */
    public void removeOneTreasure() {
        if (!hasTreasures()) throw new NoTreasureOnCellException("Cell does not contain any treasure");
        this.treasures--;
    }

    /**
     * Check if the cell is a mountain
     * @return true if the cell is a mountain, false otherwise
     */
    public boolean isMountain() {
        return isMountain;
    }

    /**
     * Get the Adventurer on the cell
     * @return the Adventurer on the cell, null if there is no Adventurer
     */
    public Adventurer getAdventurer() {
        return adventurer;
    }

    /**
     * Get the amount of treasures on the cell
     * @return the amount of treasures on the cell
     */
    public int getTreasures() {
        return treasures;
    }

    /**
     * Place an Adventurer on the cell
     * @param adventurer the Adventurer to be placed
     * @throws AdventurerOnAdventurerException if the cell is a Mountain
     * @throws AdventurerOnAdventurerException if the cell already contains an Adventurer
     */
    public void placeAdventurer(Adventurer adventurer) {
        if (isMountain()) throw new AdventurerOnMountainException();
        if (hasAdventurer()) throw new AdventurerOnAdventurerException();

        this.adventurer = adventurer;
    }

    /**
     * Remove the Adventurer from the cell
     */
    public void removeAdventurer() {
        this.adventurer = null;
    }
}
