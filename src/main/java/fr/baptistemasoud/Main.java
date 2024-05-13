package fr.baptistemasoud;

import fr.baptistemasoud.functional.Adventurer;
import fr.baptistemasoud.functional.Game;
import fr.baptistemasoud.functional.Map;
import fr.baptistemasoud.technical.AdventurerConfiguration;
import fr.baptistemasoud.technical.ConfigurationReader;
import fr.baptistemasoud.technical.MapConfiguration;
import fr.baptistemasoud.technical.MapGameWriter;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.awt.*;
import java.io.*;

public class Main {

    public static void main(String[] args) {
        // Parse command line
        CommandLine commandLine = getCommandLine(args);
        if (commandLine == null) return;

        // Read configuration
        ConfigurationReader configurationReader = getConfigurationReader(commandLine.getOptionValue("input"));
        if (configurationReader == null) return;

        // Create map and adventurers from configuration
        Map map = createMapFromConfiguration(configurationReader.getMapConfiguration());
        Triple<Adventurer, Point, Game.Movement[]>[] adventurersFromConfiguration = createAdventurersFromConfiguration(configurationReader.getAdventurersConfigurations());

        // Play the game
        Game game = new Game(map, adventurersFromConfiguration);
        game.play();

        // Print the game details
        PrintStream printStream = getPrintStream(commandLine.getOptionValue("output"));
        if (printStream == null) return;
        MapGameWriter.printGameDetails(game, printStream);

        // Print beautiful map
        MapGameWriter.printBeautifulMap(map, System.out);
    }

    private static PrintStream getPrintStream(String filePath) {
        PrintStream printStream;
        try {
            printStream = new PrintStream(filePath);
        } catch (FileNotFoundException e) {
            System.err.println("Error while opening file: " + e.getMessage());
            return null;
        }
        return printStream;
    }

    private static ConfigurationReader getConfigurationReader(String filePath) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
            return null;
        }
        ConfigurationReader configurationReader;
        try {
            configurationReader = new ConfigurationReader(reader);
        } catch (IOException e) {
            System.err.println("Error while reading file: " + e.getMessage());
            return null;
        }
        return configurationReader;
    }

    private static CommandLine getCommandLine(String[] args) {
        Option inputFile = new Option("i", "input", true, "input file path");
        inputFile.setRequired(true);
        Option outputFile = new Option("o", "output", true, "output file path");
        outputFile.setRequired(true);

        Options options = new Options()
                .addOption(inputFile)
                .addOption(outputFile);
        CommandLineParser parser = new DefaultParser();

        try {
            return parser.parse(options, args);
        } catch (ParseException e) {
            new HelpFormatter().printHelp("treasure-hunt", options);
            return null;
        }
    }

    private static Map createMapFromConfiguration(MapConfiguration config) {
        Map.MapBuilder builder = new Map.MapBuilder(config.height(), config.width());
        for (Point coords : config.mountainsCoords()) {
            builder.placeMountain(coords.x, coords.y);
        }
        for (Pair<Point, Integer> coordsAndAmount : config.treasuresCoordsAndAmount()) {
            Point coords = coordsAndAmount.getLeft();
            Integer amount = coordsAndAmount.getRight();
            builder.placeTreasure(coords.x, coords.y, amount);
        }
        return builder.createMap();
    }

    private static Triple<Adventurer, Point, Game.Movement[]>[] createAdventurersFromConfiguration(AdventurerConfiguration[] adventurerConfigurations) {
        Triple<Adventurer, Point, Game.Movement[]>[] adventurers = new Triple[adventurerConfigurations.length];
        for (int i = 0; i < adventurerConfigurations.length; i++) {
            AdventurerConfiguration adventurerConfiguration = adventurerConfigurations[i];
            adventurers[i] = Triple.of(
                    new Adventurer(adventurerConfiguration.orientation(), adventurerConfiguration.name()),
                    adventurerConfiguration.coords(),
                    adventurerConfiguration.movements()
            );
        }
        return adventurers;
    }


}