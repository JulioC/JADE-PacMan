package sma.pacman.agents.player;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import sma.pacman.game.Direction;

import java.util.Random;

public class RandomMoveBehaviour extends MoveBehaviour {

    private Random random;

    public RandomMoveBehaviour(Agent a) {
        super(a);

        random = new Random( hashCode() + System.currentTimeMillis());
    }

    @Override
    protected Direction generateMove() {
        Direction direction = null;
        switch (random.nextInt(4)) {
            case 0:
                direction = Direction.TOP;
                break;
            case 1:
                direction = Direction.RIGHT;
                break;
            case 2:
                direction = Direction.DOWN;
                break;
            case 3:
                direction = Direction.LEFT;
                break;
        }

        return direction;
    }

}
