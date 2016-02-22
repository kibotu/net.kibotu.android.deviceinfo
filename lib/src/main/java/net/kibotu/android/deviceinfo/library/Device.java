package net.kibotu.android.deviceinfo.library;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import net.kibotu.android.deviceinfo.library.legacy.DisplayHelper;

import java.util.LinkedHashMap;
import java.util.Map;

import static net.kibotu.android.deviceinfo.library.SystemService.getActivityManager;
import static net.kibotu.android.deviceinfo.library.SystemService.getWindowManager;

/**
 * Created by Nyaruhodo on 20.02.2016.
 */
final public class Device {

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
        String radio = android.os.Build.RADIO;
        if (isAtLeastVersion(Build.VERSION_CODES.ICE_CREAM_SANDWICH))
            radio = ReflectionHelper.get(android.os.Build.class, "getRadioVersion", null);
        return radio;
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

}
