package net.runelite.client.plugins.usernotifier;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("usernotifier")
public interface UserNotifierConfig extends Config {
    @ConfigItem(
        keyName = "userReasons",
        name = "User Reasons",
        description = "Stores the list of usernames and their reasons",
        hidden = true
    )
    default String userReasons() {
        return "";
    }

    @ConfigItem(
        keyName = "userReasons",
        name = "User Reasons",
        description = "Stores the list of usernames and their reasons"
    )
    void userReasons(String data);
}
