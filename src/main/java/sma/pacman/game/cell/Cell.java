package sma.pacman.game.cell;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sma.pacman.Direction;
import sma.pacman.game.Board;
import sma.pacman.game.character.Character;
import sma.pacman.util.ResourceUtils;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Cell {

    private static final Logger logger = LogManager.getLogger(Cell.class.getName());

    private final Point position;

    protected Image image;

    protected Map<Direction, Boolean> reachableFrom = new HashMap<Direction, Boolean>();

    public Cell(Point position) {
        this.position = position;
    }

    protected void loadImage(String name) {
        logger.trace("Loading tile image resource {}", name);

        this.image = null;

        try {
            this.image = ResourceUtils.getImage("/images/tile/" + name + ".png");
        } catch (IllegalArgumentException e) {
            logger.error("Failed to set tile image from resource {}", name, e);
        } catch (IOException e) {
            logger.error("Failed to set tile image from resource {}", name, e);
        }
    }

    public void draw(Graphics g) {
        int tile_width = Board.TILE_WIDTH;
        int tile_height = Board.TILE_HEIGHT;

        int draw_x = position.x * tile_width;
        int draw_y = position.y * tile_height;

        g.drawImage(image, draw_x, draw_y, tile_width, tile_height, null);
    }

    public Image getSprite() {
        return image;
    }

    public Boolean isSpawnFor(Character character) {
        return false;
    }

    public Boolean isWalkableFor(Character character) {
        return true;
    }

    public void setReachableFrom(Direction direction, Boolean value) {
        reachableFrom.put(direction, value);
    }

    public Boolean getReachableFrom(Direction direction) {
        return reachableFrom.get(direction);
    }

    public Point getPosition() {
        return position;
    }
}
