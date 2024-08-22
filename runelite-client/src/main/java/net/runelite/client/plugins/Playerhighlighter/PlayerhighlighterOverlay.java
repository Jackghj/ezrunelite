package net.runelite.client.plugins.playerhighlighter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.SkullIcon;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

public class PlayerhighlighterOverlay extends Overlay {
    private final Client client;
    private final ModelOutlineRenderer modelOutlineRenderer;
    private final ItemManager itemManager;
    private final PlayerhighlighterConfig config;

    @Inject
    public PlayerhighlighterOverlay(Client client, ModelOutlineRenderer modelOutlineRenderer, ItemManager itemManager, PlayerhighlighterConfig config) {
        this.client = client;
        this.modelOutlineRenderer = modelOutlineRenderer;
        this.itemManager = itemManager;
        this.config = config;
        setPosition(OverlayPosition.DYNAMIC);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        for (Player player : client.getPlayers()) {
            if (player != null) {
                long totalValue = calculateTotalEquipmentValue(player);
                Color color = determineColorBasedOnValue(totalValue);
                modelOutlineRenderer.drawOutline(player, config.borderWidth(), color, config.outlineFeather());

                final String valueText = formatValue(totalValue);
                Point canvasPoint = player.getCanvasTextLocation(graphics, valueText, player.getLogicalHeight() + 40); // Adjusted to move text further south
                if (canvasPoint != null) {
                    OverlayUtil.renderTextLocation(graphics, canvasPoint, valueText, color);
                }
            }
        }
        return null;
    }

    private boolean isPlayerSkulled(Player player) {
        SkullIcon skull = player.getSkullIcon();
        // General check for a skull icon, assumes any skulled state is relevant.
        // This doesn't distinguish between different types of skulls like keys or bounty hunter,
        // as specific enums might not be available for each case.
        return skull != null;
    }

    private long calculateTotalEquipmentValue(Player player) {
        boolean isSkulled = isPlayerSkulled(player);
        int itemsToExclude = determineItemsToExclude(isSkulled);

        List<Integer> itemPrices = new ArrayList<>();
        for (int itemId : player.getPlayerComposition().getEquipmentIds()) {
            if (itemId > 2048) { // Changed from -512 August
                itemId -= 2048; // Changed from -512 August
                itemPrices.add(itemManager.getItemPrice(itemId));
            }
        }

        itemPrices.sort(Comparator.reverseOrder());
        return itemPrices.stream().skip(itemsToExclude).mapToLong(Integer::intValue).sum();
    }

    private int determineItemsToExclude(boolean isSkulled) {
        if (config.pkMode()) {
            return isSkulled ? 1 : 4;
        }

        if (config.smitedMode()) {
            // Adjust the exclusion based on skull status and smited mode
            if (isSkulled) {
                // Smited and skulled players lose everything, so exclude none
                return 0;
            } else {
                // Non-skulled but smited, adjust based on PK mode being active or not
                return config.pkMode() ? 3 : 3; // Adjust if needed for non-skulled smited logic
            }
        }
        // Default behavior when neither pkMode nor smitedMode is active
        return isSkulled ? 1 : 3; // Adjust this based on your requirements for skulled players
    }

    private Color determineColorBasedOnValue(long totalValue) {
        if (totalValue >= config.threshold5()) return config.color5();
        else if (totalValue >= config.threshold4()) return config.color4();
        else if (totalValue >= config.threshold3()) return config.color3();
        else if (totalValue >= config.threshold2()) return config.color2();
        else if (totalValue >= config.threshold1()) return config.color1();
        else return config.colorDefault();
    }

    private String formatValue(long value) {
        if (value >= 1_000_000) return String.format("%.1fm", value / 1_000_000.0);
        else if (value >= 1000) return String.format("%.1fk", value / 1000.0);
        else return String.valueOf(value);
    }
}
