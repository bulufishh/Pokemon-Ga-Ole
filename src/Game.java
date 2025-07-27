import java.util.*;
import java.io.*;

public class Game {
    private static final String SCORE_FILE = "scores.txt";
    private static final String PLAYER_DATA_FILE = "playerdata.txt";
    private static List<Pokemon> globalWildPool;

    public static void start() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("🪙 Insert Coin to Start (type 1): ");
        while (true) {
            if (scanner.hasNextInt()) {
                int input = scanner.nextInt();
                if (input == 1) {
                    break;
                } else {
                    System.out.println("❌ Invalid input. Please insert coin (type 1): ");
                }
            } else {
                scanner.next();
                System.out.println("❌ Invalid input. Please insert coin (type 1): ");
            }
        }

        System.out.print("🎮 Enter your name: ");
        String playerName = scanner.next();
        Player player = new Player(playerName);
        player.loadPlayerData(PLAYER_DATA_FILE);

        Stage currentStage = selectStage(scanner);
        globalWildPool = PokemonDatabase.generateWildPool(currentStage);

        System.out.println("📍 Switched to Stage: " + currentStage.getDisplayName());

        boolean playing = true;

        System.out.println("\n==============================");
        System.out.println("🎮 Welcome to Pokémon Ga-Olé!");
        System.out.println("==============================");

        while (playing) {
            System.out.println("\n📍 Current Stage: " + currentStage.getDisplayName());
            player.grantMiracleItem(scanner);

            System.out.println("\n ===== MAIN MENU =====");
            System.out.println("🏅 Medals: " + player.getMedals());
            System.out.println("🧪 Potions available: " + player.getPotions());
            System.out.println("❗Tips: Medals are collected from Golden Chip and Golden Sand which are chances dropped from defeated Pokémons");
            System.out.println("❗Tips: You get a miracle item chance for every 3 medals collected");
            if (player.hasUnclaimedMiracle()) {
                System.out.println("⚡ Miracle Item available! Activate it before battle.");
            } else if (player.getActiveMiracleItem() != null) {
                String activeItem = player.getActiveMiracleItem() != null ? player.getActiveMiracleItem() : "none";
                System.out.println("💫 Active Miracle Item: " + activeItem);
            }

            System.out.println("\n=== You will need at least 2 Pokémon to Battle ===\n");
            System.out.println("0: Buy potion (1 coin)");
            System.out.println("1: Catch Time");
            System.out.println("2: Battle");
            System.out.println("3: View Disk Collection");
            System.out.println("4: View Top Scores");
            System.out.println("5: View Miracle Items");
            System.out.println("6: Exit");
            System.out.println("7: Reset Game");
            System.out.println("8: Release Pokémon");
            System.out.println("9: Heal All Pokémon (1 potion)");
            System.out.printf("🧪 Potions: %d | 💰 Coins: %d\n", player.getPotions(), player.getCoins());
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                scanner.next();
                System.out.println("⚠️ Please enter a valid number.");
                continue;
            }

            switch (choice) {
                case 0:
                    player.buyPotion();
                    break;
                case 1:
                    CatchTime.start(player, globalWildPool, scanner, currentStage);
                    break;
                case 2:
                    scanner.nextLine(); 
                    player.remindActiveMiracleItem(); // shows current active item
                    player.activateMiracleItem(scanner); // Already prints effect inside Player.java


                    // Start battle
                    int score = BattleEngine.start(player, globalWildPool, scanner, currentStage);

                    // Save score
                    if (score >= 0) {
                        saveScore(score);
                    }

                    // Stage selection after battle
                    while (true) {
                        System.out.print("🔁 Do you want to continue playing in the same stage? (yes/no): ");
                        String cont = scanner.next().trim().toLowerCase();
                        if (cont.equals("yes")) {
                            break;
                        } else if (cont.equals("no")) {
                            currentStage = selectStage(scanner);
                            globalWildPool = PokemonDatabase.generateWildPool(currentStage);
                            break;
                        } else {
                            System.out.println("⚠️ Please enter 'yes' or 'no'.");
                        }
                    }
                    break;

                case 3:
                    player.showCollection();
                    break;
                case 4:
                    showTopScores();
                    break;
                case 5:
                    player.showMiracleItems();
                    break;
                case 6:
                    player.savePlayerData(PLAYER_DATA_FILE);
                    playing = false;
                    System.out.println("👋 Thanks for playing!");
                    break;
                case 7:
                    System.out.print("⚠️ Are you sure you want to reset all data? (yes/no): ");
                    String confirm = scanner.next().trim().toLowerCase();
                    if (confirm.equals("yes")) {
                        resetGameData();
                        System.out.println("🗑️ All game data has been deleted. Restarting game...");
                        System.out.print("🎮 Enter your name: ");
                        playerName = scanner.next();
                        player = new Player(playerName);
                        currentStage = selectStage(scanner);
                        globalWildPool = PokemonDatabase.generateWildPool(currentStage);
                    } else {
                        System.out.println("✅ Reset cancelled.");
                    }
                    break;
                case 8:
                    player.releasePokemon(scanner);
                    break;
                case 9:
                    if (player.getPotions() > 0) {
                        player.healAllPokemon();  // Heal all Pokémon
                        player.usePotion();       // Consume potion
                        System.out.println("✅ All your Pokémon have been healed.");
                    } else {
                        System.out.println("⚠️ You don't have enough Potions.");
                    }
                    break;
                default:
                    System.out.println("⚠ Invalid option.");
            }
        }

        scanner.close();
    }

    public static void determineDropRewards(Stage stage, Player player) {
        double rand = Math.random();
        boolean gotSand = false;
        boolean gotChip = false;

        switch (stage) {
            case BEGINNER:
                if (rand < 0.20) {
                    if (Math.random() < 0.8) gotSand = true;
                    else gotChip = true;
                }
                break;
            case ADVANCED:
                if (rand < 0.40) {
                    double inner = Math.random();
                    if (inner < 0.6) gotSand = true;
                    else if (inner < 0.9) gotChip = true;
                    else {
                        gotSand = true;
                        gotChip = true;
                    }
                }
                break;
            case EXPERT:
                if (rand < 0.70) {
                    double inner = Math.random();
                    if (inner < 0.4) gotSand = true;
                    else if (inner < 0.7) gotChip = true;
                    else {
                        gotSand = true;
                        gotChip = true;
                    }
                }
                break;
        }

        if (gotSand) {
            System.out.println("✨ You got a Golden Sand!");
            player.addMedalProgress("Golden Sand");
        }
        if (gotChip) {
            System.out.println("✨ You got a Golden Chip!");
            player.addMedalProgress("Golden Chip");
        }
    }

    private static Stage selectStage(Scanner scanner) {
        System.out.println("\n🏆 Choose a Stage:");
        for (int i = 0; i < Stage.values().length; i++) {
            Stage stage = Stage.values()[i];
            System.out.printf(" %d. %s (⭐ %d, Catch Modifier: %.2f)\n",
                    i + 1, stage.getDisplayName(), stage.getRarityStars(), stage.getCatchModifier());
        }

        while (true) {
            System.out.print("Enter stage number (1-" + Stage.values().length + "): ");
            try {
                int input = scanner.nextInt();
                if (input >= 1 && input <= Stage.values().length) {
                    Stage chosen = Stage.values()[input - 1];
                    System.out.println("✅ You chose: " + chosen.getDisplayName());
                    return chosen;
                } else {
                    System.out.println("❌ Invalid number.");
                }
            } catch (InputMismatchException e) {
                scanner.next();
                System.out.println("⚠️ Please enter a valid number.");
            }
        }
    }

    private static void resetGameData() {
        File playerFile = new File(PLAYER_DATA_FILE);
        File scoreFile = new File(SCORE_FILE);

        if (playerFile.exists()) playerFile.delete();
        if (scoreFile.exists()) scoreFile.delete();
    }

    private static void saveScore(int score) {
        try {
            File file = new File(SCORE_FILE);
            List<Integer> scores = new ArrayList<>();
            if (file.exists()) {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextInt()) {
                    scores.add(scanner.nextInt());
                }
                scanner.close();
            }
            scores.add(score);
            scores.sort(Collections.reverseOrder());
            if (scores.size() > 5) {
                scores = scores.subList(0, 5);
            }
            PrintWriter writer = new PrintWriter(file);
            for (int s : scores) {
                writer.println(s);
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("❌ Error saving score.");
        }
    }

    private static void showTopScores() {
        try {
            File file = new File(SCORE_FILE);
            if (!file.exists()) {
                System.out.println("No scores recorded yet.");
                return;
            }
            Scanner scanner = new Scanner(file);
            System.out.println("\n📊 ===== TOP 5 SCORES =====");
            int rank = 1;
            while (scanner.hasNextInt()) {
                System.out.printf(" %d. %d pts\n", rank++, scanner.nextInt());
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("❌  Error reading scores.");
        }
        System.out.println("📂 Game saved to '" + PLAYER_DATA_FILE + "'.");
    }
    
}
