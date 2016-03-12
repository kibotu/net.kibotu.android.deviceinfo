package net.kibotu.android.deviceinfo.library.display;

import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import net.kibotu.android.deviceinfo.library.R;

import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR1;
import static java.lang.Math.*;
import static net.kibotu.android.deviceinfo.library.Device.getContext;
import static net.kibotu.android.deviceinfo.library.services.SystemService.getWindowManager;
import static net.kibotu.android.deviceinfo.library.version.Version.isAtLeastVersion;

public class Display {

    public static android.view.Display getDefaultDisplay() {
        return getWindowManager().getDefaultDisplay();
    }

    public static float getRefreshRate() {
        return getDefaultDisplay().getRefreshRate();
    }

    @NonNull
    public static DisplayMetrics getDisplayMetrics() {
        final DisplayMetrics metrics = new DisplayMetrics();
        getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }


    @TargetApi(JELLY_BEAN_MR1)
    @NonNull
    public static DisplayMetrics getRealDisplayMetrics() {
        final DisplayMetrics metrics = new DisplayMetrics();
        getDefaultDisplay().getRealMetrics(metrics);
        return metrics;
    }

//    @Deprecated
//    public static String getUsableResolution() {
//        return String.format("%dx%d", Math.max(Display.mScreenWidth, Display.mScreenHeight), Math.min(Display.mScreenWidth, Display.mScreenHeight));
//    }
//
//    @Deprecated
//    public static String getUsableResolutionDp() {
//        return String.format("%.0fx%.0f", Math.max(Display.mScreenWidth, Display.mScreenHeight) / Display.mDensity, Math.min(Display.mScreenWidth, Display.mScreenHeight) / Display.mDensity);
//    }
//
//    @Deprecated
//    public static String getResolution() {
//        return String.format("%dx%d", Math.max(Display.absScreenWidth, Display.absScreenHeight), Math.min(Display.absScreenWidth, Display.absScreenHeight));
//    }
//
//    @Deprecated
//    public static String getResolutionDp() {
//        return String.format("%.0fx%.0f", Math.max(Display.absScreenWidth, Display.absScreenHeight) / Display.mDensity, Math.min(Display.absScreenWidth, Display.absScreenHeight) / Display.mDensity);
//    }

    public static double getScreenDiagonalPixel() {
        final Dimension screen = getScreenDimensions();
        return sqrt(screen.width * screen.width + screen.height * screen.height);
    }

    public static boolean hasSoftKeys() {
        return getSoftKeyHeight() > 0;
    }

    public static boolean isTabletByLayout() {
        return getContext().getResources().getBoolean(R.bool.IsTablet);
    }

    /**
     * <a href="http://stackoverflow.com/a/18387977">determine-if-the-device-is-a-smartphone-or-tablet</a>
     */
    public static boolean isTablet() {
        return (getContext().getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static int getOrientation() {
        return isTablet()
                ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    }

    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getContext().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static String getDisplayCountry() {
        return getContext().getResources().getConfiguration().locale.getDisplayCountry();
    }

    public static int getUsableScreenHeight() {
        return getScreenDimensions().height - getStatusBarHeight() - getSoftKeyHeight();
    }

    public static int getSoftKeyHeight() {
        return isAtLeastVersion(JELLY_BEAN_MR1)
                ? getRealDisplayMetrics().heightPixels - getDisplayMetrics().heightPixels
                : getScreenDimensions().height - getDisplayMetrics().heightPixels;
    }

    @NonNull
    public static Dimension getScreenDimensionsPortrait() {
        final Dimension dimension = getScreenDimensions();
        return new Dimension(min(dimension.width, dimension.height), max(dimension.width, dimension.height));
    }

    @NonNull
    public static Dimension getScreenDimensionsLandscape() {
        final Dimension dimension = getScreenDimensions();
        return new Dimension(max(dimension.width, dimension.height), min(dimension.width, dimension.height));
    }

    @NonNull
    public static Dimension getScreenDimensions() {

        final DisplayMetrics dm = new DisplayMetrics();
        final android.view.Display display = getDefaultDisplay();
        display.getMetrics(dm);

        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;

        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17) {
            try {
                screenWidth = (Integer) android.view.Display.class.getMethod("getRawWidth").invoke(display);
                screenHeight = (Integer) android.view.Display.class.getMethod("getRawHeight").invoke(display);
            } catch (Exception ignored) {
            }
        }
        if (Build.VERSION.SDK_INT >= 17) {
            try {
                Point realSize = new Point();
                android.view.Display.class.getMethod("getRealSize", Point.class).invoke(display, realSize);
                screenWidth = realSize.x;
                screenHeight = realSize.y;
            } catch (Exception ignored) {
            }
        }

        return new Dimension(screenWidth, screenHeight);
    }
}