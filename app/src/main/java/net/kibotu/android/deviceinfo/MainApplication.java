package net.kibotu.android.deviceinfo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.common.android.utils.logging.Logger;
import com.crashlytics.android.Crashlytics;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;

import net.kibotu.android.deviceinfo.library.Device;

import java.util.Calendar;

import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static net.kibotu.android.deviceinfo.BuildConfig.BRANCH;
import static net.kibotu.android.deviceinfo.BuildConfig.BUILD_DATE;
import static net.kibotu.android.deviceinfo.BuildConfig.CANONICAL_VERSION_NAME;
import static net.kibotu.android.deviceinfo.BuildConfig.COMMIT_HASH;
import static net.kibotu.android.deviceinfo.BuildConfig.DEBUG;
import static net.kibotu.android.deviceinfo.BuildConfig.FLAVOR;
import static net.kibotu.android.deviceinfo.BuildConfig.SIMPLE_VERSION_NAME;
import static net.kibotu.android.deviceinfo.BuildConfig.VERSION_CODE;
import static net.kibotu.android.deviceinfo.BuildConfig.VERSION_NAME;

/**
 * Created by Nyaruhodo on 20.02.2016.
 */
public class MainApplication extends MultiDexApplication {

    ActivityLifecycleCallbacks activityLifecycleCallbacks = createActivityLifecycleCallbacks();

    @Override
    public void onCreate() {
        MultiDex.install(getApplicationContext());
        super.onCreate();

        initFabric();

        Device.with(this);

        Logger.setLogLevel(DEBUG
                ? Logger.Level.VERBOSE
                : Logger.Level.SILENT);

        // Default font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath(UiLight.getResourcePath(getApplicationContext()))
                .setFontAttrId(R.attr.fontPath)
                .build());

        // Secure shared preferences
        Hawk.init(this)
                .setEncryptionMethod(DEBUG
                        ? HawkBuilder.EncryptionMethod.NO_ENCRYPTION
                        : HawkBuilder.EncryptionMethod.MEDIUM)
                .setStorage(HawkBuilder.newSharedPrefStorage(this))
                .setLogLevel(DEBUG
                        ? LogLevel.FULL
                        : LogLevel.NONE)
                .build();
    }

    private void initFabric() {
        Fabric.with(this, new Crashlytics());
        Crashlytics.setString("COMMIT_URL", "https://github.com/kibotu/net.kibotu.android.deviceinfo/commit/" + COMMIT_HASH);
        Crashlytics.setString("TREE_URL", "https://kibotu/net.kibotu.android.deviceinfo/tree/" + COMMIT_HASH);
        Crashlytics.setString("CANONICAL_VERSION_NAME", CANONICAL_VERSION_NAME);
        Crashlytics.setString("SIMPLE_VERSION_NAME", SIMPLE_VERSION_NAME);
        Crashlytics.setString("COMMIT_HASH", COMMIT_HASH);
        Crashlytics.setString("BRANCH", BRANCH);
        Crashlytics.setString("FLAVOR", FLAVOR);
        Crashlytics.setString("VERSION_CODE", "" + VERSION_CODE);
        Crashlytics.setString("VERSION_NAME", "" + VERSION_NAME);
        Calendar d = Calendar.getInstance();
        d.setTimeInMillis(Long.parseLong(BUILD_DATE));
    }

    @Override
    public void onTerminate() {
        unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);
        super.onTerminate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private ActivityLifecycleCallbacks createActivityLifecycleCallbacks() {
        return new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        };
    }
}
