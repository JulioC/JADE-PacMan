package sma.pacman.game.cell;

import sma.pacman.game.character.Character;
import sma.pacman.game.character.HeroCharacter;

import java.awt.*;

public class HeroSpawnCell extends Cell {

    public HeroSpawnCell(Point position) {
        super(position);
    }

    @Override
    public Boolean isSpawnFor(Character character) {
        if(character instanceof HeroCharacter) {
            return true;
        }

        return super.isSpawnFor(character);
    }
}
