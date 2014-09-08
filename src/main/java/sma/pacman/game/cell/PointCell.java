package sma.pacman.game.cell;

import sma.pacman.game.Bullet;

import java.awt.*;

public class PointCell extends Cell{

    private Boolean consumed = false;

    public PointCell(Point position) {
        super(position);

        loadImage("point");
    }

    @Override
    public Boolean hasBullet() {
        return !consumed;
    }

    @Override
    public Bullet consumeBullet() {
        if(consumed) {
            return null;
        }

        consumed = true;
        loadImage("empty");

        return Bullet.BOOST;
    }

}
