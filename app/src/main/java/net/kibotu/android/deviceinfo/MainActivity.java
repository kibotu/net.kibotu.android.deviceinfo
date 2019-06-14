package net.kibotu.android.deviceinfo;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.exozet.android.core.utils.DeviceExtensions;
import com.exozet.android.core.utils.FragmentExtensions;
import com.robohorse.gpversionchecker.GPVersionChecker;
import com.robohorse.gpversionchecker.base.CheckingStrategy;
import net.kibotu.android.deviceinfo.ui.BaseFragment;
import net.kibotu.android.deviceinfo.ui.buildinfo.BuildFragment;
import net.kibotu.android.deviceinfo.ui.menu.Menu;
import net.kibotu.logger.Logger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.exozet.android.core.extensions.FragmentExtensions.currentFragment;
import static com.exozet.android.core.extensions.ResourceExtensions.getResColor;
import static com.exozet.android.core.utils.ViewExtensions.getContentRoot;

public class MainActivity extends AppCompatActivity {

    private static final java.lang.String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Keep the screen always on
        if (BuildConfig.DEBUG)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Menu.with(this);

        FragmentExtensions.replace(new BuildFragment());

        new GPVersionChecker.Builder(this)
                .setCheckingStrategy(CheckingStrategy.ALWAYS)
                .showDialog(true)
                // .forceUpdate(BuildConfig.RELEASE)
                // .setCustomPackageName("net.kibotu.android.deviceinfo")
                .setVersionInfoListener(version -> {
                    Logger.v(TAG, "version=" + version);
                })
                .create();
    }

    @Override
    protected void onResume() {
        super.onResume();
        final TextView title = getContentRoot().findViewById(R.id.actionbar_title);
        title.setTextColor(getResColor(R.color.white));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Logger.v(TAG, "[onConfigurationChanged] " + newConfig);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {

        // hide keyboard
        DeviceExtensions.hideKeyboard();

        // close menu
        if (Menu.isDrawerOpen()) {
            Menu.closeDrawer();
            return;
        }

        if (BaseFragment.onBackPressed())
            return;

        // pop back stack
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            FragmentExtensions.printBackStack();
            return;
        }

        // quit app
        finish();
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
}
