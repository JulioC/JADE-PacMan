package sma.pacman.game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sma.pacman.game.character.Character;
import sma.pacman.game.character.EnemyCharacter;
import sma.pacman.game.character.HeroCharacter;
import sma.pacman.util.ResourceUtils;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Board extends Game {

    private static final Logger logger = LogManager.getLogger(Board.class.getName());

    public static final int TILE_HEIGHT = 12;
    public static final int TILE_WIDTH = 12;

    public static final int CHARACTER_HEIGHT = 20;
    public static final int CHARACTER_WIDTH = 20;

    private String mazeImage;

    private Maze maze;

    private HeroCharacter hero;
    private List<EnemyCharacter> enemies = new ArrayList<EnemyCharacter>();

    private List<Character> characters = new ArrayList<Character>();

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

        String[] enemyColors = {"pink", "red", "yellow", "cyan"};
        for(String enemyColor: enemyColors) {
            EnemyCharacter enemy = new EnemyCharacter(enemyColor);
            enemy.spawn(maze.findSpawnPositionFor(enemy));
            characters.add(enemy);
        }

        hero = new HeroCharacter(false);
        hero.spawn(maze.findSpawnPositionFor(hero));
        characters.add(hero);
    }

    @Override
    protected void update(float elapsedTime) {
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

}
