package sma.pacman.game.maze.cell;

import sma.pacman.game.character.Character;
import sma.pacman.game.character.EnemyCharacter;

public class EnemySpawnCell extends Cell {

    public EnemySpawnCell(int x, int y) {
        super(x, y);
    }

    @Override
    public Boolean isSpawnFor(Character character) {
        if(character instanceof EnemyCharacter) {
            return true;
        }

        return super.isSpawnFor(character);
    }
}
