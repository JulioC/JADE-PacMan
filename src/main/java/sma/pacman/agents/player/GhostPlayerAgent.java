package sma.pacman.agents.player;

import sma.pacman.agents.player.behaviour.TargetedMoveBehaviour;

import java.awt.*;

public class GhostPlayerAgent extends PlayerAgent {

    TargetedMoveBehaviour targetedMoveBehaviour;

    public GhostPlayerAgent() {
        super();

        targetedMoveBehaviour = new TargetedMoveBehaviour(this);
        moveBehaviour = targetedMoveBehaviour;

        targetedMoveBehaviour.setTarget(new Point(1, 1));
    }

}
