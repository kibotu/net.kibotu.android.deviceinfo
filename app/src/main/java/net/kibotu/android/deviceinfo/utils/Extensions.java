package net.kibotu.android.deviceinfo.utils;

import android.graphics.Point;
import android.support.annotation.NonNull;
import android.view.View;

import static net.kibotu.android.deviceinfo.library.display.Display.getStatusBarHeight;

/**
 * Created by Nyaruhodo on 22.02.2016.
 */
public class Extensions {

    @NonNull
    public static Point getLocationOnScreenWithCenterPivot(@NonNull final View v) {

        final int[] position = new int[2];
        v.getLocationOnScreen(position);

        // moving pivot to center
        position[0] += v.getMeasuredWidth() / 2;
        position[1] += v.getMeasuredHeight() / 2;

        // taking status bar top offset into account
        position[1] -= getStatusBarHeight();

        return new Point(position[0], position[1]);
    }

    @NonNull
    public static Point getLocationOnScreenWithTopLeftPivot(@NonNull final View v) {
        final int[] position = new int[2];
        v.getLocationOnScreen(position);

        // taking status bar top offset into account
        position[1] -= getStatusBarHeight();

        return new Point(position[0], position[1]);
    }
}
