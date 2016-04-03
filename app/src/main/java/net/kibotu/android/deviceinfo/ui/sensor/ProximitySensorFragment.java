package net.kibotu.android.deviceinfo.ui.sensor;

import android.hardware.SensorEventListener;
import android.support.annotation.CallSuper;
import net.kibotu.android.deviceinfo.ui.BaseFragment;

/**
 * Created by Nyaruhodo on 03.04.2016.
 */
public class ProximitySensorFragment extends SensorValuesFragment{

    @Override
    protected String getTitle() {
        return null;
    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();
    }

    @Override
    protected void registerSensor() {

    }

    @Override
    protected void unregisterSensor() {

    }

    @Override
    protected SensorEventListener createSensorEventListener() {
        return null;
    }
}
