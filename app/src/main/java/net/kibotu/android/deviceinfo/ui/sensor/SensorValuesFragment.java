package net.kibotu.android.deviceinfo.ui.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import com.common.android.utils.ContextHelper;
import com.jjoe64.graphview.GraphView;
import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.ui.BaseFragment;

import static android.hardware.SensorManager.SENSOR_DELAY_UI;
import static net.kibotu.android.deviceinfo.R.layout.sensor;
import static net.kibotu.android.deviceinfo.library.services.SystemService.getSensorManager;

/**
 * Created by Nyaruhodo on 03.04.2016.
 */
public abstract class SensorValuesFragment extends BaseFragment {

    @NonNull
    @Bind(R.id.x)
    TextView xLabel;
    @NonNull
    @Bind(R.id.y)
    TextView yLabel;
    @NonNull
    @Bind(R.id.z)
    TextView zLabel;

    @NonNull
    @Bind(R.id.graph)
    GraphView graphView;

    protected SensorManager sensorManager;
    protected SensorEventListener sensorEventListener;

    @Override
    public int getLayout() {
        return sensor;
    }

    @CallSuper
    @Override
    protected void onViewCreated() {
        sensorEventListener = createSensorEventListener();
    }

    @Override
    public boolean lockLeftMenu() {
        return true;
    }

    @Nullable
    @Override
    protected View.OnClickListener getHomeIconClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContextHelper.getContext().onBackPressed();
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        registerSensor();
    }

    @Override
    public void onPause() {
        unregisterSensor();
        super.onPause();
    }

    protected void registerSensor() {
        sensorManager = getSensorManager();
        // SENSOR_DELAY_NORMAL, SENSOR_DELAY_UI, SENSOR_DELAY_GAME, or SENSOR_DELAY_FASTEST
        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(sensorType()), SENSOR_DELAY_UI);
    }

    protected void unregisterSensor() {
        sensorManager.unregisterListener(sensorEventListener);
    }

    protected abstract SensorEventListener createSensorEventListener();

    protected  abstract int sensorType() ;
}
