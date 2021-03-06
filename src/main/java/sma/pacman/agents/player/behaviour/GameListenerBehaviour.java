package sma.pacman.agents.player.behaviour;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sma.pacman.agents.Protocol;
import sma.pacman.agents.player.PlayerAgent;

import java.awt.*;
import java.util.Scanner;

public class GameListenerBehaviour extends CyclicBehaviour {

    private static final Logger logger = LogManager.getLogger(GameListenerBehaviour.class.getName());

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();

        if (msg != null) {
            Boolean handled = false;

            String[] arguments = Protocol.parseArguments(msg);
            String action = arguments[0];

            switch (msg.getPerformative()) {
                case ACLMessage.INFORM:
                    if(action.equals(Protocol.ACTION_CHARACTER_EVENT)) {
                        actionCharacterEvent(msg);
                        handled = true;
                    }
                    else if(action.equals(Protocol.ACTION_GAME_EVENT)) {
                        actionGameEvent(msg);
                        handled = true;
                    }
                    break;
                case ACLMessage.AGREE:
                    handled = true;
                    break;
                case ACLMessage.REFUSE:
                    actionRefuse(msg);
                    handled = true;
                    break;
                case ACLMessage.FAILURE:
                    actionFailure(msg);
                    handled = true;
                    break;
                // todo: adicionar tratamento para outros casos, como obtencao de estado do jogo
            }

            if(!handled) {
                actionDefault(msg);
            }
        }
        else {
            block();
        }
    }

    private void actionCharacterEvent(ACLMessage msg) {
        // todo: a ideia é que esse evento seja propagado para uma FSM (que fique no lugar do MoveBehaviour, talvez?)
    }

    private void actionGameEvent(ACLMessage msg) {
        PlayerAgent playerAgent = (PlayerAgent) myAgent;

        String[] arguments = Protocol.parseArguments(msg);
        String event = arguments[1];

        if(event.equals(Protocol.EVENT_ROUND_UPDATE)) {
            Scanner scanner = new Scanner(arguments[2]);
            Integer x = scanner.nextInt();
            Integer y = scanner.nextInt();

            Point position = new Point(x, y);
            playerAgent.roundUpdate(position);
        }

    }

    private void actionFailure(ACLMessage msg) {
        logger.trace("Action failure: {}", msg.getContent());
    }

    private void actionRefuse(ACLMessage msg) {
        logger.trace("Action refuse: {}", msg.getContent());
    }

    private void actionDefault(ACLMessage msg) {
        logger.trace("Unhandled message: {}", msg);

        ACLMessage reply = msg.createReply();
        reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
        myAgent.send(reply);
    }
}