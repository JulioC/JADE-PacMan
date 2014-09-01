package sma.pacman.game.cell;

import java.awt.*;

public class PointCell extends Cell{

    public PointCell(Point position) {
        super(position);

        loadImage("point");
    }

}
