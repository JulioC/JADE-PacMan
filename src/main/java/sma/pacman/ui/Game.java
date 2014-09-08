package sma.pacman.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sma.pacman.game.Board;
import sma.pacman.game.character.Character;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class Game extends JFrame {

    private static final Logger logger = LogManager.getLogger(Game.class.getName());

    private static final Integer BOARD_ZOOM = 2;

    private Board board;

    private JPanel sidePanel;

    private List<CharacterPanel> characterPanels = new ArrayList<CharacterPanel>();

    public Game(Board board) throws HeadlessException {
        super("PacMan");

        this.board = board;

        board.addListener(new Board.Listener() {
            @Override
            public void characterAdded(Character character) {
                addCharacterPanel(character);
            }

            @Override
            public void roundUpdate(Integer currentRound) {
                updateInfo();
            }
        });

        setupUI();
    }

    private void setupUI() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        setResizable(false);

        getContentPane().add(board);

        int width = BOARD_ZOOM * board.getPreferredSize().width;
        int height = BOARD_ZOOM * board.getPreferredSize().height;
        board.setPreferredSize(new Dimension(width, height));

        sidePanel = new JPanel(new FlowLayout());

        getContentPane().add(new JScrollPane(sidePanel));

        sidePanel.setPreferredSize(new Dimension(240, height));

        pack();
    }

    private void addCharacterPanel(Character character) {
        CharacterPanel characterPanel = new CharacterPanel(character);
        characterPanels.add(characterPanel);

        sidePanel.add(characterPanel);

        revalidate();
        repaint();

        pack();
    }

    private void updateInfo() {
        for(CharacterPanel characterPanel: characterPanels) {
            characterPanel.updateInfo();
        }
    }

}
