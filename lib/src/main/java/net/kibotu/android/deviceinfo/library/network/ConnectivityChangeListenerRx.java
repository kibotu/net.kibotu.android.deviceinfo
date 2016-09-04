package net.kibotu.android.deviceinfo.library.network;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.common.android.utils.logging.Logger;
import com.zplesac.connectionbuddy.ConnectionBuddy;
import com.zplesac.connectionbuddy.cache.ConnectionBuddyCache;
import com.zplesac.connectionbuddy.interfaces.ConnectivityChangeListener;
import com.zplesac.connectionbuddy.models.ConnectivityEvent;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by jan.rabe on 19/08/16.
 */

public enum ConnectivityChangeListenerRx implements ConnectivityChangeListener {

    /**
     * Singleton.
     */
    instance;

    private static final String TAG = ConnectivityChangeListenerRx.class.getSimpleName();
    private Application.ActivityLifecycleCallbacks activityLifecycleCallbacks;

    PublishSubject<ConnectivityEvent> subject = PublishSubject.create();

    ConnectivityEvent connectivityEvent;

    public static ConnectivityChangeListenerRx with(Application application) {
        instance.activityLifecycleCallbacks = instance.createActivityLifecycleCallbacks();
        application.registerActivityLifecycleCallbacks(instance.activityLifecycleCallbacks);
        return instance;
    }

    public static Observable<ConnectivityEvent> getObservable() {
        return instance.subject;
    }

    public static ConnectivityEvent getCurrent() {
        return instance.connectivityEvent;
    }

    @Override
    public void onConnectionChange(ConnectivityEvent event) {
        if (event == null)
            return;

        Logger.v(TAG, "[connectivityEvent] type=" + event.getType() + " state=" + event.getState());
        this.connectivityEvent = event;
        subject.onNext(event);
    }

    private Application.ActivityLifecycleCallbacks createActivityLifecycleCallbacks() {
        return new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                ConnectionBuddy.getInstance().registerForConnectivityEvents(instance, instance);
                if (savedInstanceState != null) {
                    ConnectionBuddyCache.clearLastNetworkState(this);
                }
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
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                ConnectionBuddy.getInstance().unregisterFromConnectivityEvents(instance);
            }
        };
    }

    public static void onTerminate(Application application) {
        application.unregisterActivityLifecycleCallbacks(instance.activityLifecycleCallbacks);
    }
}
