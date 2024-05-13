package fr.baptistemasoud.technical;

import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.util.List;

public record MapConfiguration(
        int width,
        int height,
        List<Point> mountainsCoords,
        List<Pair<Point, Integer>> treasuresCoordsAndAmount
) {
}
