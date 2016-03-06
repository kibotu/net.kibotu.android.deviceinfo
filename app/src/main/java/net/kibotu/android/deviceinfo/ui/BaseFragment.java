package net.kibotu.android.deviceinfo.ui;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.common.android.utils.interfaces.ILayout;
import com.common.android.utils.interfaces.ILogTag;
import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.ui.menu.ISupportMenu;
import net.kibotu.android.deviceinfo.ui.menu.MainMenuProvider;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Nyaruhodo on 20.02.2016.
 */
public abstract class BaseFragment extends Fragment implements ILogTag, ILayout, ISupportMenu {

    protected View rootView;

    public BaseFragment() {
        // mandatory for fragment transactions
    }

    @NotNull
    @Override
    final public String tag() {
        return getClass().getSimpleName();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null)
            rootView = inflater.inflate(getLayout(), container, false);

        updateMainMenu();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        onViewCreated();
    }

    @Override
    public void updateMainMenu() {
        MainMenuProvider.provide()
                .setTitle(getTitle())
                .showActionBar(showActionBar())
                .setHomeIcon(getHomeIcon())
                .setLeftDrawerLockMode(lockLeftMenu()
                        ? DrawerLayout.LOCK_MODE_LOCKED_CLOSED
                        : DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    @DrawableRes
    protected int getHomeIcon() {
        return android.support.design.R.drawable.abc_ic_menu_share_material;
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    protected abstract String getTitle();

    protected abstract void onViewCreated();

    @Override
    public boolean lockLeftMenu() {
        return false;
    }

    @Override
    public boolean showActionBar() {
        return true;
    }
}
