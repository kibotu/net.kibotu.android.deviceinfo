package net.kibotu.android.deviceinfo;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;

public class DisplayHelper {

    public static int mScreenWidth;
    public static int mScreenHeight;
    public static int mScreenDpi;
    public static float mDensity;
    public boolean mHasSoftKeys;
    private static Activity context;

    public static int absScreenWidth;
    public static int absScreenHeight;

    public static float xDpi;
    public static float yDpi;
    private static double screenInches;

    public DisplayHelper(final Activity context) {
        mScreenWidth = -1;
        mScreenHeight = -1;
        mScreenDpi = -1;
        xDpi = -1;
        yDpi = -1;
        mHasSoftKeys = false;
        DisplayHelper.context = context;
        init();
    }

    public static double getScreenDiagonalInches() {
        if (context == null) throw new IllegalStateException("Please instantiate first!");
        double x = Math.pow(mScreenWidth / xDpi, 2);
        double y = Math.pow(mScreenHeight / yDpi, 2);
        return Math.sqrt(x + y);
    }

    public static double getScreenDiagonalPixel() {
        if (context == null) throw new IllegalStateException("Please instantiate first!");
        double x = Math.pow(mScreenWidth, 2);
        double y = Math.pow(mScreenHeight, 2);
        return Math.sqrt(x + y);
    }

    private void init() {

        DisplayMetrics dm = new DisplayMetrics();
        Display display = context.getWindowManager().getDefaultDisplay();
        display.getMetrics(dm);

        mScreenDpi = dm.densityDpi;
        xDpi = dm.xdpi;
        yDpi = dm.ydpi;

        mDensity = dm.density;

        mScreenWidth = Math.max(dm.heightPixels, dm.widthPixels);
        mScreenHeight = Math.min(dm.heightPixels, dm.widthPixels);

        // to determine if we have softkeys: try to read absolute display size and compare to useable screen size
        absScreenWidth = mScreenWidth;
        absScreenHeight = mScreenHeight;
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17) {
            try {
                absScreenWidth = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                absScreenHeight = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            } catch (Exception ignored) {
            }
        }
        if (Build.VERSION.SDK_INT >= 17) {
            try {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(display, realSize);
                absScreenWidth = realSize.x;
                absScreenHeight = realSize.y;
            } catch (Exception ignored) {
            }
        }
    }

    public static boolean hasSoftKeys() {
        if (context == null) throw new IllegalStateException("Please instantiate first!");
        return Math.max(mScreenWidth, mScreenHeight) < Math.max(absScreenWidth,absScreenHeight) || Math.min(mScreenWidth, mScreenHeight) < Math.min(absScreenWidth, absScreenHeight) ;
    }

    public static boolean isTablet2() {
        if (context == null) throw new IllegalStateException("Please instantiate first!");
        return context.getResources().getBoolean(R.bool.IsTablet);
    }

    /**
     * <a href="http://stackoverflow.com/a/18387977">determine-if-the-device-is-a-smartphone-or-tablet</a>
     */
    public static boolean isTablet() {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static int getOrientation() {
        if (context == null) throw new IllegalStateException("Please instantiate first!");
        return isTablet() ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    }
}