import java.util.*;

public class PokeballMystery {

    public static Pokeball selectRandomBall(Scanner sc, Player player) {
        List<Pokeball> pokeballs = Arrays.asList(
            Pokeball.POKEBALL,
            Pokeball.GREATBALL,
            Pokeball.ULTRABALL,
            Pokeball.MASTERBALL
        );
        Collections.shuffle(pokeballs);

        
        char[] labels = {'A', 'B', 'C', 'D'};
        Map<Character, Pokeball> hiddenChoices = new HashMap<>();
        for (int i = 0; i < 4; i++) {
            hiddenChoices.put(labels[i], pokeballs.get(i));
        }

        System.out.println("\n🎲 Choose a mystery Poké Ball (A, B, C, or D): ");
        Pokeball selectedBall = null;
        char userChoice;

        while (true) {
            String input = sc.next().toUpperCase();
            if (input.length() == 1 && hiddenChoices.containsKey(input.charAt(0))) {
                userChoice = input.charAt(0);
                selectedBall = hiddenChoices.get(userChoice);
                System.out.println("🎯 You chose: " + userChoice + " ➤ " + selectedBall.name());
                break;
            } else {
                System.out.println("❌ Invalid choice. Choose A, B, C, or D:");
            }
        }

        // Apply Miracle Item bonus
        if (player.isMiracleActive("Poke Ball Power")) {
            switch (selectedBall) {
                case POKEBALL:
                    selectedBall = Pokeball.GREATBALL;
                    break;
                case GREATBALL:
                    selectedBall = Pokeball.ULTRABALL;
                    break;
                case ULTRABALL:
                case MASTERBALL:
                    selectedBall = Pokeball.MASTERBALL;
                    break;
            }
            System.out.println("✨ Miracle Item boosted your Poké Ball to ➤ " + selectedBall.name());
        }

        return selectedBall;
    }
}
