package sma.pacman.util;

import sma.pacman.game.Direction;
import sma.pacman.game.SecondaryDirection;

import java.awt.*;

public abstract class PointUtils {

    public static void translateTo(Point point, Direction direction) {
        switch (direction) {
            case TOP:
                point.translate(0, -1);
                break;
            case RIGHT:
                point.translate(1, 0);
                break;
            case DOWN:
                point.translate(0, 1);
                break;
            case LEFT:
                point.translate(-1, 0);
                break;
        }
    }

    public static void translateTo(Point point, SecondaryDirection direction) {
        switch (direction) {
            case SECONDARY_DIRECTION_TOP_RIGHT:
                point.translate(1, -1);
                break;
            case SECONDARY_DIRECTION_BOTTOM_RIGHT:
                point.translate(1, 1);
                break;
            case SECONDARY_DIRECTION_BOTTOM_LEFT:
                point.translate(-1, 1);
                break;
            case SECONDARY_DIRECTION_TOP_LEFT:
                point.translate(-1, -1);
                break;
        }
    }
}
