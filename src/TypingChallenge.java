// TypingChallenge.java
import java.util.*;

public class TypingChallenge {

    private static final String[] EASY = {
        "Catch them all with a Poke Ball",
        "Critical hit landed",
        "Keep your Pokemon strong",
        "Bulbasaur loves the sunshine",
        "Pikachu uses Thunderbolt"
    };

    private static final String[] MEDIUM = {
        "Team Rocket blasts off at the speed of light",
        "Pokemon trainers never give up",
        "Victory belongs to the fastest",
        "Trainers battle in the Pokemon League",
        "Courage brings strength in battle"
    };

    private static final String[] HARD = {
        "Every great Pokemon master was once a rookie who refused to give up after defeat",
        "Even the smallest spark of courage can ignite a legendary victory in the heat of battle",
        "Each battle is a step forward, a chance to grow stronger and closer to your team",
        "A true master understands the heart of their Pokemon, not just the stats or types they carry",
        "Every experience brings your team closer to becoming champions of the Pokemon world"
    };

    public static String getPromptForStage(Stage stage) {
        Random rand = new Random();
        switch (stage) {
            case BEGINNER:
                return EASY[rand.nextInt(EASY.length)];
            case ADVANCED:
                return MEDIUM[rand.nextInt(MEDIUM.length)];
            case EXPERT:
                return HARD[rand.nextInt(HARD.length)];
            default:
                return EASY[rand.nextInt(EASY.length)];
        }
    }
}
