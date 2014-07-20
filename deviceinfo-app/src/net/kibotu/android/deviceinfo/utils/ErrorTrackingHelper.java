package net.kibotu.android.deviceinfo.utils;

import android.content.Context;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import net.kibotu.android.deviceinfo.Device;
import net.kibotu.android.deviceinfo.Logger;
import net.kibotu.android.deviceinfo.UncaughtExceptionHandlerDecorator;
import org.json.JSONArray;
import org.json.JSONObject;

public class ErrorTrackingHelper {

    private static Context context;
    private static JSONObject metaData;

    public static void startErrorTracking(final Context context) {
        ErrorTrackingHelper.context = context;
        metaData = new JSONObject();
        Parse.initialize(context, "ydr6IjoEfkcmgfqBFxeTujnVHGZ8Gtvqw8XVJtde", "hOy07YE5IT8iuc8WlUZJ1Ssv23MpEdXjOdD4CMb6");
        UncaughtExceptionHandlerDecorator.setHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(final Thread thread, final Throwable e) {
                notifyError(e);
            }
        });
    }

    public static void notifyError(final Throwable e) {
        if (context == null) {
            Logger.e("Error tracking not initialized and therefore disabled. Please call first ErrorTrackingHelper.startErrorTracking()");
            return;
        }

        ParseObject testObject = new ParseObject("Throwable");
        testObject.put("metaData", metaData);

        JSONArray exceptions = new JSONArray();
        addException(exceptions, e);
        testObject.put("exceptionJson", ""+exceptions);

        testObject.getObjectId();
        testObject.saveEventually(new SaveCallback() {
            @Override
            public void done(final ParseException e) {
                Logger.v("Error has been send.");
            }
        });
    }

    private static void addException(final JSONArray exceptions, final Throwable throwable) {
        // Unwrap exceptions
        if (throwable != null) {
            Throwable currentEx = throwable;
            while (currentEx != null) {
                final JSONObject exception = new JSONObject(); // current exception
                JSONUtils.safePut(exception, "errorType", currentEx.getClass().getName());
                JSONUtils.safePut(exception, "message", currentEx.getMessage());
                addStacktraceElements(exception, currentEx.getStackTrace(), 0); // add current stacktrace to the current exception
                exceptions.put(exception); // add the current exception to all exception array
                currentEx = currentEx.getCause();
            }
        } else {
            Logger.w("Exception is null.");
        }
    }

    private static void addStacktraceElements(final JSONObject json, StackTraceElement[] stackTrace, final int startIndex) {
        if (stackTrace.length < startIndex) {
            Logger.w("stackTrace.length < startIndex");
            return;
        }

        final JSONArray stacktrace = new JSONArray();
        final String packageName = Device.getPackageName();

        for (int i = startIndex; i < stackTrace.length; ++i) {
            try {
                final StackTraceElement el = stackTrace[i];
                final JSONObject line = new JSONObject();
                JSONUtils.safePut(line, "class", el.getClassName());
                JSONUtils.safePut(line, "method", el.getMethodName());
                JSONUtils.safePut(line, "file", el.getLineNumber() == -2 ? "<Native Method>" : el.getFileName() == null ? "Unknown" : el.getFileName()); // line number -2 = native method
                JSONUtils.safePut(line, "lineNumber", el.getLineNumber());

                // Check if line is inProject
                if (el.getClassName().startsWith(packageName)) {
                    line.put("inProject", true);
                    // break;
                }
                stacktrace.put(line);
            } catch (final Exception e) {
                Logger.e(e.getMessage(), e);
            }
        }
        JSONUtils.safeAppendToJsonArray(json, "stacktrace", stacktrace);
    }

    public static void addMetaData(final JSONObject metaData) {
        JSONUtils.merge(ErrorTrackingHelper.metaData,metaData);
    }

    public static void endSession() {
        UncaughtExceptionHandlerDecorator.release();
    }
}