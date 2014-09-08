package sma.pacman.ui;

import sma.pacman.game.Direction;
import sma.pacman.game.character.Character;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class CharacterPanel extends JPanel {

    Character character;

    JTextField fieldName;
    JTextField fieldScore;
    JComboBox fieldMove;

    public CharacterPanel(Character character) {
        this.character = character;

        this.setLayout(new GridLayout(0, 2));

        addComponents();

        character.addListener(new Character.Listener() {
            @Override
            public void nextMoveSet(Direction move) {
                updateInfo();
            }
        });
    }

    private void addComponents() {
        fieldName = new JTextField(character.getName());
        fieldName.setEditable(false);
        this.add(new JLabel("Name:"));
        this.add(fieldName);

        fieldScore = new JTextField("0");
        fieldScore.setEditable(false);
        this.add(new JLabel("Score:"));
        this.add(fieldScore);

        fieldMove = new JComboBox(Direction.values());
        fieldMove.insertItemAt("NONE", 0);
        fieldMove.setSelectedIndex(0);
        fieldMove.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    Object item = e.getItem();
                    if(item instanceof Direction) {
                        character.setNextMove((Direction) item);
                    }
                    else {
                        character.setNextMove(null);
                    }
                }
            }
        });
        this.add(new JLabel("Move:"));
        this.add(fieldMove);
    }

    public void updateInfo() {
        fieldScore.setText(character.getScore().toString());

        Direction move = character.getNextMove();
        if(move == null) {
            fieldMove.setSelectedIndex(0);
        }
        else {
            fieldMove.setSelectedItem(move);
        }
    }

}
