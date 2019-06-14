package net.kibotu.android.deviceinfo.ui.app;

import android.content.pm.FeatureInfo;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;

import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.library.buildinfo.BuildInfo;
import net.kibotu.android.deviceinfo.model.ListItem;
import net.kibotu.android.deviceinfo.ui.list.ListFragment;

import java.util.Map;

import static net.kibotu.android.deviceinfo.library.Device.getFileSize;
import static net.kibotu.android.deviceinfo.library.Device.getRuntimeFreeMemory;
import static net.kibotu.android.deviceinfo.library.Device.getRuntimeMaxMemory;
import static net.kibotu.android.deviceinfo.library.Device.getRuntimeTotalMemory;
import static net.kibotu.android.deviceinfo.library.Device.getUsedMemorySize;
import static net.kibotu.android.deviceinfo.library.ViewHelper.formatBytes;

/**
 * Created by Nyaruhodo on 21.02.2016.
 */
public class AppFragment extends ListFragment {

    @Override
    public String getTitle() {
        return getString(R.string.menu_item_app);
    }

    @Override
    protected int getHomeIcon() {
        return R.drawable.info;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        for (String permission : BuildInfo.getPermissions())
            item.addChild(new ListItem().setLabel(permission));

        addSubListItem(item);
    }

    private void addSharedLibraries() {
        final Map<String, FeatureInfo> systemAvailableFeatures = BuildInfo.getSystemAvailableFeatures();
        final ListItem item = new ListItem().setLabel("Shared Libraries").setDescription("List of shared libraries that are available on the system.");
        for (String lib : BuildInfo.getSharedLibraries())
            item.addChild(new ListItem().setLabel(lib));

        addSubListItem(item);
    }
}
