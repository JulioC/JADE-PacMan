package sma.pacman.game.character;

public class EnemyCharacter extends Character {
    public EnemyCharacter(String color) {
        String name = "enemy/" + color;
        loadAnimations(name, 2, 0.2f);
    }
}
