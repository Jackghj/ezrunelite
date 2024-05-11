package net.runelite.client.plugins.playerlogger;

import java.time.Instant;

public class PlayerDetails {
    private final String name;
    private final int combatLevel;
    private final int world;
    private final Instant firstSeen;

    public PlayerDetails(String name, int combatLevel, int world, Instant firstSeen) {
        this.name = name;
        this.combatLevel = combatLevel;
        this.world = world;
        this.firstSeen = firstSeen;
    }

    // Getters
    public String getName() { return name; }
    public int getCombatLevel() { return combatLevel; }
    public int getWorld() { return world; }
    public Instant getFirstSeen() { return firstSeen; }
}
