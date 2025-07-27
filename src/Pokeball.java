//Pokeball.java
public enum Pokeball {
    POKEBALL(0.5),
    GREATBALL(0.65),
    ULTRABALL(0.8),
    MASTERBALL(1.0);

    private final double successRate;

    Pokeball(double rate) {
        this.successRate = rate;
    }

    public boolean attemptCatch() {
        return Math.random() < successRate;
    }

    public double getSuccessRate() {
        return successRate;
    }
}
