package sma.pacman.game.maze.cell;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sma.pacman.Direction;
import sma.pacman.game.character.Character;
import sma.pacman.util.ResourceUtils;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PointCell extends Cell{

    public PointCell(int x, int y) {
        super(x, y);

        setTileImageResource("/tile.point.png");
    }

}
