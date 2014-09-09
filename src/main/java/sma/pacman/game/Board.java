package sma.pacman.game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sma.pacman.game.character.*;
import sma.pacman.game.character.Character;
import sma.pacman.util.PointUtils;
import sma.pacman.util.ResourceUtils;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board extends Engine {

    public interface Listener {

        void characterAdded(Character character);

        void roundUpdate(Integer currentRound);

    }

    private static final Logger logger = LogManager.getLogger(Board.class.getName());

    public static final int TILE_HEIGHT = 12;
    public static final int TILE_WIDTH = 12;

    public static final int CHARACTER_HEIGHT = 20;
    public static final int CHARACTER_WIDTH = 20;

    private static Integer PROTECTION_ROUNDS = 7;
    private static Integer BOOST_ROUNDS = 20;

    private String mazeImage;

    private Maze maze;

    private HeroCharacter hero;
    private List<EnemyCharacter> enemies = new ArrayList<EnemyCharacter>();

    private List<Character> characters = new ArrayList<Character>();

    private Integer currentRound;
    private Integer boostRoundsLeft = 0;

    private Boolean started = false;

    private List<Listener> listeners = new ArrayList<Listener>();

    public Board() {
        super();

        mazeImage = "/map.png";
    }

    @Override
    protected void init() {
        try {
            maze = new Maze(ResourceUtils.getImage(mazeImage));
        } catch (IOException e) {
            logger.error("Failed to read maze image", e);
        } catch (MazeException e) {
            logger.error("Failed to process maze image", e);
        }

        setCanvasSize(new Dimension(maze.getWidth(), maze.getHeight()));
        setCanvasRatioFractional(false);
    }

    @Override
    protected void update(float elapsedTime) {
        if(started) {
            if(movesReady()) {
                updateRound();
            }
        }

        for(Character character: characters) {
            character.update(elapsedTime);
        }
    }

    @Override
    protected void render(Graphics g, float interpolationTime) {
        maze.draw(g);

        for(Character character: characters) {
            character.draw(g);
        }
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void addCharacter(Character character) {
        if(!started) {
            characters.add(character);

            if(character instanceof HeroCharacter) {
                hero = (HeroCharacter) character;
            }
            if(character instanceof EnemyCharacter) {
                enemies.add((EnemyCharacter) character);
            }

            fireCharacterAdded(character);
        }
    }

    private void fireCharacterAdded(Character character) {
        for (Listener listener: listeners) {
            listener.characterAdded(character);
        }
    }

    public HeroCharacter getHero() {
        return hero;
    }

    public Boolean isReady() {
        if(hero == null) {
            return false;
        }

        if(enemies.isEmpty()) {
            return false;
        }

        return true;
    }

    public void start() {
        started = true;
        currentRound = 0;

        for(Character character: characters) {
            character.spawn(maze.findSpawnPositionFor(character));
        }
    }

    protected Boolean canMoveCharacterTo(Character character, Direction direction) {
        Point position = new Point(character.getPosition());

        PointUtils.translateTo(position, direction);

        return maze.isWalkablePositionFor(position, character);
    }

    protected void processBullets() {
        Point position = hero.getPosition();

        if(maze.hasBullet(position)) {
            Bullet bullet = maze.consumeBullet(position);
            hero.addScore(bullet.getScore());

            if(bullet == Bullet.BOOST) {
                boostRoundsLeft += BOOST_ROUNDS;

                for(Character c: characters) {
                    hero.setHeroBoost(true);
                }
            }
        }
    }

    protected void processDeaths() {
        if(hero.isAlive()) {
            Point heroPosition = hero.getPosition();

            for(EnemyCharacter enemy: enemies) {
                if(enemy.isAlive() && heroPosition.equals(enemy.getPosition())) {
                    if(hasBoost()) {
                        if(!enemy.hasSpawnProtection()) {
                            enemy.die();
                            hero.addScore(1000);
                        }
                    }
                    else {
                        if(!hero.hasSpawnProtection()) {
                            hero.die();
                            enemy.addScore(1);
                        }
                    }
                }
            }
        }
    }

    protected void processBoost() {
        if(hasBoost()) {
            boostRoundsLeft -= 1;

            if(boostRoundsLeft == 0) {
                for(Character character: characters) {
                    character.setHeroBoost(false);
                }
            }
        }
    }

    protected void processProtection() {
        for(Character character: characters) {
            if(character.isAlive() && character.hasSpawnProtection()) {
                if(character.getAliveRounds() > PROTECTION_ROUNDS) {
                    character.setSpawnProtection(false);
                }
            }
        }
    }

    protected Boolean hasBoost() {
        return (boostRoundsLeft > 0);
    }

    protected void updateRound() {
        for(Character character: characters) {
            if(character.isAlive()) {
                Boolean canMove = canMoveCharacterTo(character, character.getNextMove());
                character.doNextMove(!canMove);
            }
        }

        processBullets();
        processDeaths();
        processBoost();
        processProtection();

        currentRound++;

        fireRoundUpdate();
    }

    private void fireRoundUpdate() {
        for (Listener listener: listeners) {
            listener.roundUpdate(currentRound);
        }
    }

    protected Boolean movesReady() {
        for(Character character: characters) {
            if(character.isAlive()) {
                if(!character.hasNextMove()) {
                    return false;
                }
            }
        }

        return true;
    }

    public Integer[][] getMapArrayFor(Character character) {
        Point p = new Point();

        Integer height = maze.getGridHeight();
        Integer width = maze.getGridWidth();
        Integer[][] map = new Integer[width][height];

        for(p.x = 0; p.x < width; ++p.x) {
            for(p.y = 0; p.y < height; ++p.y) {
                Integer value;

                if(maze.isWalkablePositionFor(p, character)) {
                    value = 0;
                }
                else {
                    // todo: add other tile types
                    value = 1;
                }

                map[p.x][p.y] = value;
            }
        }

        return map;
    }




}
