package sma.pacman.game.maze.cell;

import sma.pacman.Direction;
import sma.pacman.game.character.Character;
import sma.pacman.game.character.EnemyCharacter;

public class GateCell extends Cell {

    public GateCell(int x, int y) {
        super(x, y);

        setTileImageResource("/tile.gate.png");
    }

    @Override
    public Boolean isWalkableBy(Character character) {
        if(character instanceof EnemyCharacter) {
            if(character.isNewBorn()) {
                return true;
            }
        }

        return false;
    }
}
