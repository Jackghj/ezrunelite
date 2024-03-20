package net.runelite.client.plugins.playerhighlighter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
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

    private long calculateTotalEquipmentValue(Player player) {
        boolean isSkulled = player.getSkullIcon() != null && player.getSkullIcon() == SkullIcon.SKULL;
        int itemsToExclude = 0; // Default no items excluded

        if (config.pkMode()) {
            itemsToExclude = isSkulled ? 1 : 4;
        }

        if (config.smitedMode()) {
            if (isSkulled) {
                itemsToExclude = 0;
            } else {
                itemsToExclude = 3;
            }
        }

        List<Integer> itemPrices = new ArrayList<>();
        for (int itemId : player.getPlayerComposition().getEquipmentIds()) {
            if (itemId > 512) {
                itemId -= 512; // Correct the item ID
                itemPrices.add(itemManager.getItemPrice(itemId));
            }
        }

        itemPrices.sort(Collections.reverseOrder());
        long totalValue = itemPrices.stream().skip(itemsToExclude).mapToLong(Integer::intValue).sum();

        return totalValue;
    }

    private Color determineColorBasedOnValue(long totalValue) {
        if (totalValue >= config.threshold5()) {
            return config.color5();
        } else if (totalValue >= config.threshold4()) {
            return config.color4();
        } else if (totalValue >= config.threshold3()) {
            return config.color3();
        } else if (totalValue >= config.threshold2()) {
            return config.color2();
        } else if (totalValue >= config.threshold1()) {
            return config.color1();
        } else {
            return config.colorDefault();
        }
    }

    private String formatValue(long value) {
        if (value >= 1_000_000) {
            return String.format("%.1fm", value / 1_000_000.0);
        } else if (value >= 1000) {
            return String.format("%.1fk", value / 1000.0);
        } else {
            return String.valueOf(value);
        }
    }
}
