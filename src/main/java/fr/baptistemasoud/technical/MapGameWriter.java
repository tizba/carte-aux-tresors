package fr.baptistemasoud.technical;

import fr.baptistemasoud.functional.Adventurer;
import fr.baptistemasoud.functional.Cell;
import fr.baptistemasoud.functional.Game;
import fr.baptistemasoud.functional.Map;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class MapGameWriter {
    private MapGameWriter() {}

    public static void printBeautifulMap(Map map, PrintStream stream) {
        int height = map.getHeight();
        int width = map.getWidth();

        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                Cell cell = map.getCell(x, y);
                if (cell.hasAdventurer()) stream.print(cell.getAdventurer().getName().charAt(0));
                else if (cell.isPlain()) stream.print(".");
                else if (cell.isMountain()) stream.print("M");
                else if (cell.hasTreasures()) stream.print(cell.getTreasures());
                stream.print(" ");
            }
            stream.println();
        }
    }

    public static void printGameDetails(Game game, PrintStream stream) {
        Map map = game.getMap();
        int width = map.getWidth();
        int height = map.getHeight();
        List<Point> mountainsCoords = new ArrayList<>();
        List<Pair<Point, Integer>> treasuresCoordsAndAmounts = new ArrayList<>();
        List<Pair<Adventurer, Point>> adventurersCoords = new ArrayList<>();

        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                Cell cell = map.getCell(x, y);
                if (cell.isMountain()) mountainsCoords.add(new Point(x, y));
                if (cell.hasTreasures()) treasuresCoordsAndAmounts.add(Pair.of(new Point(x, y), cell.getTreasures()));
                if (cell.hasAdventurer()) adventurersCoords.add(Pair.of(cell.getAdventurer(), new Point(x, y)));
            }
        }

        stream.printf("C - %d - %d%n", width, height);
        for (Point mountainCoords : mountainsCoords) {
            stream.printf("M - %d - %d%n", mountainCoords.y, mountainCoords.x);
        }
        for (Pair<Point, Integer> treasureCoordsAndAmount : treasuresCoordsAndAmounts) {
            Point treasureCoords = treasureCoordsAndAmount.getLeft();
            stream.printf("T - %d - %d - %d%n", treasureCoords.y, treasureCoords.x, treasureCoordsAndAmount.getRight());
        }
        for (Pair<Adventurer, Point> adventurerCoords : adventurersCoords) {
            Point coords = adventurerCoords.getRight();
            Adventurer adventurer = adventurerCoords.getLeft();
            stream.printf("A - %s - %d - %d - %s - %d%n",
                    adventurer.getName(),
                    coords.y,
                    coords.x,
                    adventurer.getOrientation().toString().charAt(0),
                    game.getCollectedTreasures(adventurer));
        }
    }
}
