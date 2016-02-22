package net.kibotu.android.deviceinfo.library;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.*;
import android.hardware.Sensor;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.webkit.WebView;
import net.kibotu.android.deviceinfo.library.legacy.Battery;
import net.kibotu.android.deviceinfo.library.legacy.Bluetooth;
import net.kibotu.android.deviceinfo.library.legacy.DisplayHelper;
import net.kibotu.android.deviceinfo.library.legacy.ProxySettings;

import java.io.File;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;

import static android.os.Build.TAGS;
import static android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR2;
import static net.kibotu.android.deviceinfo.library.SystemService.*;

/**
 * Created by Nyaruhodo on 20.02.2016.
 */
final public class Device {

    private static final String TAG = Device.class.getSimpleName();
    private static Context context;
    private static DisplayHelper displayHelper;

    private Device() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static void setContext(Activity context) {
        Device.context = context;
        displayHelper = new DisplayHelper(context);
    }

    public static Context getContext() {
        if (context == null)
            throw new IllegalStateException("'context' must not be null. Please invoke Device.setContext().");
        return context;
    }

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
        return Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getRadio() {
        if (isAtLeastVersion(ICE_CREAM_SANDWICH)) {
            return ReflectionHelper.get(Build.class, "getRadioVersion", null);
        } else
            return android.os.Build.RADIO;
    }

    public static boolean isAtLeastVersion(@VersionCode final int version) {
        return Build.VERSION.SDK_INT >= version;
    }

    public static Map<String, FeatureInfo> getSystemAvailableFeatures() {
        final PackageManager pm = getContext().getPackageManager();
        final FeatureInfo[] features = pm.getSystemAvailableFeatures();
        final LinkedHashMap<String, FeatureInfo> featureMap = new LinkedHashMap<>();
        for (final FeatureInfo f : features) {
            if (f.name != null) {
                featureMap.put(f.name, f);
            }
        }
        return featureMap;
    }

    public static boolean supportsOpenGLES2() {
        return getOpenGLVersion() >= 0x20000;
    }

    public static int getOpenGLVersion() {
        return getActivityManager()
                .getDeviceConfigurationInfo()
                .reqGlEsVersion;
    }

    @Deprecated
    public static String getUsableResolution() {
        return String.format("%dx%d", Math.max(DisplayHelper.mScreenWidth, DisplayHelper.mScreenHeight), Math.min(DisplayHelper.mScreenWidth, DisplayHelper.mScreenHeight));
    }

    @Deprecated
    public static String getUsableResolutionDp() {
        return String.format("%.0fx%.0f", Math.max(DisplayHelper.mScreenWidth, DisplayHelper.mScreenHeight) / DisplayHelper.mDensity, Math.min(DisplayHelper.mScreenWidth, DisplayHelper.mScreenHeight) / DisplayHelper.mDensity);
    }

    @Deprecated
    public static String getResolution() {
        return String.format("%dx%d", Math.max(DisplayHelper.absScreenWidth, DisplayHelper.absScreenHeight), Math.min(DisplayHelper.absScreenWidth, DisplayHelper.absScreenHeight));
    }

    @Deprecated
    public static String getResolutionDp() {
        return String.format("%.0fx%.0f", Math.max(DisplayHelper.absScreenWidth, DisplayHelper.absScreenHeight) / DisplayHelper.mDensity, Math.min(DisplayHelper.absScreenWidth, DisplayHelper.absScreenHeight) / DisplayHelper.mDensity);
    }

    public static Display getDefaultDisplay() {
        return getWindowManager().getDefaultDisplay();
    }

    public static float getRefreshRate() {
        return getDefaultDisplay().getRefreshRate();
    }

    public static DisplayMetrics getDisplayMetrics() {
        DisplayMetrics metrics = new DisplayMetrics();
        getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

    /**
     * Returns the unique subscriber ID, for example, the IMSI for a GSM phone.
     * <p/>
     * Disadvantages:
     * - Android devices should have telephony services
     * - It doesn't work reliably
     * - Serial Number
     * - When it does work, that value survives device wipes (Factory resets)
     * and thus you could end up making a nasty mistake when one of your customers wipes their device
     * and passes it on to another person.
     */
    public static String getSubscriberIdFromTelephonyManager() {
        return getTelephonyManager().getSubscriberId();
    }

    /**
     * Returns the unique device ID. for example,the IMEI for GSM and the MEID or ESN for CDMA phones.
     * <p/>
     * IMPORTANT! it requires READ_PHONE_STATE permission in AndroidManifest.xml
     * <p/>
     * Disadvantages:
     * - Android devices should have telephony services
     * - It doesn't work reliably
     * - Serial Number
     * - When it does work, that value survives device wipes (Factory resets)
     * and thus you could end up making a nasty mistake when one of your customers wipes their device
     * and passes it on to another person.
     */
    public static String getDeviceIdFromTelephonyManager() {
        return getTelephonyManager().getDeviceId();
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

    /**
     * Returns MAC Address.
     * <p/>
     * IMPORTANT! requires {@link android.Manifest.permission#ACCESS_WIFI_STATE}
     * <p/>
     * Disadvantages:
     * - Device should have Wi-Fi (where not all devices have Wi-Fi)
     * - If Wi-Fi present in Device should be turned on otherwise does not report the MAC address
     */
    public static String getMacAdress() {
        return getWifiManager().getConnectionInfo().getMacAddress();
    }

    /**
     * Returns MAC address of the given interface name.
     *
     * @param interfaceName eth0, wlan0 or NULL=use first interface
     * @return mac address or empty string
     */
    public static String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac == null) return "";
                StringBuilder buf = new StringBuilder();
                for (final byte aMac : mac) buf.append(String.format("%02X:", aMac));
                if (buf.length() > 0) buf.deleteCharAt(buf.length() - 1);
                return buf.toString();
            }
        } catch (final Exception ignored) {
        } // for now eat exceptions
        return "";
       /*try {
           // this is so Linux hack
           return loadFileAsString("/sys/class/net/" +interfaceName + "/address").toUpperCase().trim();
       } catch (IOException ex) {
           return null;
       }*/
    }

    /**
     * Get IP address from first non-localhost interface
     *
     * @param useIPv4 true=return ipv4, false=return ipv6
     * @return address or empty string
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                                return delim < 0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (final Exception ignored) {
            // for now eat exceptions
        }
        return "";
    }

    public static String getUserAgent() {
        return new WebView(getContext()).getSettings().getUserAgentString();
    }

    public static ProxySettings getProxySettings() {
        return new ProxySettings(getContext());
    }

    public static Bluetooth getBluetooth() {
        return new Bluetooth(getContext());
    }

    public static List<Sensor> getSensorList() {
        return getSensorManager().getSensorList(Sensor.TYPE_ALL); // SensorManager.SENSOR_ALL
    }

    public static int getVersionFromPackageManager() {
        PackageManager packageManager = getContext().getPackageManager();
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
     * @see android.content.pm.FeatureInfo#getGlEsVersion()
     */
    private static int getMajorVersion(int glEsVersion) {
        return ((glEsVersion & 0xffff0000) >> 16);
    }


    /**
     * credits:
     * http://stackoverflow.com/questions/3170691/how-to-get-current-memory-usage-in-android
     */
    public static long getFreeMemoryByActivityService() {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        getActivityManager().getMemoryInfo(mi);
        return mi.availMem;
    }

    public static boolean isLowMemory() {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        getActivityManager().getMemoryInfo(mi);
        return mi.lowMemory;
    }

    @Deprecated
    public static long getFreeMemoryByEnvironment() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    @Deprecated
    public static long getTotalMemoryByEnvironment() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getBlockCount();
        return availableBlocks * blockSize;
    }

    public static long getRuntimeTotalMemory() {
        long memory = 0L;
        try {
            final Runtime info = Runtime.getRuntime();
            memory = info.totalMemory();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return memory;
    }

    public static long getRuntimeMaxMemory() {
        long memory = 0L;
        try {
            Runtime info = Runtime.getRuntime();
            memory = info.maxMemory();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return memory;
    }

    public static long getRuntimeFreeMemory() {
        long memory = 0L;
        try {
            final Runtime info = Runtime.getRuntime();
            memory = info.freeMemory();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return memory;
    }

    public static long getUsedMemorySize() {
        long usedSize = 0L;
        try {
            final Runtime info = Runtime.getRuntime();
            usedSize = info.totalMemory() - info.freeMemory();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return usedSize;
    }

    /**
     * @see <a href="http://stackoverflow.com/questions/2630158/detect-application-heap-size-in-android">detect-application-heap-size-in-android</a>
     */
    public static long getMaxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    public static int getMemoryClass() {
        return getActivityManager().getMemoryClass();
    }

    public static String getFileSize(String file) {
        return getFileSize(new File(file));
    }

    private static volatile HashMap<File, String> cache;

    public static String getFileSize(File file) {
        if (cache == null) cache = new HashMap<>();
        if (cache.containsKey(file)) return cache.get(file);
        long size = Device.getFileSizeDir(file.toString());
        String ret = size == 0 ? file.toString() : file + " (" + size + " MB)";
        cache.put(file, ret);
        return ret;
    }

    public static long getFileSizeDir(String path) {
        File directory = new File(path);

        if (!directory.exists()) return 0;

        StatFs statFs = new StatFs(directory.getAbsolutePath());
        long blockSize;
        if (isAtLeastVersion(JELLY_BEAN_MR2)) {
            blockSize = statFs.getBlockSizeLong();
        } else {
            blockSize = statFs.getBlockSize();
        }

        return getDirectorySize(directory, blockSize) / (1024 * 1024);
    }

    public static long getDirectorySize(File directory, long blockSize) {
        File[] files = directory.listFiles();
        if (files == null) {
            return 0;
        }

        // space used by directory itself
        long size = directory.length();

        for (File file : files) {
            if (file.isDirectory()) {
                // space used by subdirectory
                size += getDirectorySize(file, blockSize);
            } else {
                // file size need to rounded up to full block sizes
                // (not a perfect function, it adds additional block to 0 sized files
                // and file who perfectly fill their blocks)
                size += (file.length() / blockSize + 1) * blockSize;
            }
        }
        return size;
    }

    public static ArrayList<String> getPermissions() {
        final PackageManager pm = getContext().getPackageManager();
        final ArrayList<String> permissions = new ArrayList<>();
        final List<PermissionGroupInfo> lstGroups = pm.getAllPermissionGroups(0);
        for (final PermissionGroupInfo pgi : lstGroups) {
            // permissions.add(pgi.name);
            try {
                final List<PermissionInfo> lstPermissions = pm.queryPermissionsByGroup(pgi.name, 0);
                for (final PermissionInfo pi : lstPermissions) {
                    if (getContext().checkCallingOrSelfPermission(pi.name) == PackageManager.PERMISSION_GRANTED)
                        permissions.add(pi.name);
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        return permissions;
    }

    public static List<String> getSharedLibraries() {
        return Arrays.asList(getContext().getPackageManager().getSystemSharedLibraryNames());
    }

    /**
     * Checks if the phone is rooted.
     *
     * @return <code>true</code> if the phone is rooted, <code>false</code>
     * otherwise.
     * @credits: http://stackoverflow.com/a/6425854
     */
    public static boolean isPhoneRooted() {

        // get from build info
        String buildTags = TAGS;
        if (buildTags != null && buildTags.contains("test-keys")) {
            Log.v(TAG, "is rooted by build tag");
            return true;
        }

        // check if /system/app/Superuser.apk is present
        try {
            File file = new File("/system/app/Superuser.apk");
            if (file.exists()) {
                Log.v(TAG, "is rooted by /system/app/Superuser.apk");
                return true;
            }
        } catch (Throwable e1) {
            // ignore
        }

        // from excecuting shell command
        return executeShellCommand("which su");
    }

    /**
     * Executes a shell command.
     *
     * @param command - Unix shell command.
     * @return <code>true</code> if shell command was successful.
     * @credits http://stackoverflow.com/a/15485210
     */
    public static boolean executeShellCommand(final String command) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            Log.v(TAG, "'" + command + "' successfully excecuted.");
            Log.v(TAG, "is rooted by su command");
            return true;
        } catch (final Exception e) {
            Log.e(TAG, "" + e.getMessage());
            return false;
        } finally {
            if (process != null) {
                try {
                    process.destroy();
                } catch (final Exception e) {
                    Log.e(TAG, "" + e.getMessage());
                }
            }
        }
    }

    public static List<ResolveInfo> installedApps() {
        final PackageManager pm = getContext().getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> apps = pm.queryIntentActivities(intent, PackageManager.GET_META_DATA);
//        for(int i = 0; i < apps.size(); ++i) {
//            Logger.v(""+apps.get(i).activityInfo.name);
//        }
        return apps;
    }

    public static Battery getBattery() {
        return new Battery();
    }
}
