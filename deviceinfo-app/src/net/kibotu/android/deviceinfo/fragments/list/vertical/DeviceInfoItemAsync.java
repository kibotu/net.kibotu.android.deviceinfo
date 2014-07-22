package net.kibotu.android.deviceinfo.fragments.list.vertical;


import net.kibotu.android.error.tracking.Logger;

public abstract class DeviceInfoItemAsync extends DeviceInfoItem implements Runnable {

    public DeviceInfoItemAsync(final int order) {
        super(order);
    }

    protected DeviceInfoItemAsync(final String tag, final String description, final String value) {
        super(tag, description, value);
    }

    protected DeviceInfoItemAsync() {
    }

    protected DeviceInfoItemAsync(final String tag, final String description, final String value, final int order) {
        super(tag, description, value, order);
    }

    @Override
    public void run() {
        try {
            async();
        } catch (final Exception e) {
            Logger.e(e);
        }
    }

    protected abstract void async();
}