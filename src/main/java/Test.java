import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sma.pacman.agents.Protocol;
import sma.pacman.agents.game.GameAgent;
import sma.pacman.agents.player.GhostPlayerAgent;
import sma.pacman.agents.player.RandomPlayerAgent;
import sma.pacman.agents.player.PlayerAgent;

public class Test {
    private static final Logger logger = LogManager.getLogger(Test.class.getName());

    public static void main(String[] args) {
        startMainContainer("localhost", Profile.LOCAL_PORT, "PACMAN");

        addAgent(containerController, "game", GameAgent.class.getName(), null);

        addAgent(containerController, "pacman", PlayerAgent.class.getName(),
                new Object[]{Protocol.PLAYER_HERO, Protocol.PLAYER_HERO_MALE});

        addAgent(containerController, "cyan-ghost", GhostPlayerAgent.class.getName(),
                new Object[]{Protocol.PLAYER_ENEMY, Protocol.PLAYER_ENEMY_CYAN});

        addAgent(containerController, "red-ghost", RandomPlayerAgent.class.getName(),
                new Object[]{Protocol.PLAYER_ENEMY, Protocol.PLAYER_ENEMY_RED});

        addAgent(containerController, "pink-ghost", RandomPlayerAgent.class.getName(),
                new Object[]{Protocol.PLAYER_ENEMY, Protocol.PLAYER_ENEMY_PINK});

        addAgent(containerController, "rma", jade.tools.rma.rma.class.getName(), null);

        addAgent(containerController, "Sniffer", "jade.tools.sniffer.Sniffer",
                new Object[]{"game", ";", "pacman", ";", "red-ghost", ";", "cyan-ghost", ";", "pink-ghost"});
    }

    static ContainerController containerController;
    static AgentController agentController;


    public static void startMainContainer(String host, String port, String name) {
        jade.core.Runtime runtime = jade.core.Runtime.instance();
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST, host);
        profile.setParameter(Profile.MAIN_PORT, port);
        profile.setParameter(Profile.PLATFORM_ID, name);

        containerController = runtime.createMainContainer(profile);
    }

    public static void addAgent(ContainerController cc, String agent, String classe, Object[] args) {
        try {
            agentController = cc.createNewAgent(agent, classe, args);
            agentController.start();
        } catch (StaleProxyException s) {
            s.printStackTrace();
        }
    }



}
