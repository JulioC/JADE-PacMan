package sma.pacman.agents;

public abstract class Protocol {
    public static final String SEPARATOR = ";";

    public static final String SERVICE_TYPE_PLAY = "play";
    public static final String GAME_NAME = "pacman";

    public static final String ACTION_JOIN = "join";
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
    public static final String EVENT_ROUND_UPDATE = "game started";

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
}
