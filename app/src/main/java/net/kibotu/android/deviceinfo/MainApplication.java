package net.kibotu.android.deviceinfo;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.common.android.utils.ContextHelper;

/**
 * Created by Nyaruhodo on 20.02.2016.
 */
public class MainApplication extends Application {

    ActivityLifecycleCallbacks callbacks = createActivityLifecycleCallbacks();

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(callbacks);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    private ActivityLifecycleCallbacks createActivityLifecycleCallbacks() {
        return new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                setContext(activity);
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
    }
}
