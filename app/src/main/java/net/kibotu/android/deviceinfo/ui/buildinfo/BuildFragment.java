package net.kibotu.android.deviceinfo.ui.buildinfo;

import android.content.pm.FeatureInfo;

import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.library.buildinfo.BuildInfo;
import net.kibotu.android.deviceinfo.library.version.Version;
import net.kibotu.android.deviceinfo.model.ListItem;
import net.kibotu.android.deviceinfo.ui.list.ListFragment;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormat;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import static android.os.Build.BOARD;
import static android.os.Build.BOOTLOADER;
import static android.os.Build.BRAND;
import static android.os.Build.CPU_ABI;
import static android.os.Build.CPU_ABI2;
import static android.os.Build.DEVICE;
import static android.os.Build.DISPLAY;
import static android.os.Build.FINGERPRINT;
import static android.os.Build.HARDWARE;
import static android.os.Build.HOST;
import static android.os.Build.ID;
import static android.os.Build.MANUFACTURER;
import static android.os.Build.MODEL;
import static android.os.Build.PRODUCT;
import static android.os.Build.SERIAL;
import static android.os.Build.SUPPORTED_ABIS;
import static android.os.Build.TAGS;
import static android.os.Build.TIME;
import static android.os.Build.TYPE;
import static android.os.Build.UNKNOWN;
import static android.os.Build.USER;
import static android.os.Build.VERSION;
import static android.os.Build.VERSION_CODES;
import static net.kibotu.android.deviceinfo.library.ViewHelper.formatSdkString;

/**
 * Created by Nyaruhodo on 21.02.2016.
 */
public class BuildFragment extends ListFragment {

    @Override
    protected String getTitle() {
        return getString(R.string.title_build);
    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();

        // http://developer.android.com/reference/android/os/Build.html

        addHorizontallyCard("Model", MODEL, "The end-user-visible name for the end product.");
        addHorizontallyCard("Manufacturer", MANUFACTURER, "The manufacturer of the product/hardware.");
        addHorizontallyCard("Release", VERSION.RELEASE, "The user-visible version string. E.g., \"1.0\" or \"3.4b5\".");
        addHorizontallyCard("SDK_INT", formatSdkString(Version.getOsAsString(VERSION.SDK_INT)) + " (" + VERSION.SDK_INT + ")", "The user - visible SDK version of the framework; its possible values are defined in BuildInfo.VERSION_CODES.");
        addHorizontallyCard("Android Id", BuildInfo.getAndroidId(), "More specifically, Settings.Secure.ANDROID_ID. A 64-bit number (as a hex string) that is randomly generated on the device's first boot and should remain constant for the lifetime of the device (The value may change if a factory reset is performed on the device.)");
        addVerticallyCard("TIME", PeriodFormat.getDefault().print(new Period(new DateTime(TIME), DateTime.now())), "BuildInfo Time: " + new Date(TIME).toString());
        if (Version.isAtLeastVersion(VERSION_CODES.LOLLIPOP)) {
            addHorizontallyCard("SUPPORTED_ABIS", Arrays.toString(SUPPORTED_ABIS), "The name of the instruction set (CPU type + ABI convention) of native code.");
        }
        addHorizontallyCard("CPU_ABI", CPU_ABI, "The name of the instruction set (CPU type + ABI convention) of native code.");
        addHorizontallyCard("CPU_ABI2", CPU_ABI2, "The name of the second instruction set (CPU type + ABI convention) of native code.");


        addHorizontallyCard("Board", BOARD, "The name of the underlying board, like \"goldfish\"");
        addHorizontallyCard("Bootloader", BOOTLOADER, "The system bootloader version number");
        addHorizontallyCard("Brand", BRAND, "The consumer-visible brand with which the product/hardware will be associated, if any.");
        addHorizontallyCard("Device", DEVICE, "The name of the industrial design.");
        addHorizontallyCard("Display", DISPLAY, "A build ID string meant for displaying to the user.");
        addVerticallyCard("Fingerprint", FINGERPRINT, "A string that uniquely identifies this build.");
        addHorizontallyCard("Hardware", HARDWARE, "The name of the hardware (from the kernel command line or /proc).");
        addHorizontallyCard("Host", HOST, "description");
        addHorizontallyCard("Id", ID, "Either a changelist number, or a label like \"M4-rc20\".");
        addHorizontallyCard("Product", PRODUCT, "The name of the overall product.");
        addHorizontallyCard("Radio", BuildInfo.getRadio(), "The radio firmware version number. Note API >= 14: use getRadioVersion()");
        addHorizontallyCard("Serial", SERIAL, "A hardware serial number, if available.");
        addHorizontallyCard("Tags", TAGS, "Comma-separated tags describing the build, like \"unsigned,debug\".");
        addHorizontallyCard("Type", TYPE, "The type of build, like \"user\" or \"eng\".");
        addHorizontallyCard("User", USER, "description");
        addHorizontallyCard("Unknown", UNKNOWN, "Value used for when a build property is unknown.");

        // http://developer.android.com/reference/android/os/Build.VERSION.html
        addHorizontallyCard("Codename", VERSION.CODENAME, "The current development codename, or the string \"REL\" if this is a release build.");
        addHorizontallyCard("Incremental", VERSION.INCREMENTAL, "The internal value used by the underlying source control to represent this build. E.g., a perforce changelist number or a git hash. ");

        addSystemFeatures();
    }

    private void addSystemFeatures() {
        final Map<String, FeatureInfo> systemAvailableFeatures = BuildInfo.getSystemAvailableFeatures();
        final ListItem item = new ListItem().setLabel("Features").setDescription("List of features that are available on the system. This device supports " + systemAvailableFeatures.size() + " Features.");
        for (final Map.Entry<String, FeatureInfo> entry : systemAvailableFeatures.entrySet()) {
            item.addChild(new ListItem().setLabel(entry.getKey()));

        }

        addSubListItem(item);
    }

    @Override
    protected int getHomeIcon() {
        return R.drawable.build;
    }
}
