package net.kibotu.android.deviceinfo;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.core.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;

import com.common.android.utils.extensions.FragmentExtensions;
import com.common.android.utils.extensions.ViewExtensions;
import com.common.android.utils.logging.Logger;
import com.robohorse.gpversionchecker.GPVersionChecker;
import com.robohorse.gpversionchecker.base.CheckingStrategy;

import net.kibotu.android.deviceinfo.ui.BaseFragment;
import net.kibotu.android.deviceinfo.ui.buildinfo.BuildFragment;
import net.kibotu.android.deviceinfo.ui.menu.Menu;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.common.android.utils.ContextHelper.getActivity;
import static com.common.android.utils.extensions.DeviceExtensions.hideKeyboard;
import static com.common.android.utils.extensions.FragmentExtensions.currentFragment;
import static com.common.android.utils.extensions.ResourceExtensions.color;

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

        new GPVersionChecker.Builder(getActivity())
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
        final TextView title = (TextView) ViewExtensions.getContentRoot().findViewById(R.id.actionbar_title);
        title.setTextColor(color(R.color.white));
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
        hideKeyboard();

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
