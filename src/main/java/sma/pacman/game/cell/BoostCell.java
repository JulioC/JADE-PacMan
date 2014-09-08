package sma.pacman.game.cell;

import sma.pacman.game.Bullet;

import java.awt.*;

public class BoostCell extends Cell{

    private Boolean consumed = false;

    public BoostCell(Point position) {
        super(position);

        loadImage("boost");
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
