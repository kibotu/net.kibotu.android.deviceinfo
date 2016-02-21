package net.kibotu.android.deviceinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import com.canelmas.let.DeniedPermission;
import com.canelmas.let.Let;
import com.canelmas.let.RuntimePermissionListener;
import com.canelmas.let.RuntimePermissionRequest;
import com.common.android.utils.ContextHelper;
import com.common.android.utils.interfaces.ILogTag;
import com.common.android.utils.logging.Logger;
import net.kibotu.android.deviceinfo.ui.menu.*;
import org.jetbrains.annotations.NotNull;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import java.util.List;

import static com.common.android.utils.extensions.DeviceExtensions.hideKeyboard;
import static com.common.android.utils.extensions.FragmentExtensions.currentFragment;
import static com.common.android.utils.extensions.FragmentExtensions.popBackStackImmediate;
import static net.kibotu.android.deviceinfo.ui.FragmentProvider.showBuildConfigFragment;

public class MainActivity extends AppCompatActivity implements ILogTag, IMenuProvider, FragmentManager.OnBackStackChangedListener, RuntimePermissionListener {

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
    }

    @NonNull
    @Override
    public IMainMenu getMainMenu() {
        if (mainMenu == null)
            mainMenu = new MainMenu();
        return mainMenu;
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
            if (getSupportFragmentManager().getBackStackEntryCount() > 0 && !ContextHelper.getContext().isFinishing())
                popBackStackImmediate(getSupportFragmentManager());
            else
                super.onBackPressed();
        }
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
        Let.handle(this, requestCode, permissions, grantResults);
    }

    @CallSuper
    @Override
    public void onShowPermissionRationale(List<String> list, RuntimePermissionRequest runtimePermissionRequest) {
        Logger.v(tag(), "onShowPermissionRationale " + list + " " + runtimePermissionRequest);
    }

    @CallSuper
    @Override
    public void onPermissionDenied(List<DeniedPermission> list) {
        Logger.v(tag(), "onPermissionDenied " + list);
    }

    @NotNull
    @Override
    final public String tag() {
        return getClass().getSimpleName();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
