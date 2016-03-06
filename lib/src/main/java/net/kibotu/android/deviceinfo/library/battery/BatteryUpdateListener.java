package net.kibotu.android.deviceinfo.library.battery;

import java.util.Observable;
import java.util.Observer;

public abstract class BatteryUpdateListener implements Observer {

    @Override
    public void update(Observable observable, Object data) {
        if (data instanceof Battery)
            update((Battery) data);
    }

    protected abstract void update(Battery battery);
}