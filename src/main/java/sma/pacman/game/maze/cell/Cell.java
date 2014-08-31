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

public class Cell {

    private static final Logger logger = LogManager.getLogger(Cell.class.getName());

    private final int x;
    private final int y;

    protected Image tileImage;

    protected Map<Direction, Boolean> reachableFrom = new HashMap<Direction, Boolean>();

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    protected void setTileImageResource(String name) {
        logger.trace("Loading tile image resouce {}", name);

        this.tileImage = null;

        try {
            this.tileImage = ResourceUtils.getImage(name);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to set tile image from resource {}", name, e);
        } catch (IOException e) {
            logger.error("Failed to set tile image from resource {}", name, e);
        }
    }

    public Image getTileImage() {
        return tileImage;
    }

    public Boolean isSpawnFor(Character character) {
        return false;
    }

    public Boolean isWalkableBy(Character character) {
        return true;
    }

    public void setReachableFrom(Direction direction, Boolean value) {
        reachableFrom.put(direction, value);
    }

    public Boolean getReachableFrom(Direction direction) {
        return reachableFrom.get(direction);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
