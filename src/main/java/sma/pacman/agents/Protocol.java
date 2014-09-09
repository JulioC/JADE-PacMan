package sma.pacman.agents;

import jade.lang.acl.ACLMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.util.Scanner;

public abstract class Protocol {

    private static final Logger logger = LogManager.getLogger(Protocol.class.getName());

    public static final String SEPARATOR = ";";

    public static final String SERVICE_TYPE_PLAY = "play";
    public static final String GAME_NAME = "pacman";

    public static final String ACTION_JOIN = "join";
    public static final String ACTION_MAP = "map";
    public static final String ACTION_GAME_EVENT = "game event";
    public static final String ACTION_CHARACTER_EVENT = "character event";
    public static final String ACTION_MOVE = "move";
    public static final String ACTION_CAN_MOVE = "can move";

    public static final String PLAYER_HERO = "hero";
    public static final String PLAYER_ENEMY = "enemy";

    public static final String PLAYER_HERO_MALE = "male";
    public static final String PLAYER_HERO_FEMALE = "female";

    public static final String PLAYER_ENEMY_CYAN = "cyan";
    public static final String PLAYER_ENEMY_PINK = "pink";
    public static final String PLAYER_ENEMY_RED = "red";
    public static final String PLAYER_ENEMY_YELLOW = "yellow";

    public static final String MESSAGE_OK = "ok";
    public static final String MESSAGE_INVALID_ARGUMENTS = "invalid arguments";
    public static final String MESSAGE_NO_FREE_SLOTS = "no free slots";
    public static final String MESSAGE_PLAYER_ALREADY_IN = "player already in game";
    public static final String MESSAGE_PLAYER_NOT_IN = "player not in game";

    public static final String EVENT_GAME_STARTED = "game started";
    public static final String EVENT_ROUND_UPDATE = "round update";

    public static String build(String... args) {
        Boolean first = true;
        StringBuilder result = new StringBuilder();
        for(String arg: args) {
            if(first) {
                first = false;
            }
            else {
                result.append(SEPARATOR);
            }

            result.append(arg);
        }

        return result.toString();
    }

    public static String[] parseArguments(ACLMessage msg) {
        return msg.getContent().split(Protocol.SEPARATOR);
    }

    public static String buildMap(Integer[][] map) {
        StringBuilder sb = new StringBuilder();

        Integer width = map.length;
        Integer height = map[0].length;

        sb.append(width);
        sb.append(" ");
        sb.append(height);

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; ++x) {
                sb.append(" ");
                sb.append(map[x][y]);
            }
        }

        return sb.toString();
    }

    public static Integer[][] parseMap(String mapString) {
        Scanner scanner = new Scanner(mapString);

        Integer[][] map;
        try {
            Integer width = scanner.nextInt();
            Integer height = scanner.nextInt();

            map = new Integer[width][height];

            for(int y = 0; y < height; ++y) {
                for(int x = 0; x < width; x++) {
                    map[x][y] = scanner.nextInt();
                }
            }
        }
        catch (Exception e) {
            return null;
        }

        return map;
    }
}
