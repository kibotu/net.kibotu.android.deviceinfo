package net.kibotu.android.deviceinfo.utils;

import android.content.res.Configuration;

import static com.common.android.utils.ContextHelper.getContext;

/**
 * Created by Nyaruhodo on 22.02.2016.
 */
public class Extensions {

    public static Configuration configuration() {
        return getContext().getResources().getConfiguration();
    }
}
