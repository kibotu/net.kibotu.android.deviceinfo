package net.kibotu.android.deviceinfo.fragments.list;

import net.kibotu.android.deviceinfo.Logger;

public abstract class DeviceInfoItemAsync extends DeviceInfoItem implements Runnable {

    private final DeviceInfoFragment fragment;

    protected DeviceInfoItemAsync(final DeviceInfoFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void run() {
        try {
            async();
            fragment.list.notifyDataSetChanged();
        } catch (final Exception e) {
            Logger.e("" + e.getMessage(), e);
        }
    }

    protected abstract void async();
}