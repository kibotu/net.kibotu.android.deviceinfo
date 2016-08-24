package net.kibotu.android.deviceinfo.library;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Jan Rabe on 30/09/15.
 */
class ContextHelper {

    @Nullable
    private static WeakReference<Application> application;

    @Nullable
    private static WeakReference<Context> context;

    private ContextHelper() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static void with(@NonNull final Application application) {
        ContextHelper.application = new WeakReference<>(application);
        application.registerActivityLifecycleCallbacks(createActivityLifecycleCallbacks());
    }

    public static void setApplication(@Nullable Application application) {
        if (application == null) {
            ContextHelper.application = null;
        }

        ContextHelper.application = new WeakReference<>(application);
    }

    public static void setContext(@Nullable final Context context) {
        if (context == null) {
            ContextHelper.context = null;
            return;
        }

        ContextHelper.context = new WeakReference<>(context);
    }

    @Nullable
    public static Application getApplication() {
        return application != null
                ? application.get()
                : null;
    }

    @Nullable
    public static Context getContext() {
        return context != null
                ? context.get()
                : null;
    }

    @Nullable
    public static Activity getActivity() {
        if (context == null)
            return null;

        return context.get() instanceof Activity
                ? (Activity) context.get()
                : null;
    }

    public static final AtomicBoolean isRunning = new AtomicBoolean(false);

    private static Application.ActivityLifecycleCallbacks createActivityLifecycleCallbacks() {
        return new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                setContext(activity);
                isRunning.set(true);
            }

            @Override
            public void onActivityStarted(Activity activity) {
                setContext(activity);
                isRunning.set(true);
            }

            @Override
            public void onActivityResumed(Activity activity) {
                setContext(activity);
                isRunning.set(true);
            }

            @Override
            public void onActivityPaused(Activity activity) {
                isRunning.set(false);
            }

            @Override
            public void onActivityStopped(Activity activity) {
                isRunning.set(false);
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                isRunning.set(false);
            }
        };
    }

    public static void onTerminate() {

        if (context != null)
            context.clear();

        context = null;

        if (application != null)
            application.clear();

        application = null;

        isRunning.set(false);
    }
}