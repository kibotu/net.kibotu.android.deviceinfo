package net.kibotu.android.deviceinfo.fragments.list;

import net.kibotu.android.deviceinfo.Logger;

public abstract class DeviceInfoItemAsync extends DeviceInfoItem implements Runnable {

    @Override
    public void run() {
        try {
            async();
        } catch (final Exception e) {
            Logger.e("" + e.getMessage(), e);
        }
    }

    protected abstract void async();
}