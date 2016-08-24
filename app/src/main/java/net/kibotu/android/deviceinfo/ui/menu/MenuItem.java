package net.kibotu.android.deviceinfo.ui.menu;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;

import com.common.android.utils.interfaces.LogTag;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Nyaruhodo on 20.02.2016.
 */
public class MenuItem {

    @StringRes
    final int name;

    @DrawableRes
    final int icon;

    @Nullable
    private Class<? extends LogTag> fragment;

    public <T extends Fragment & LogTag> MenuItem(@StringRes final int name, @DrawableRes final int icon, @Nullable final Class<T> fragment) {
        this.name = name;
        this.icon = icon;
        this.fragment = fragment;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public <T extends Fragment & LogTag> T getFragment() {
        T result = null;
        try {
            result = (T) fragment.getConstructors()[0].newInstance();
        } catch (final InstantiationException e) {
            e.printStackTrace();
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
        } catch (final InvocationTargetException e) {
            e.printStackTrace();
        } catch (final NullPointerException e) {
            e.printStackTrace();
        }

        if (result == null)
            throw new IllegalArgumentException("fragment has no empty constructor");

        return result;
    }
}