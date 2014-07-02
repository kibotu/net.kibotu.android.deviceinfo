package net.kibotu.android.deviceinfo;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.wooga.sbs.error.tracking.android.intern.SBSErrorTrackingCore.TAG;

/**
 * Implementation ILogger for Android logcat.
 */
public class LogcatLogger implements Logger.ILogger {

    private Context context;

    public LogcatLogger(@NotNull final Context context) {
        this.context = context;
    }

    @Override
    public void debug(@NotNull final String tag, @NotNull final String message, @Nullable final Throwable e) {
        Log.d(tag + Logger.Level.DEBUG.TAG, message, e);
    }

    @Override
    public void verbose(@NotNull final String tag, @NotNull final String message, @Nullable final Throwable e) {
        Log.v(tag + Logger.Level.VERBOSE.TAG, message, e);
    }

    @Override
    public void information(@NotNull final String tag, @NotNull final String message, @Nullable final Throwable e) {
        Log.i(tag + Logger.Level.INFO.TAG, message, e);
    }

    @Override
    public void warning(@NotNull final String tag, @NotNull final String message, @Nullable final Throwable e) {
        Log.w(tag + Logger.Level.WARNING.TAG, message, e);
    }

    @Override
    public void error(@NotNull final String tag, @NotNull final String message, @Nullable final Throwable e) {
        Log.e(tag + Logger.Level.ERROR.TAG, message, e);
    }

    @Override
    public void fatal(String tag, String message, @Nullable final Throwable e) {
        Log.e(tag + Logger.Level.FATAL.TAG, message, e);
    }

    @Override
    public void debug(@NotNull final String tag, @NotNull final String message) {
        Log.d(tag + Logger.Level.DEBUG.TAG, message);
    }

    @Override
    public void verbose(@NotNull final String tag, @NotNull final String message) {
        Log.v(tag + Logger.Level.VERBOSE.TAG, message);
    }

    @Override
    public void information(@NotNull final String tag, @NotNull final String message) {
        Log.i(tag + Logger.Level.INFO.TAG, message);
    }

    @Override
    public void warning(@NotNull final String tag, @NotNull final String message) {
        Log.w(tag + Logger.Level.WARNING.TAG, message);
    }

    @Override
    public void error(@NotNull final String tag, @NotNull final String message) {
        Log.e(tag + Logger.Level.ERROR.TAG, message);
    }

    @Override
    public void fatal(String tag, String message) {
        Log.e(tag + Logger.Level.FATAL.TAG, message);
    }

    @Override
    public void toast(@NotNull final String message) {
        if (context == null) {
            fatal(TAG, "No context defined!", null);
            return;
        }
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
