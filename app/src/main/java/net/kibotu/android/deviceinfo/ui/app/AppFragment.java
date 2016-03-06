package net.kibotu.android.deviceinfo.ui.app;

import android.content.pm.FeatureInfo;
import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.library.build.BuildInfo;
import net.kibotu.android.deviceinfo.model.ListItem;
import net.kibotu.android.deviceinfo.ui.list.ListFragment;

import java.util.Map;

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

        addHorizontallyCard("App Version", BuildInfo.getVersionFromPackageManager(), "");
        addHorizontallyCard("Threads Count", Thread.getAllStackTraces().keySet().size(), "");
        addAppRuntimeMemory();
        addVerticallyCard("APK Storage Path", getFileSize(getContext().getPackageCodePath()), "");
        addVerticallyCard("Internal Storage Path", getFileSize(getContext().getFilesDir().getParent()), "");
        addPermissions();
        addSharedLibraries();
    }

    private void addAppRuntimeMemory() {
        addSubListItem(new ListItem().setLabel("Runtime Memory App")
                .addChild(new ListItem().setLabel("Max").setValue(formatBytes(getRuntimeMaxMemory())))
                .addChild(new ListItem().setLabel("Total").setValue(formatBytes(getRuntimeTotalMemory())))
                .addChild(new ListItem().setLabel("Free").setValue(formatBytes(getRuntimeFreeMemory())))
                .addChild(new ListItem().setLabel("Used").setValue(formatBytes(getUsedMemorySize())))
                .addChild(new ListItem().setLabel("Max").setValue(formatBytes(getRuntimeMaxMemory()))));
    }

    private void addPermissions() {
        final Map<String, FeatureInfo> systemAvailableFeatures = BuildInfo.getSystemAvailableFeatures();
        final ListItem item = new ListItem().setLabel("Permissions").setDescription("All enabled permissions for this app.");
        for(String permission:  BuildInfo.getPermissions())
            item.addChild(new ListItem().setLabel(permission));

        addSubListItem(item);
    }

    private void addSharedLibraries() {
        final Map<String, FeatureInfo> systemAvailableFeatures = BuildInfo.getSystemAvailableFeatures();
        final ListItem item = new ListItem().setLabel("Shared Libraries").setDescription( "List of shared libraries that are available on the system.");
        for(String lib:  BuildInfo.getSharedLibraries())
            item.addChild(new ListItem().setLabel(lib));

        addSubListItem(item);
    }

    @Override
    protected int getHomeIcon() {
        return R.drawable.info;
    }
}
