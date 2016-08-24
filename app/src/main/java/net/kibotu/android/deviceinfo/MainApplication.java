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

import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Nyaruhodo on 20.02.2016.
 */
public class MainApplication extends MultiDexApplication {

    ActivityLifecycleCallbacks activityLifecycleCallbacks = createActivityLifecycleCallbacks();

    @Override
    public void onCreate() {
        MultiDex.install(getApplicationContext());
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        Device.with(this);

        Logger.setLogLevel(BuildConfig.DEBUG
                ? Logger.Level.VERBOSE
                : Logger.Level.SILENT);

        // Default font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath(UiLight.getResourcePath(getApplicationContext()))
                .setFontAttrId(R.attr.fontPath)
                .build());

        // Secure shared preferences
        Hawk.init(this)
                .setEncryptionMethod(BuildConfig.DEBUG
                        ? HawkBuilder.EncryptionMethod.NO_ENCRYPTION
                        : HawkBuilder.EncryptionMethod.MEDIUM)
                .setStorage(HawkBuilder.newSharedPrefStorage(this))
                .setLogLevel(BuildConfig.DEBUG
                        ? LogLevel.FULL
                        : LogLevel.NONE)
                .build();
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
