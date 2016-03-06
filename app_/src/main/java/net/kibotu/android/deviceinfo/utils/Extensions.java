package net.kibotu.android.deviceinfo.utils;

import android.content.res.Configuration;
import android.support.annotation.LayoutRes;
import android.view.View;

import static com.common.android.utils.ContextHelper.getContext;

/**
 * Created by Nyaruhodo on 22.02.2016.
 */
public class Extensions {

    public static Configuration configuration() {
        return getContext().getResources().getConfiguration();
    }

    public static View inflate(@LayoutRes final int layout) {
        return getContext().getLayoutInflater().inflate(layout, null);
    }
}
