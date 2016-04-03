package net.kibotu.android.deviceinfo.ui.sensor;

import android.hardware.Sensor;
import android.support.annotation.NonNull;
import android.view.View;
import com.common.android.utils.extensions.FragmentExtensions;
import com.common.android.utils.ui.recyclerView.OnItemClickListener;
import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.library.Device;
import net.kibotu.android.deviceinfo.model.ListItem;
import net.kibotu.android.deviceinfo.ui.ViewHelper;
import net.kibotu.android.deviceinfo.ui.list.ListFragment;

import java.util.List;

import static android.hardware.Sensor.*;
import static android.os.Build.VERSION_CODES.KITKAT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static com.common.android.utils.extensions.FragmentExtensions.replaceBySlidingHorizontally;
import static net.kibotu.android.deviceinfo.library.version.Version.isAtLeastVersion;

/**
 * Created by Nyaruhodo on 21.02.2016.
 */
public class SensorFragment extends ListFragment {

    private List<Sensor> sensorList;

    @Override
    protected String getTitle() {
        return getString(R.string.menu_item_sensor);
    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();

        sensorList = Device.getSensorList();
        for (final Sensor s : sensorList) {
            ListItem listItem = new ListItem().setLabel(s.getName());

            if (isAtLeastVersion(LOLLIPOP)) {
                listItem.addChild(new ListItem().setLabel("Reporting Mode").setValue(s.getReportingMode()));
            }
            if (isAtLeastVersion(KITKAT)) {
                listItem.addChild(new ListItem().setLabel("Fifo Reserved Event Count").setValue(s.getFifoReservedEventCount()));
            }
            if (isAtLeastVersion(KITKAT)) {
                listItem.addChild(new ListItem().setLabel("Fifo Max Event Count").setValue(s.getFifoMaxEventCount()));
            }

            if (isAtLeastVersion(LOLLIPOP)) {
                listItem.addChild(new ListItem().setLabel("Max Delay").setValue(s.getMaxDelay()));
            }
            if (isAtLeastVersion(LOLLIPOP)) {
                listItem.addChild(new ListItem().setLabel("Wake Up Sensor").setValue(s.isWakeUpSensor()));
            }

            addSubListItem(listItem
                    .addChild(new ListItem().setLabel("Type").setValue(ViewHelper.getSensorName(s)))
                    .addChild(new ListItem().setLabel("Vendor").setValue(s.getVendor()))
                    .addChild(new ListItem().setLabel("Version").setValue(s.getVersion()))
                    .addChild(new ListItem().setLabel("Resolution").setValue(s.getResolution()))
                    .addChild(new ListItem().setLabel("Min Delay").setValue(s.getMinDelay()))
                    .addChild(new ListItem().setLabel("Max Range").setValue(s.getMaximumRange()))
            );
        }

        adapter.setOnItemClickListener(new OnItemClickListener<ListItem>() {
            @Override
            public void onItemClick(@NonNull ListItem listItem, @NonNull View view, int position) {
                showSensorData(sensorList.get(position));
            }
        });
    }

    private static void showSensorData(final Sensor sensor) {

        switch (sensor.getType()) {
            case TYPE_ACCELEROMETER:
                FragmentExtensions.replaceToBackStackBySlidingHorizontally(new AccelerationSensorFragment());
                break;
            case TYPE_AMBIENT_TEMPERATURE:
            case TYPE_GAME_ROTATION_VECTOR:
            case TYPE_GEOMAGNETIC_ROTATION_VECTOR:
            case 24:
                String name = "android.sensor.glance_gesture";
            case TYPE_GRAVITY:
            case TYPE_GYROSCOPE:
            case TYPE_GYROSCOPE_UNCALIBRATED:
            case TYPE_HEART_RATE:
            case TYPE_LIGHT:
            case TYPE_LINEAR_ACCELERATION:
            case TYPE_MAGNETIC_FIELD:
            case TYPE_MAGNETIC_FIELD_UNCALIBRATED:
            case 25:
                name = "android.sensor.pick_up_gesture";
            case TYPE_PRESSURE:
            case TYPE_PROXIMITY:
            case TYPE_RELATIVE_HUMIDITY:
            case TYPE_ROTATION_VECTOR:
            case TYPE_SIGNIFICANT_MOTION:
            case TYPE_STEP_COUNTER:
            case TYPE_STEP_DETECTOR:
            case 22:
                name = "android.sensor.tilt_detector";
            case 23:
                name = "android.sensor.wake_gesture";
            case TYPE_ORIENTATION:
            case TYPE_TEMPERATURE:
            default:
                name = android.os.Build.UNKNOWN;
        }
    }

    @Override
    protected int getHomeIcon() {
        return R.drawable.sensors;
    }
}
