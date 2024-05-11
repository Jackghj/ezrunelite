package net.runelite.client.plugins.nightmare;

import net.runelite.client.plugins.nightmare.Prayer;

import java.awt.Color;

public enum NightmareAttack
{
	MELEE(8594, net.runelite.client.plugins.nightmare.Prayer.PROTECT_FROM_MELEE, Color.RED, 129),
	MAGIC(8595, net.runelite.client.plugins.nightmare.Prayer.PROTECT_FROM_MAGIC, Color.CYAN, 127),
	RANGE(8596, net.runelite.client.plugins.nightmare.Prayer.PROTECT_FROM_MISSILES, Color.GREEN, 128),
	CURSE_MELEE(8594, net.runelite.client.plugins.nightmare.Prayer.PROTECT_FROM_MISSILES, Color.GREEN, 128),    //correct prayer icon to use for her melee while you are cursed
	CURSE_MAGIC(8595, net.runelite.client.plugins.nightmare.Prayer.PROTECT_FROM_MELEE, Color.RED, 129),    //correct prayer icon to use for her mage while you are cursed
	CURSE_RANGE(8596, net.runelite.client.plugins.nightmare.Prayer.PROTECT_FROM_MAGIC, Color.CYAN, 127);    //correct prayer icon to use for her range while you are cursed

	private final int animation;
	private final net.runelite.client.plugins.nightmare.Prayer prayer;
	private final Color tickColor;
	private final int prayerSpriteId;

	NightmareAttack(final int animation, final net.runelite.client.plugins.nightmare.Prayer prayer, final Color tickColor, final int prayerSpriteId)
	{
		this.animation = animation;
		this.prayer = prayer;
		this.tickColor = tickColor;
		this.prayerSpriteId = prayerSpriteId;
	}

	public int getAnimation()
	{
		return animation;
	}

	public Prayer getPrayer()
	{
		return prayer;
	}

	public Color getTickColor()
	{
		return tickColor;
	}

	public int getPrayerSpriteId()
	{
		return prayerSpriteId;
	}
}
