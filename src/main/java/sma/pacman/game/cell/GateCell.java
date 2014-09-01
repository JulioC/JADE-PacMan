package sma.pacman.game.cell;

import sma.pacman.game.character.Character;
import sma.pacman.game.character.EnemyCharacter;

import java.awt.*;

public class GateCell extends Cell {

    public GateCell(Point position) {
        super(position);

        loadImage("gate");
    }

    @Override
    public Boolean isWalkableFor(Character character) {
        if(character instanceof EnemyCharacter) {
            if(character.hasSpawnProtection()) {
                return true;
            }
        }

        return false;
    }
}
