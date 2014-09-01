package sma.pacman.game.character;

public class HeroCharacter extends Character {
    public HeroCharacter(Boolean alt) {
        String name = "hero/" + (alt ? "female" : "male");
        loadAnimations(name, 3, 0.2f);
    }
}
