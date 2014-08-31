package sma.pacman.game.maze.cell;

import sma.pacman.Direction;
import sma.pacman.SecondaryDirection;
import sma.pacman.game.character.Character;

public class WallCell extends Cell {

    private Boolean unreachable;

    private SecondaryDirection orientation;

    public WallCell(int x, int y) {
        super(x, y);

        setTileImageResource("/tile.wall.png");
    }

    public void setOrientation(SecondaryDirection orientation) {
        this.orientation = orientation;
    }

    public void setUnreachable(Boolean unreachable) {
        this.unreachable = unreachable;
    }

    public void updateTileImage() {
        String connections = "xxxx";
        if(unreachable) {
            if(orientation != null) {
                switch (orientation) {
                    case SECONDARY_DIRECTION_TOP_RIGHT:
                        connections = "xxoo";
                        break;
                    case SECONDARY_DIRECTION_BOTTOM_RIGHT:
                        connections = "oxxo";
                        break;
                    case SECONDARY_DIRECTION_BOTTOM_LEFT:
                        connections = "ooxx";
                        break;
                    case SECONDARY_DIRECTION_TOP_LEFT:
                        connections = "xoox";
                        break;
                }
            }
        }
        else {
            connections = "";
            for(Direction direction: Direction.values()) {
                Boolean reachable = reachableFrom.get(direction);
                if(reachable == null) {
                    connections += "o";
                }
                else if(reachable) {
                    connections += "o";
                }
                else {
                    connections += "x";
                }
            }
        }

        setTileImageResource("/tile.wall." + connections + ".png");
        if(tileImage == null) {
            setTileImageResource("/tile.wall.png");
        }
    }

    @Override
    public Boolean isWalkableBy(Character character) {
        return false;
    }
}
