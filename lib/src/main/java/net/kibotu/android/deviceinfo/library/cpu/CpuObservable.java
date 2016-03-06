package net.kibotu.android.deviceinfo.library.cpu;

import java.util.Observable;

public class CpuObservable extends Observable {

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