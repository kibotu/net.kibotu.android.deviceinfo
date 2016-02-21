package net.kibotu.android.deviceinfo.ui.menu;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import java.util.List;

import static com.common.android.utils.ContextHelper.getContext;

/**
 * Created by Nyaruhodo on 20.02.2016.
 */
public class MainMenuProvider {

    private static IMainMenu stub = createStub();

    public static IMainMenu provide() {
        final FragmentActivity context = getContext();
        return context instanceof IMenuProvider
                ? ((IMenuProvider) context).getMainMenu()
                : stub;
    }

    private static IMainMenu createStub() {
        return new IMainMenu() {
            @Override
            public IMainMenu prepareDrawers() {
                return this;
            }

            @Override
            public IMainMenu setLeftDrawerTitle(@NonNull String title) {
                return this;
            }

            @Override
            public IMainMenu setTitle(@NonNull String title) {
                return this;
            }

            @Override
            public IMainMenu showActionBar(boolean isShowing) {
                return this;
            }

            @Override
            public IMainMenu setLeftDrawerLockMode(@LockMode int lockMode) {
                return this;
            }

            @Override
            public IMainMenu setMenuItems(List<MenuItem> menu) {
                return this;
            }

            @Override
            public boolean isDrawerOpen() {
                return false;
            }

            @Override
            public IMainMenu closeDrawers() {
                return this;
            }
        };
    }
}
