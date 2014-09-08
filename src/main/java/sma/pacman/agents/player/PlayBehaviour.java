package sma.pacman.agents.player;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.lang.acl.ACLMessage;
import sma.pacman.agents.Protocol;

public class PlayBehaviour extends ParallelBehaviour {

    MoveBehaviour moveBehaviour;

    public PlayBehaviour(Agent a, MoveBehaviour moveBehaviour) {
        super(a, WHEN_ANY);

        addSubBehaviour(moveBehaviour);
        addSubBehaviour(new GameListenerBehaviour());
    }
}
