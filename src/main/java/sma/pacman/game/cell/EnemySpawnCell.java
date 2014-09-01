package sma.pacman.game.cell;

import sma.pacman.game.character.Character;
import sma.pacman.game.character.EnemyCharacter;

import java.awt.*;

public class EnemySpawnCell extends Cell {

    public EnemySpawnCell(Point position) {
        super(position);
    }

    @Override
    public Boolean isSpawnFor(Character character) {
        if(character instanceof EnemyCharacter) {
            return true;
        }

        return super.isSpawnFor(character);
    }
}
