package sma.pacman.game;

public enum Bullet {
    POINT(10),
    BOOST(200);

    private int score;

    Bullet(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}