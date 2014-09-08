package sma.pacman.agents.player;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sma.pacman.agents.Protocol;

public class JoinBehaviour extends Behaviour {

    private static final Logger logger = LogManager.getLogger(JoinBehaviour.class.getName());

    private Boolean joined = false;

    private Boolean waitingReply = false;

    private String type;
    private String variation;

    public JoinBehaviour(Agent a, String type, String variation) {
        super(a);

        this.type = type;
        this.variation = variation;
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();

        if (msg != null) {
            Boolean handled = false;

            String[] arguments = msg.getContent().split(Protocol.SEPARATOR);
            String action = arguments[0];

            switch (msg.getPerformative()) {
                case ACLMessage.CFP:
                    if(action.equals(Protocol.ACTION_JOIN)) {
                        actionPropose(msg);
                        handled = true;
                    }
                    break;
                case ACLMessage.ACCEPT_PROPOSAL:
                    actionAccepted(msg);
                    handled = true;
                    break;
                case ACLMessage.REJECT_PROPOSAL:
                    actionRejected(msg);
                    handled = true;
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

    private void actionPropose(ACLMessage msg) {
        if(!waitingReply) {
            String[] arguments = msg.getContent().split(Protocol.SEPARATOR);
            String game = arguments[1];

            if(game.equals(Protocol.GAME_NAME)) {
                ACLMessage reply = msg.createReply();

                reply.setPerformative(ACLMessage.PROPOSE);
                reply.setContent(Protocol.build(
                        Protocol.ACTION_JOIN, type, variation));

                waitingReply = true;
                myAgent.send(reply);
            }
        }
    }

    private void actionAccepted(ACLMessage msg) {
        waitingReply = false;
        joined = true;

        PlayerAgent playerAgent = (PlayerAgent) myAgent;
        playerAgent.setGameAgent(msg.getSender());

        logger.trace("Joined game {}", msg.getSender().getName());
    }

    private void actionRejected(ACLMessage msg) {
        waitingReply = false;

        logger.error("Proposal rejected: {}", msg.getContent());
    }

    private void actionDefault(ACLMessage msg) {
        logger.trace("Unhandled message: {}", msg);

        ACLMessage reply = msg.createReply();
        reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
        myAgent.send(reply);
    }

    @Override
    public boolean done() {
        return joined;
    }
}