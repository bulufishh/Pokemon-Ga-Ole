public enum Stage {
    BEGINNER("Beginner Stage", 2, 0.9),  // Allows up to 2⭐
    ADVANCED("Advanced Stage", 4, 0.75), // Allows up to 4⭐
    EXPERT("Expert Stage", 5, 0.6);      // Allows up to 5⭐

    private final String name;
    private final int rarityStars;
    private final double catchModifier;

    Stage(String name, int rarityStars, double catchModifier) {
        this.name = name;
        this.rarityStars = rarityStars;
        this.catchModifier = catchModifier;
    }

    public String getDisplayName() {
        return name;
    }

    public int getRarityStars() {
        return rarityStars;
    }

    public double getCatchModifier() {
        return catchModifier;
    }
    
    public double getGoldenChipDropRate() {
        switch (this) {
            case BEGINNER: return 0.1;
            case ADVANCED: return 0.2;
            case EXPERT: return 0.4;
            default: return 0.1;
        }
    }

    public double getGoldenSandDropRate() {
        switch (this) {
            case BEGINNER: return 0.1;
            case ADVANCED: return 0.2;
            case EXPERT: return 0.4;
            default: return 0.1;
        }
    }
}
