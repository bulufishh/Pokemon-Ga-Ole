import java.util.*;

public class PokemonDatabase {

    private static final List<Pokemon> allPokemon = new ArrayList<>();

    static {
        // ⭐ Stage 1 (1–2 Stars)
        allPokemon.add(new Pokemon("Pikachu", 100, MoveType.ELECTRIC, MoveType.NORMAL, 1));
        allPokemon.add(new Pokemon("Charmander", 100, MoveType.FIRE, MoveType.GRASS, 1));
        allPokemon.add(new Pokemon("Bulbasaur", 125, MoveType.GRASS, MoveType.WATER, 2));
        allPokemon.add(new Pokemon("Squirtle", 125, MoveType.WATER, MoveType.FIRE, 2));
        allPokemon.add(new Pokemon("Eevee", 125, MoveType.NORMAL, MoveType.DARK, 2));

        // ⭐⭐ Stage 2 (3–4 Stars)
        allPokemon.add(new Pokemon("Raichu", 150, MoveType.ELECTRIC, MoveType.FLYING, 3));
        allPokemon.add(new Pokemon("Charmeleon", 150, MoveType.FIRE, MoveType.ICE, 3));
        allPokemon.add(new Pokemon("Ivysaur", 175, MoveType.GRASS, MoveType.GROUND, 4));
        allPokemon.add(new Pokemon("Wartortle", 175, MoveType.WATER, MoveType.ROCK, 4));
        allPokemon.add(new Pokemon("Flareon", 175, MoveType.FIRE, MoveType.GRASS, 4));

        // ⭐⭐⭐ Stage 3 (5 Stars)
        allPokemon.add(new Pokemon("Charizard", 200, MoveType.FIRE, MoveType.GRASS, 5));
        allPokemon.add(new Pokemon("Venusaur", 200, MoveType.GRASS, MoveType.ROCK, 5));
        allPokemon.add(new Pokemon("Blastoise", 200, MoveType.WATER, MoveType.GROUND, 5));
        allPokemon.add(new Pokemon("Zapdos", 200, MoveType.ELECTRIC, MoveType.FLYING, 5));
        allPokemon.add(new Pokemon("Gyarados", 200, MoveType.WATER, MoveType.ELECTRIC, 5));
    }

    /**
     * Returns a filtered list of Pokémon strictly matching the selected stage's rarity:
     * - Stage 1: 1–2⭐
     * - Stage 2: 3–4⭐
     * - Stage 3: 5⭐ only
     */
    public static List<Pokemon> generateWildPool(Stage stage) {
        List<Pokemon> pool = new ArrayList<>();

        for (Pokemon p : allPokemon) {
            int rarity = p.getRarityStars();

            switch (stage) {
                case BEGINNER:
                    if (rarity == 1 || rarity == 2) pool.add(p);
                    break;
                case ADVANCED:
                    if (rarity == 3 || rarity == 4) pool.add(p);
                    break;
                case EXPERT:
                    if (rarity == 5) pool.add(p);
                    break;
            }
        }

        // Fallback if no match (unlikely)
        return pool.isEmpty() ? new ArrayList<>(allPokemon) : pool;
    }

    public static List<Pokemon> getAllPokemon() {
        return new ArrayList<>(allPokemon);
    }
}
