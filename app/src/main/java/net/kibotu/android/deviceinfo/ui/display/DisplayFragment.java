package net.kibotu.android.deviceinfo.ui.display;

import android.content.res.Configuration;
import android.os.Build;
import android.util.DisplayMetrics;
import com.common.android.utils.logging.Logger;
import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.library.display.Dimension;
import net.kibotu.android.deviceinfo.library.display.Display;
import net.kibotu.android.deviceinfo.model.ListItem;
import net.kibotu.android.deviceinfo.ui.list.ListFragment;

import static net.kibotu.android.deviceinfo.library.display.Display.*;
import static net.kibotu.android.deviceinfo.library.version.Version.isAtLeastVersion;
import static net.kibotu.android.deviceinfo.ui.ViewHelper.*;

/**
 * Created by Nyaruhodo on 21.02.2016.
 */
public class DisplayFragment extends ListFragment {

    @Override
    protected String getTitle() {
        return getString(R.string.menu_item_display);
    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();

        addScreen();
        addVerticallyCard("Screen Window Offset", getStatusBarHeight() + getSoftKeyHeight() + "px", "");

//        addVerticallyCard("Screen Diagonal Length", inchToCm(getScreenDiagonalInches()) + " | " + formatInches(getScreenDiagonalInches()) + " | " + formatPixel(getScreenDiagonalPixel()), "");
        addHorizontallyCard("Refresh Rate", Display.getRefreshRate() + " FPS", "Gets the refresh rate of this display in frames per second.");
        addHorizontallyCard("Is Tablet", formatBool(isTablet()), "");
        addHorizontallyCard("Has Softkeys", formatBool(hasSoftKeys()), "");
        addHorizontallyCard("Screen Class", getString(R.string.screen_size), "");
        addHorizontallyCard("Density", getString(R.string.density) + " | " + getDisplayMetrics().densityDpi + " | " + getDisplayMetrics().density, "");
//        addHorizontallyCard("xyDPI", xDpi + " x " + yDpi, "");
        addHorizontallyCard("Orientation", getString(R.string.orientation), "");
        addHorizontallyCard("Rotation", nameForRotation(getDefaultDisplay().getRotation()), "");
        addHorizontallyCard("Display ID", getDefaultDisplay().getDisplayId(), "Each logical display has a unique id. The default display has id DEFAULT_DISPLAY. (" + android.view.Display.DEFAULT_DISPLAY + ")");
        addHorizontallyCard("PixelFormat", nameForPixelFormat(getDefaultDisplay().getPixelFormat()), "");
        addHorizontallyCard("Display Country", getDisplayCountry(), "");

        addDisplayMetrics();

        addRealDisplayMetrics();
    }

    private void addScreen() {

        final Dimension screenDimensions = getScreenDimensions();

        addSubListItem(new ListItem().setLabel("Screen")
                .addChild(new ListItem().setLabel("Screen Resolution").setValue(screenDimensions.width + "x" + screenDimensions.height))
        );

        addVerticallyCard("Screen Resolution", screenDimensions.width + "x" + screenDimensions.height, "");
        addVerticallyCard("Usable Resolution", getUsableScreenHeight() + "px", "");
        addVerticallyCard("Status Bar Size", getStatusBarHeight() + "px", "");
        addVerticallyCard("Soft Key Height", getSoftKeyHeight() + "px", "");


    }

    private void addDisplayMetrics() {

        final DisplayMetrics metrics = getDisplayMetrics();

        addMetricSubList(metrics, "Display Metrics");
    }

    private void addRealDisplayMetrics() {

        if (!isAtLeastVersion(Build.VERSION_CODES.JELLY_BEAN_MR1))
            return;

        final DisplayMetrics metrics = getRealDisplayMetrics();

        addMetricSubList(metrics, "Real Display Metrics");
    }

    private void addMetricSubList(DisplayMetrics metrics, String label) {
        addSubListItem(new ListItem().setLabel(label).setDescription("file:///E:/android-sdk/docs/reference/android/util/DisplayMetrics.html")
                .addChild(new ListItem().setLabel("density").setValue(metrics.density).setDescription("The logical density of the display. This is a scaling factor for the Density Independent Pixel unit, where one DIP is one pixel on an approximately 160 dpi screen (for example a 240x320, 1.5\"x2\" screen), providing the baseline of the system's display. Thus on a 160dpi screen this density value will be 1; on a 120 dpi screen it would be .75; etc.\n" +
                        "\n" +
                        "This value does not exactly follow the real screen size (as given by xdpi and ydpi, but rather is used to scale the size of the overall UI in steps based on gross changes in the display dpi. For example, a 240x320 screen will have a density of 1 even if its width is 1.8\", 1.3\", etc. However, if the screen resolution is increased to 320x480 but the screen size remained 1.5\"x2\" then the density would be increased (probably to 1.5)."))
                .addChild(new ListItem().setLabel("scaledDensity").setValue(metrics.scaledDensity).setDescription("A scaling factor for fonts displayed on the display."))
                .addChild(new ListItem().setLabel("densityDpi").setValue(metrics.densityDpi).setDescription("The screen density expressed as dots-per-inch. The screen density expressed as dots-per-inch. May be either DENSITY_LOW, DENSITY_MEDIUM, or DENSITY_HIGH. "))
                .addChild(new ListItem().setLabel("widthPixels").setValue(metrics.widthPixels).setDescription("The absolute width of the display in pixels."))
                .addChild(new ListItem().setLabel("heightPixels").setValue(metrics.heightPixels).setDescription("The absolute height of the display in pixels."))
                .addChild(new ListItem().setLabel("xdpi").setValue(metrics.xdpi).setDescription("The exact physical pixels per inch of the screen in the X dimension."))
                .addChild(new ListItem().setLabel("ydpi").setValue(metrics.ydpi).setDescription("The exact physical pixels per inch of the screen in the Y dimension.")));
    }

    @Override
    protected int getHomeIcon() {
        return R.drawable.display;
    }


}
