package net.kibotu.android.deviceinfo.ui.menu;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import java.util.List;

import static com.common.android.utils.ContextHelper.getContext;

/**
 * Created by Nyaruhodo on 20.02.2016.
 */
public class MainMenuProvider {

    @NonNull
    private static IMainMenu stub = createStub();

    @NonNull
    public static IMainMenu provide() {
        final Context context = getContext();
        return context instanceof IMenuProvider
                ? ((IMenuProvider) context).getMainMenu()
                : stub;
    }

    @NonNull
    protected static IMainMenu createStub() {
        return new IMainMenu() {
            @NonNull
            @Override
            public IMainMenu prepareDrawers() {
                return this;
            }

            @NonNull
            @Override
            public IMainMenu setLeftDrawerTitle(@NonNull final String title) {
                return this;
            }

            @NonNull
            @Override
            public IMainMenu setTitle(@NonNull final String title) {
                return this;
            }

            @NonNull
            @Override
            public IMainMenu showActionBar(final boolean isShowing) {
                return this;
            }

            @NonNull
            @Override
            public IMainMenu setHomeIcon(@DrawableRes final int drawable) {
                return this;
            }

            @NonNull
            @Override
            public IMainMenu setLeftDrawerLockMode(@LockMode final int lockMode) {
                return this;
            }

            @NonNull
            @Override
            public IMainMenu setMenuItems(final List<MenuItem> menu) {
                return this;
            }

            @Override
            public boolean isDrawerOpen() {
                return false;
            }

            @NonNull
            @Override
            public IMainMenu closeDrawers() {
                return this;
            }
        };
    }
}