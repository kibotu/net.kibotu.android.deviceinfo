package net.kibotu.android.deviceinfo.ui;

import android.support.v4.app.FragmentManager;

import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.ui.buildinfo.BuildFragment;

import static com.common.android.utils.ContextHelper.getFragmentActivity;
import static com.common.android.utils.extensions.FragmentExtensions.newInstance;

/**
 * Created by Nyaruhodo on 20.02.2016.
 */
public class FragmentProvider {

    private FragmentProvider() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static void showBuildConfigFragment() {
        final FragmentManager fm = getFragmentActivity().getSupportFragmentManager();
        final BaseFragment fragment = newInstance(BuildFragment.class, null);
        fm.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
