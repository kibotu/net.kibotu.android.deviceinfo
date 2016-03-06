package net.kibotu.android.deviceinfo.library;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.hardware.Sensor;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import net.kibotu.android.deviceinfo.library.battery.BatteryReceiver;
import net.kibotu.android.deviceinfo.library.bluetooth.Bluetooth;
import net.kibotu.android.deviceinfo.library.display.DisplayHelper;
import net.kibotu.android.deviceinfo.library.gpu.InfoLoader;
import net.kibotu.android.deviceinfo.library.gpu.OpenGLGles10Info;
import net.kibotu.android.deviceinfo.library.gpu.OpenGLGles20Info;
import net.kibotu.android.deviceinfo.library.misc.Callback;
import net.kibotu.android.deviceinfo.library.misc.ShellExtensions;
import net.kibotu.android.deviceinfo.library.version.Version;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import static android.os.Build.TAGS;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR2;
import static net.kibotu.android.deviceinfo.library.services.SystemService.*;

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

    public static Bluetooth getBluetooth() {
        return new Bluetooth(getContext());
    }

    public static List<Sensor> getSensorList() {
        return getSensorManager().getSensorList(Sensor.TYPE_ALL); // SensorManager.SENSOR_ALL
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static long getFileSizeDir(String path) {
        File directory = new File(path);

        if (!directory.exists()) return 0;

        StatFs statFs = new StatFs(directory.getAbsolutePath());
        long blockSize;
        if (Version.isAtLeastVersion(JELLY_BEAN_MR2)) {
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
        return ShellExtensions.executeShellCommand("which su");
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

    public static BatteryReceiver getBatteryReceiver() {
        return new BatteryReceiver();
    }

    public static void loadOpenGLGles10Info(final Callback<OpenGLGles10Info> callback) {
        new InfoLoader<>(new OpenGLGles10Info()).loadInfo(callback);
    }

    public static void loadOpenGLGles20Info(final Callback<OpenGLGles20Info> callback) {
        new InfoLoader<>(new OpenGLGles20Info()).loadInfo(callback);
    }

    public static View getContentRootView() {
        return ((Activity) getContext())
                .getWindow()
                .getDecorView()
                .findViewById(android.R.id.content);
    }

}
