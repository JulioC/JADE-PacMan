package sma.pacman.game.character;

public class HeroCharacter extends Character {

    public HeroCharacter(String name, Boolean alt) {
        super(name);

        String path = "hero/" + (alt ? "female" : "male");
        loadAnimations(path, 3, 0.2f);
    }

}
