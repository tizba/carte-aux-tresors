package fr.baptistemasoud.technical;

import fr.baptistemasoud.functional.Adventurer;
import fr.baptistemasoud.functional.Game;
import fr.baptistemasoud.technical.exception.ConfigurationFormatException;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationReader {
    private final List<AdventurerConfiguration> adventurersConfigurations = new ArrayList<>();
    private final List<Point> mountainsCoords = new ArrayList<>();
    private final List<Pair<Point, Integer>> treasuresCoordsAndAmount = new ArrayList<>();
    private MapConfiguration mapConfiguration;
    private int height;
    private int width;
    private boolean foundDimensions = false;

    public ConfigurationReader(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.replace(" ", "");

            char c = line.charAt(0);
            if (c == '#') continue;
            switch (c) {
                case 'C' -> handleMapDimensionsLine(line);
                case 'M' -> handleMountainLine(line);
                case 'T' -> handleTreasureLine(line);
                case 'A' -> handleAdventurerLine(line);
                default -> throw new ConfigurationFormatException(
                        "A line cannot start with %s, it must start with one of the following characters: #, C, M, T or A"
                                .formatted(c)
                );
            }
        }

        initConfiguration();
    }

    public MapConfiguration getMapConfiguration() {
        return mapConfiguration;
    }

    public AdventurerConfiguration[] getAdventurersConfigurations() {
        return adventurersConfigurations.toArray(AdventurerConfiguration[]::new);
    }

    private void initConfiguration() {
        if (!foundDimensions) throw new ConfigurationFormatException("File must contain a map dimensions line");
        mapConfiguration = new MapConfiguration(width, height, mountainsCoords, treasuresCoordsAndAmount);
    }

    private void handleTreasureLine(String line) {
        String[] split = line.split("-");
        if (split.length != 4) throw new ConfigurationFormatException(
                "A treasure line must contain 4 parts separated with '-'"
        );

        Point coords = new Point(Integer.parseInt(split[2]), Integer.parseInt(split[1]));
        int amount = Integer.parseInt(split[3]);
        treasuresCoordsAndAmount.add(Pair.of(coords, amount));
    }

    private void handleMountainLine(String line) {
        String[] split = line.split("-");
        if (split.length != 3) throw new ConfigurationFormatException(
                "A treasure line must contain 3 parts separated with '-'"
        );

        mountainsCoords.add(new Point(Integer.parseInt(split[2]), Integer.parseInt(split[1])));
    }

    private void handleMapDimensionsLine(String line) {
        if (foundDimensions) throw new ConfigurationFormatException("File must define map dimensions once");

        foundDimensions = true;

        String[] split = line.split("-");
        if (split.length != 3) throw new ConfigurationFormatException(
                "A map dimensions line must contain 3 parts separated with '-'"
        );

        width = Integer.parseInt(split[1]);
        height = Integer.parseInt(split[2]);
    }

    private void handleAdventurerLine(String line) {
        String[] split = line.split("-");
        if (split.length != 6) throw new ConfigurationFormatException(
                "An adventurer line must contain 6 parts separated with '-'"
        );

        adventurersConfigurations.add(new AdventurerConfiguration(
                split[1],
                getOrientationFromChar(split[4].charAt(0)),
                new Point(Integer.parseInt(split[3]), Integer.parseInt(split[2])),
                getMovementsFromCharArray(split[5].toCharArray())
        ));
    }

    private Adventurer.Orientation getOrientationFromChar(char orientationChar) {
        return switch (orientationChar) {
            case 'S' -> Adventurer.Orientation.SOUTH;
            case 'N' -> Adventurer.Orientation.NORTH;
            case 'W' -> Adventurer.Orientation.WEST;
            case 'E' -> Adventurer.Orientation.EAST;
            default -> throw new ConfigurationFormatException(
                    "Adventurer orientation char cannot be %s, it must be either N, S, E or W".formatted(orientationChar)
            );

        };
    }

    private Game.Movement[] getMovementsFromCharArray(char[] movementsCharArray) {
        Game.Movement[] movements = new Game.Movement[movementsCharArray.length];
        for (int i = 0; i < movementsCharArray.length; i++) {
            char c = movementsCharArray[i];
            movements[i] = switch (c) {
                case 'A' -> Game.Movement.FORWARD;
                case 'D' -> Game.Movement.ROTATE_RIGHT;
                case 'G' -> Game.Movement.ROTATE_LEFT;
                default -> throw new ConfigurationFormatException(
                        "Adventurer movement char cannot be %s, it must be either A, D, G".formatted(c)
                );
            };
        }
        return movements;
    }
}
