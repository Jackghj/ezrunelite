package net.runelite.client.plugins.nightmare;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import javax.inject.Inject;
import javax.inject.Singleton;

import net.runelite.client.plugins.nightmare.Prayer;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;

import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

import static net.runelite.client.ui.overlay.OverlayUtil.renderPolygon;

@Singleton
@Slf4j
class NightmarePrayerOverlay extends Overlay
{
	private final Client client;
	private final NightmarePlugin plugin;
	private final NightmareConfig config;

	@Inject
	private NightmarePrayerOverlay(final Client client, final NightmarePlugin plugin, final NightmareConfig config)
	{
		this.client = client;
		this.plugin = plugin;
		this.config = config;
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
		setPriority(OverlayPriority.LOW);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!plugin.isInFight() || plugin.getNm() == null)
		{
			return null;
		}

		NightmareAttack attack = plugin.getPendingNightmareAttack();

		if (attack == null)
		{
			return null;
		}

		if (!config.prayerHelper().showWidgetHelper())
		{
			return null;
		}

		Color color = client.isPrayerActive(attack.getPrayer().getApiPrayer()) ? Color.GREEN : Color.RED;
		renderPrayerOverlay(graphics, client, attack.getPrayer(), color);

		return null;
	}

	public static Rectangle renderPrayerOverlay(Graphics2D graphics, Client client, Prayer prayer, Color color)
	{
		Widget widget = null;
		if(prayer == Prayer.PROTECT_FROM_MAGIC) {
			widget = client.getWidget(Prayer.PROTECT_FROM_MAGIC.getWidgetInfo().getPackedId());
		}
		else if(prayer == Prayer.PROTECT_FROM_MELEE) {
			widget = client.getWidget(Prayer.PROTECT_FROM_MELEE.getWidgetInfo().getPackedId());
		}
		else {
			widget = client.getWidget(Prayer.PROTECT_FROM_MISSILES.getWidgetInfo().getPackedId());
		}

		if (widget == null )
		{
			return null;
		}

		Rectangle bounds = widget.getBounds();
		renderPolygon(graphics, rectangleToPolygon(bounds), color);
		return bounds;
	}

	private static Polygon rectangleToPolygon(Rectangle rect)
	{
		int[] xpoints = {rect.x, rect.x + rect.width, rect.x + rect.width, rect.x};
		int[] ypoints = {rect.y, rect.y, rect.y + rect.height, rect.y + rect.height};

		return new Polygon(xpoints, ypoints, 4);
	}
}