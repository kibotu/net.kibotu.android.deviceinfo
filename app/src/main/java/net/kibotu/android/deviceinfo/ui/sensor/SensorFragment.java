package net.kibotu.android.deviceinfo.ui.sensor;

import android.hardware.Sensor;
import android.view.LayoutInflater;
import android.view.View;
import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.library.Device;
import net.kibotu.android.deviceinfo.ui.ViewHelper;
import net.kibotu.android.deviceinfo.ui.list.ListFragment;

import java.util.List;

import static net.kibotu.android.deviceinfo.ui.ViewHelper.BR;

/**
 * Created by Nyaruhodo on 21.02.2016.
 */
public class SensorFragment extends ListFragment {

    @Override
    protected String getTitle() {
        return getString(R.string.menu_item_sensor);
    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();

        final List<Sensor> list = Device.getSensorList();
        for (final Sensor s : list) {

            String keys = "Type:" + BR;
            String values = ViewHelper.getSensorName(s) + BR;
            keys += "Vendor:" + BR;
            values += s.getVendor() + BR;
            keys += "Version:" + BR;
            values += s.getVersion() + BR;
            keys += "Resolution:" + BR;
            values += s.getResolution() + BR;
            keys += "Min Delay:" + BR;
            values += s.getMinDelay() + BR;
            keys += "Max Range:" + BR;
            values += s.getMaximumRange() + BR;
            keys += "Power:";
            values += s.getPower();

//            addListItemWithTitle(s.getName(), keys, values, "");
        }
    }

    @Override
    protected int getHomeIcon() {
        return R.drawable.sensors_i;
    }
}
