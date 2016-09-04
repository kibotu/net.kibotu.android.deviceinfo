package net.kibotu.android.deviceinfo.library.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;

import static net.kibotu.android.deviceinfo.library.services.SystemService.getSensorManager;

/**
 * Created by Nyaruhodo on 30.08.2016.
 */

public enum ShakeSensorRx implements SensorEventListener {

    /**
     * Singleton.
     */
    instance;

    private final PublishSubject<Float> subject = PublishSubject.create();

    private int minUpdateInterval = 100;
    private float prevX;
    private float prevY;
    private float prevZ;
    private long prevTime;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        long curTime = System.currentTimeMillis();
        // only allow one update every 100ms.
        if ((curTime - prevTime) <= minUpdateInterval) {
            return;
        }
        float[] values = sensorEvent.values;

        long dt = (curTime - prevTime);
        prevTime = curTime;

        float x = values[0];
        float y = values[1];
        float z = values[2];

        float speed = Math.abs(x + y + z - prevX - prevY - prevZ) / dt * 5;

        subject.onNext(speed);

        prevX = x;
        prevY = y;
        prevZ = z;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // unused
    }

    // region sensor listener

    public static ShakeSensorRx registerSensorListener() {
        SensorManager sensorManager = getSensorManager();
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        for (Sensor sensor : sensors) {
            sensorManager.registerListener(instance, sensor, SensorManager.SENSOR_DELAY_GAME);
            break;
        }
        return instance;
    }

    public static ShakeSensorRx unregisterSensorListener() {
        getSensorManager().unregisterListener(instance);
        return instance;
    }

    // endregion

    public static int getMinUpdateInterval() {
        return instance.minUpdateInterval;
    }

    public static ShakeSensorRx setMinUpdateInterval(int interval) {
        instance.minUpdateInterval = interval;
        return instance;
    }

    public static Observable<Float> getObservable() {
        return instance.subject;
    }
}