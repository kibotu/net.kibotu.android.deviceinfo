package net.kibotu.android.deviceinfo.library.buildinfo;

import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.provider.Settings;

import net.kibotu.android.deviceinfo.library.Device;
import net.kibotu.android.deviceinfo.library.misc.ReflectionHelper;
import net.kibotu.android.deviceinfo.library.version.Version;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH;

/**
 * Created by Nyaruhodo on 06.03.2016.
 */
public class BuildInfo {
    /**
     * More specifically, Settings.Secure.ANDROID_ID. A 64-bit number (as a hex string)
     * that is randomly generated on the device's first boot and should remain constant
     * for the lifetime of the device (The value may change if a factory reset is performed on the device.)
     * ANDROID_ID seems a good choice for a unique device identifier.
     * <p/>
     * Disadvantages:
     * - Not 100% reliable of Android prior to 2.2 (�Froyo�) devices
     * - Also, there has been at least one widely-observed bug in a popular
     * handset from a major manufacturer, where every instance has the same ANDROID_ID.
     */
    public static String getAndroidId() {
        return Settings.Secure.getString(Device.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static Map<String, FeatureInfo> getSystemAvailableFeatures() {
        final PackageManager pm = Device.getContext().getPackageManager();
        final FeatureInfo[] features = pm.getSystemAvailableFeatures();
        final LinkedHashMap<String, FeatureInfo> featureMap = new LinkedHashMap<String, FeatureInfo>();
        for (final FeatureInfo f : features) {
            if (f.name != null) {
                featureMap.put(f.name, f);
            }
        }
        return featureMap;
    }

    public static String getRadio() {
        if (Version.isAtLeastVersion(ICE_CREAM_SANDWICH)) {
            return ReflectionHelper.get(android.os.Build.class, "getRadioVersion", null);
        } else
            return android.os.Build.RADIO;
    }

    /**
     * System Property ro.serialno returns the serial number as unique number Works for Android 2.3 and above. Can return null.
     * <p/>
     * Disadvantages:
     * - Serial Number is not available with all android devices
     */
    public static String getSerialNumber() {
        String hwID = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            hwID = (String) (get.invoke(c, "ro.serialno", "unknown"));
        } catch (final Exception ignored) {
        }
        if (hwID != null) return hwID;
        try {
            Class<?> myclass = Class.forName("android.os.SystemProperties");
            Method[] methods = myclass.getMethods();
            Object[] params = new Object[]{"ro.serialno", "Unknown"};
            hwID = (String) (methods[2].invoke(myclass, params));
        } catch (final Exception ignored) {
        }
        return hwID;
    }

    public static int getVersionFromPackageManager() {
        PackageManager packageManager = Device.getContext().getPackageManager();
        FeatureInfo[] featureInfos = packageManager.getSystemAvailableFeatures();
        if (featureInfos != null && featureInfos.length > 0) {
            for (FeatureInfo featureInfo : featureInfos) {
                // Null feature name means this feature is the open gl es
                // version feature.
                if (featureInfo.name == null) {
                    if (featureInfo.reqGlEsVersion != FeatureInfo.GL_ES_VERSION_UNDEFINED) {
                        return getMajorVersion(featureInfo.reqGlEsVersion);
                    } else {
                        return 1; // Lack of property means OpenGL ES version 1
                    }
                }
            }
        }
        return 1;
    }

    /**
     * @see FeatureInfo#getGlEsVersion()
     */
    private static int getMajorVersion(int glEsVersion) {
        return ((glEsVersion & 0xffff0000) >> 16);
    }

    public static ArrayList<String> getPermissions() {
        final PackageManager pm = Device.getContext().getPackageManager();
        final ArrayList<String> permissions = new ArrayList<String>();
        final List<PermissionGroupInfo> lstGroups = pm.getAllPermissionGroups(0);
        for (final PermissionGroupInfo pgi : lstGroups) {
            // permissions.add(pgi.name);
            try {
                final List<PermissionInfo> lstPermissions = pm.queryPermissionsByGroup(pgi.name, 0);
                for (final PermissionInfo pi : lstPermissions) {
                    if (Device.getContext().checkCallingOrSelfPermission(pi.name) == PackageManager.PERMISSION_GRANTED)
                        permissions.add(pi.name);
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        return permissions;
    }

    public static List<String> getSharedLibraries() {
        return Arrays.asList(Device.getContext().getPackageManager().getSystemSharedLibraryNames());
    }
}
