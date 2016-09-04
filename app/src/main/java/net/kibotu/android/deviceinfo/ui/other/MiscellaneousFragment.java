package net.kibotu.android.deviceinfo.ui.other;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.ui.list.ListFragment;

import static net.kibotu.android.deviceinfo.library.Device.installedApps;
import static net.kibotu.android.deviceinfo.library.misc.ShellExtensions.isPhoneRooted;

/**
 * Created by Nyaruhodo on 21.02.2016.
 */
public class MiscellaneousFragment extends ListFragment {

    @Override
    public String getTitle() {
        return getString(R.string.menu_item_other);
    }

    @Override
    protected int getHomeIcon() {
        return R.drawable.others;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addHorizontallyCard("Rooted", isPhoneRooted(), "Determines if this device has been rooted.");
        addHorizontallyCard("Installed Apps", installedApps().size(), "Amount of currently installed applications.");
    }
}
