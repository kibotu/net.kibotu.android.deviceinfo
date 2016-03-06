package net.kibotu.android.deviceinfo.ui.app;

import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.library.build.Build;
import net.kibotu.android.deviceinfo.model.ListItem;
import net.kibotu.android.deviceinfo.ui.list.ListFragment;

import static net.kibotu.android.deviceinfo.library.Device.*;
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

        addHorizontallyCard("App Version", Build.getVersionFromPackageManager(), "");
        addHorizontallyCard("Threads Count", Thread.getAllStackTraces().keySet().size(), "");
        addAppRuntimeMemory();
        addHorizontallyCard("APK Storage Path", getFileSize(getContext().getPackageCodePath()), "");
        addHorizontallyCard("Internal Storage Path", getFileSize(getContext().getFilesDir().getParent()), "");
        addVerticallyCard("Permissions", Build.getPermissions(), "All enabled permissions for this app.");
        addVerticallyCard("Shared Libraries", Build.getSharedLibraries(), "List of shared libraries that are available on the system.");
    }

    private void addAppRuntimeMemory() {
        addSubListItem(new ListItem().setLabel("Runtime Memory App")
                .addChild(new ListItem().setLabel("Max").setValue(formatBytes(getRuntimeMaxMemory())))
                .addChild(new ListItem().setLabel("Total").setValue(formatBytes(getRuntimeTotalMemory())))
                .addChild(new ListItem().setLabel("Free").setValue(formatBytes(getRuntimeFreeMemory())))
                .addChild(new ListItem().setLabel("Used").setValue(formatBytes(getUsedMemorySize())))
                .addChild(new ListItem().setLabel("Max").setValue(formatBytes(getRuntimeMaxMemory()))));
    }

    @Override
    protected int getHomeIcon() {
        return R.drawable.info_i;
    }
}
