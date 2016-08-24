package net.kibotu.android.deviceinfo.library.misc;

import android.app.Activity;

import net.kibotu.android.deviceinfo.library.Device;

import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by Nyaruhodo on 12.03.2016.
 */
public abstract class UpdateTimer<T> {

    public static abstract class UpdateListener<T> implements Observer {

        @Override
        public void update(Observable observable, Object data) {
            try {
                update((T) data);
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }

        protected abstract void update(T t);
    }

    private Observable observable;

    private Timer timer;
    private long updateInterval;

    public UpdateTimer() {
        observable = new Observable() {

            @Override
            public void notifyObservers(Object data) {
                setChanged();
                super.notifyObservers(data);
            }
        };
        updateInterval = TimeUnit.SECONDS.toMillis(1);
    }

    public UpdateTimer<T> addObserver(UpdateListener<T> listener) {
        observable.addObserver(listener);
        return this;
    }

    public UpdateTimer<T> deleteObserver(UpdateListener<T> listener) {
        observable.deleteObserver(listener);
        return this;
    }

    public UpdateTimer<T> setUpdateInterval(long timeInMillis) {
        updateInterval = timeInMillis;
        return this;
    }

    public UpdateTimer<T> start() {
        if (timer != null)
            return this;

        timer = new Timer();

        // scheduling cpu usage updates
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                final T data = getData();

                // doing ui updates on main thread
                ((Activity) Device.getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        observable.notifyObservers(data);
                    }
                });
            }
        }, 0, updateInterval);

        return this;
    }

    protected abstract T getData();

    public UpdateTimer<T> stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        return this;
    }
}
