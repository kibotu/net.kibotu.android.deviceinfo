package net.kibotu.android.deviceinfo.ui.buildConfig;

import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.ui.BaseFragment;

/**
 * Created by Nyaruhodo on 20.02.2016.
 */
public class BuildConfigFragment extends BaseFragment {

    @Override
    protected String getTitle() {
        return "Build Config";
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_build_config;
    }
}
