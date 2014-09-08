package sma.pacman.agents.player;

public class RandomPlayerAgent extends PlayerAgent {


    public RandomPlayerAgent() {
        super();

        moveBehaviour = new RandomMoveBehaviour(this);
    }

}
