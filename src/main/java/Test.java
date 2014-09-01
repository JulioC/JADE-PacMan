import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sma.pacman.game.Board;
import sma.pacman.game.Maze;
import sma.pacman.game.MazeException;
import sma.pacman.util.ResourceUtils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Test {
    private static final Logger logger = LogManager.getLogger(Test.class.getName());

    public static void main(String[] args) {
        Board board = new Board();
        board.start(1 / 60f, 5);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(board);

        int zoom = 2;
        int width = zoom * board.getPreferredSize().width;
        int height = zoom * board.getPreferredSize().height;

        frame.setSize(width + 16, height + 48);

        frame.setMinimumSize(board.getMinimumSize());
        frame.setVisible(true);
    }

}
