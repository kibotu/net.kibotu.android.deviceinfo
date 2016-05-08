package net.kibotu.android.deviceinfo.ui.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.common.android.utils.logging.Logger;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import net.kibotu.android.deviceinfo.R;

import java.util.Random;

import static com.common.android.utils.extensions.ResourceExtensions.color;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static net.kibotu.android.deviceinfo.library.ViewHelper.getAccuracyName;
import static net.kibotu.android.deviceinfo.library.ViewHelper.getSensorName;

/**
 * Created by Nyaruhodo on 18.03.2016.
 * <p>
 * <a href="http://developer.android.com/guide/topics/sensors/sensors_motion.html#sensors-motion-accel">Using the Accelerometer</a>
 */
public class AccelerationSensorFragment extends SensorValuesFragment {


    private LineGraphSeries<DataPoint> xSeries;
    private LineGraphSeries<DataPoint> ySeries;
    private LineGraphSeries<DataPoint> zSeries;
    double graph2LastXValue;

    // Create a constant to convert nanoseconds to seconds.
    private static final float NS2S = 1.0f / 1000000000.0f;
    private final float[] deltaRotationVector = new float[4];
    private float timestamp;
    public static final float EPSILON = 0.000000001f;

    @Override
    protected String getTitle() {
        return "Acceleration Sensor";
    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();

        // init example series data

        xSeries = new LineGraphSeries<>();
        xSeries.setColor(color(R.color.red));
        ySeries = new LineGraphSeries<>();
        ySeries.setColor(color(R.color.green));
        zSeries = new LineGraphSeries<>();
        zSeries.setColor(color(R.color.blue));
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(40);
        graphView.addSeries(xSeries);
        graphView.addSeries(ySeries);
        graphView.addSeries(zSeries);
    }

    @Override
    protected int sensorType() {
        return Sensor.TYPE_ACCELEROMETER;
    }

    private DataPoint[] generateData() {
        Random rand = new Random();
        int count = 30;
        DataPoint[] values = new DataPoint[count];
        for (int i = 0; i < count; i++) {
            double x = i;
            double f = rand.nextDouble() * 0.15 + 0.3;
            double y = sin(i * f + 2) + rand.nextDouble() * 0.3;
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
        return values;
    }

    protected SensorEventListener createSensorEventListener() {
        return new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
//                Logger.v(tag(), format("[{0} | {1} | {2} | {3}]", event.timestamp, event.sensor, event.accuracy, event.values));

                set(event.values[0], event.values[1], event.values[2]);
                //set2(event);
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

    private void set2(SensorEvent event) {
        // This timestep's delta rotation to be multiplied by the current rotation
        // after computing it from the gyro sample data.
        if (timestamp != 0) {
            final float dT = (event.timestamp - timestamp) * NS2S;
            // Axis of the rotation sample, not normalized yet.
            float axisX = event.values[0];
            float axisY = event.values[1];
            float axisZ = event.values[2];

            // Calculate the angular speed of the sample
            float omegaMagnitude = (float) sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ);

            // Normalize the rotation vector if it's big enough to get the axis
            // (that is, EPSILON should represent your maximum allowable margin of error)
            if (omegaMagnitude > EPSILON) {
                axisX /= omegaMagnitude;
                axisY /= omegaMagnitude;
                axisZ /= omegaMagnitude;
            }

            // Integrate around this axis with the angular speed by the timestep
            // in order to get a delta rotation from this sample over the timestep
            // We will convert this axis-angle representation of the delta rotation
            // into a quaternion before turning it into the rotation matrix.
            float thetaOverTwo = omegaMagnitude * dT / 2.0f;
            float sinThetaOverTwo = (float) sin(thetaOverTwo);
            float cosThetaOverTwo = (float) cos(thetaOverTwo);
            deltaRotationVector[0] = sinThetaOverTwo * axisX;
            deltaRotationVector[1] = sinThetaOverTwo * axisY;
            deltaRotationVector[2] = sinThetaOverTwo * axisZ;
            deltaRotationVector[3] = cosThetaOverTwo;
        }
        timestamp = event.timestamp;
        float[] deltaRotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector);
        // User code should concatenate the delta rotation we computed with the current rotation
        // in order to get the updated rotation.
        // rotationCurrent = rotationCurrent * deltaRotationMatrix;


        graph2LastXValue += 1d;
        xSeries.appendData(new DataPoint(graph2LastXValue, deltaRotationVector[0]), true, 40);
        ySeries.appendData(new DataPoint(graph2LastXValue, deltaRotationVector[1]), true, 40);
        zSeries.appendData(new DataPoint(graph2LastXValue, deltaRotationVector[2]), true, 40);
    }
}
