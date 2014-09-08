package sma.pacman.game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sma.pacman.game.cell.*;
import sma.pacman.game.character.Character;
import sma.pacman.util.ColorUtils;
import sma.pacman.util.PointUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;

public class Maze {

    private static final Logger logger = LogManager.getLogger(Maze.class.getName());

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

    private Dimension gridSize;

    private Cell[] cellList;
    private Cell[][] cellMap;

    public Maze(BufferedImage image) throws MazeException {
        initFromImage(image);
    }

    public void initFromImage(BufferedImage image) throws MazeException {
        int columns = image.getWidth();
        int rows = image.getHeight();
        gridSize = new Dimension(columns, rows);
        logger.trace("Grid size {}", gridSize);

        populateCells(image);
        processCells();
    }

    private void populateCells(BufferedImage image) throws MazeException {
        cellList = new Cell[gridSize.width * gridSize.height];
        cellMap = new Cell[gridSize.width][];

        int i = 0;
        for(int x = 0; x < gridSize.width; ++x) {
            cellMap[x] = new Cell[gridSize.height];

            for(int y = 0; y < gridSize.height; ++y) {
                Color tileColor = new Color(image.getRGB(x, y));
                Point position = new Point(x, y);
                logger.trace("Tile {} at {}", ColorUtils.toHexString(tileColor), position);

                Cell cell = instantiateCell(tileColor, position);
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
                    if(neighbor.isWalkableFor(null)) {
                        reachable = true;
                    }

                    logger.trace("Found neighbor at {} for cell at {} (reachable? {})", direction.toString(), cell.getPosition(), reachable);

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
                            if(neighbor.isWalkableFor(null)) {
                                logger.trace("Found orientation {} for cell at {}", direction.toString(), cell.getPosition());

                                wallCell.setOrientation(direction);
                            }
                        }
                    }
                }

                wallCell.updateTileImage();
            }
        }
    }

    private Cell instantiateCell(Color tileColor, Point position) {
        Cell cell = null;

        if(TILES_MAPPING.containsKey(tileColor)) {
            Class<? extends Cell> cellClass = TILES_MAPPING.get(tileColor);
            logger.trace("Cell class {} for tile {}", cellClass.getName(), ColorUtils.toHexString(tileColor));

            try {
                cell = cellClass.getConstructor(Point.class).newInstance(position);
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

    public void draw(Graphics g) {
        for(Cell cell: cellList) {
            cell.draw(g);
        }
    }

    private Cell findCellNeighbor(Cell cell, Direction direction) {
        Point position = new Point(cell.getPosition());

        PointUtils.translateTo(position, direction);

        return getCell(position);
    }

    private Cell findCellNeighbor(Cell cell, SecondaryDirection direction) {
        Point position = new Point(cell.getPosition());

        PointUtils.translateTo(position, direction);

        return getCell(position);
    }

    private Cell getCell(Point position) {
        if(!isValidPosition(position)) {
            return null;
        }

        return cellMap[position.x][position.y];
    }

    private Boolean isValidPosition(Point position) {
        if(position.x < 0 || position.y < 0) {
            return false;
        }

        if(position.x >= gridSize.width || position.y >= gridSize.height) {
            return false;
        }

        return true;
    }

    public Boolean isWalkablePositionFor(Point position, Character character) {
        Cell cell = getCell(position);
        if(cell == null) {
            return false;
        }

        return cell.isWalkableFor(character);
    }

    public Point findSpawnPositionFor(Character character) {
        List<Point> spawnPositions = new ArrayList<Point>();

        for(Cell cell: cellList) {
            if(cell.isSpawnFor(character)) {
                spawnPositions.add(cell.getPosition());
            }
        }

        if(spawnPositions.isEmpty()) {
            return null;
        }

        Collections.shuffle(spawnPositions);
        return spawnPositions.get(0);
    }

    public Boolean hasBullet(Point position) {
        Cell cell = getCell(position);
        return cell.hasBullet();
    }

    public Bullet consumeBullet(Point position) {
        Cell cell = getCell(position);
        return cell.consumeBullet();
    }

    public int getWidth() { return gridSize.width * Board.TILE_WIDTH; }

    public int getHeight() { return gridSize.height * Board.TILE_HEIGHT; }
}
