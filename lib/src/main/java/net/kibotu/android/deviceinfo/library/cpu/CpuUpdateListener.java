package net.kibotu.android.deviceinfo.library.cpu;

import net.kibotu.android.deviceinfo.library.battery.Battery;

import java.util.Observable;
import java.util.Observer;

public abstract class CpuUpdateListener implements Observer {

    @Override
    public void update(Observable observable, Object data) {
        if (data instanceof Cpu)
            update((Cpu) data);
    }

    protected abstract void update(Cpu cpu);
}