package net.kibotu.android.deviceinfo.ui.menu;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import net.kibotu.android.deviceinfo.ui.BaseFragment;

import java.lang.reflect.InvocationTargetException;

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

    @SuppressWarnings("unchecked")
    @NonNull
    public <T extends BaseFragment> T getFragment() {
        T result = null;
        try {
            result = (T) fragment.getConstructors()[0].newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        if (result == null)
            throw new IllegalArgumentException("fragment has no empty constructor");

        return result;
    }
}
