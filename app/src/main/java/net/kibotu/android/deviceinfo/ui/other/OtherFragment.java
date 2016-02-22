package net.kibotu.android.deviceinfo.ui.other;

import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.ui.list.ListFragment;

import static net.kibotu.android.deviceinfo.library.Device.installedApps;
import static net.kibotu.android.deviceinfo.library.Device.isPhoneRooted;

/**
 * Created by Nyaruhodo on 21.02.2016.
 */
public class OtherFragment extends ListFragment {

    @Override
    protected String getTitle() {
        return getString(R.string.menu_item_other);
    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();

        addListItemHorizontally("Rooted", isPhoneRooted(), "Determines if this device has been rooted.");
        addListItemHorizontally("Installed Apps", installedApps().size(), "Amount of currently installed applications.");

    }
}
