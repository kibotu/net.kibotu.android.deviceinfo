package net.kibotu.android.deviceinfo;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import net.kibotu.android.error.tracking.Logger;
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
                    Logger.e(e);
                }
                return null;
            }
        };
        task.execute();
        return task;
    }
}
