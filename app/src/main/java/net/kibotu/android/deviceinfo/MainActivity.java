package net.kibotu.android.deviceinfo;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;
import com.common.android.utils.extensions.FragmentExtensions;
import com.common.android.utils.extensions.ViewExtensions;
import com.common.android.utils.interfaces.LogTag;
import com.common.android.utils.logging.Logger;
import com.common.android.utils.ui.menu.*;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import java.util.List;

import static com.common.android.utils.extensions.DeviceExtensions.hideKeyboard;
import static com.common.android.utils.extensions.FragmentExtensions.currentFragment;
import static com.common.android.utils.extensions.ResourceExtensions.color;
import static net.kibotu.android.deviceinfo.ui.FragmentProvider.showBuildConfigFragment;

public class MainActivity extends AppCompatActivity implements LogTag, IMenuProvider, FragmentManager.OnBackStackChangedListener {

    IMainMenu mainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Keep the screen always on
        if (BuildConfig.DEBUG)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        getMainMenu().setMenuItems(MainMenuFactory.createMenu());

        showBuildConfigFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMainMenu().prepareDrawers();

        final TextView title = (TextView) ViewExtensions.getContentRoot().findViewById(R.id.actionbar_title);
        title.setTextColor(color(R.color.white));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Logger.v(tag(), "[onConfigurationChanged] " + newConfig);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackStackChanged() {
        hideKeyboard();
        final Fragment fragment = currentFragment(R.id.fragment_container);
        if (fragment instanceof ISupportMenu)
            ((ISupportMenu) fragment).updateMainMenu();
    }

    @Override
    public void onBackPressed() {

        hideKeyboard();

        if (MainMenuProvider.provide().isDrawerOpen() && currentFragment(R.id.overlay_container) == null)
            MainMenuProvider.provide().closeDrawers();
        else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            FragmentExtensions.popBackStack();
        } else
            super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final Fragment fragment = currentFragment(R.id.fragment_container);
        if (fragment != null)
            fragment.onActivityResult(requestCode, resultCode, data);
    }

    @CallSuper
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @NonNull
    @Override
    final public String tag() {
        return getClass().getSimpleName();
    }

    @NonNull
    @Override
    public IMainMenu getMainMenu() {
        if (mainMenu == null)
            mainMenu = new MainMenu();
        return mainMenu;
    }
}
