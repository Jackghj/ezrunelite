package net.runelite.client.plugins.renderhighlighter;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import java.awt.Color;

@ConfigGroup("renderHighlighter")
public interface RenderHighlighterConfig extends Config
{
    @ConfigItem(
            keyName = "boxSize",
            name = "Box Size",
            description = "Size of the box, configured in the plugin settings",
            position = 1
    )
    default int boxSize()
    {
        return 33; // Default size for the box
    }

    @ConfigItem(
            keyName = "boxColor",
            name = "Box Color",
            description = "Color of the box, configured in the plugin settings",
            position = 2
    )
    default Color boxColor()
    {
        return Color.RED; // Default color for the box
    }

    // Add more config items here as needed
}
