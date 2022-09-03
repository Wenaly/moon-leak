package com.moon.api.utils.misc;

public class TimerUtils {
    private long time = -1L;

    public boolean passedMs(long ms) {
        return this.passedNs(this.convertToNs(ms));
    }

    public boolean passedNs(long ns) {
        return System.nanoTime() - this.time >= ns;
    }

    public TimerUtils reset() {
        this.time = System.nanoTime();
        return this;
    }

    public long convertToNs(long time) {
        return time * 1000000L;
    }
}
