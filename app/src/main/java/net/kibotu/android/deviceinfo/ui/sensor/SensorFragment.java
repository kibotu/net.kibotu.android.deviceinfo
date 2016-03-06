package net.kibotu.android.deviceinfo.ui.sensor;

import android.hardware.Sensor;
import android.view.LayoutInflater;
import android.view.View;
import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.library.Device;
import net.kibotu.android.deviceinfo.model.ListItem;
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
            ListItem listItem = new ListItem().setLabel(s.getName());
            addSubListItem(listItem
                    .addChild(new ListItem().setLabel("Type").setValue(ViewHelper.getSensorName(s)))
                    .addChild(new ListItem().setLabel("Vendor").setValue(s.getVendor()))
                    .addChild(new ListItem().setLabel("Version").setValue(s.getVersion()))
                    .addChild(new ListItem().setLabel("Resolution").setValue(s.getResolution()))
                    .addChild(new ListItem().setLabel("Min Delay").setValue(s.getMinDelay()))
                    .addChild(new ListItem().setLabel("Max Range").setValue(s.getMaximumRange()))
                    .addChild(new ListItem().setLabel("Power").setValue(s.getPower()))
            );
        }
    }

    @Override
    protected int getHomeIcon() {
        return R.drawable.sensors;
    }
}
