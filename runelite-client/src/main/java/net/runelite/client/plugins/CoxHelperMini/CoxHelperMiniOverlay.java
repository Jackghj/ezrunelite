package net.runelite.client.plugins.CoxHelperMini;

import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.Prayer;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;
import javax.inject.Inject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import net.runelite.api.Point;
import net.runelite.client.ui.overlay.OverlayUtil;

public class CoxHelperMiniOverlay extends Overlay {
    private static final int[] OLM_MAGE_PROJECTILE_IDS = {1339, 100};  // Example Mage Projectile IDs
    private static final int[] OLM_RANGE_PROJECTILE_IDS = {1340, 442}; // Example Range Projectile IDs

    @Inject
    private Client client;

    @Inject
    private CoxHelperMiniPlugin plugin;

    @Inject
    private ModelOutlineRenderer modelOutlineRenderer;

    @Inject
    private CoxHelperMiniConfig config;

    public CoxHelperMiniOverlay() {
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        Player localPlayer = client.getLocalPlayer();
        if (localPlayer != null) {
            int lastProjectileId = plugin.getLastProjectileId();
            Color highlightColor = determineHighlightColor(lastProjectileId);
            if (highlightColor != null) {
                modelOutlineRenderer.drawOutline(localPlayer, config.borderWidth(), highlightColor, config.outlineFeather());
            }
        }
        return null;
    }

    private Color determineHighlightColor(int projectileId) {
        boolean isPrayingMagic = client.isPrayerActive(Prayer.PROTECT_FROM_MAGIC);
        boolean isPrayingMissiles = client.isPrayerActive(Prayer.PROTECT_FROM_MISSILES);

        if (contains(OLM_MAGE_PROJECTILE_IDS, projectileId) && !isPrayingMagic) {
            return new Color(0, 0, 255, 125);  // Semi-transparent blue for Mage
        } else if (contains(OLM_RANGE_PROJECTILE_IDS, projectileId) && !isPrayingMissiles) {
            return new Color(0, 255, 0, 125);  // Semi-transparent green for Range
        }
        return null;  // No highlight if the player is using the correct prayer
    }

    private boolean contains(int[] array, int value) {
        for (int i : array) {
            if (i == value) {
                return true;
            }
        }
        return false;
    }
}
