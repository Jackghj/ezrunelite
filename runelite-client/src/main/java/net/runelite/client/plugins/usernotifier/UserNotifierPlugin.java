package net.runelite.client.plugins.usernotifier;

import com.google.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.events.PlayerSpawned;
import net.runelite.api.ChatMessageType;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;

import java.awt.image.BufferedImage;
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
    private ChatMessageManager chatMessageManager;

    private UserNotifierPanel panel;
    private NavigationButton navButton;

    // Map to store usernames and reasons
    private final Map<String, String> userReasons = new HashMap<>();

    // Map to track last notification time for usernames
    private final Map<String, Instant> lastNotificationTime = new HashMap<>();

    @Override
    protected void startUp() {
        panel = new UserNotifierPanel(this);

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

    // Method to update the list of usernames and reasons from the panel
    public void updateUserReasons(Map<String, String> updatedUserReasons) {
        userReasons.clear();
        userReasons.putAll(updatedUserReasons);
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
                sendChatMessage(playerName + " is near: " + reason);
                lastNotificationTime.put(playerName, now);
            }
        }
    }

    // Method to send a chat message
    private void sendChatMessage(String message) {
        final String chatMessage = new ChatMessageBuilder()
                .append(ChatColorType.HIGHLIGHT)
                .append(message)
                .build();

        chatMessageManager.queue(
                QueuedMessage.builder()
                        .type(ChatMessageType.CONSOLE) // Use ChatMessageType.CONSOLE as in DailyTasksPlugin
                        .runeLiteFormattedMessage(chatMessage)
                        .build()
        );
    }
}
