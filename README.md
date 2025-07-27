## Pokémon Ga-Olé (Java Console Edition)
A Java-based console game inspired by the official Pokémon Ga-Olé arcade system. This project simulates core mechanics of the real game, including Catch Time, Battle Mode, Poké Ball usage, Type effectiveness, and a unique Ga-Olé Medal system that unlocks Miracle Items.

Features
* Stage Selection System (Stage 1–3 with increasing rarity and difficulty)

* Catch Time Phase – Choose and try to catch 1 of 3 wild Pokémon

* Turn-Based Battle System – 2v2 battles with move type effectiveness

* Type Chart – Damage multipliers based on classic Pokémon logic

* Poké Balls – Includes PokéBall, GreatBall, UltraBall, and MasterBall with different success rates

* Ga-Olé Medals – Earn medals from battles, trade 3 for a Miracle Item

* Miracle Items – Boost battles with Attack Capsules, Defense Capsules, or Poké Ball Power

* Potion Healing – Restore Pokémon using potions dropped after battle

* Save/Load – Store scores, player collections, medals, and items across sessions

* High Score System – Automatically saves top 5 scores to a file


🧩 Key Classes
Game.java – Main game flow controller

BattleEngine.java – Handles turn-based combat logic

CatchTime.java – Implements Pokémon catching phase

Player.java – Stores player’s Pokémon, medals, items, and healing

Pokeball.java – Enum for Poké Ball types and success rates

MoveType.java & TypeChart.java – Move and defender types with effectiveness multipliers

Pokemon.java – Model class for each Pokémon's stats and types

PokemonDatabase.java – Generates and stores Pokémon pools based on stage

Stage.java – Enum defining Pokémon rarity tiers per stage

TypingChallenge.java – Optional typing minigame for extra challenge

PokeballMystery.java – Manages mystery effects for Poké Ball enhancements

