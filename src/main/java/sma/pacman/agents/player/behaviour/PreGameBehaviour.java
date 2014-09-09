package sma.pacman.agents.player.behaviour;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sma.pacman.agents.Protocol;
import sma.pacman.agents.player.PlayerAgent;

import java.awt.*;
import java.util.Scanner;

public class PreGameBehaviour extends SimpleBehaviour {

    private static final Logger logger = LogManager.getLogger(PreGameBehaviour.class.getName());

    private Boolean done = false;

    public PreGameBehaviour(Agent a) {
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
                case ACLMessage.INFORM:
                    if(action.equals(Protocol.ACTION_MAP)) {
                        actionMap(msg);
                        handled = true;
                    }
                    if(action.equals(Protocol.ACTION_GAME_EVENT)) {
                        actionEvent(msg);
                        handled = true;
                    }
                    break;
                case ACLMessage.AGREE:
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

    private void requestMap() {
        PlayerAgent playerAgent = (PlayerAgent) myAgent;

        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(playerAgent.getGameAgent());
        msg.setContent(Protocol.build(Protocol.ACTION_MAP));
        myAgent.send(msg);
    }

    private void actionEvent(ACLMessage msg) {
        String[] arguments = Protocol.parseArguments(msg);
        String event = arguments[1];

        if(event.equals(Protocol.EVENT_GAME_STARTED)) {
            PlayerAgent playerAgent = (PlayerAgent) myAgent;

            Scanner scanner = new Scanner(arguments[2]);
            Integer x = scanner.nextInt();
            Integer y = scanner.nextInt();

            Point position = new Point(x, y);
            playerAgent.setPosition(position);

            requestMap();
        }
    }

    private void actionMap(ACLMessage msg) {
        String[] arguments = Protocol.parseArguments(msg);
        Integer[][] map = Protocol.parseMap(arguments[1]);

        if(map != null) {
            PlayerAgent playerAgent = (PlayerAgent) myAgent;
            playerAgent.setMap(map);

            done = true;
        }
        else {
            logger.error("Invalid map '{}'", arguments[1]);
        }

    }

    @Override
    public boolean done() {
        return done;
    }

    private void actionDefault(ACLMessage msg) {
        logger.trace("Unhandled message: {}", msg);

        ACLMessage reply = msg.createReply();
        reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
        myAgent.send(reply);
    }
}
