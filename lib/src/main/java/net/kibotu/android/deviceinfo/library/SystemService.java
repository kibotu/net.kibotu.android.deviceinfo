package net.kibotu.android.deviceinfo.library;

import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.*;
import android.app.admin.DevicePolicyManager;
import android.app.job.JobScheduler;
import android.app.usage.NetworkStatsManager;
import android.app.usage.UsageStatsManager;
import android.appwidget.AppWidgetManager;
import android.bluetooth.BluetoothManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.RestrictionsManager;
import android.content.pm.LauncherApps;
import android.hardware.ConsumerIrManager;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraManager;
import android.hardware.display.DisplayManager;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.input.InputManager;
import android.hardware.usb.UsbManager;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaRouter;
import android.media.midi.MidiManager;
import android.media.projection.MediaProjectionManager;
import android.media.session.MediaSessionManager;
import android.media.tv.TvInputManager;
import android.net.ConnectivityManager;
import android.net.nsd.NsdManager;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.nfc.NfcManager;
import android.os.*;
import android.os.storage.StorageManager;
import android.print.PrintManager;
import android.service.wallpaper.WallpaperService;
import android.telecom.TelecomManager;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.CaptioningManager;
import android.view.inputmethod.InputMethodManager;
import android.view.textservice.TextServicesManager;

import static android.os.Build.VERSION_CODES.*;
import static net.kibotu.android.deviceinfo.library.Device.getContext;

/**
 * Created by Nyaruhodo on 22.02.2016.
 *
 * @see <a href="http://developer.android.com/reference/android/content/Context.html#getSystemService%28java.lang.Class%3CT%3E%29">getSystemService</a>
 */
public class SystemService {

    /**
     * For giving the user feedback for UI events through the registered event listeners.
     */
    @TargetApi(DONUT)
    public static AccessibilityManager getAccessibilityManager() {
        return (AccessibilityManager) getContext().getSystemService(Context.ACCESSIBILITY_SERVICE);
    }

    /**
     * For receiving intents at a time of your choosing.
     */
    @TargetApi(ECLAIR)
    public static AccountManager getAccountManager() {
        return (AccountManager) getContext().getSystemService(Context.ACCOUNT_SERVICE);
    }

    /**
     * A ActivityManager for interacting with the global activity state of the system.
     */
    public static ActivityManager getActivityManager() {
        return (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
    }

    /**
     * A AlarmManager for receiving intents at the time of your choosing.
     */
    public static AlarmManager getAlarmManager() {
        return (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
    }

    /**
     * For accessing AppWidgets.
     */
    @TargetApi(LOLLIPOP)
    public static AppWidgetManager getAppWidgetManager() {
        return (AppWidgetManager) getContext().getSystemService(Context.APPWIDGET_SERVICE);
    }

    /**
     * For handling management of volume, ringer modes and audio routing.
     */
    public static AudioManager getAudioManager() {
        return (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
    }

    /**
     * A BatteryManager for managing battery state
     */
    @TargetApi(LOLLIPOP)
    public static BatteryManager getBatteryManager() {
        return (BatteryManager) getContext().getSystemService(Context.BATTERY_SERVICE);
    }

    /**
     * For using Bluetooth.
     */
    @TargetApi(JELLY_BEAN_MR2)
    public static BluetoothManager getBluetoothManager() {
        return (BluetoothManager) getContext().getSystemService(Context.BLUETOOTH_SERVICE);
    }

    /**
     * For interacting with camera devices.
     */
    @TargetApi(LOLLIPOP)
    public static CameraManager getCameraManager() {
        return (CameraManager) getContext().getSystemService(Context.CAMERA_SERVICE);
    }

    /**
     * For obtaining captioning properties and listening for changes in captioning preferences.
     */
    @TargetApi(LOLLIPOP)
    public static CaptioningManager getCaptioningManager() {
        return (CaptioningManager) getContext().getSystemService(Context.CAPTIONING_SERVICE);
    }

    /**
     * For obtaining captioning properties and listening for changes in captioning preferences.
     */
    @TargetApi(M)
    public static CarrierConfigManager getCarrierConfigManager() {
        return (CarrierConfigManager) getContext().getSystemService(Context.CARRIER_CONFIG_SERVICE);
    }

    /**
     * For accessing and modifying ClipboardManager for accessing and modifying the contents of the global clipboard.
     */
    public static ClipboardManager getClipboardManager() {
        return (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
    }

    /**
     * A ConnectivityManager for handling management of network connections.
     */
    public static ConnectivityManager getConnectivityManager() {
        return (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * For transmitting infrared signals from the device.
     */
    @TargetApi(KITKAT)
    public static ConsumerIrManager getConsumerIrManager() {
        return (ConsumerIrManager) getContext().getSystemService(Context.CONSUMER_IR_SERVICE);
    }

    /**
     * For working with global device policy management.
     */
    public static DevicePolicyManager getDevicePolicyManager() {
        return (DevicePolicyManager) getContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
    }

    /**
     * For interacting with display devices.
     */
    @TargetApi(JELLY_BEAN_MR1)
    public static DisplayManager getDisplayManager() {
        return (DisplayManager) getContext().getSystemService(Context.DISPLAY_SERVICE);
    }

    /**
     * A DownloadManager for requesting HTTP downloads
     */
    public static DownloadManager getDownloadManager() {
        return (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
    }

    /**
     * Instance for recording diagnostic logs.
     */
    public static DropBoxManager getDropBoxManager() {
        return (DropBoxManager) getContext().getSystemService(Context.DROPBOX_SERVICE);
    }

    /**
     * For handling management of fingerprints.
     */
    @TargetApi(M)
    public static FingerprintManager getFingerprintManager() {
        return (FingerprintManager) getContext().getSystemService(Context.FINGERPRINT_SERVICE);
    }

    /**
     * An InputMethodManager for management of input methods.
     */
    public static InputMethodManager getInputMethodManager() {
        return (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    /**
     * For interacting with input devices.
     */
    @TargetApi(JELLY_BEAN)
    public static InputManager getInputManager() {
        return (InputManager) getContext().getSystemService(Context.INPUT_SERVICE);
    }

    /**
     * A JobScheduler for managing scheduled tasks
     */
    @TargetApi(LOLLIPOP)
    public static JobScheduler getJobScheduler() {
        return (JobScheduler) getContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
    }

    /**
     * A KeyguardManager for controlling keyguard.
     */
    public static KeyguardManager getKeyguardManager() {
        return (KeyguardManager) getContext().getSystemService(Context.KEYGUARD_SERVICE);
    }

    /**
     * For querying and monitoring launchable apps across profiles of a user.
     */
    @TargetApi(LOLLIPOP)
    public static LauncherApps getLauncherApps() {
        return (LauncherApps) getContext().getSystemService(Context.LAUNCHER_APPS_SERVICE);
    }

    /**
     * A LayoutInflater for inflating layout resources in this context.
     */
    public static LayoutInflater getLayoutInflater() {
        return (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * A LocationManager for controlling location (e.g., GPS) updates.
     */
    public static LocationManager getLocationManager() {
        return (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     * Instance for managing media projection sessions.
     */
    @TargetApi(LOLLIPOP)
    public static MediaProjectionManager getMediaProjectionManager() {
        return (MediaProjectionManager) getContext().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
    }

    /**
     * For controlling and managing routing of media.
     */
    @TargetApi(JELLY_BEAN)
    public static MediaRouter getMediaRouter() {
        return (MediaRouter) getContext().getSystemService(Context.MEDIA_ROUTER_SERVICE);
    }

    /**
     * For managing media Sessions.
     */
    @TargetApi(LOLLIPOP)
    public static MediaSessionManager getMediaSessionManager() {
        return (MediaSessionManager) getContext().getSystemService(Context.MEDIA_SESSION_SERVICE);
    }

    /**
     * For accessing the MIDI service.
     */
    @TargetApi(M)
    public static MidiManager getMidiManager() {
        return (MidiManager) getContext().getSystemService(Context.MIDI_SERVICE);
    }

    /**
     * A NetworkStatsManager for querying network usage statistics.
     */
    @TargetApi(M)
    public static NetworkStatsManager getNetworkStatsManager() {
        return (NetworkStatsManager) getContext().getSystemService(Context.NETWORK_STATS_SERVICE);
    }

    /**
     * For using NFC.
     */
    public static NfcManager getNfcManager() {
        return (NfcManager) getContext().getSystemService(Context.NFC_SERVICE);
    }

    /**
     * A NotificationManager for informing the user of background events.
     */
    public static NotificationManager getNotificationManager() {
        return (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
    }

    /**
     * For handling management of network service discovery
     */
    @TargetApi(JELLY_BEAN)
    public static NsdManager geNsdManager() {
        return (NsdManager) getContext().getSystemService(Context.NSD_SERVICE);
    }

    /**
     * A PowerManager for controlling power management.
     */
    public static PowerManager getPowerManager() {
        return (PowerManager) getContext().getSystemService(Context.POWER_SERVICE);
    }

    /**
     * For printing and managing printers and print tasks.
     */
    @TargetApi(KITKAT)
    public static PrintManager getPrintManager() {
        return (PrintManager) getContext().getSystemService(Context.PRINT_SERVICE);
    }

    /**
     * For retrieving application restrictions and requesting permissions for restricted operations.
     */
    @TargetApi(LOLLIPOP)
    public static RestrictionsManager getRestrictionsManager() {
        return (RestrictionsManager) getContext().getSystemService(Context.RESTRICTIONS_SERVICE);
    }

    /**
     * A SearchManager for handling search.
     */
    public static SearchManager getSearchManager() {
        return (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
    }

    /**
     * For accessing sensors.
     */
    public static SensorManager getSensorManager() {
        return (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
    }

    /**
     * For accessing system storage functions.
     */
    public static StorageManager getStorageManager() {
        return (StorageManager) getContext().getSystemService(Context.STORAGE_SERVICE);
    }

    /**
     * To manage telecom-related features of the device.
     */
    @TargetApi(LOLLIPOP)
    public static TelecomManager getTelecomManager() {
        return (TelecomManager) getContext().getSystemService(Context.TELECOM_SERVICE);
    }

    /**
     * For handling management the telephony features of the device
     */
    @TargetApi(LOLLIPOP_MR1)
    public static TelephonyManager getTelephonyManager() {
        return (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
    }

    /**
     * For handling management the telephony subscriptions of the device.
     */
    @TargetApi(LOLLIPOP_MR1)
    public static SubscriptionManager getSubscriptionManager() {
        return (SubscriptionManager) getContext().getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
    }

    /**
     * For accessing text services.
     */
    @TargetApi(ICE_CREAM_SANDWICH)
    public static TextServicesManager getTextServicesManager() {
        return (TextServicesManager) getContext().getSystemService(Context.TEXT_SERVICES_MANAGER_SERVICE);
    }

    /**
     * For accessing text services.
     */
    @TargetApi(LOLLIPOP)
    public static TvInputManager getTvInputManager() {
        return (TvInputManager) getContext().getSystemService(Context.TV_INPUT_SERVICE);
    }

    /**
     * An UiModeManager for controlling UI modes.
     */
    public static UiModeManager getUiModeManager() {
        return (UiModeManager) getContext().getSystemService(Context.UI_MODE_SERVICE);
    }

    /**
     * For querying device usage stats.
     */
    @TargetApi(LOLLIPOP_MR1)
    public static UsageStatsManager getUsageStatsManager() {
        return (UsageStatsManager) getContext().getSystemService(Context.USAGE_STATS_SERVICE);
    }

    /**
     * For access to USB devices (as a USB host) and for controlling this device's behavior as a USB device.
     */
    public static UsbManager getUsbManager() {
        return (UsbManager) getContext().getSystemService(Context.USB_SERVICE);
    }

    /**
     * For managing users on devices that support multiple users.
     */
    @TargetApi(JELLY_BEAN_MR1)
    public static UserManager getUserManager() {
        return (UserManager) getContext().getSystemService(Context.USER_SERVICE);
    }

    /**
     * A Vibrator for interacting with the vibrator hardware.
     */
    public static Vibrator getVibrator() {
        return (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
    }

    /**
     * A Vibrator for interacting with the vibrator hardware.
     */
    public static WallpaperService getWallpaperService() {
        return (WallpaperService) getContext().getSystemService(Context.WALLPAPER_SERVICE);
    }

    /**
     * A WifiP2pManager for management of Wi-Fi Direct connectivity.
     */
    public static WifiP2pManager getWifiP2pManager() {
        return (WifiP2pManager) getContext().getSystemService(Context.WIFI_P2P_SERVICE);
    }

    /**
     * A WifiManager for management of Wi-Fi connectivity.
     */
    public static WifiManager getWifiManager() {
        return (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
    }

    /**
     * The top-level window manager in which you can place custom windows. The returned object is a WindowManager.
     */
    public static WindowManager getWindowManager() {
        return (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
    }
}
