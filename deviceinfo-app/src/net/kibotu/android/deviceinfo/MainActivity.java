package net.kibotu.android.deviceinfo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import com.flurry.android.FlurryAgent;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import net.kibotu.android.deviceinfo.fragments.menu.MenuFragment;
import net.kibotu.android.deviceinfo.utils.ErrorTrackingHelper;
import net.kibotu.android.deviceinfo.utils.JSONUtils;
import org.json.JSONObject;

public class MainActivity extends FragmentActivity {

    public static SlidingMenu menu;
    private volatile MenuFragment arcList;
    public static final String THEME_PREFERENCE = "themePreference";

    @Override
    public void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this, "HXN4YB6BKTKQT76NDHSR");
    }

    @Override
    public void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
        ErrorTrackingHelper.endSession();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init logger
        Logger.init(new LogcatLogger(this), "DeviceInfo", Logger.Level.VERBOSE);

        // init device
        Device.setContext(this);

        // setup error tracking
        ErrorTrackingHelper.startErrorTracking(this);
        JSONObject metaData = new JSONObject();
        JSONUtils.safePut(metaData, "SDK", "" + Device.getApiLevel());
        ErrorTrackingHelper.addMetaData(metaData);

        // inject logged exceptions
        Logger.setThrowableHook(new Logger.ThrowableHook() {
            @Override
            void handleException(final Throwable e) {
                ErrorTrackingHelper.notifyError(e);
            }
        });

        // set theme
        setTheme(PreferenceManager.getDefaultSharedPreferences(this).getInt(THEME_PREFERENCE, R.style.light_theme));

        arcList = new MenuFragment(this);

        for (Registry item : Registry.values())
            arcList.addItem(item.name(), item.iconR);

        // set the Above View
        setContentView(R.layout.content_frame);
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
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.menu_frame);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.menu_frame, arcList)
                .commit();

        arcList.lastItemList = Registry.Build;
        menu.showMenu();

        // time bomb
        Device.ACTIVATE_TB = false;
        Device.checkTimebombDialog();
    }

    // region Option Menu

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (menu.isMenuShowing()) {
            Device.killApp();
//            finish();
            return true;
//            return moveTaskToBack(true);
        }

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            menu.showMenu();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

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
    }

    // endregion

    public void doParseStuff() {
//
//        ParseQuery query = ParseQuery.getQuery("TestObject");
//
//
//        query.getInBackground(testObject.getObjectId(), new GetCallback() {
//
//            @Override
//            public void done(ParseObject parseObject, ParseException e) {
//                if (e == null) {
//                    // object will be your game score
//                } else {
//                    // something went wrong
//                }
//            }
//        });
    }
}