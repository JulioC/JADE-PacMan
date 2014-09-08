package sma.pacman.agents.game;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sma.pacman.agents.Protocol;
import sma.pacman.game.Direction;

public class GameBehaviour extends CyclicBehaviour {

    private static final Logger logger = LogManager.getLogger(GameBehaviour.class.getName());

    @Override
    public void onStart() {
        GameAgent gameAgent = (GameAgent) myAgent;

        gameAgent.startGame();
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();

        if (msg != null && msg.getContent() != null) {
            Boolean handled = false;

            String[] arguments = msg.getContent().split(Protocol.SEPARATOR);
            String action = arguments[0];

            switch (msg.getPerformative()) {
                case ACLMessage.REQUEST:
                    if(action.equals(Protocol.ACTION_MOVE)) {
                        actionMove(msg);
                        handled = true;
                    }
                    break;
                case ACLMessage.QUERY_IF:
                    if(action.equals(Protocol.ACTION_CAN_MOVE)) {
                        actionCanMove(msg);
                        handled = true;
                    }
                    break;
            }

            if(!handled) {
                actionDefault(msg);
            }
        }
        else {
            block();
        }
    }

    private void actionMove(ACLMessage msg) {
        ACLMessage reply = msg.createReply();
        AID player = msg.getSender();

        String[] arguments = msg.getContent().split(Protocol.SEPARATOR);
        Direction direction = Direction.valueOf(arguments[1]);

        GameAgent gameAgent = (GameAgent) myAgent;

        try {
            gameAgent.movePlayer(player, direction);

            reply.setPerformative(ACLMessage.AGREE);
            reply.setContent(Protocol.MESSAGE_OK);
        } catch (Exception e) {
            reply.setPerformative(ACLMessage.FAILURE);
            reply.setContent(e.getMessage());
        }

        myAgent.send(reply);
    }

    private void actionCanMove(ACLMessage msg) {

    }

    private void actionDefault(ACLMessage msg) {
        logger.trace("Unhandled message: {}", msg);

        ACLMessage reply = msg.createReply();
        reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
        myAgent.send(reply);
    }
}
