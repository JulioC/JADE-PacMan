package sma.pacman.agents.player;

import sma.pacman.agents.player.behaviour.RandomMoveBehaviour;

public class RandomPlayerAgent extends PlayerAgent {


    public RandomPlayerAgent() {
        super();

        moveBehaviour = new RandomMoveBehaviour(this);
    }

}
