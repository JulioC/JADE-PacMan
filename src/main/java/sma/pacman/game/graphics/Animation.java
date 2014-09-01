package sma.pacman.game.graphics;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Animation {

    private float frameDelay;
    private float frameElapsedTime;

    private int currentFrame;
    private int totalFrames;

    private boolean stopped;

    private List<Frame> frames = new ArrayList<Frame>();

    public Animation(Image[] frames, float frameDelay) {
        this.frameDelay = frameDelay;
        this.stopped = true;

        for (int i = 0; i < frames.length; i++) {
            addFrame(frames[i], frameDelay);
        }

        this.frameElapsedTime = 0;
        this.frameDelay = frameDelay;
        this.currentFrame = 0;
        this.totalFrames = this.frames.size();
    }

    public void start() {
        if (!stopped) {
            return;
        }

        if (frames.size() == 0) {
            return;
        }

        stopped = false;
    }

    public void stop() {
        if (frames.size() == 0) {
            return;
        }

        stopped = true;
    }

    public void restart() {
        if (frames.size() == 0) {
            return;
        }

        stopped = false;
        currentFrame = 0;
    }

    public void reset() {
        this.stopped = true;
        this.frameElapsedTime = 0;
        this.currentFrame = 0;
    }

    private void addFrame(Image frame) {
        addFrame(frame, frameDelay);
    }

    private void addFrame(Image frame, float duration) {
        if (duration <= 0) {
            throw new RuntimeException("Invalid duration: " + duration);
        }

        frames.add(new Frame(frame, duration));
        currentFrame = 0;
    }

    public Image getSprite() {
        return frames.get(currentFrame).getFrame();
    }

    public void update(float elapsedTime) {
        if (!stopped) {
            frameElapsedTime += elapsedTime;

            if (frameElapsedTime > frameDelay) {
                frameElapsedTime -= frameDelay;
                currentFrame += 1;

                if (currentFrame > totalFrames - 1) {
                    currentFrame = 0;
                }
            }
        }

    }

}