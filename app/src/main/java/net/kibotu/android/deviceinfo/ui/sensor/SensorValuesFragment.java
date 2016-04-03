package net.kibotu.android.deviceinfo.ui.sensor;

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

import static net.kibotu.android.deviceinfo.R.layout.sensor;

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

    protected abstract void registerSensor();

    protected abstract void unregisterSensor();

    protected abstract SensorEventListener createSensorEventListener();
}
