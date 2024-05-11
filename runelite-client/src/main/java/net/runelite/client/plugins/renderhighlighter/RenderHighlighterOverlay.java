package net.runelite.client.plugins.renderhighlighter;

import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

import javax.inject.Inject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class RenderHighlighterOverlay extends Overlay
{
    private final Client client;
    private final RenderHighlighterConfig config;

    @Inject
    private RenderHighlighterOverlay(Client client, RenderHighlighterConfig config)
    {
        this.client = client;
        this.config = config;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        LocalPoint localLocation = client.getLocalPlayer().getLocalLocation();
        if (localLocation == null)
        {
            return null;
        }

        drawBox(graphics, localLocation);

        return null;
    }

    private void drawBox(Graphics2D graphics, LocalPoint center)
    {
        int size = config.boxSize(); // Size of the box, configured in the plugin settings
        graphics.setColor(config.boxColor()); // Color of the box, configured in the plugin settings

        Polygon poly = Perspective.getCanvasTileAreaPoly(client, center, size);
        if (poly != null)
        {
            graphics.drawPolygon(poly);
        }
    }
}
