package sma.pacman.game.maze.cell;

import sma.pacman.game.character.Character;
import sma.pacman.game.character.HeroCharacter;

public class HeroSpawnCell extends Cell {

    public HeroSpawnCell(int x, int y) {
        super(x, y);
    }

    @Override
    public Boolean isSpawnFor(Character character) {
        if(character instanceof HeroCharacter) {
            return true;
        }

        return super.isSpawnFor(character);
    }
}
