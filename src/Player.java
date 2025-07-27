import java.util.*;
import java.io.*;
import java.util.Scanner;


public class Player {
    private String name;
    private ArrayList<Pokemon> collection;
    private ArrayList<Pokemon> selectedTeam;
    private int coins;
    private int potions;
    private int medals;
    private ArrayList<String> miracleItems;
    private String activeMiracleItem;
    private boolean skippedMiracleItemThisBattle;

    public Player(String name) {
        this.name = name;
        this.collection = new ArrayList<>();
        this.selectedTeam = new ArrayList<>();
        this.coins = 0;
        this.potions = 0;
        this.medals = 0;
        this.miracleItems = new ArrayList<>();
        this.activeMiracleItem = null;
        this.skippedMiracleItemThisBattle = false;
    }

    // === Getters ===
    public String getName() { return name; }
    public int getCoins() { return coins; }
    public int getPotions() { return potions; }
    public int getMedals() { return medals; }
    public ArrayList<String> getMiracleItems() { return miracleItems; }
    public String getActiveMiracleItem() { return activeMiracleItem; }
    public boolean hasMiracleItems() { return !miracleItems.isEmpty(); }
    public boolean hasActiveMiracle() { return activeMiracleItem != null; }
    public boolean isMiracleActive(String itemName) {
    	return itemName != null && activeMiracleItem != null && itemName.equalsIgnoreCase(activeMiracleItem);
    }
 // Returns current number of Ga-Olé Medals
    public int getMedalCount() {
        return medals;
    }

    public void addCoins(int amount) {
        coins += amount;
    }
    public void addMedalProgress(String source) {
        addMedal();
    }

    // === Currency & Items ===
    public void addCoin() { coins++; }
    public void addPotion() { potions++; }
    public void addMedal() { medals++; }

    public boolean usePotion() {
        if (potions > 0) {
            potions--;
            return true;
        }
        return false;
    }

    public void usePotion(Pokemon p) {
        if (potions > 0 && !p.isFainted()) {
            p.restoreHp();
            potions--;
            System.out.println(p.getName() + " healed to " + p.getMaxHp() + " HP.");
        } else {
            System.out.println("❌ Cannot use Potion.");
        }
    }

    public void healAllPokemon() {
        if (potions <= 0) {
            System.out.println("❌ No potions available.");
            return;
        }

        boolean healed = false;
        for (Pokemon p : collection) {
            if (p.getHp() < p.getMaxHp()) {
                p.restoreHp();
                healed = true;
            }
        }
        if (healed) {
            potions--;
            System.out.println("🩺 All your Pokémon were healed using 1 potion.");
        } else {
            System.out.println("🩷 Your Pokémon are already at full health.");
        }
    }

    public void buyPotion() {
        if (coins >= 1) {
            potions++;
            coins--;
            System.out.println("🧪 Potion bought!");
        } else {
            System.out.println("❌ Not enough coins.");
        }
    }

    // === Miracle Items ===
    public void checkMiracleExchange(Scanner sc) {
        while (medals >= 3) {
            System.out.print("🎖️ You have " + medals + " Ga-Olé Medals. Exchange 3 for a Miracle Item? (yes/no): ");
            String input = sc.nextLine().trim().toLowerCase();
            while (!input.equals("yes") && !input.equals("no")) {
                System.out.print("Please enter 'yes' or 'no': ");
                input = sc.nextLine().trim().toLowerCase();
            }
            if (input.equals("yes")) {
                chooseOneMiracleItem(sc);
                medals -= 3;
                System.out.println("✅ Miracle Item claimed! Remaining medals: " + medals);
            } else {
                break;
            }
        }
    }

    public void chooseOneMiracleItem(Scanner sc) {
        String[] options = {"Attack Capsule", "Defense Capsule", "Poké Ball Power"};
        System.out.println("Choose ONE Miracle Item:");
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ") " + options[i]);
        }

        int choice = 0;
        while (choice < 1 || choice > 3) {
            System.out.print("Enter choice (1-3): ");
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                choice = 0;
            }
        }

        String selected = options[choice - 1];
        miracleItems.add(selected);
        System.out.println("✅ " + selected + " added to your Miracle Items!");
    }
    
    public boolean hasUnclaimedMiracle() {
        return medals >= 3;
    }

    public void remindActiveMiracleItem() {
        if (activeMiracleItem != null) {
            System.out.println("💫 Active Miracle Item: " + activeMiracleItem);
            switch (activeMiracleItem) {
                case "Attack Capsule":
                    System.out.println("→ Your Pokémon's attack power is boosted this round.");
                    break;
                case "Defense Capsule":
                    System.out.println("→ Your Pokémon takes reduced damage this round.");
                    break;
                case "Poké Ball Power":
                    System.out.println("→ Your Poké Balls have higher catch success this round.");
                    break;
            }
        } else {
            System.out.println("💫 Active Miracle Item: none");
        }
    }

    public void endBattleReset() {
        activeMiracleItem = null;
        skippedMiracleItemThisBattle = false;
    }

    public void handleBattleRewards(Stage currentStage) {
        boolean droppedGoldenChip = false;
        boolean droppedGoldenSand = false;

        double baseRate = 0.05;
        int stageStars = currentStage.getRarityStars();

        double chipChance = switch (stageStars) {
            case 2 -> 0.05;   // Beginner
            case 4 -> 0.15;   // Advanced
            case 5 -> 0.3;    // Expert
            default -> baseRate;
        };

        double sandChance = switch (stageStars) {
            case 2 -> 0.05;
            case 4 -> 0.15;
            case 5 -> 0.3;
            default -> baseRate;
        };

        if (Math.random() < chipChance) {
            droppedGoldenChip = true;
            System.out.println("🔶 You obtained a Golden Chip!");
        }

        if (Math.random() < sandChance) {
            droppedGoldenSand = true;
            System.out.println("🌟 You obtained Golden Sand!");
        }

        if (droppedGoldenChip || droppedGoldenSand) {
            addMedal();
            System.out.println("🎖️ You received a Ga-Olé Medal!");
        }

        if (Math.random() < 0.3) {
            addPotion();
            System.out.println("🧪 You received a Potion!");
        }
    }

    // === Pokémon Management ===
    public void addPokemon(Pokemon p) { collection.add(p); }
    public ArrayList<Pokemon> getCollection() { return collection; }

    public void showCollection() {
        System.out.println("\n📀 Your Disk Collection:");
        if (collection.isEmpty()) {
            System.out.println("No Pokémon in collection.");
            return;
        }

        for (int i = 0; i < collection.size(); i++) {
            Pokemon p = collection.get(i);
            System.out.printf("%d) %s | ⭐ %d | HP: %d/%d %s\n",
                    i + 1,
                    p.getName(),
                    p.getRarityStars(),
                    p.getHp(),
                    p.getMaxHp(),
                    p.isFainted() ? "(💀 Fainted)" : ""
            );
        }
    }

    public void releasePokemon(Scanner sc) {
        showCollection();
        if (collection.isEmpty()) {
            System.out.println("❌ You have no Pokémon to release.");
            return;
        }

        System.out.print("Enter the number of the Pokémon you want to release: ");
        try {
            int index = Integer.parseInt(sc.nextLine()) - 1;
            if (index >= 0 && index < collection.size()) {
                Pokemon removed = collection.remove(index);
                System.out.println("📤 " + removed.getName() + " has been released.");
            } else {
                System.out.println("❌ Invalid index.");
            }
        } catch (Exception e) {
            System.out.println("❌ Invalid input.");
        }
    }

    public void selectTeam(Scanner sc) {
        selectedTeam.clear();
        if (collection.size() < 2) {
            System.out.println("⚠️ You need at least 2 Pokémon to battle.");
            return;
        }

        System.out.println("Choose 2 Pokémon for battle:");
        for (int i = 0; i < collection.size(); i++) {
            Pokemon p = collection.get(i);
            System.out.printf("%d) %s (%d★, HP: %d/%d)%s\n",
                    i + 1,
                    p.getName(),
                    p.getRarityStars(),
                    p.getHp(),
                    p.getMaxHp(),
                    p.isFainted() ? " 💀 Fainted" : ""
            );
        }

        while (selectedTeam.size() < 2) {
            System.out.print("Select Pokémon #" + (selectedTeam.size() + 1) + ": ");
            try {
                int choice = Integer.parseInt(sc.nextLine()) - 1;
                if (choice >= 0 && choice < collection.size()
                        && !selectedTeam.contains(collection.get(choice))
                        && !collection.get(choice).isFainted()) {
                    selectedTeam.add(collection.get(choice));
                } else {
                    System.out.println("❌ Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("❌ Invalid input.");
            }
        }
    }

    public ArrayList<Pokemon> getSelectedTeam() { return selectedTeam; }

    public void showMiracleItems() {
        if (miracleItems.isEmpty()) {
            System.out.println("📦 You have no Miracle Items.");
        } else {
            System.out.println("📦 Miracle Items:");
            for (String item : miracleItems) {
                System.out.println("- " + item);
            }
        }
    }

    // === Save/Load ===
    public void savePlayerData(String filename) {
        try (PrintWriter writer = new PrintWriter(filename)) {
            writer.println("COINS:" + coins);
            writer.println("POTIONS:" + potions);
            writer.println("MEDALS:" + medals);
            for (Pokemon p : collection) {
                writer.println("POKEMON:" + p.toSaveString());
            }
            for (String item : miracleItems) {
                writer.println("ITEM:" + item);
            }
            if (activeMiracleItem != null) {
                writer.println("ACTIVE:" + activeMiracleItem);
            }
        } catch (IOException e) {
            System.out.println("❌ Error saving player data.");
        }
    }

    public void loadPlayerData(String filename) {
        try {
            File file = new File(filename);
            if (!file.exists()) return;

            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line.startsWith("COINS:")) {
                    coins = Integer.parseInt(line.substring(6));
                } else if (line.startsWith("POTIONS:")) {
                    potions = Integer.parseInt(line.substring(8));
                } else if (line.startsWith("MEDALS:")) {
                    medals = Integer.parseInt(line.substring(7));
                } else if (line.startsWith("POKEMON:")) {
                    Pokemon p = Pokemon.fromSaveString(line.substring(8));
                    collection.add(p);
                } else if (line.startsWith("ITEM:")) {
                    miracleItems.add(line.substring(5));
                } else if (line.startsWith("ACTIVE:")) {
                    activeMiracleItem = line.substring(7);
                }
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("❌ Error loading player data.");
        }
    }
    public void grantMiracleItem(Scanner sc) {
        if (skippedMiracleItemThisBattle || medals < 3) return;

        System.out.println("\n🎖️ You have " + medals + " Ga-Olé Medals.");
        System.out.print("💡 Would you like to exchange 3 medals for a Miracle Item? (yes/no): ");
        String input = sc.nextLine().trim().toLowerCase();

        while (!input.equals("yes") && !input.equals("no")) {
            System.out.print("Please enter 'yes' or 'no': ");
            input = sc.nextLine().trim().toLowerCase();
        }

        if (input.equals("yes")) {
            medals -= 3;

            String[] items = {"Attack Capsule", "Defense Capsule", "Poké Ball Power"};
            System.out.println("Choose a Miracle Item:");
            for (int i = 0; i < items.length; i++) {
                System.out.println((i + 1) + ") " + items[i]);
            }

            int choice = -1;
            while (choice < 1 || choice > items.length) {
                System.out.print("Enter the number of your choice: ");
                try {
                    choice = Integer.parseInt(sc.nextLine());
                } catch (Exception e) {
                    System.out.println("❗ Invalid input. Please enter a number.");
                }
            }

            String chosenItem = items[choice - 1];
            miracleItems.add(chosenItem);
            System.out.println("🎁 You received a Miracle Item: " + chosenItem + "!");
        } else {
            System.out.println("👉 You chose not to exchange medals this time.");
        }

        skippedMiracleItemThisBattle = true; // ✅ Prevent prompting again this round
    }
    public String activateMiracleItem(Scanner sc) {
        if (miracleItems.isEmpty()) return null;

        System.out.println("\n✨ You have the following Miracle Items:");
        for (int i = 0; i < miracleItems.size(); i++) {
            System.out.println((i + 1) + ") " + miracleItems.get(i));
        }

        System.out.print("💡 Do you want to activate one before battle? (yes/no): ");
        String input = sc.nextLine().trim().toLowerCase();

        while (!input.equals("yes") && !input.equals("no")) {
            System.out.print("Please enter 'yes' or 'no': ");
            input = sc.nextLine().trim().toLowerCase();
        }

        if (input.equals("no")) {
            skippedMiracleItemThisBattle = true;
            return null;
        }

        int choice = -1;
        while (choice < 1 || choice > miracleItems.size()) {
            System.out.print("Enter the number of the item to activate: ");
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("❗ Invalid input. Please enter a number.");
            }
        }

        String activatedItem = miracleItems.remove(choice - 1);
        activeMiracleItem = activatedItem; // ✅ store the item as active
        System.out.println("✅ Activated Miracle Item: " + activatedItem + "!");

        // 🎯 Add this switch to print the specific effect
        switch (activatedItem) {
            case "Attack Capsule":
                System.out.println("💥 Your Pokémon’s Attack has been boosted by the Attack Capsule!");
                break;
            case "Defense Capsule":
                System.out.println("🛡️ Your Pokémon is shielded by the Defense Capsule!");
                break;
            case "Poké Ball Power":
                System.out.println("🎯 Poké Ball Power activated – higher catch chance!");
                break;
            default:
                System.out.println("⚠️ Unknown miracle item effect.");
        }
		return activatedItem;

    }


}

