package sma.pacman.agents.player.behaviour;

import jade.core.Agent;
import sma.pacman.agents.player.PlayerAgent;
import sma.pacman.agents.player.logic.PathFinder;
import sma.pacman.game.Direction;

import java.awt.*;
import java.util.*;

public class TargetedMoveBehaviour extends MoveBehaviour {

    private Point target;

    private Queue<Direction> path;

    public TargetedMoveBehaviour(Agent a) {
        super(a);
    }

    @Override
    protected Direction generateMove() {
        Direction direction = null;

        if(path == null || path.isEmpty()) {
            buildPath();
        }

        if(!path.isEmpty()) {
            direction = path.remove();
        }

        return direction;
    }

    public void buildPath() {
        PlayerAgent playerAgent = (PlayerAgent) myAgent;

        PathFinder pf = new PathFinder(playerAgent.getMap());
        path = pf.findPath(playerAgent.getPosition(), target);
    }

    public void setTarget(Point target) {
        this.target = target;


    }
}
