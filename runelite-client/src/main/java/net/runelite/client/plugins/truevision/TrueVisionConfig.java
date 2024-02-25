package net.runelite.client.plugins.truevision;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import java.awt.Color;

@ConfigGroup("truevision")
public interface TrueVisionConfig extends Config
{
    @ConfigItem(
        keyName = "highlightAreaSize",
        name = "Highlight Area Size",
        description = "Size of the area highlighted around the player",
        position = 1
    )
    default int highlightAreaSize()
    {
        return 33; // Default size for the highlighted area
    }

    @ConfigItem(
        keyName = "highlightAreaColor",
        name = "Highlight Area Color",
        description = "Color of the area highlighted around the player",
        position = 2
    )
    default Color highlightAreaColor()
    {
        return Color.RED; // Default color for the highlighted area
    }

    // Add more config items here as needed
}
