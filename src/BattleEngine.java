import java.util.*;

public class BattleEngine {

    public static int start(Player player, List<Pokemon> wildPool, Scanner sc, Stage currentStage) {
        Random rand = new Random();

        if (player.getCollection().size() < 2) {
            System.out.println("⚠️ You need at least 2 Pokémon to battle.");
            return -1;
        }

        //  Prompt for Miracle Item use if eligible
        if (player.getMedalCount() >= 3) {
            player.grantMiracleItem(sc); 
        }
        //  Generate 2 Wild Enemies
        List<Pokemon> enemies = new ArrayList<>();
        while (enemies.size() < 2) {
            Pokemon wild = wildPool.get(rand.nextInt(wildPool.size()));
            boolean exists = enemies.stream().anyMatch(p -> p.getName().equals(wild.getName()));
            if (!exists) {
                int baseHp = 100 + (wild.getRarityStars() - 1) * 25;
                enemies.add(new Pokemon(wild.getName(), baseHp, wild.getMoveType(), wild.getDefenderType(), wild.getRarityStars()));
            }
        }

        System.out.println("\n🔥 Wild Pokémon appeared!");

        for (int i = 0; i < enemies.size(); i++) {
            System.out.printf(" %d. %s\n", i + 1, enemies.get(i).getStats());
        }

        // Choose 2 Pokemon from player collection
        System.out.println("\n️Choose 2 of your Pokémon for battle:");
        player.showCollection();
        List<Pokemon> team = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            while (true) {
                System.out.print(" Select Pokémon " + (i + 1) + ": ");
                try {
                    int choice = sc.nextInt();
                    sc.nextLine();
                    if (choice >= 1 && choice <= player.getCollection().size()) {
                        Pokemon selected = player.getCollection().get(choice - 1);
                        team.add(selected);
                        break;
                    } else {
                        System.out.println("❌ Invalid selection. Try again.");
                    }
                } catch (InputMismatchException e) {
                    sc.next();
                    System.out.println("⚠️ Please enter a valid number.");
                }
            }
        }

        // Optional Heal Before Battle
        String healChoice;
        while (true) {
            System.out.print("🩺 Would you like to heal all your Pokémon before battle? (yes/no): ");
            healChoice = sc.nextLine().trim().toLowerCase();
            if (healChoice.equals("yes") || healChoice.equals("no")) break;
            System.out.println("❗ Please type 'yes' or 'no'.");
        }

        if (healChoice.equals("yes")) {
            if (player.getPotions() > 0) {
                player.healAllPokemon();
                player.usePotion();
                System.out.println("✅ All your Pokémon have been healed.");
            } else {
                System.out.println("❌ You have no Potions to use.");
            }
        }

        // Show Active Miracle Item
        player.remindActiveMiracleItem();
        // Battles
        int totalDamage = 0;
        int caught = 0;

        for (int i = 0; i < enemies.size(); i++) {
            Pokemon enemy = enemies.get(i);
            Pokemon ally = team.get(i);
            System.out.println("\n⚔️ Battle: " + ally.getName() + " vs. " + enemy.getName());

            while (!enemy.isFainted() && !ally.isFainted()) {
                String prompt = TypingChallenge.getPromptForStage(currentStage);

                System.out.println("🧠 TYPE THIS ASAP TO ATTACK:");
                System.out.println(">> " + prompt);
                System.out.print(">> ");

                long start = System.currentTimeMillis();
                String typed = sc.nextLine();
                long end = System.currentTimeMillis();

                double time = (end - start) / 1000.0;
                int baseAttack = 10 + (ally.getRarityStars() - 1) * 5;
                boolean correct = typed.trim().equals(prompt.trim());

                int bonus = correct ? (int)(150 / Math.max(1, time)) : -5;

                if (correct) {
                    System.out.printf("⚡ Typed in %.2f seconds! Speed bonus: +%d\n", time, bonus);
                } else {
                    System.out.println("❌ Incorrect typing! No speed bonus.");
                }

                int dmg = baseAttack + bonus;
                if (player.isMiracleActive("Attack Capsule")) dmg += 10;

                dmg *= TypeChart.getEffectiveness(ally.getMoveType(), enemy.getDefenderType());
                enemy.takeDamage(Math.max(0, dmg));
                totalDamage += dmg;
                System.out.printf(" %s deals %d damage ➤ %s HP: %d\n", ally.getName(), dmg, enemy.getName(), enemy.getHp());

                if (enemy.isFainted()) break;

                // Enemy Counter-Attack
                double factor = 1.0 + (currentStage.getRarityStars() - 1) * 0.25;
                int enemyDmg = (int)(30 * factor * TypeChart.getEffectiveness(enemy.getMoveType(), ally.getDefenderType()));
                if (player.isMiracleActive("Defense Capsule")) enemyDmg -= 5;

                ally.takeDamage(Math.max(0, enemyDmg));
                System.out.printf(" %s deals %d damage ➤ %s HP: %d\n", enemy.getName(), enemyDmg, ally.getName(), ally.getHp());
            }

            // Post-Battle Rewards
            if (enemy.isFainted()) {
                System.out.println("\n🎉 You defeated " + enemy.getName() + "!");
                player.handleBattleRewards(currentStage);

                //  Catch Attempt
                int fullHp = 100 + (enemy.getRarityStars() - 1) * 25;
                Pokemon caughtPokemon = new Pokemon(enemy.getName(), fullHp, enemy.getMoveType(), enemy.getDefenderType(), enemy.getRarityStars());
                caughtPokemon.restoreHp();

                Pokeball selectedBall = PokeballMystery.selectRandomBall(sc, player);
                sc.nextLine();
                double baseRate = selectedBall.getSuccessRate();
                double stageMod = currentStage.getCatchModifier();
                double finalRate = baseRate * stageMod;

                if (player.isMiracleActive("Poke Ball Power")) finalRate += 0.3;

                if (Math.random() < finalRate) {
                    System.out.println("✅ Success! You caught " + caughtPokemon.getName());
                    player.addPokemon(caughtPokemon);
                    caught++;
                } else {
                    System.out.println("❌ It escaped!");
                }

            } else {
                System.out.println("💀 Your " + ally.getName() + " fainted!");
            }
        }

        player.endBattleReset();

        // Final Score + Coin Reward
        int score = totalDamage + caught * 50;
        System.out.println("\n==============================");
        System.out.println("🏁 BATTLE FINISHED");
        System.out.printf("🎯 Caught Pokémon: %d\n", caught);
        System.out.printf("⚔️ Total Damage Dealt: %d\n", totalDamage);
        System.out.printf("💯 Total Score: %d\n", score);
        System.out.println("==============================");

        int coinReward = currentStage.getRarityStars(); // 1 for Beginner, 2 for Intermediate, etc.
        player.addCoins(coinReward);
        System.out.printf("💰 You earned %d coin(s) for this battle!\n", coinReward);

        return score;
    }
}
