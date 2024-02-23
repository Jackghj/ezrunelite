package net.runelite.client.plugins.truevision;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

public class TrueVisionOverlay extends Overlay
{
    private final Client client;

    @Inject
    private TrueVisionOverlay(Client client)
    {
        this.client = client;
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

        drawHighlightArea(graphics, localLocation);
        drawHighlightArea1(graphics, localLocation);

        return null;
    }

    private void drawHighlightArea(Graphics2D graphics, LocalPoint center)
    {
        int size = 31; // Size of the highlight area (adjust as needed)
        Color color = Color.RED; // Color of the highlight area (adjust as needed)

        graphics.setColor(color);

        Polygon poly = Perspective.getCanvasTileAreaPoly(client, center, size);
        if (poly != null && poly.npoints > 0)
        {
            graphics.drawPolygon(poly);
        }
    }

    private void drawHighlightArea1(Graphics2D graphics, LocalPoint center)
    {
        int size = 63; // Size of the highlight area (adjust as needed)
        Color color = Color.BLUE; // Color of the highlight area (adjust as needed)

        graphics.setColor(color);

        Polygon poly = Perspective.getCanvasTileAreaPoly(client, center, size);
        if (poly != null && poly.npoints > 0)
        {
            graphics.drawPolygon(poly);
        }
    }
}
