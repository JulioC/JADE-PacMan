package sma.pacman.agents.player;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import sma.pacman.agents.Protocol;
import sma.pacman.game.Direction;

public class MoveBehaviour extends CyclicBehaviour {

    private Boolean moveSent = false;

    public MoveBehaviour(Agent a) {
        super(a);
    }

    @Override
    public final void action() {
        if(!moveSent) {
            Direction move = generateMove();
            if(move != null) {
                sendMove(move);
            }
        }

        block();
    }

    protected Direction generateMove() {
        return null;
    }

    protected void sendMove(Direction direction) {
        PlayerAgent playerAgent = (PlayerAgent) myAgent;

        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(playerAgent.getGameAgent());
        msg.setContent(Protocol.build(
                Protocol.ACTION_MOVE,
                direction.toString()));
        myAgent.send(msg);

        moveSent = true;
    }

    public void roundUpdate() {
        moveSent = false;
    }
}
