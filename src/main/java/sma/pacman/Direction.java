package sma.pacman;

public enum Direction {
    DIRECTION_TOP,
    DIRECTION_RIGHT,
    DIRECTION_DOWN,
    DIRECTION_LEFT;

    @Override
    public String toString() {
        switch (this) {
            case DIRECTION_TOP:
                return "top";
            case DIRECTION_RIGHT:
                return "right";
            case DIRECTION_DOWN:
                return "down";
            case DIRECTION_LEFT:
                return "left";
        }

        return super.toString();
    }
}
