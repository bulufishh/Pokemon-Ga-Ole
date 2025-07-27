public class Pokemon {
    private String name;
    private int hp;
    private MoveType moveType;
    private MoveType defenderType;
    private int rarityStars; // ⭐ 1 to 5

    // Main constructor
    public Pokemon(String name, int hp, MoveType moveType, MoveType defenderType, int rarityStars) {
        this.name = name;
        this.hp = hp;
        this.moveType = moveType;
        this.defenderType = defenderType;
        this.rarityStars = rarityStars;
    }

    // Backward-compatible constructor
    public Pokemon(String name, int hp, MoveType moveType, MoveType defenderType) {
        this(name, hp, moveType, defenderType, 1); // Default rarity = 1 star
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public MoveType getMoveType() {
        return moveType;
    }

    public MoveType getDefenderType() {
        return defenderType;
    }

    public int getRarityStars() {
        return rarityStars;
    }

    public void takeDamage(int damage) {
        hp -= damage;
        if (hp < 0) hp = 0;
    }

    public boolean isFainted() {
        return hp <= 0;
    }

    public String getStats() {
        return String.format("%s | ⭐ %d | HP: %d/%d | Move: %s | Defend: %s",
                name, rarityStars, hp, getMaxHp(), moveType, defenderType);
    }

    public void restoreHp() {
        this.hp = getMaxHp();
    }

    public int getMaxHp() {
        // Base: 100 HP. Each extra star adds 25 HP.
        return 100 + (rarityStars - 1) * 25;
    }

    public int getAttackPower() {
        // Attack strength can scale with rarity
        return 20 + (rarityStars * 5); // e.g., 1-star = 25, 5-star = 45
    }

    // 🔄 Converts to save string
    public String toSaveString() {
        return String.format("%s,%d,%s,%s,%d", name, hp, moveType.name(), defenderType.name(), rarityStars);
    }

    //  Rebuilds a Pokémon from saved string
    public static Pokemon fromSaveString(String data) {
        String[] parts = data.split(",");
        if (parts.length != 5) {
            throw new IllegalArgumentException("Invalid saved Pokémon data: " + data);
        }

        String name = parts[0];
        int hp = Integer.parseInt(parts[1]);
        MoveType moveType = MoveType.valueOf(parts[2]);
        MoveType defenderType = MoveType.valueOf(parts[3]);
        int rarityStars = Integer.parseInt(parts[4]);

        return new Pokemon(name, hp, moveType, defenderType, rarityStars);
    }
}
