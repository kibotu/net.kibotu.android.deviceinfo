package net.kibotu.android.deviceid;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import org.apache.http.conn.util.InetAddressUtils;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 * Variety ways to retrieve device id in Android.
 * <p/>
 * - Unique number (IMEI, MEID, ESN, IMSI)
 * - MAC Address
 * - Serial Number
 * - ANDROID_ID
 * <p/>
 * Further information @see http://developer.samsung.com/android/technical-docs/How-to-retrieve-the-Device-Unique-ID-from-android-device
 */
public class UIDeviceId {

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
        if (MainActivity.mActivity == null)
            throw new IllegalStateException("'LinesActivity.mActivity' must not be null.");
        return ((TelephonyManager) MainActivity.mActivity.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
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
        if (MainActivity.mActivity == null)
            throw new IllegalStateException("'LinesActivity.mActivity' must not be null.");
        return ((TelephonyManager) MainActivity.mActivity.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
    }

    /**
     * Returns MAC Address.
     * <p/>
     * IMPORTANT! requires ACCESS_WIFI_STATE permission in AndroidManifest.xml
     * <p/>
     * Disadvantages:
     * - Device should have Wi-Fi (where not all devices have Wi-Fi)
     * - If Wi-Fi present in Device should be turned on otherwise does not report the MAC address
     */
    public static String getMacAdress() {
        if (MainActivity.mActivity == null)
            throw new IllegalStateException("'LinesActivity.mActivity' must not be null.");
        return ((WifiManager) MainActivity.mActivity.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getMacAddress();
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
                for (int idx = 0; idx < mac.length; idx++)
                    buf.append(String.format("%02X:", mac[idx]));
                if (buf.length() > 0) buf.deleteCharAt(buf.length() - 1);
                return buf.toString();
            }
        } catch (Exception ex) {
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
     * @param ipv4 true=return ipv4, false=return ipv6
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
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
    }

    /**
     * System Property ro.serialno returns the serial number as unique number Works for Android 2.3 and above. Can return null.
     * <p/>
     * Disadvantages:
     * - Serial Number is not available with all android devices
     */
    public static String getSerialNummer() {
        String hwID = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            hwID = (String) (get.invoke(c, "ro.serialno", "unknown"));
        } catch (Exception ignored) {
        }
        if (hwID != null) return hwID;
        try {
            Class<?> myclass = Class.forName("android.os.SystemProperties");
            Method[] methods = myclass.getMethods();
            Object[] params = new Object[]{new String("ro.serialno"),
                    new String("Unknown")};
            hwID = (String) (methods[2].invoke(myclass, params));
        } catch (Exception ignored) {
        }
        return hwID;
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
        if (MainActivity.mActivity == null)
            throw new IllegalStateException("'LinesActivity.mActivity' must not be null.");
        return Settings.Secure.getString(MainActivity.mActivity.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
