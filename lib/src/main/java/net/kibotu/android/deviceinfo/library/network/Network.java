package net.kibotu.android.deviceinfo.library.network;

import android.webkit.WebView;
import net.kibotu.android.deviceinfo.library.Device;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import static net.kibotu.android.deviceinfo.library.services.SystemService.getWifiManager;

/**
 * Created by Nyaruhodo on 06.03.2016.
 */
public class Network {
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
        return new WebView(Device.getContext()).getSettings().getUserAgentString();
    }

    public static ProxySettings getProxySettings() {
        return new ProxySettings(Device.getContext());
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
}
