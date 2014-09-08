package sma.pacman.agents.game;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sma.pacman.game.Board;
import sma.pacman.game.Direction;
import sma.pacman.game.character.*;
import sma.pacman.game.character.Character;
import sma.pacman.agents.Protocol;
import sma.pacman.ui.Game;

import java.util.HashMap;
import java.util.Map;

public class GameAgent extends Agent {

    private static final Logger logger = LogManager.getLogger(GameAgent.class.getName());

    private static final String STATE_PREGAME = "pregame";
    private static final String STATE_IN_GAME = "waiting";

    private static Integer ANNOUNCE_INTERVAL = 5*1000;

    private Board board;

    private Map<AID, Character> players = new HashMap<AID, Character>();

    @Override
    protected void setup() {
        setupGame();

        FSMBehaviour fsm = new FSMBehaviour();

        ParallelBehaviour preGameBehaviour = new ParallelBehaviour(this, ParallelBehaviour.WHEN_ANY);
        preGameBehaviour.addSubBehaviour(new JoinBehaviour(this));
        preGameBehaviour.addSubBehaviour(new InviteBehaviour(this, ANNOUNCE_INTERVAL));
        fsm.registerFirstState(preGameBehaviour, STATE_PREGAME);

        fsm.registerState(new GameBehaviour(), STATE_IN_GAME);

        fsm.registerDefaultTransition(STATE_PREGAME, STATE_IN_GAME);

        addBehaviour(fsm);
    }

    private void setupGame() {
        board = new Board();
        board.start(1 / 60f, 5);

        board.addListener(new Board.Listener() {
            @Override
            public void characterAdded(Character character) {
            }

            @Override
            public void roundUpdate(Integer currentRound) {
                notifyRoundUpdate();
            }
        });

        Game game = new Game(board);
        game.setVisible(true);
    }

    public Boolean isGameReady() {
        return board.isReady();
    }

    public void startGame() {
        board.start();

        notifyPlayers(Protocol.EVENT_GAME_STARTED);
    }

    public void registerPlayer(AID player, String type, String variation) throws Exception {
        if(players.containsKey(player)) {
            throw new Exception(Protocol.MESSAGE_PLAYER_ALREADY_IN);
        }

        String name = player.getLocalName();

        Character character;
        if(type.equals(Protocol.PLAYER_HERO)) {
            if(board.getHero() != null) {
                throw new Exception(Protocol.MESSAGE_NO_FREE_SLOTS);
            }

            Boolean alt = variation.equals(Protocol.PLAYER_HERO_FEMALE);
            character = new HeroCharacter(name, alt);
        }
        else if(type.equals(Protocol.PLAYER_ENEMY)) {
            if(variation.equals(Protocol.PLAYER_ENEMY_CYAN)
                    || variation.equals(Protocol.PLAYER_ENEMY_PINK)
                    || variation.equals(Protocol.PLAYER_ENEMY_RED)
                    || variation.equals(Protocol.PLAYER_ENEMY_YELLOW)) {
                character = new EnemyCharacter(name, variation);
            }
            else {
                logger.error("Invalid player variation {}", variation);
                throw new Exception(Protocol.MESSAGE_INVALID_ARGUMENTS);
            }
        }
        else {
            logger.error("Invalid player type {}", type);
            throw new Exception(Protocol.MESSAGE_INVALID_ARGUMENTS);
        }

        board.addCharacter(character);
        players.put(player, character);

        logger.trace("Player {} joined", player.getName());
    }

    public Boolean isRegisteredPlayer(AID player) {
        return players.containsKey(player);
    }

    public void notifyPlayers(String event) {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent(Protocol.build(
                Protocol.ACTION_GAME_EVENT,
                event));

        for(AID player: players.keySet()) {
            msg.addReceiver(player);
        }

        send(msg);
    }

    public void notifyRoundUpdate() {
        for(AID player: players.keySet()) {
            Character character = players.get(player);

            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(player);

            msg.setContent(Protocol.build(
                    Protocol.ACTION_GAME_EVENT,
                    Protocol.EVENT_ROUND_UPDATE));
            send(msg);

            for(CharacterEvent event: character.getEvents()) {
                msg.setContent(Protocol.build(
                        Protocol.ACTION_CHARACTER_EVENT,
                        event.toString()));
                send(msg);
            }
        }
    }

    public void movePlayer(AID player, Direction direction) throws Exception {
        if(!players.containsKey(player)) {
            logger.error("Player {} not registered", player.getName());
            throw new Exception(Protocol.MESSAGE_PLAYER_NOT_IN);
        }

        if(direction == null) {
            logger.error("Invalid move direction {}", direction);
            throw new Exception(Protocol.MESSAGE_INVALID_ARGUMENTS);
        }

        Character character = players.get(player);
        character.setNextMove(direction);
    }

}
