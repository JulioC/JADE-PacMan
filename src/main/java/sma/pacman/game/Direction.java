package sma.pacman.game;

public enum Direction {
    TOP,
    RIGHT,
    DOWN,
    LEFT;

    public static Direction opposite(Direction direction) {
        switch (direction) {
            case TOP:
                return DOWN;
            case RIGHT:
                return LEFT;
            case DOWN:
                return TOP;
            case LEFT:
                return RIGHT;
        }

        return null;
    }
}
