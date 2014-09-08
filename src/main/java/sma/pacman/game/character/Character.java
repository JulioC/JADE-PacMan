package sma.pacman.game.character;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sma.pacman.game.Direction;
import sma.pacman.game.Board;
import sma.pacman.game.graphics.Animation;
import sma.pacman.util.PointUtils;
import sma.pacman.util.ResourceUtils;

import java.awt.*;
import java.io.IOException;
import java.util.*;

public class Character {

    public interface Listener {

        void nextMoveSet(Direction move);

    }

    private static final Logger logger = LogManager.getLogger(Character.class.getName());

    private static final Float SPAWN_BLINK_INTERVAL = 0.02f;
    private Point position;

    private Direction orientation;

    private Animation currentAnimation;
    private Boolean alive;

    private Float lifeTime;
    private Integer aliveRounds;

    private Boolean spawnProtection = false;

    protected Boolean heroBoost = false;

    protected Set<CharacterEvent> events = new HashSet<CharacterEvent>();

    private Map<Direction, Animation> animations = new HashMap<Direction, Animation>();

    private Direction nextMove;

    private Integer score = 0;

    private String name;

    private java.util.List<Listener> listeners = new ArrayList<Listener>();

    public Character(String name) {
        alive = false;
        this.name = name;
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void spawn(Point p) {
        alive = true;
        lifeTime = 0f;
        aliveRounds = 0;

        position = p;
        orientation = Direction.RIGHT;
        currentAnimation = animations.get(orientation);

        setSpawnProtection(true);

        events.add(CharacterEvent.SPAWN);
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

    public Integer getAliveRounds() {
        return aliveRounds;
    }

    public void setSpawnProtection(Boolean state) {
        if(state) {
            events.add(CharacterEvent.PROTECTION_ON);
        }
        else {
            events.add(CharacterEvent.PROTECTION_OFF);
        }

        spawnProtection = state;
    }

    public Boolean hasSpawnProtection() {
        return spawnProtection;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void setOrientation(Direction orientation) {
        this.orientation = orientation;
        currentAnimation = animations.get(orientation);
    }

    public void setNextMove(Direction nextMove) {
        this.nextMove = nextMove;
        fireNextMoveSet();
    }

    private void fireNextMoveSet() {
        for (Listener listener: listeners) {
            listener.nextMoveSet(nextMove);
        }
    }

    public Direction getNextMove() {
        return nextMove;
    }

    public Boolean hasNextMove() {
        return (nextMove != null);
    }

    public void doNextMove(Boolean blocked) {
        if(!blocked) {
            Point newPosition = new Point(position);
            PointUtils.translateTo(newPosition, nextMove);

            setPosition(newPosition);
        }

        setOrientation(nextMove);

        nextMove = null;
        events.clear();

        aliveRounds++;
    }

    public void setHeroBoost(Boolean state) {
            if(state) {
                events.add(CharacterEvent.BOOST_ON);
            }
            else {
                events.add(CharacterEvent.BOOST_OFF);
            }

            heroBoost = state;
        }

    public Boolean isAlive() { return alive; }

    public void die() {
        events.add(CharacterEvent.DEATH);

        alive = false;
    }

    public void addScore(Integer amount) {
        score += amount;

        events.add(CharacterEvent.SCORE_UP);
    }

    public Integer getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public Set<CharacterEvent> getEvents() {
        return events;
    }
}
