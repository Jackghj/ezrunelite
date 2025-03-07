package net.runelite.client.plugins.nex;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.inject.Inject;
import javax.inject.Singleton;

import net.runelite.client.plugins.nex.Prayer;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;


@Singleton
@Slf4j
class NexPrayerOverlay extends Overlay
{
	private final Client client;
	private final NexPlugin plugin;
	private final NexConfig config;

	@Inject
	private NexPrayerOverlay(final Client client, final NexPlugin plugin, final NexConfig config)
	{
		this.client = client;
		this.plugin = plugin;
		this.config = config;

		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
		setPriority(OverlayPriority.HIGHEST);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!plugin.isInFight() || plugin.getNex() == null || client.getLocalPlayer() == null)
		{
			return null;
		}

		if (!config.prayerHelper().showWidgetHelper())
		{
			return null;
		}

		Prayer prayer = NexPhase.phasePrayer(plugin.getCurrentPhase(), client.getLocalPlayer(), plugin.getNex(), plugin.isTrappedInIce());

		if (prayer == null)
		{
			return null;
		}

		final Widget meleePrayerWidget = client.getWidget(Prayer.PROTECT_FROM_MELEE.getWidgetInfo().getPackedId());
		final Widget rangePrayerWidget = client.getWidget(Prayer.PROTECT_FROM_MISSILES.getWidgetInfo().getPackedId());
		final Widget magicPrayerWidget = client.getWidget(Prayer.PROTECT_FROM_MAGIC.getWidgetInfo().getPackedId());


		var prayerWidgetHidden = meleePrayerWidget == null
			|| rangePrayerWidget == null
			|| magicPrayerWidget == null
			|| meleePrayerWidget.isHidden()
			|| rangePrayerWidget.isHidden()
			|| magicPrayerWidget.isHidden();


		if (!prayerWidgetHidden || config.alwaysShowPrayerHelper())
		{
			if (client.isPrayerActive(prayer.getApiPrayer()) && !config.indicatePrayerIsCorrect())
			{
				return null;
			}
			Color color = client.isPrayerActive(prayer.getApiPrayer()) ? Color.GREEN : Color.RED;
			renderPrayerOverlay(graphics, client, prayer, color);
		}

		return null;
	}

	private void renderPrayerOverlay(Graphics2D graphics, Client client, Prayer prayer, Color color)
	{
		Widget prayerWidget = null;
		if(prayer == Prayer.PROTECT_FROM_MAGIC) {
			prayerWidget = client.getWidget(Prayer.PROTECT_FROM_MAGIC.getWidgetInfo().getPackedId());
		}
		else if(prayer == Prayer.PROTECT_FROM_MELEE) {
			prayerWidget = client.getWidget(Prayer.PROTECT_FROM_MELEE.getWidgetInfo().getPackedId());
		}
		else {
			prayerWidget = client.getWidget(Prayer.PROTECT_FROM_MISSILES.getWidgetInfo().getPackedId());
		}
		if (prayerWidget == null)
		{
			return;
		}

		final Rectangle prayerRectangle = new Rectangle((int) prayerWidget.getBounds().getWidth(), (int) prayerWidget.getBounds().getHeight());
		prayerRectangle.translate((int) prayerWidget.getBounds().getX(), (int) prayerWidget.getBounds().getY());

		OverlayUtil.renderPolygon(graphics, prayerRectangle, color);
	}
}