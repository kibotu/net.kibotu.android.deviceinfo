package net.kibotu.android.deviceinfo.ui.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import com.common.android.utils.extensions.ResourceExtensions;
import com.common.android.utils.logging.Logger;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.Series;
import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.ui.BaseFragment;

import java.util.Random;

import static android.hardware.SensorManager.SENSOR_DELAY_FASTEST;
import static android.hardware.SensorManager.SENSOR_DELAY_UI;
import static com.common.android.utils.extensions.ResourceExtensions.color;
import static net.kibotu.android.deviceinfo.R.layout.sensor;
import static net.kibotu.android.deviceinfo.library.services.SystemService.getSensorManager;
import static net.kibotu.android.deviceinfo.ui.ViewHelper.getAccuracyName;
import static net.kibotu.android.deviceinfo.ui.ViewHelper.getSensorName;

/**
 * Created by Nyaruhodo on 18.03.2016.
 * <p>
 * <a href="http://developer.android.com/guide/topics/sensors/sensors_motion.html#sensors-motion-accel">Using the Accelerometer</a>
 */
public class AccelerationSensorFragment extends BaseFragment {

    @NonNull
    @Bind(R.id.x)
    TextView xLabel;
    @NonNull
    @Bind(R.id.y)
    TextView yLabel;
    @NonNull
    @Bind(R.id.z)
    TextView zLabel;

    private SensorManager sensorManager;
    private SensorEventListener sensorEventListener;

    private GraphView graph;
    private LineGraphSeries<DataPoint> xSeries;
    private LineGraphSeries<DataPoint> ySeries;
    private LineGraphSeries<DataPoint> zSeries;
    double graph2LastXValue;

    @Override
    public int getLayout() {
        return sensor;
    }

    @Override
    protected String getTitle() {
        return tag();
    }

    @Override
    protected void onViewCreated() {
        sensorEventListener = createSensorEventListener();

        // init example series data

        graph = new GraphView(getContext());

        xSeries = new LineGraphSeries<>();
        xSeries.setColor(color(R.color.red));
        ySeries = new LineGraphSeries<>();
        ySeries.setColor(color(R.color.green));
        zSeries = new LineGraphSeries<>();
        zSeries.setColor(color(R.color.blue));
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(40);
        graph.addSeries(xSeries);
        graph.addSeries(ySeries);
        graph.addSeries(zSeries);

        ((ViewGroup) rootView).addView(graph);
    }

    private void registerSensor() {
        sensorManager = getSensorManager();
        // SENSOR_DELAY_NORMAL, SENSOR_DELAY_UI, SENSOR_DELAY_GAME, or SENSOR_DELAY_FASTEST
        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SENSOR_DELAY_UI);
    }

    private void unregisterSensor() {
        sensorManager.unregisterListener(sensorEventListener);
    }

    private DataPoint[] generateData() {
        Random rand = new Random();
        int count = 30;
        DataPoint[] values = new DataPoint[count];
        for (int i = 0; i < count; i++) {
            double x = i;
            double f = rand.nextDouble() * 0.15 + 0.3;
            double y = Math.sin(i * f + 2) + rand.nextDouble() * 0.3;
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
        return values;
    }


    private SensorEventListener createSensorEventListener() {
        return new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
//                Logger.v(tag(), format("[{0} | {1} | {2} | {3}]", event.timestamp, event.sensor, event.accuracy, event.values));

                set(event.values[0], event.values[1], event.values[2]);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                Logger.v(tag(), "[onAccuracyChanged] " + getSensorName(sensor) + " to " + getAccuracyName(accuracy));
            }
        };
    }


    private float[] gravity = new float[3];
    private float[] linear_acceleration = new float[3];

    private void set(final float x, final float y, final float z) {

        // In this example, alpha is calculated as t / (t + dT),
        // where t is the low-pass filter's time-constant and
        // dT is the event delivery rate.

        final float alpha = 0.8f;

        // Isolate the force of gravity with the low-pass filter.
        gravity[0] = alpha * gravity[0] + (1 - alpha) * x;
        gravity[1] = alpha * gravity[1] + (1 - alpha) * y;
        gravity[2] = alpha * gravity[2] + (1 - alpha) * z;

        // Remove the gravity contribution with the high-pass filter.
        linear_acceleration[0] = x - gravity[0];
        linear_acceleration[1] = y - gravity[1];
        linear_acceleration[2] = z - gravity[2];

        xLabel.setText(String.valueOf(linear_acceleration[0]));
        yLabel.setText(String.valueOf(linear_acceleration[1]));
        zLabel.setText(String.valueOf(linear_acceleration[2]));

        graph2LastXValue += 1d;
        xSeries.appendData(new DataPoint(graph2LastXValue, linear_acceleration[0]), true, 40);
        ySeries.appendData(new DataPoint(graph2LastXValue, linear_acceleration[1]), true, 40);
        zSeries.appendData(new DataPoint(graph2LastXValue, linear_acceleration[2]), true, 40);
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
}
