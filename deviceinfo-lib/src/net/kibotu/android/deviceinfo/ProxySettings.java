package net.kibotu.android.deviceinfo;

import android.content.Context;
import android.net.ConnectivityManager;
import net.kibotu.android.error.tracking.Logger;

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
        Method method = null;
        try {
            method = ConnectivityManager.class.getMethod("getProxy");
        } catch (final NoSuchMethodException e) {
            Logger.e(e);
            // Normal situation for pre-ICS devices
            return;
        } catch (final Exception e) {
            Logger.e(e);
            return;
        }

        try {
            final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final Object pp = method.invoke(connectivityManager);
            if (pp == null)
                return;

            getUserProxy(pp);
        } catch (final Exception e) {
            Logger.e(e);
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
