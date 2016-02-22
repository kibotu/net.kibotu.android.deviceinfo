package net.kibotu.android.deviceinfo.ui.app;

import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.library.Device;
import net.kibotu.android.deviceinfo.ui.list.ListFragment;

import static net.kibotu.android.deviceinfo.library.Device.*;
import static net.kibotu.android.deviceinfo.ui.ViewHelper.BR;
import static net.kibotu.android.deviceinfo.ui.ViewHelper.formatBytes;

/**
 * Created by Nyaruhodo on 21.02.2016.
 */
public class AppFragment extends ListFragment {

    @Override
    protected String getTitle() {
        return getString(R.string.menu_item_app);
    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();

        addListItemHorizontally("App Version", getVersionFromPackageManager(), "");
        addListItemHorizontally("Threads Count", Thread.getAllStackTraces().keySet().size(), "");
        addAppRuntimeMemory();
        addListItemHorizontally("APK Storage Path", getFileSize(getContext().getPackageCodePath()), "");
        addListItemHorizontally("Internal Storage Path", getFileSize(getContext().getFilesDir().getParent()), "");
        addListItemVertically("Permissions",  getPermissions(), "All enabled permissions for this app.");
        addListItemVertically("Shared Libraries",  getSharedLibraries(),"List of shared libraries that are available on the system.");
    }

    private void addAppRuntimeMemory() {
        String keys = "Max:" + BR;
        String values = formatBytes(getRuntimeMaxMemory()) + BR;
        keys += "Total:" + BR;
        values += formatBytes(getRuntimeTotalMemory()) + BR;
        keys += "Free:" + BR;
        values += formatBytes(getRuntimeFreeMemory()) + BR;
        keys += "Used:" + BR;
        values += formatBytes(getUsedMemorySize()) + BR;
        addListItemWithTitle("Runtime Memory App", keys, values, "Currently reserved runtime memory by this App.");
    }
}
