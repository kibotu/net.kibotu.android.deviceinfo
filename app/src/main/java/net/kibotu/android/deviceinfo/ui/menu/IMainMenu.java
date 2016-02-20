package net.kibotu.android.deviceinfo.ui.menu;

import android.support.annotation.NonNull;

/**
 * Created by Nyaruhodo on 20.02.2016.
 */
public interface IMainMenu {

    IMainMenu prepareDrawers();

    IMainMenu setLeftDrawerTitle(@NonNull String title);

    IMainMenu setTitle(@NonNull String title);

    IMainMenu showActionBar(boolean isShowing);

    IMainMenu setLeftDrawerLockMode(@LockMode int lockMode);
}
