package sma.pacman.game.character;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sma.pacman.Direction;
import sma.pacman.game.Board;
import sma.pacman.game.graphics.Animation;
import sma.pacman.util.ResourceUtils;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Character {

    private static final Logger logger = LogManager.getLogger(Character.class.getName());

    private static final float SPAWN_BLINK_INTERVAL = 0.02f;
    private Point position;

    private Direction orientation;

    private Animation currentAnimation;
    private Boolean alive;

    private float lifeTime;

    private float spawnProtection;

    private Map<Direction, Animation> animations = new HashMap<Direction, Animation>();

    public Character() {
        alive = false;

        spawnProtection = 2f;
    }

    public void spawn(Point p) {
        alive = true;
        lifeTime = 0;

        position = p;
        orientation = Direction.DIRECTION_RIGHT;
        currentAnimation = animations.get(orientation);
    }

    protected void loadAnimations(String name, int frameCount, float frameDelay) {
        logger.trace("Loading character animations resource {}", name);

        animations.clear();
        currentAnimation = null;

        try {
            Image[] images = new Image[frameCount];
            for(Direction direction: Direction.values()) {
                for(int i = 0; i < frameCount; ++i) {
                    String resourceName = String.format("/images/character/%s/%s/%d.png", name, direction, (i+ 1));
                    images[i] = ResourceUtils.getImage(resourceName);
                }

                Animation animation = new Animation(images, frameDelay);
                animation.start();

                animations.put(direction, animation);
            }

            currentAnimation = animations.get(orientation);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to set tile image from resource {}", name, e);
        } catch (IOException e) {
            logger.error("Failed to set tile image from resource {}", name, e);
        }
    }

    public void update(float elapsedTime) {
        if(alive) {
            currentAnimation.update(elapsedTime);

            lifeTime += elapsedTime;
        }
    }

    public void draw(Graphics g) {
        Boolean visible = false;

        if(alive) {
            if(hasSpawnProtection()) {
                visible = (((int)(lifeTime / SPAWN_BLINK_INTERVAL) % 2) != 0);
            }
            else {
                visible = true;
            }
        }

        if(visible) {
            int tile_width = Board.TILE_WIDTH;
            int tile_height = Board.TILE_HEIGHT;

            int character_width = Board.CHARACTER_WIDTH;
            int character_height = Board.CHARACTER_HEIGHT;

            int draw_x = position.x * tile_width - (character_width - tile_width)/2;
            int draw_y = position.y * tile_height - (character_height - tile_height)/2;

            if(currentAnimation != null) {
                Image image = currentAnimation.getSprite();

                g.drawImage(image, draw_x, draw_y, character_width, character_height, null);
            }
        }
    }

    public Boolean hasSpawnProtection() {
        return lifeTime < spawnProtection;
    }

    public void setSpawnProtection(float spawnProtection) {
        this.spawnProtection = spawnProtection;
    }
}
