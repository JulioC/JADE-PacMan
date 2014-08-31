import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sma.pacman.game.Board;
import sma.pacman.game.maze.Maze;
import sma.pacman.game.maze.MazeException;
import sma.pacman.util.ResourceUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Test {
    private static final Logger logger = LogManager.getLogger(Test.class.getName());

    public static void main(String[] args) {
        try {
            Maze maze = new Maze(ResourceUtils.getImage("/map.png"));
            Board board = new Board(maze);

            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.add(board);
            frame.setSize(board.getDrawWidth() + 16, board.getDrawHeight() + 48);
            frame.setVisible(true);

        } catch (IOException e) {
            logger.error("Failed to read maze tileImage", e);
        } catch (MazeException e) {
            logger.error("Failed to process maze tileImage", e);
        }
    }

}
