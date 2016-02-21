package net.kibotu.android.deviceinfo.ui.battery;

import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.ui.list.ListFragment;

/**
 * Created by Nyaruhodo on 21.02.2016.
 */
public class BatteryFragment extends ListFragment {

    @Override
    protected String getTitle() {
        return getString(R.string.menu_item_battery);
    }
}
