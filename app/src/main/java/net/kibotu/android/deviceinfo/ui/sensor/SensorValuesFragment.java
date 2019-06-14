package net.kibotu.android.deviceinfo.ui.sensor;

import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.common.android.utils.interfaces.TitleProvider;
import com.jjoe64.graphview.GraphView;

import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.ui.BaseFragment;

import butterknife.BindView;

import static android.hardware.SensorManager.SENSOR_DELAY_UI;
import static net.kibotu.android.deviceinfo.library.services.SystemService.getSensorManager;

/**
 * Created by Nyaruhodo on 03.04.2016.
 */
public abstract class SensorValuesFragment extends BaseFragment implements TitleProvider {

    @NonNull
    @BindView(R.id.x)
    TextView xLabel;
    @NonNull
    @BindView(R.id.y)
    TextView yLabel;
    @NonNull
    @BindView(R.id.z)
    TextView zLabel;

    @NonNull
    @BindView(R.id.graph)
    GraphView graphView;

    protected SensorManager sensorManager;
    protected SensorEventListener sensorEventListener;

    @Override
    public int getLayout() {
        return R.layout.sensor;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sensorEventListener = createSensorEventListener();
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

    protected abstract int sensorType();
}
