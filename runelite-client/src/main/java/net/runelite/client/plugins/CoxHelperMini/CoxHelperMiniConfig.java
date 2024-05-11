package net.runelite.client.plugins.CoxHelperMini;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("coxHelperMini")
public interface CoxHelperMiniConfig extends Config {
    @ConfigItem(
        keyName = "borderWidth",
        name = "Border Width",
        description = "Width of the player outline border.",
        position = 0
    )
    default int borderWidth() {
        return 4;  // Default border width
    }

    @ConfigItem(
        keyName = "outlineFeather",
        name = "Outline Feather",
        description = "Specify how much the player outline should be feathered.",
        position = 1
    )
    default int outlineFeather() {
        return 1;  // Default feather amount
    }
}
