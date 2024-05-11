package net.runelite.client.plugins.playerlogger;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("playerlogger")
public interface PlayerLoggerConfig extends Config {
    @ConfigItem(
        position = 10,
        keyName = "onlyInWilderness",
        name = "Log Only in Wilderness",
        description = "Only log details when the player is in the Wilderness."
    )
    default boolean onlyInWilderness() {
        return false;
    }
}
