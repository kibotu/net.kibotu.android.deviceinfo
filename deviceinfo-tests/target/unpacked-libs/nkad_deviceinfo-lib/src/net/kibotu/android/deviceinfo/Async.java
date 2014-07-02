package net.kibotu.android.deviceinfo;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import org.jetbrains.annotations.NotNull;

public class Async {

    /**
     * Creates an async task and executes it.
     *
     * @param delegate Runnable.
     * @return AsyncTask.
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static AsyncTask<Void, Void, Void> safeAsync(@NotNull final Runnable delegate) {
        final AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(final Void... voi) {
                try {
                    delegate.run();
                } catch (final Exception e) {
                    Logger.e("Error in SBSErrorTracking", e);
                }
                return null;
            }
        };
        task.execute();
        return task;
    }
}
