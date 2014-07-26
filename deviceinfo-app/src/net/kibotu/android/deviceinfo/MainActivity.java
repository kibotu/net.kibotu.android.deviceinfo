package net.kibotu.android.deviceinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageButton;
import com.actionbarsherlock.view.ActionProvider;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.flurry.android.FlurryAgent;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import net.kibotu.android.deviceinfo.fragments.list.vertical.DeviceInfoItem;
import net.kibotu.android.deviceinfo.fragments.menu.MenuFragment;
import net.kibotu.android.deviceinfo.utils.CustomWebView;
import net.kibotu.android.deviceinfo.utils.RateMeMaybe;
import net.kibotu.android.error.tracking.ErrorTracking;
import net.kibotu.android.error.tracking.JSONUtils;
import net.kibotu.android.error.tracking.Logger;
import org.json.JSONObject;

import java.lang.reflect.Field;

public class MainActivity extends SlidingFragmentActivity {

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

    public void forceOverflowMenuItems() {
        try {
            final ViewConfiguration config = ViewConfiguration.get(this);
            final Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (final Exception e) {
            Logger.e(e);
        }
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {

        // forceOverflowMenuItems();

        // open menu after creating
        if (savedInstanceState == null) {
            savedInstanceState = new Bundle();
            savedInstanceState.putBoolean("SlidingActivityHelper.open", true);
        }
        super.onPostCreate(savedInstanceState);

        // More customized example
//        RateMeMaybe rmm = new RateMeMaybe(this);
//        rmm.setPromptMinimums(3, 14, 10, 30);
//        rmm.setDialogMessage("You really seem to like this app, "
//                +"since you have already used it %totalLaunchCount% times! "
//                +"It would be great if you took a moment to rate it.");
//        rmm.setDialogTitle("Rate this app");
//        rmm.setPositiveBtn("Yeeha!");
//        rmm.run();
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

        setContentView(R.layout.content_frame);
        setBehindContentView(R.layout.menu_frame);
        // set the Above View
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, Registry.Build.getFragmentList())
                .commit();

        // configure the SlidingMenu
        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        getSlidingMenu().setShadowWidthRes(R.dimen.shadow_width);
        getSlidingMenu().setShadowDrawable(R.drawable.shadow);
        getSlidingMenu().setBehindOffsetRes(R.dimen.slidingmenu_offset);
        getSlidingMenu().setFadeDegree(0.35f);
        getSlidingMenu().setMenu(R.layout.menu_frame);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.menu_frame, arcList)
                .commit();

        arcList.lastItemList = Registry.Build;

        // time bomb
        Device.ACTIVATE_TB = false;
        Device.checkTimebombDialog();

        View customNav = LayoutInflater.from(this).inflate(R.layout.navigation, null);
//        final ImageButton button = (ImageButton) customNav.findViewById(R.id.imageButton);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CustomWebView.showWebViewInDialog(Device.context(), "http://kibotu.github.io/net.kibotu.android.deviceinfo/", 0, 0, DisplayHelper.absScreenWidth, DisplayHelper.absScreenHeight);
//            }
//        });

        setSlidingActionBarEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setCustomView(customNav);
//        getSupportActionBar().setDisplayShowCustomEnabled(true);

        setTitle("Build");
        getSupportActionBar().setIcon(Registry.Build.iconR_i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_rate:
                rateMeMaybe();
                return true;
            case R.id.menu_facebook:

                final ParseObject infos = new ParseObject("DeviceInfo");
                parseStoreDeviceInfoAsync(infos, new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Logger.toast("Uploaded ObjectId: " + infos.getObjectId()));
                    }
                });

                return true;
            case R.id.menu_twitter:
                return true;
            case R.id.menu_email:
                return true;
            case R.id.menu_about:
                CustomWebView.showWebViewInDialog(Device.context(), "http://kibotu.github.io/net.kibotu.android.deviceinfo/", 0, 0, DisplayHelper.absScreenWidth, DisplayHelper.absScreenHeight);
                return true;
            case R.id.menu_github:
                CustomWebView.showWebViewInDialog(Device.context(), "https://github.com/kibotu/net.kibotu.android.deviceinfo/blob/master/README.md", 0, 0, DisplayHelper.absScreenWidth, DisplayHelper.absScreenHeight);
                return true;
            case R.id.menu_feedback:
                CustomWebView.showWebViewInDialog(Device.context(), "http://blog.kibotu.net/contact", 0, 0, DisplayHelper.absScreenWidth, DisplayHelper.absScreenHeight);
                return true;
            case R.id.menu_request:
                CustomWebView.showWebViewInDialog(Device.context(), "http://blog.kibotu.net/contact", 0, 0, DisplayHelper.absScreenWidth, DisplayHelper.absScreenHeight);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ParseObject parseStoreDeviceInfoAsync(final ParseObject infos, final SaveCallback cb) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (Registry topic : Registry.values()) {
                    final JSONObject registryItem = new JSONObject();
                    for (int i = 0; i < topic.cachedList.list.getCount(); ++i) {
                        DeviceInfoItem item = topic.cachedList.list.getItem(i);
                        JSONUtils.safePutOpt(registryItem, item.keys != null ? item.keys : item.tag, item.value);
                    }
                    infos.put(topic.name(), registryItem);
                }
                infos.saveEventually(cb);
            }
        }).start();
        return infos;
    }

    private void rateMeMaybe() {
        RateMeMaybe rmm = new RateMeMaybe(this);
        rmm.setDialogTitle("Thank you for using this App.");
        rmm.forceShow();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (!getSlidingMenu().isMenuShowing()) {
            getSlidingMenu().showMenu(true);
            return;
        } else {
            Device.killApp();
//            finish();
//            return;
        }

        super.onBackPressed();
    }

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