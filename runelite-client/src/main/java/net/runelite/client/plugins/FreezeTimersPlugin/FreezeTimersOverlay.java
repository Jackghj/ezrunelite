package net.runelite.client.plugins.freezetimers;

import javax.inject.Inject;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.Map; // Ensure this import is included


public class FreezeTimersOverlay extends Overlay {
    private final Client client;
    private final FreezeTimersPlugin plugin;

    @Inject
    public FreezeTimersOverlay(Client client, FreezeTimersPlugin plugin) {
        this.client = client;
        this.plugin = plugin;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        long currentTick = client.getTickCount();

        // Use iterator to safely remove expired timers during iteration
        Iterator<Map.Entry<Actor, FreezeTimer>> iterator = plugin.getActiveTimers().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Actor, FreezeTimer> entry = iterator.next();
            FreezeTimer timer = entry.getValue();

            if (timer.isExpired(currentTick)) {
                iterator.remove(); // Remove expired timers
                continue; // Skip rendering logic for expired timers
            }

            // Render active timers
            Actor actor = entry.getKey();
            Point textLocation = actor.getCanvasTextLocation(graphics, "", 40); // Adjust as needed
            if (textLocation != null) {
                int remainingTicks = timer.getRemainingTicks(currentTick);
                String text = String.format("%d ticks", remainingTicks);

                // Change color to red for the last 5 negative ticks
                Color color = remainingTicks >= 0 ? Color.CYAN : Color.RED;
                graphics.setColor(color);
                graphics.drawString(text, textLocation.getX(), textLocation.getY());
            }
        }

        return null;
    }
}
