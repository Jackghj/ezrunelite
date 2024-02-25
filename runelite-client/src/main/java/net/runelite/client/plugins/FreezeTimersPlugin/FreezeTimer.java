package net.runelite.client.plugins.freezetimers;

public class FreezeTimer {
    private final long startTick;
    private final int totalDurationTicks;

    public FreezeTimer(long startTick, int durationTicks) {
        this.startTick = startTick;
        this.totalDurationTicks = durationTicks; // Duration is directly in ticks
    }

    public int getRemainingTicks(long currentTick) {
        long elapsedTicks = currentTick - startTick;
        // Allow countdown to continue to -5 ticks
        return totalDurationTicks - (int) elapsedTicks;
    }

    public boolean isExpired(long currentTick) {
        // Consider the timer expired when it's below -5 ticks
        return getRemainingTicks(currentTick) < -5;
    }
}
