package net.runelite.client.plugins.usernotifier;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Provides;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.events.PlayerSpawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;

import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@PluginDescriptor(
    name = "User Notifier",
    description = "Notifies you when specified usernames are near you",
    tags = {"username", "notify", "friend"}
)
public class UserNotifierPlugin extends Plugin {
    @Inject
    private Client client;

    @Inject
    private ClientToolbar clientToolbar;

    @Inject
    private ConfigManager configManager;

    private UserNotifierPanel panel;
    private NavigationButton navButton;

    private final Gson gson = new Gson();

    // Map to store usernames and reasons
    private final Map<String, String> userReasons = new HashMap<>();

    // Map to track last notification time for usernames
    private final Map<String, Instant> lastNotificationTime = new HashMap<>();

    private static final String CONFIG_GROUP = "usernotifier";
    private static final String CONFIG_KEY = "userReasons";

    @Override
    protected void startUp() {
        loadUserReasons(); // Load list from config
        panel = new UserNotifierPanel(this);

        // Populate the panel with loaded data
        panel.updatePanel(userReasons);

        final BufferedImage icon = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB); // Replace with actual icon if needed
        navButton = NavigationButton.builder()
            .tooltip("User Notifier")
            .icon(icon)
            .priority(5)
            .panel(panel)
            .build();

        clientToolbar.addNavigation(navButton);
    }

    @Override
    protected void shutDown() {
        clientToolbar.removeNavigation(navButton);
        userReasons.clear();
        lastNotificationTime.clear();
    }

    // Load the user list from config
    private void loadUserReasons() {
        String data = configManager.getConfiguration(CONFIG_GROUP, CONFIG_KEY);
        if (data != null && !data.isEmpty()) {
            Type type = new TypeToken<Map<String, String>>() {}.getType();
            Map<String, String> loadedData = gson.fromJson(data, type);
            if (loadedData != null) {
                userReasons.putAll(loadedData);
                System.out.println("Loaded " + loadedData.size() + " users from config.");
            }
        }
    }

    // Save the user list to config
    private void saveUserReasons() {
        String data = gson.toJson(userReasons);
        configManager.setConfiguration(CONFIG_GROUP, CONFIG_KEY, data);
        System.out.println("Saved user list to config: " + data);
    }

    public void updateUserReasons(Map<String, String> updatedUserReasons) {
        userReasons.clear();
        userReasons.putAll(updatedUserReasons);
        saveUserReasons(); // Save the updated list
        panel.updatePanel(userReasons); // Update the UI
    }

    @Subscribe
    public void onPlayerSpawned(PlayerSpawned event) {
        Player player = event.getPlayer();
        if (player == null) return;

        String playerName = player.getName();
        if (userReasons.containsKey(playerName)) {
            String reason = userReasons.get(playerName);
            Instant now = Instant.now();
            Instant lastNotified = lastNotificationTime.getOrDefault(playerName, Instant.MIN);

            // Notify every 120 seconds if the player is nearby
            if (now.isAfter(lastNotified.plusSeconds(120))) {
                sendChatMessage(playerName, reason);
                lastNotificationTime.put(playerName, now);
            }
        }
    }

    // Method to send a styled chat message
    private void sendChatMessage(String playerName, String reason) {
        final String chatMessage = "<col=ff0000><b>" + playerName + "</b></col> is near: <col=008000>" + reason + "</col>";
        client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", chatMessage, null);
    }
}
