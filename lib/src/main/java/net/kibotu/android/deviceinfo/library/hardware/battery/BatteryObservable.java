package net.kibotu.android.deviceinfo.library.hardware.battery;

import java.util.Observable;

public class BatteryObservable extends Observable {

    @Override
    public void notifyObservers() {
        setChanged();
        super.notifyObservers();
    }

    @Override
    public void notifyObservers(Object data) {
        setChanged();
        super.notifyObservers(data);
    }
}