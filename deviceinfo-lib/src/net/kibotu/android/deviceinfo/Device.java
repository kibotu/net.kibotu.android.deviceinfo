package net.kibotu.android.deviceinfo;


import android.app.Activity;
import android.content.Context;
import org.jetbrains.annotations.NotNull;

public class Device {

    // region Device

    private static volatile Context context;

    private Device() throws IllegalAccessException {
        throw new IllegalAccessException("static class");
    }

    public static void init(@NotNull final Context context) {
        Device.context = context;
    }

    public static Activity context() {
        if (context == null)
            throw new IllegalStateException("'context' must not be null. Please init Device.init() first.");
        return (Activity) context;
    }

    // endregion


}