import java.util.*;

public class CatchTime {
    public static void start(Player player, List<Pokemon> pool, Scanner sc, Stage currentStage) {
        if (pool.size() < 3) {
            System.out.println("Not enough Pokémon in the wild pool.");
            return;
        }

        System.out.println("\n🎯 === CATCH TIME ===");
        System.out.println("📍 Current Stage: " + currentStage.getDisplayName() +
                " | Catch Modifier: x" + currentStage.getCatchModifier());
        System.out.println("Choose 1 of the 3 wild Pokémon below:");

        List<Pokemon> options = new ArrayList<>();
        Random rand = new Random();
        while (options.size() < 3) {
            Pokemon candidate = pool.get(rand.nextInt(pool.size()));
            boolean exists = options.stream().anyMatch(p -> p.getName().equals(candidate.getName()));
            if (!exists) options.add(candidate);
        }

        System.out.println("------------------------------");
        for (int i = 0; i < 3; i++) {
            System.out.printf(" %d. %s\n", i + 1, options.get(i).getStats());
        }
        System.out.println("------------------------------");

        int choice = -1;
        while (choice < 1 || choice > 3) {
            System.out.print("Choose your Pokémon (1-3): ");
            try {
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                sc.next(); // clear invalid input
                System.out.println("⚠️ Please enter a valid number.");
            }
        }

        Pokemon selected = options.get(choice - 1);
        Pokemon caughtPokemon = new Pokemon(selected.getName(), 100, selected.getMoveType(), selected.getDefenderType(), selected.getRarityStars());

        // Mystery Poké Ball Selection (A–D)
        List<Pokeball> pokeballs = Arrays.asList(
            Pokeball.POKEBALL,
            Pokeball.GREATBALL,
            Pokeball.ULTRABALL,
            Pokeball.MASTERBALL
        );
        Collections.shuffle(pokeballs);

        char[] labels = {'A', 'B', 'C', 'D'};
        Map<Character, Pokeball> hiddenChoices = new HashMap<>();
        for (int j = 0; j < 4; j++) {
            hiddenChoices.put(labels[j], pokeballs.get(j));
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

        // Apply Miracle Item Boost
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

        double finalChance = selectedBall.getSuccessRate() * currentStage.getCatchModifier();
        System.out.printf("📊 Estimated Catch Chance: %.2f%%\n", finalChance * 100);

        boolean caught = Math.random() < finalChance;

        if (caught) {
            System.out.println("✅ Success! You caught " + caughtPokemon.getName());
            caughtPokemon.restoreHp();
            player.addPokemon(caughtPokemon);
        } else {
            System.out.printf("❌ It escaped! (Final chance was %.2f%%)\n", finalChance * 100);
        }
    }
}
