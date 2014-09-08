package sma.pacman.game.character;

public class EnemyCharacter extends Character {
    public EnemyCharacter(String name, String color) {
        super(name);

        String path = "enemy/" + color;
        loadAnimations(path, 2, 0.2f);
    }

    @Override
    public void setHeroBoost(Boolean state) {
        super.setHeroBoost(state);

        if(state) {
            String path = "enemy/blue";
            loadAnimations(path, 2, 0.2f);
        }
    }
}
