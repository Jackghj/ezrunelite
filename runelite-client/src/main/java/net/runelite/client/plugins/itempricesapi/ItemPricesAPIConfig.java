package net.runelite.client.plugins.itempricesapi;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("itempricesapi")
public interface ItemPricesAPIConfig extends Config {

    @ConfigItem(
            keyName = "refreshInterval",
            name = "Refresh Interval",
            description = "Time in seconds between price updates"
    )
    default int refreshInterval() {
        return 60;
    }
}
