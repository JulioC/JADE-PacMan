package sma.pacman.game.graphics;

import java.awt.*;

public class Frame {

    private Image frame;
    private float duration;

    public Frame(Image frame, float duration) {
        this.frame = frame;
        this.duration = duration;
    }

    public Image getFrame() {
        return frame;
    }

    public void setFrame(Image frame) {
        this.frame = frame;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

}