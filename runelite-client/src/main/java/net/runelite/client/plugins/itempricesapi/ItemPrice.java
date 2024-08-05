package net.runelite.client.plugins.itempricesapi;

import java.time.Instant;
import java.util.Map;

public class ItemPrice {
    private final int high;
    private final long highTime;
    private final int low;
    private final long lowTime;
    private static Map<Integer, String> itemIdToNameMap;

    public ItemPrice(int high, long highTime, int low, long lowTime) {
        this.high = high;
        this.highTime = highTime;
        this.low = low;
        this.lowTime = lowTime;
    }

    public int getHigh() {
        return high;
    }

    public Instant getHighTime() {
        return Instant.ofEpochSecond(highTime);
    }

    public int getLow() {
        return low;
    }

    public Instant getLowTime() {
        return Instant.ofEpochSecond(lowTime);
    }

    public static void setItemIdToNameMap(Map<Integer, String> map) {
        itemIdToNameMap = map;
    }

    public static String getName(int id) {
        return itemIdToNameMap.getOrDefault(id, "Unknown Item");
    }
}
