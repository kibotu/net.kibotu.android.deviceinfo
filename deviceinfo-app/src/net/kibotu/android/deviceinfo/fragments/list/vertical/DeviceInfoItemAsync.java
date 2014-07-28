package net.kibotu.android.deviceinfo.fragments.list.vertical;


import net.kibotu.android.error.tracking.Logger;

public abstract class DeviceInfoItemAsync extends DeviceInfoItem implements Runnable {

    public static final int DEFAULT_SLEEP_TIME = 1;
    private volatile int sleepTime;
    private volatile boolean isRunning;

    public DeviceInfoItemAsync(final int order) {
        this(DEFAULT_SLEEP_TIME, "", "", "", order);
    }

    protected DeviceInfoItemAsync(final String tag, final String description, final String value) {
        this(DEFAULT_SLEEP_TIME, tag, description, value, Integer.MAX_VALUE);
    }

    protected DeviceInfoItemAsync() {
        this(DEFAULT_SLEEP_TIME, "", "", "", Integer.MAX_VALUE);
    }

    protected DeviceInfoItemAsync(final float sleepTime, final String tag, final String description, final String value, final int order) {
        super(tag, description, value, order);
        this.sleepTime = (int) (sleepTime * 1000f);
    }

    protected abstract void async();

    final public DeviceInfoItemAsync start() {
        if (isRunning) return this;
        new Thread(this).start();
        return this;
    }

    final public DeviceInfoItemAsync stop() {
        isRunning = false;
        return this;
    }

    public void setSleepTime(final float sleepTime) {
        this.sleepTime = (int) (sleepTime * 1000);
    }

    @Override
    final public void run() {
        isRunning = true;
        do {
            async();
            try {
                Thread.sleep(sleepTime);
            } catch (final InterruptedException e) {
                Logger.e(e);
            }
        } while (isRunning);
    }
}