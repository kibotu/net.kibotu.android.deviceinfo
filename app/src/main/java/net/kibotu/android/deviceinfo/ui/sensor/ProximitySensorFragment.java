package net.kibotu.android.deviceinfo.ui.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import java.util.Arrays;

/**
 * Created by Nyaruhodo on 03.04.2016.
 */
public class ProximitySensorFragment extends SensorValuesFragment {

    @Override
    protected String getTitle() {
        return "Proximity Sensor";
    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();
    }

    @Override
    protected int sensorType() {
        return Sensor.TYPE_PROXIMITY;
    }

    @Override
    protected SensorEventListener createSensorEventListener() {
        return new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float distance = event.values[0];
                xLabel.setText(Arrays.toString(event.values));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }
}
