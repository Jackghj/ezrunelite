package net.runelite.client.plugins.playerlogger;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.PlayerSpawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;
import java.awt.image.BufferedImage;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@PluginDescriptor(
    name = "Player Logger",
    description = "Logs nearby players along with their combat level and timestamp of first sighting.",
    tags = {"pvp", "player", "logger", "jack", "tracking"}
)
public class PlayerLoggerPlugin extends Plugin {
    @Inject
    private Client client;

    @Inject
    private PlayerLoggerConfig config;

    @Inject
    private ClientToolbar clientToolbar;

    private PlayerLoggerPanel panel;
    private NavigationButton navButton;

    private final Map<String, PlayerDetails> playersLogged = new HashMap<>();

    @Provides
    PlayerLoggerConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(PlayerLoggerConfig.class);
    }

    @Override
    protected void startUp() {
        panel = new PlayerLoggerPanel(this);

        final BufferedImage icon = ImageUtil.loadImageResource(getClass(), "panel_icon.png");
        navButton = NavigationButton.builder()
            .tooltip("Player Logger")
            .icon(icon)
            .priority(5)
            .panel(panel)
            .build();

        clientToolbar.addNavigation(navButton);
    }

    @Override
    protected void shutDown() {
        clientToolbar.removeNavigation(navButton);
        playersLogged.clear();
    }

    @Subscribe
    public void onPlayerSpawned(PlayerSpawned event) {
        Player player = event.getPlayer();
        String name = player.getName();
        int combatLevel = player.getCombatLevel();
        int world = client.getWorld();
        if (!config.onlyInWilderness() || isPlayerInWilderness()) {
            playersLogged.putIfAbsent(name, new PlayerDetails(name, combatLevel, world, Instant.now()));
            panel.updatePlayers(new ArrayList<>(playersLogged.values()));
        }
    }

    private boolean isPlayerInWilderness() {
        WorldPoint loc = client.getLocalPlayer().getWorldLocation();
        return loc.getX() >= 2940 && loc.getX() < 3395 && loc.getY() >= 3525 && loc.getY() < 4000;
    }
}
