package net.kibotu.android.deviceinfo.ui.other;

import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.ui.list.ListFragment;

import static net.kibotu.android.deviceinfo.library.Device.installedApps;
import static net.kibotu.android.deviceinfo.library.Device.isPhoneRooted;

/**
 * Created by Nyaruhodo on 21.02.2016.
 */
public class MiscellaneousFragment extends ListFragment {

    @Override
    protected String getTitle() {
        return getString(R.string.menu_item_other);
    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();

        addHorizontallyCard("Rooted", isPhoneRooted(), "Determines if this device has been rooted.");
        addHorizontallyCard("Installed Apps", installedApps().size(), "Amount of currently installed applications.");

    }

    @Override
    protected int getHomeIcon() {
        return R.drawable.others;
    }
}
