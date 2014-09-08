package sma.pacman.agents.player;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sma.pacman.agents.Protocol;

public class PlayerAgent extends Agent {

    private static final Logger logger = LogManager.getLogger(PlayerAgent.class.getName());

    private static final String STATE_JOIN = "join";
    private static final String STATE_WAITING = "waiting";
    private static final String STATE_PLAYING = "playing";

    private AID gameAgent;

    protected MoveBehaviour moveBehaviour;

    public PlayerAgent() {
        moveBehaviour = new MoveBehaviour(this);
    }

    @Override
    protected void setup() {
        super.setup();

        Object[] args = getArguments();
        String type = (String) args[0];
        String variation = (String) args[1];

        FSMBehaviour fsm = new FSMBehaviour();
        fsm.registerFirstState(new JoinBehaviour(this, type, variation), STATE_JOIN);
        fsm.registerState(new WaitBehaviour(this), STATE_WAITING);
        fsm.registerState(new PlayBehaviour(this, moveBehaviour), STATE_PLAYING);

        fsm.registerDefaultTransition(STATE_JOIN, STATE_WAITING);
        fsm.registerDefaultTransition(STATE_WAITING, STATE_PLAYING);

        addBehaviour(fsm);

        registerService();
    }

    private void registerService() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());

        ServiceDescription sd = new ServiceDescription();
        sd.setType(Protocol.SERVICE_TYPE_PLAY);
        sd.setName(Protocol.GAME_NAME);
        dfd.addServices(sd);

        try {
            DFService.register(this, dfd);
        }
        catch (FIPAException e) {
            logger.error(e);
        }

    }

    @Override
    protected void takeDown() {
        super.takeDown();

        try {
            DFService.deregister(this);
        }
        catch (FIPAException e) {
            logger.error(e);
        }
    }

    public void setGameAgent(AID gameAgent) {
        this.gameAgent = gameAgent;
    }

    public AID getGameAgent() {
        return gameAgent;
    }

    public void roundUpdate() {
        moveBehaviour.roundUpdate();
        moveBehaviour.restart();
    }

}
