package sma.pacman.game.maze;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sma.pacman.Direction;
import sma.pacman.SecondaryDirection;
import sma.pacman.game.maze.cell.*;
import sma.pacman.util.ColorUtils;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Maze {

    private static final Logger logger = LogManager.getLogger(Maze.class.getName());

    public static final int IMAGE_TILE_WIDTH = 24;
    public static final int IMAGE_TILE_HEIGHT = 24;

    public static final Color TILE_EMPTY = new Color(255, 255, 255);
    public static final Color TILE_WALL = new Color(0, 0, 0);
    public static final Color TILE_GATE = new Color(0, 0, 255);
    public static final Color TILE_HERO_SPAWN = new Color(0, 255, 0);
    public static final Color TILE_ENEMY_SPAWN = new Color(255, 0, 255);
    public static final Color TILE_POINT_BULLET = new Color(255, 255, 0);
    public static final Color TILE_BOOST_BULLET = new Color(255, 0, 0);

    private static final Map<Color, Class<? extends Cell>> TILES_MAPPING;
    static {
        Map<Color, Class<? extends Cell>> map = new HashMap<Color, Class<? extends Cell>>();

        map.put(TILE_EMPTY, Cell.class);
        map.put(TILE_WALL, WallCell.class);
        map.put(TILE_GATE, GateCell.class);
        map.put(TILE_HERO_SPAWN, HeroSpawnCell.class);
        map.put(TILE_ENEMY_SPAWN, EnemySpawnCell.class);
        map.put(TILE_POINT_BULLET, PointCell.class);
        map.put(TILE_BOOST_BULLET, BoostCell.class);

        TILES_MAPPING = Collections.unmodifiableMap(map);
    }

    private Integer width;
    private Integer height;

    private Cell[] cellList;
    private Cell[][] cellMap;

    public Maze(BufferedImage image) throws MazeException {
        initFromImage(image);
    }

    public void initFromImage(BufferedImage image) throws MazeException {
        width = image.getWidth();
        height = image.getHeight();
        logger.trace("Maze size {}x{}", width, height);

        populateCells(image);
        processCells();
    }

    private void populateCells(BufferedImage image) throws MazeException {
        cellList = new Cell[width * height];
        cellMap = new Cell[width][];

        int i = 0;
        for(int x = 0; x < width; ++x) {
            cellMap[x] = new Cell[height];

            for(int y = 0; y < height; ++y) {
                Color tileColor = new Color(image.getRGB(x, y));
                Point2D coordinates = new Point2D.Double(x, y);
                logger.trace("Tile {} at ({}, {})", ColorUtils.toHexString(tileColor), x, y);

                Cell cell = instantiateCell(tileColor, x, y);
                if(cell == null) {
                    logger.fatal("Invalid cell at {}", ColorUtils.toHexString(tileColor), x, y);
                    throw new MazeException("Invalid cell " + ColorUtils.toHexString(tileColor));
                }

                cellList[i] = cell;
                i += 1;

                cellMap[x][y] = cell;
            }
        }
    }

    private void processCells() {
        for(Cell cell: cellList) {
            Boolean unreachable = true;

            for(Direction direction: Direction.values()) {
                Boolean reachable = false;

                Cell neighbor = findCellNeighbor(cell, direction);
                if(neighbor != null) {
                    // todo: this test should be more verbose
                    if(neighbor.isWalkableBy(null)) {
                        reachable = true;
                    }

                    logger.trace("Found neighbor at {} for cell at ({}, {}) (reachable? {})", direction.toString(), cell.getX(), cell.getY(), reachable);

                    cell.setReachableFrom(direction, reachable);

                    if(reachable) {
                        unreachable = false;
                    }
                }
            }

            // todo: find out a better way to handle this case
            if(cell instanceof WallCell) {
                WallCell wallCell = (WallCell) cell;

                wallCell.setUnreachable(unreachable);

                if(unreachable) {
                    for(SecondaryDirection direction: SecondaryDirection.values()) {
                        Cell neighbor = findCellNeighbor(cell, direction);
                        if(neighbor != null) {
                            // todo: this test should be more verbose
                            if(neighbor.isWalkableBy(null)) {
                                logger.trace("Found orientation {} for cell at ({}, {})", direction.toString(), cell.getX(), cell.getY());

                                wallCell.setOrientation(direction);
                            }
                        }
                    }
                }

                wallCell.updateTileImage();
            }
        }
    }

    private Cell instantiateCell(Color tileColor, int x, int y) {
        Cell cell = null;

        if(TILES_MAPPING.containsKey(tileColor)) {
            Class<? extends Cell> cellClass = TILES_MAPPING.get(tileColor);
            logger.trace("Cell class {} for tile {}", cellClass.getName(), ColorUtils.toHexString(tileColor));

            try {
                cell = (Cell) cellClass.getConstructor(int.class, int.class).newInstance(x, y);
            } catch (InvocationTargetException e) {
                logger.error("Failed to instantiate cell class {}", cellClass.getName(), e);
            } catch (NoSuchMethodException e) {
                logger.error("Failed to instantiate cell class {}", cellClass.getName(), e);
            } catch (InstantiationException e) {
                logger.error("Failed to instantiate cell class {}", cellClass.getName(), e);
            } catch (IllegalAccessException e) {
                logger.error("Failed to instantiate cell class {}", cellClass.getName(), e);
            }

        }
        else {
            logger.error("Unknown tile {}", ColorUtils.toHexString(tileColor));
        }

        return cell;
    }

    public void paint(Graphics2D g2d) {
        for(Cell cell: cellList) {
            int draw_x = cell.getX() * IMAGE_TILE_WIDTH;
            int draw_y = cell.getY() * IMAGE_TILE_HEIGHT;

            g2d.drawImage(
                    cell.getTileImage(),
                    draw_x, draw_y,
                    IMAGE_TILE_WIDTH, IMAGE_TILE_HEIGHT,
                    null);
        }
    }

    private Cell findCellNeighbor(Cell cell, Direction direction) {
        int x = cell.getX(),
                y = cell.getY();

        switch (direction) {
            case DIRECTION_TOP:
                y -= 1;
                break;
            case DIRECTION_RIGHT:
                x += 1;
                break;
            case DIRECTION_DOWN:
                y += 1;
                break;
            case DIRECTION_LEFT:
                x -= 1;
                break;
        }

        if(!isValidPosition(x, y)) {
            return null;
        }

        return cellMap[x][y];
    }

    private Cell findCellNeighbor(Cell cell, SecondaryDirection direction) {
        int x = cell.getX(),
                y = cell.getY();

        switch (direction) {
            case SECONDARY_DIRECTION_TOP_RIGHT:
                x += 1;
                y -= 1;
                break;
            case SECONDARY_DIRECTION_BOTTOM_RIGHT:
                x += 1;
                y += 1;
                break;
            case SECONDARY_DIRECTION_BOTTOM_LEFT:
                x -= 1;
                y += 1;
                break;
            case SECONDARY_DIRECTION_TOP_LEFT:
                x -= 1;
                y -= 1;
                break;
        }

        if(!isValidPosition(x, y)) {
            return null;
        }

        return cellMap[x][y];
    }

    private Boolean isValidPosition(int x, int y) {
        if(x < 0 || y < 0) {
            return false;
        }

        if(x >= width || y >= height) {
            return false;
        }

        return true;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getDrawWidth() {
        return width * IMAGE_TILE_WIDTH;
    }

    public Integer getDrawHeight() {
        return height * IMAGE_TILE_HEIGHT;
    }
}
