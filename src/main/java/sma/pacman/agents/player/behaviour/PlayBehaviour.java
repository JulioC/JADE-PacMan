package sma.pacman.agents.player.behaviour;

import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;

public class PlayBehaviour extends ParallelBehaviour {

    MoveBehaviour moveBehaviour;

    public PlayBehaviour(Agent a, MoveBehaviour moveBehaviour) {
        super(a, WHEN_ANY);

        addSubBehaviour(moveBehaviour);
        addSubBehaviour(new GameListenerBehaviour());
    }
}
