package net.kibotu.android.deviceinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.internal.widget.IcsAdapterView;
import com.actionbarsherlock.internal.widget.IcsSpinner;
import com.actionbarsherlock.view.ActionProvider;
import com.actionbarsherlock.view.MenuItem;
import com.flurry.android.FlurryAgent;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import net.kibotu.android.deviceinfo.fragments.menu.MenuFragment;
import net.kibotu.android.deviceinfo.utils.CustomWebView;
import net.kibotu.android.error.tracking.ErrorTracking;
import net.kibotu.android.error.tracking.JSONUtils;
import net.kibotu.android.error.tracking.Logger;
import org.json.JSONObject;

public class MainActivity extends SlidingFragmentActivity {

    public static SlidingMenu menu;
    private volatile MenuFragment arcList;
    public static final String THEME_PREFERENCE = "themePreference";
    public static JSONObject appConfig;

    @Override
    public void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this, appConfig.optString("flurryAgent"));
    }

    @Override
    public void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
        ErrorTracking.endSession();
    }

    private void testLowMemory() {
        long[] l = new long[Integer.MAX_VALUE];
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set theme
//        setTheme(PreferenceManager.getDefaultSharedPreferences(this).getInt(THEME_PREFERENCE, R.style.light_theme));

        // load secrets
        appConfig = JSONUtils.loadJsonFromAssets(this, "app.json");

        // start error tracking
        ErrorTracking.startSession(this, appConfig.optString("applicationId"), appConfig.optString("clientKey"));
        Logger.setLogLevel(Logger.Level.SILENT);

        // add api level
        JSONObject metaData = new JSONObject();
        JSONUtils.safePut(metaData, "SDK", "" + Device.getApiLevel());
        ErrorTracking.addMetaData(metaData);

        // init device
        Device.setContext(this);

        arcList = new MenuFragment(this);

        for (Registry item : Registry.values())
            arcList.addItem(item.name(), item.iconR);

        // set the Above View
        setContentView(R.layout.content_frame);
        setBehindContentView(R.layout.menu_frame);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, Registry.Build.getFragmentList())
                .commit();

        // configure the SlidingMenu
        menu = new SlidingMenu(this);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        menu.setMenu(R.layout.menu_frame);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.menu_frame, arcList)
                .commit();

        arcList.lastItemList = Registry.Build;
        menu.showMenu();
        setTitle("Build");
        getSupportActionBar().setIcon(Registry.Build.iconR_i);

        // time bomb
        Device.ACTIVATE_TB = false;
        Device.checkTimebombDialog();

        setSlidingActionBarEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View customNav = LayoutInflater.from(this).inflate(R.layout.navigation, null);

        final ImageButton button = (ImageButton) customNav.findViewById(R.id.imageButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Device.context().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CustomWebView.showWebViewInDialog(Device.context(), "http://kibotu.github.io/net.kibotu.android.deviceinfo/", 0, 0, DisplayHelper.absScreenWidth, DisplayHelper.absScreenHeight);
                    }
                });
            }
        });

        getSupportActionBar().setCustomView(customNav);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        getSupportActionBar().setNavigationMode(ActionBar.DISPLAY_SHOW_CUSTOM);
    }

    // region Option Menu

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (menu.isMenuShowing()) {
            Device.killApp();
            finish();
            return true;
//            return moveTaskToBack(true);
        }

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            menu.showMenu();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.daynighttheme:

                // toggle preference
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
                editor.putInt(THEME_PREFERENCE, (R.style.dark_theme == pref.getInt(THEME_PREFERENCE, R.style.dark_theme)) ? R.style.light_theme : R.style.dark_theme);
                editor.commit();

                // restart device
                Device.Restart();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    public static class SettingsActionProvider extends ActionProvider {

        /**
         * An intent for launching the system settings.
         */
        private static final Intent sSettingsIntent = new Intent(Settings.ACTION_SETTINGS);

        /**
         * Context for accessing resources.
         */
        private final Context mContext;

        /**
         * Creates a new instance.
         *
         * @param context Context for accessing resources.
         */
        public SettingsActionProvider(Context context) {
            super(context);
            mContext = context;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public View onCreateActionView() {
            // Inflate the action view to be shown on the action bar.
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            View view = layoutInflater.inflate(R.layout.settings_action_provider, null);
            ImageButton button = (ImageButton) view.findViewById(R.id.button);
            // Attach a click listener for launching the system settings.
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(sSettingsIntent);
                }
            });
            return view;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean onPerformDefaultAction() {
            // This is called if the host menu item placed in the overflow menu of the
            // action bar is clicked and the host activity did not handle the click.
            mContext.startActivity(sSettingsIntent);
            return true;
        }
    }
}