package sma.pacman.game;

import sma.pacman.game.maze.Maze;

import javax.swing.*;
import java.awt.*;

public class Board extends JPanel implements Runnable {

    private Maze maze;

    public Board(Maze maze) {
        this.maze = maze;

        setBackground(Color.BLACK);
        setDoubleBuffered(true);
    }

    public void paint(Graphics g){
        super.paint(g);

        Graphics2D g2d = (Graphics2D)g;

        maze.paint(g2d);

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    public void run() {
        while(true){
            repaint();
        }
    }

    public Integer getDrawWidth() {
        return maze.getDrawWidth();
    }

    public Integer getDrawHeight() {
        return maze.getDrawHeight();
    }
}
