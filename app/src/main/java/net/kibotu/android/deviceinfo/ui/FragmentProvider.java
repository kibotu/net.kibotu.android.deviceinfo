package net.kibotu.android.deviceinfo.ui;

import android.support.v4.app.FragmentManager;

import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.ui.sensor.SensorFragment;

import static com.common.android.utils.ContextHelper.getContext;
import static com.common.android.utils.extensions.FragmentExtensions.newInstance;

/**
 * Created by Nyaruhodo on 20.02.2016.
 */
public class FragmentProvider {

    private FragmentProvider() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static void showBuildConfigFragment() {
        final FragmentManager fm = getContext().getSupportFragmentManager();
        final BaseFragment fragment = newInstance(SensorFragment.class, null);
        fm.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
