package net.kibotu.android.deviceinfo.ui.menu;

import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import net.kibotu.android.deviceinfo.ui.BaseFragment;

/**
 * Created by Nyaruhodo on 20.02.2016.
 */
public class MenuItem {

    @Nullable
    final Class<? extends BaseFragment> fragment;

    @StringRes
    final int name;

    @DrawableRes
    final int icon;

    public MenuItem(@StringRes int name, @DrawableRes final int icon, @Nullable final Class<? extends BaseFragment> fragment) {
        this.fragment = fragment;
        this.name = name;
        this.icon = icon;
    }

}
