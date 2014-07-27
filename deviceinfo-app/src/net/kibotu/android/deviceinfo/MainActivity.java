package net.kibotu.android.deviceinfo;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
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
import net.kibotu.android.deviceinfo.utils.TweetToTwitter;
import net.kibotu.android.error.tracking.ErrorTracking;
import net.kibotu.android.error.tracking.JSONUtils;
import net.kibotu.android.error.tracking.Logger;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends SlidingFragmentActivity {

    private volatile MenuFragment arcList;
    public static final String THEME_PREFERENCE = "themePreference";
    public volatile static JSONObject appConfig;

    private volatile UiLifecycleHelper uiHelper;

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
        Registry.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
        Registry.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

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
        Logger.setLogLevel(Logger.Level.VERBOSE);

        // add api level
        JSONObject metaData = new JSONObject();
        JSONUtils.safePut(metaData, "SDK", "" + Device.getApiLevel());
        ErrorTracking.addMetaData(metaData);

        // init device
        Device.setContext(this);

        // get fb keyhash
//        fbKeyHash();

        arcList = new MenuFragment(this);

        for (Registry item : Registry.values()) {
            arcList.addItem(item.name(), item.iconR);
            // pre-load
            item.getFragmentList();
        }

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

        // add facebook
        uiHelper = new UiLifecycleHelper(this, null);
        uiHelper.onCreate(savedInstanceState);
    }

    private void fbKeyHash() {
        // Add code to print out the key hash
        try {
            final PackageInfo info = getPackageManager().getPackageInfo("net.kibotu.android.deviceinfo", PackageManager.GET_SIGNATURES);
            for (final Signature signature : info.signatures) {
                final MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Logger.d("KeyHash: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (final PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            Logger.e(e);
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
            @Override
            public void onError(final FacebookDialog.PendingCall pendingCall, final Exception error, final Bundle data) {
                Logger.e(String.format("Error: %s", error.toString()));
            }

            @Override
            public void onComplete(final FacebookDialog.PendingCall pendingCall, final Bundle data) {
                Logger.i("Success!");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_rate:
                rateMeMaybe();
                return true;
            case R.id.menu_facebook:
                shareLinkOnFacebook();
                return true;
            case R.id.menu_twitter:
                tweet();
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

    private TweetToTwitter tweetToTwitter;

    public void tweet() {
        if (tweetToTwitter == null)
            tweetToTwitter = new TweetToTwitter(this, appConfig.optString("twitterApiKey"), appConfig.optString("twitterSecret"));
        final ParseObject p = new ParseObject("DeviceInfo");
        parseStoreDeviceInfoAsync(p, new SaveCallback() {
            @Override
            public void done(final ParseException e) {
                tweetToTwitter.login(new AsyncCallback() {
                    @Override
                    public void callback(Object o) {
                        CustomWebView.destroy();
                        tweetToTwitter.tweetMessage("Android Device Information for " + Build.MODEL + " at " + createSiteUrl(p));
                    }
                });
            }
        });
    }

    public String createSiteUrl(final ParseObject p) {
        return "http://kibotu.github.io/net.kibotu.android.deviceinfo/?device=" + p.getObjectId();
    }

    public String createQrUrl(final ParseObject p) {
        return "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=" + createSiteUrl(p);
    }

    private void shareLinkOnFacebook() {
        final ParseObject infos = new ParseObject("DeviceInfo");
        parseStoreDeviceInfoAsync(infos, new SaveCallback() {
            @Override
            public void done(final ParseException e) {
                final FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(Device.context())
                        .setLink(createSiteUrl(infos))
                        .setPicture(createQrUrl(infos))
                        .setApplicationName("Android Device Information")
                        .setCaption("blog.kibotu.net")
                        .setDescription(Build.MODEL)
                        .build();
                uiHelper.trackPendingDialogCall(shareDialog.present());
            }
        });
    }

    private ParseObject parseStoreDeviceInfoAsync(final ParseObject infos, final SaveCallback cb) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final JSONObject allInfo = new JSONObject();
                for (final Registry topic : Registry.values()) {
                    final JSONObject registryItem = new JSONObject();
                    for (int i = 0; i < topic.getFragmentList().list.getCount(); ++i) {
                        final DeviceInfoItem item = topic.cachedList.list.getItem(i);
                        JSONUtils.safePutOpt(registryItem, item.keys != null ? item.keys : item.tag, item.value);
                    }
                    JSONUtils.safePutOpt(allInfo, topic.name(), "" + registryItem);
                }
                infos.put("deviceinfo", allInfo);
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
}