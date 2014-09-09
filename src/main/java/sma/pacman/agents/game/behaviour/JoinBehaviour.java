package sma.pacman.agents.game.behaviour;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sma.pacman.agents.Protocol;
import sma.pacman.agents.game.GameAgent;

public class JoinBehaviour extends CyclicBehaviour {

    private static final Logger logger = LogManager.getLogger(JoinBehaviour.class.getName());

    public JoinBehaviour(Agent a) {
        super(a);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();

        if (msg != null) {
            Boolean handled = false;

            String[] arguments = Protocol.parseArguments(msg);
            String action = arguments[0];

            switch (msg.getPerformative()) {
                case ACLMessage.PROPOSE:
                    if(action.equals(Protocol.ACTION_JOIN)) {
                        actionJoin(msg);
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

    private void actionJoin(ACLMessage msg) {
        ACLMessage reply = msg.createReply();
        AID player = msg.getSender();

        String[] arguments = Protocol.parseArguments(msg);
        String type = arguments[1];
        String variation = arguments[2];

        GameAgent gameAgent = (GameAgent) myAgent;

        try {
            gameAgent.registerPlayer(player, type, variation);

            reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
            reply.setContent(Protocol.MESSAGE_OK);
        } catch (Exception e) {
            reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
            reply.setContent(e.getMessage());
        }

        myAgent.send(reply);
    }

    private void actionDefault(ACLMessage msg) {
        logger.trace("Unhandled message: {}", msg);

        ACLMessage reply = msg.createReply();
        reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
        myAgent.send(reply);
    }
}
