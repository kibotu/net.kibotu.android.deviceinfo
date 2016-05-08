package net.kibotu.android.deviceinfo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.FragmentActivity;

import com.common.android.utils.ContextHelper;
import com.common.android.utils.logging.Logger;
import com.crashlytics.android.Crashlytics;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;

import net.kibotu.android.deviceinfo.library.Device;

import io.fabric.sdk.android.Fabric;

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
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks);

        // TODO: 21.02.2016
//        // Default font
//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath(UiLight.getResourcePath(getApplicationContext()))
//                .setFontAttrId(R.attr.fontPath)
//                .build());
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
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                setContext(activity);

                Logger.setLogLevel(BuildConfig.DEBUG
                        ? Logger.Level.VERBOSE
                        : Logger.Level.SILENT);

                // Secure shared preferences
                Hawk.init(activity)
                        .setEncryptionMethod(BuildConfig.DEBUG
                                ? HawkBuilder.EncryptionMethod.NO_ENCRYPTION
                                : HawkBuilder.EncryptionMethod.MEDIUM)
                        .setStorage(HawkBuilder.newSharedPrefStorage(activity))
                        .setLogLevel(BuildConfig.DEBUG
                                ? LogLevel.FULL
                                : LogLevel.NONE)
                        .build();
            }

            @Override
            public void onActivityStarted(Activity activity) {
                setContext(activity);
            }

            @Override
            public void onActivityResumed(Activity activity) {
                setContext(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        };
    }

    private void setContext(Activity activity) {
        if (activity instanceof FragmentActivity)
            ContextHelper.setContext((FragmentActivity) activity);

        Device.setContext(activity);
    }
}
