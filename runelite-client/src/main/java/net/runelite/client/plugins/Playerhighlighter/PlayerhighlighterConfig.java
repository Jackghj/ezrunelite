package net.runelite.client.plugins.playerhighlighter;

import java.awt.Color;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("playerhighlighter")
public interface PlayerhighlighterConfig extends Config {
    @ConfigItem(
        keyName = "pkMode",
        name = "Pk Mode",
        description = "If enabled, exclude the 4 most expensive items from the total value calculation.",
        position = 13
    )
    default boolean pkMode() {
        return false;
    }

    @ConfigItem(
        keyName = "smitedMode",
        name = "Smited",
        description = "If enabled, adjust the number of excluded items by one when applicable.",
        position = 14
    )
    default boolean smitedMode() {
        return false;
    }
    @ConfigItem(
            keyName = "threshold1",
            name = "Threshold 1",
            description = "Value threshold for the first range.",
            position = 0
    )
    default int threshold1() {
        return 100_000;
    }

    @Alpha
    @ConfigItem(
            keyName = "color1",
            name = "Color for Threshold 1",
            description = "Color for the first threshold range.",
            position = 1
    )
    default Color color1() {
        return Color.WHITE;
    }

    @ConfigItem(
            keyName = "threshold2",
            name = "Threshold 2",
            description = "Value threshold for the second range.",
            position = 2
    )
    default int threshold2() {
        return 500_000;
    }

    @Alpha
    @ConfigItem(
            keyName = "color2",
            name = "Color for Threshold 2",
            description = "Color for the second threshold range.",
            position = 3
    )
    default Color color2() {
        return Color.GREEN;
    }

    @ConfigItem(
            keyName = "threshold3",
            name = "Threshold 3",
            description = "Value threshold for the third range.",
            position = 4
    )
    default int threshold3() {
        return 1_000_000;
    }

    @Alpha
    @ConfigItem(
            keyName = "color3",
            name = "Color for Threshold 3",
            description = "Color for the third threshold range.",
            position = 5
    )
    default Color color3() {
        return Color.BLUE;
    }

    @ConfigItem(
            keyName = "threshold4",
            name = "Threshold 4",
            description = "Value threshold for the fourth range.",
            position = 6
    )
    default int threshold4() {
        return 2_500_000;
    }

    @Alpha
    @ConfigItem(
            keyName = "color4",
            name = "Color for Threshold 4",
            description = "Color for the fourth threshold range.",
            position = 7
    )
    default Color color4() {
        return Color.ORANGE;
    }

    @ConfigItem(
            keyName = "threshold5",
            name = "Threshold 5",
            description = "Value threshold for the fifth range.",
            position = 8
    )
    default int threshold5() {
        return 5_000_000;
    }

    @Alpha
    @ConfigItem(
            keyName = "color5",
            name = "Color for Threshold 5",
            description = "Color for the fifth threshold range.",
            position = 9
    )
    default Color color5() {
        return Color.PINK;
    }

    @Alpha
    @ConfigItem(
            keyName = "colorDefault",
            name = "Default Color",
            description = "Default color for values not meeting any threshold.",
            position = 10
    )
    default Color colorDefault() {
        return Color.RED;
    }

    @ConfigItem(
            keyName = "borderWidth",
            name = "Border Width",
            description = "Width of the player outline border.",
            position = 11
    )
    default int borderWidth() {
        return 4;
    }

    @ConfigItem(
            keyName = "outlineFeather",
            name = "Outline Feather",
            description = "Specify between 0-4 how much the player outline should be faded.",
            position = 12
    )
    default int outlineFeather() {
        return 4;
    }
}
