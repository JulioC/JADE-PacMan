package sma.pacman.game.cell;

import sma.pacman.game.Direction;
import sma.pacman.game.SecondaryDirection;
import sma.pacman.game.character.Character;

import java.awt.*;

public class WallCell extends Cell {

    private Boolean unreachable;

    private SecondaryDirection orientation;

    public WallCell(Point position) {
        super(position);

        loadImage("wall/generic");
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

        loadImage("wall/" + connections);
    }

    @Override
    public Boolean isWalkableFor(Character character) {
        return false;
    }
}
