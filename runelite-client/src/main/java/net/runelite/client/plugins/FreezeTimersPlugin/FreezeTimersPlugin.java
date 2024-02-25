package net.runelite.client.plugins.freezetimers;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.events.GraphicChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import java.util.HashMap;
import java.util.Map;

@PluginDescriptor(
    name = "Freeze Timers",
    description = "Displays timers for various freezing and binding spells",
    tags = {"freeze", "timers", "overlay", "pvp", "pvm"}
)
public class FreezeTimersPlugin extends Plugin {
    private static final int ICE_BLITZ_GRAPHIC_ID = 367;
    private static final int ICE_BARRAGE_GRAPHIC_ID = 369;
    private static final int ENTANGLE_GRAPHIC_ID = 179; // Entangle graphic ID

    @Inject
    private Client client;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private FreezeTimersOverlay overlay;

    private final Map<Actor, FreezeTimer> activeTimers = new HashMap<>();

    @Provides
    FreezeTimersConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(FreezeTimersConfig.class);
    }

    @Override
    protected void startUp() {
        overlayManager.add(overlay);
    }

    @Override
    protected void shutDown() {
        overlayManager.remove(overlay);
        activeTimers.clear();
    }

    @Subscribe
    public void onGraphicChanged(GraphicChanged event) {
        Actor actor = event.getActor();
        int graphicId = actor.getGraphic();
        int durationTicks = 0;

        // Check for Ice Blitz or Entangle, which have the same duration
        if (graphicId == ICE_BLITZ_GRAPHIC_ID || graphicId == ENTANGLE_GRAPHIC_ID) {
            durationTicks = 24; // Duration for Ice Blitz and Entangle
        } else if (graphicId == ICE_BARRAGE_GRAPHIC_ID) {
            durationTicks = 32; // Duration for Ice Barrage
        }

        if (durationTicks > 0) {
            FreezeTimer existingTimer = activeTimers.get(actor);
            if (existingTimer == null || existingTimer.isExpired(client.getTickCount())) {
                activeTimers.put(actor, new FreezeTimer(client.getTickCount(), durationTicks));
            }
        }
    }

    public Map<Actor, FreezeTimer> getActiveTimers() {
        return activeTimers;
    }
}
