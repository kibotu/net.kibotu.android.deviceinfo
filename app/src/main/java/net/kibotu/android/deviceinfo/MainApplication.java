package net.kibotu.android.deviceinfo;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.common.android.utils.ContextHelper;
import com.common.android.utils.extensions.FragmentExtensions;
import com.common.android.utils.logging.LogcatLogger;
import com.common.android.utils.logging.Logger;
import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;
import com.squareup.leakcanary.LeakCanary;

import net.kibotu.android.deviceinfo.library.Device;
import net.kibotu.android.deviceinfo.library.network.ConnectivityChangeListenerRx;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static net.kibotu.android.deviceinfo.BuildConfig.BRANCH;
import static net.kibotu.android.deviceinfo.BuildConfig.BUILD_DATE;
import static net.kibotu.android.deviceinfo.BuildConfig.BUILD_TYPE;
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

    private static final String TAG = MainApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        MultiDex.install(getApplicationContext());
        super.onCreate();

        initLeakCanary(this);

        ContextHelper.with(this);
        Device.with(this);
        initHawk(this);
        initLogger();

        initFabric(this);

        initCalligraphy();

        initConnectivityChangeListener();

        if (DEBUG)
            Stetho.initializeWithDefaults(this);
    }

    private static void initLeakCanary(Application context) {
        // checking memory leaks
        if (BuildConfig.DEBUG)
            LeakCanary.install(context);
    }

    public static void initHawk(Context context) {
        Hawk.init(context)
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.NO_ENCRYPTION)
                .setStorage(HawkBuilder.newSharedPrefStorage(context))
                .setLogLevel(DEBUG
                        ? LogLevel.FULL
                        : LogLevel.NONE)
                .build();
    }

    private static void initLogger() {
        Logger.addLogger(new LogcatLogger());
        Logger.setLogLevel(DEBUG
                ? Logger.Level.VERBOSE
                : Logger.Level.SILENT);
        FragmentExtensions.setLoggingEnabled(DEBUG);
        logBuildConfig();
    }

    private static void logBuildConfig() {
        Map<String, String> buildInfo = createBuildInfo();
        for (Map.Entry<String, String> entry : buildInfo.entrySet())
            Logger.i(TAG, entry.getKey() + " : " + entry.getValue());
    }

    private static Map<String, String> createBuildInfo() {
        Map<String, String> info = new HashMap<>();
        info.put("CANONICAL_VERSION_NAME", CANONICAL_VERSION_NAME);
        info.put("SIMPLE_VERSION_NAME", SIMPLE_VERSION_NAME);
        info.put("VERSION_NAME", "" + VERSION_NAME);
        info.put("VERSION_CODE", "" + VERSION_CODE);
        info.put("BUILD_TYPE", BUILD_TYPE);
        info.put("FLAVOR", FLAVOR);
        Calendar d = Calendar.getInstance();
        d.setTimeInMillis(Long.parseLong(BUILD_DATE));
        info.put("BUILD_DATE", "" + d.getTime());
        info.put("BRANCH", BRANCH);
        info.put("COMMIT_HASH", COMMIT_HASH);
        info.put("COMMIT_URL", "https://github.com/kibotu/net.kibotu.android.deviceinfo/commits/" + COMMIT_HASH);
        info.put("TREE_URL", "https://kibotu/net.kibotu.android.deviceinfo/tree/" + COMMIT_HASH);
        return info;
    }

    private static void initFabric(Context context) {
        Fabric.with(context, new Crashlytics());
        Map<String, String> buildInfo = createBuildInfo();
        for (Map.Entry<String, String> entry : buildInfo.entrySet())
            Crashlytics.setString(entry.getKey(), entry.getValue());
    }

    private static void initCalligraphy() {
        // Default font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                // .setDefaultFontPath(getString(R.string.font_FaktSoftPro_Blond))
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

    private void initConnectivityChangeListener() {
        ConnectivityChangeListenerRx.getObservable()
                .subscribe(connectivityEvent -> {
                    Logger.v(TAG, "[connectivityEvent] " + connectivityEvent);
                }, Throwable::printStackTrace);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Logger.v(TAG, "[onConfigurationChanged] " + newConfig);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onTerminate() {
        ContextHelper.onTerminate();
        Device.onTerminate();
        super.onTerminate();
    }
}
