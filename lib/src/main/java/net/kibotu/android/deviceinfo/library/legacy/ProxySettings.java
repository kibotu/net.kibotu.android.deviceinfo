package net.kibotu.android.deviceinfo.library.legacy;

import android.content.Context;
import android.net.ConnectivityManager;
import net.kibotu.android.deviceinfo.library.SystemService;

import java.lang.reflect.Method;

public class ProxySettings {

    public String Host;
    public int Port;
    public String ExclusionList;

    public ProxySettings(final Context context) {
        loadProxySettings(context);
    }

    /**
     * Credits to: http://stackoverflow.com/questions/10811698/getting-wifi-proxy-settings-in-android
     */
    private void loadProxySettings(final Context context) {
        Method method;
        try {
            method = ConnectivityManager.class.getMethod("getProxy");
        } catch (final NoSuchMethodException e) {
            e.printStackTrace();
            // Normal situation for pre-ICS devices
            return;
        }

        try {
            final ConnectivityManager connectivityManager = SystemService.getConnectivityManager();
            final Object pp = method.invoke(connectivityManager);
            if (pp == null)
                return;

            getUserProxy(pp);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private void getUserProxy(final Object pp) throws Exception {
        String className = "android.net.ProxyProperties";
        Class<?> c = Class.forName(className);
        Method method;

        method = c.getMethod("getHost");
        Host = (String) method.invoke(pp);

        method = c.getMethod("getPort");
        Port = (Integer) method.invoke(pp);

        method = c.getMethod("getExclusionList");
        ExclusionList = (String) method.invoke(pp);
    }
}
