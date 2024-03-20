package net.runelite.client.plugins.playerhighlighter;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
    name = "Player Highlighter",
    description = "Highlzzizghts players based on equipment value thresholds",
    tags = {"highlight", "players", "jack", "outline"}
)
public class PlayerhighlighterPlugin extends Plugin {
    @Inject
    private OverlayManager overlayManager;

    @Inject
    private PlayerhighlighterOverlay playerhighlighterOverlay;

    @Inject
    private PlayerhighlighterConfig config;

    @Provides
    PlayerhighlighterConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(PlayerhighlighterConfig.class);
    }

    @Override
    protected void startUp() {
        overlayManager.add(playerhighlighterOverlay);
    }

    @Override
    protected void shutDown() {
        overlayManager.remove(playerhighlighterOverlay);
    }
}
