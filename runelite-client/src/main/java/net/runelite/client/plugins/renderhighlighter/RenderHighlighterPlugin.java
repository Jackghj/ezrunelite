package net.runelite.client.plugins.renderhighlighter;

import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@PluginDescriptor(
        name = "Render Highlighter",
        description = "Attempts to draw a box around the player using various methods",
        tags = {"highlight", "overlay", "box", "player"}
)
public class RenderHighlighterPlugin extends Plugin
{
    private static final Logger log = LoggerFactory.getLogger(RenderHighlighterPlugin.class);

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private RenderHighlighterOverlay overlay;

    @Override
    protected void startUp() throws Exception
    {
        overlayManager.add(overlay);
        log.info("Render Highlighter Plugin started!");
    }

    @Override
    protected void shutDown() throws Exception
    {
        overlayManager.remove(overlay);
        log.info("Render Highlighter Plugin stopped.");
    }
}
