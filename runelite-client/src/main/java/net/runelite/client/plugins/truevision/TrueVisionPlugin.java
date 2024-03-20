package net.runelite.client.plugins.truevision;

import javax.inject.Inject;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
    name = "True Vision",
    description = "Highlights the area around the player",
    tags = {"highlight", "overlay", "area", "jack", "player"}
)
public class TrueVisionPlugin extends Plugin
{
    @Inject
    private OverlayManager overlayManager;

    @Inject
    private TrueVisionOverlay overlay;

    @Override
    protected void startUp() throws Exception
    {
        overlayManager.add(overlay);
    }

    @Override
    protected void shutDown() throws Exception
    {
        overlayManager.remove(overlay);
    }
}
