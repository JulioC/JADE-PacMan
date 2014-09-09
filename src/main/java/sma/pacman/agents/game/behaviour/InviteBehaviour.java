package sma.pacman.agents.game.behaviour;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sma.pacman.agents.Protocol;
import sma.pacman.agents.game.GameAgent;

import java.util.ArrayList;
import java.util.List;

public class InviteBehaviour extends TickerBehaviour {

    private static final Logger logger = LogManager.getLogger(InviteBehaviour.class.getName());

    public InviteBehaviour(Agent a, long period) {
        super(a, period);
    }

    @Override
    protected void onTick() {
        GameAgent gameAgent = (GameAgent) myAgent;

        if(gameAgent.isGameReady()) {
            stop();
        }
        else {
            sendInvites();
        }
    }

    private void sendInvites() {
        GameAgent gameAgent = (GameAgent) myAgent;

        ACLMessage msg = new ACLMessage(ACLMessage.CFP);
        msg.setContent(Protocol.build(
                Protocol.ACTION_JOIN,
                Protocol.GAME_NAME));

        List<AID> players = getPlayers();
        for(AID player: players) {
            if(!gameAgent.isRegisteredPlayer(player)) {
                logger.trace("Inviting {}", player.getName());
                msg.addReceiver(player);
            }
        }

        myAgent.send(msg);
    }

    protected List<AID> getPlayers() {
        List<AID> players = new ArrayList<AID>();

        DFAgentDescription template = new DFAgentDescription();

        ServiceDescription sd = new ServiceDescription();
        sd.setType(Protocol.SERVICE_TYPE_PLAY);
        sd.setName(Protocol.GAME_NAME);

        template.addServices(sd);

        try {
            DFAgentDescription[] results = DFService.search(myAgent, template);
            for (DFAgentDescription result: results) {
                players.add(result.getName());
            }
        }
        catch (FIPAException e) {
            logger.error(e);
        }

        return players;
    }

}
