package fr.baptistemasoud.technical;

import fr.baptistemasoud.functional.Adventurer;
import fr.baptistemasoud.functional.Game;

import java.awt.*;

public record AdventurerConfiguration(
        String name,
        Adventurer.Orientation orientation,
        Point coords,
        Game.Movement[] movements
) {
}
