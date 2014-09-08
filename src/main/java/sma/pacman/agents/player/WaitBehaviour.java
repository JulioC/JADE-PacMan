package sma.pacman.agents.player;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sma.pacman.agents.Protocol;

public class WaitBehaviour extends SimpleBehaviour {

    private static final Logger logger = LogManager.getLogger(WaitBehaviour.class.getName());

    private Boolean gameStarted = false;

    public WaitBehaviour(Agent a) {
        super(a);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();

        if (msg != null) {
            Boolean handled = false;

            String[] arguments = msg.getContent().split(Protocol.SEPARATOR);
            String action = arguments[0];

            switch (msg.getPerformative()) {
                case ACLMessage.INFORM:
                    if(action.equals(Protocol.ACTION_GAME_EVENT)) {
                        actionEvent(msg);
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

    private void actionEvent(ACLMessage msg) {
        String[] arguments = msg.getContent().split(Protocol.SEPARATOR);
        String event = arguments[1];

        if(event.equals(Protocol.EVENT_GAME_STARTED)) {
            gameStarted = true;
        }
    }

    @Override
    public boolean done() {
        return gameStarted;
    }

    private void actionDefault(ACLMessage msg) {
        logger.trace("Unhandled message: {}", msg);

        ACLMessage reply = msg.createReply();
        reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
        myAgent.send(reply);
    }
}
