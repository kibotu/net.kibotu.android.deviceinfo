package net.kibotu.android.deviceinfo.ui.app;

import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.ui.list.ListFragment;

import static net.kibotu.android.deviceinfo.library.Device.*;

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

        addHorizontallyCard("App Version", getVersionFromPackageManager(), "");
        addHorizontallyCard("Threads Count", Thread.getAllStackTraces().keySet().size(), "");
        addAppRuntimeMemory();
        addHorizontallyCard("APK Storage Path", getFileSize(getContext().getPackageCodePath()), "");
        addHorizontallyCard("Internal Storage Path", getFileSize(getContext().getFilesDir().getParent()), "");
        addVerticallyCard("Permissions",  getPermissions(), "All enabled permissions for this app.");
        addVerticallyCard("Shared Libraries",  getSharedLibraries(),"List of shared libraries that are available on the system.");
    }

    private void addAppRuntimeMemory() {
//        String keys = "Max:" + BR;
//        String values = formatBytes(getRuntimeMaxMemory()) + BR;
//        keys += "Total:" + BR;
//        values += formatBytes(getRuntimeTotalMemory()) + BR;
//        keys += "Free:" + BR;
//        values += formatBytes(getRuntimeFreeMemory()) + BR;
//        keys += "Used:" + BR;
//        values += formatBytes(getUsedMemorySize()) + BR;
//        addSubListItem(new ListItem().setLabel("Runtime Memory App").addChild(new ListItem().setLabel()), keys, values, "Currently reserved runtime memory by this App.");
    }

    @Override
    protected int getHomeIcon() {
        return R.drawable.info_i;
    }
}
