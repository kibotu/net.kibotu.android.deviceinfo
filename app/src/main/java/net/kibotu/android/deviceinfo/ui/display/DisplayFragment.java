package net.kibotu.android.deviceinfo.ui.display;

import android.graphics.Point;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;

import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.library.display.Dimension;
import net.kibotu.android.deviceinfo.library.display.Display;
import net.kibotu.android.deviceinfo.model.ListItem;
import net.kibotu.android.deviceinfo.ui.list.ListFragment;

import java.util.Arrays;

import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR1;
import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static android.os.Build.VERSION_CODES.M;
import static java.text.MessageFormat.format;
import static net.kibotu.android.deviceinfo.library.ViewHelper.formatBool;
import static net.kibotu.android.deviceinfo.library.ViewHelper.formatInches;
import static net.kibotu.android.deviceinfo.library.ViewHelper.formatPixel;
import static net.kibotu.android.deviceinfo.library.ViewHelper.formatSupportedModes;
import static net.kibotu.android.deviceinfo.library.ViewHelper.nameForPixelFormat;
import static net.kibotu.android.deviceinfo.library.ViewHelper.nameForRotation;
import static net.kibotu.android.deviceinfo.library.display.Display.getDefaultDisplay;
import static net.kibotu.android.deviceinfo.library.display.Display.getDisplayCountry;
import static net.kibotu.android.deviceinfo.library.display.Display.getDisplayMetrics;
import static net.kibotu.android.deviceinfo.library.display.Display.getRealDisplayMetrics;
import static net.kibotu.android.deviceinfo.library.display.Display.getScreenDiagonalAsInch;
import static net.kibotu.android.deviceinfo.library.display.Display.getScreenDiagonalAsPixel;
import static net.kibotu.android.deviceinfo.library.display.Display.getScreenDimensions;
import static net.kibotu.android.deviceinfo.library.display.Display.getSoftKeyHeight;
import static net.kibotu.android.deviceinfo.library.display.Display.getStatusBarHeight;
import static net.kibotu.android.deviceinfo.library.display.Display.getUsableScreenHeight;
import static net.kibotu.android.deviceinfo.library.version.Version.isAtLeastVersion;

/**
 * Created by Nyaruhodo on 21.02.2016.
 */
public class DisplayFragment extends ListFragment {

    @Override
    public String getTitle() {
        return getString(R.string.menu_item_display);
    }

    @Override
    protected int getHomeIcon() {
        return R.drawable.display;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addScreen();

        addMisc();

        addDisplayMetrics();

        addRealDisplayMetrics();
    }

    private void addMisc() {

        final android.view.Display display = getDefaultDisplay();

        ListItem item = new ListItem().setLabel("Misc")
                .addChild(new ListItem().setLabel("Refresh Rate").setValue(Display.getRefreshRate() + " FPS").setDescription("Gets the refresh rate of this display in frames per second."))
                .addChild(new ListItem().setLabel("Display ID").setValue(getDefaultDisplay().getDisplayId()).setDescription("Each logical display has a unique id. The default display has id DEFAULT_DISPLAY. (" + android.view.Display.DEFAULT_DISPLAY + ")"))
                .addChild(new ListItem().setLabel("Orientation").setValue(getString(R.string.orientation)))
                .addChild(new ListItem().setLabel("Rotation").setValue(nameForRotation(getDefaultDisplay().getRotation())))
                .addChild(new ListItem().setLabel("PixelFormat").setValue(nameForPixelFormat(getDefaultDisplay().getPixelFormat())))
                .addChild(new ListItem().setLabel("Display Country").setValue(getDisplayCountry()));

        if (isAtLeastVersion(JELLY_BEAN_MR1))
            item.addChild(new ListItem().setLabel("Name").setValue(display.getName()).setDescription("Gets the name of the display. Note that some displays may be renamed by the user. "));

        if (isAtLeastVersion(JELLY_BEAN_MR1))
            item.addChild(new ListItem().setLabel("Valid").setValue(formatBool(display.isValid())).setDescription("Returns true if this display is still valid, false if the display has been removed. If the display is invalid, then the methods of this class will continue to report the most recently observed display information. However, it is unwise (and rather fruitless) to continue using a Display object after the display's demise. It's possible for a display that was previously invalid to become valid again if a display with the same id is reconnected."));

        if (isAtLeastVersion(LOLLIPOP))
            item.addChild(new ListItem().setLabel("Supported Refresh Rates").setValue(Arrays.toString(display.getSupportedRefreshRates())).setDescription("Get the supported refresh rates of this display in frames per second. This method only returns refresh rates for the display's default modes. For more options, use getSupportedModes()."));

        if (isAtLeastVersion(M))
            item.addChild(new ListItem().setLabel("Supported Modes").setValue(formatSupportedModes(display.getSupportedModes())).setDescription("Gets the supported modes of this display."));

        final Point outSmallestSize = new Point();
        final Point outLargestSize = new Point();
        display.getCurrentSizeRange(outSmallestSize, outLargestSize);
        item.addChild(new ListItem().setLabel("Current Size Range")
                .setValue(format("{0}x{1} to {2}x{3}", outSmallestSize.x, outSmallestSize.y, outLargestSize.x, outLargestSize.y))
                .setDescription("Return the range of display sizes an application can expect to encounter under normal operation, as long as there is no physical change in screen size. This is basically the sizes you will see as the orientation changes, taking into account whatever screen decoration there is in each rotation. For example, the status bar is always at the top of the screen, so it will reduce the height both in landscape and portrait, and the smallest height returned here will be the smaller of the two. This is intended for applications to get an idea of the range of sizes they will encounter while going through device rotations, to provide a stable UI through rotation. The sizes here take into account all standard system decorations that reduce the size actually available to the application: the status bar, navigation bar, system bar, etc. It does not take into account more transient elements like an IME soft keyboard."));

        if (isAtLeastVersion(LOLLIPOP))
            item.addChild(new ListItem().setLabel("App Vsync Offset Nanos").setValue(display.getAppVsyncOffsetNanos() + " ns").setDescription("Gets the app VSYNC offset, in nanoseconds. This is a positive value indicating the phase offset of the VSYNC events provided by Choreographer relative to the display refresh. For example, if Choreographer reports that the refresh occurred at time N, it actually occurred at (N - appVsyncOffset).\n" +
                    "\n" +
                    "Apps generally do not need to be aware of this. It's only useful for fine-grained A/V synchronization. "));

        addSubListItem(item);
    }

    // region screen

    private void addScreen() {

        final Dimension screenDimensions = getScreenDimensions();

        addSubListItem(new ListItem().setLabel("Screen")
                .addChild(new ListItem().setLabel("Resolution").setValue(screenDimensions.width + "x" + screenDimensions.height + " px"))
                .addChild(new ListItem().setLabel("Diagonal Length").setValue(formatInches(getScreenDiagonalAsInch()) + " | " + formatPixel(getScreenDiagonalAsPixel())))
                .addChild(new ListItem().setLabel("Usable").setValue(screenDimensions.width + "x" + getUsableScreenHeight() + " px"))
                .addChild(new ListItem().setLabel("Status Bar Height").setValue(getStatusBarHeight() + " px"))
                .addChild(new ListItem().setLabel("Soft Key Bar Height").setValue(getSoftKeyHeight() + " px"))
                .addChild(new ListItem().setLabel("Density: Size | Dpi | Scale").setValue(getString(R.string.density) + " | " + getDisplayMetrics().densityDpi + " | " + getDisplayMetrics().density))
                .addChild(new ListItem().setLabel("Density: Width x Height").setValue(getDisplayMetrics().xdpi + " x " + getDisplayMetrics().ydpi + " px"))

        );
    }

    // endregion

    // region display metrics

    private void addDisplayMetrics() {

        final DisplayMetrics metrics = getDisplayMetrics();

        addMetricSubList(metrics, "Display Metrics");
    }

    private void addRealDisplayMetrics() {

        if (!isAtLeastVersion(JELLY_BEAN_MR1))
            return;

        final DisplayMetrics metrics = getRealDisplayMetrics();

        addMetricSubList(metrics, "Real Display Metrics (SDK > 17)");
    }

    private void addMetricSubList(DisplayMetrics metrics, String label) {
        addSubListItem(new ListItem().setLabel(label).setDescription("file:///E:/android-sdk/docs/reference/android/util/DisplayMetrics.html")
                .addChild(new ListItem().setLabel("Width Pixels").setValue(metrics.widthPixels).setDescription("The absolute width of the display in pixels."))
                .addChild(new ListItem().setLabel("Height Pixels").setValue(metrics.heightPixels).setDescription("The absolute height of the display in pixels."))
                .addChild(new ListItem().setLabel("Density Dpi").setValue(metrics.densityDpi).setDescription("The screen density expressed as dots-per-inch. The screen density expressed as dots-per-inch. May be either DENSITY_LOW, DENSITY_MEDIUM, or DENSITY_HIGH. "))
                .addChild(new ListItem().setLabel("Density").setValue(metrics.density).setDescription("The logical density of the display. This is a scaling factor for the Density Independent Pixel unit, where one DIP is one pixel on an approximately 160 dpi screen (for example a 240x320, 1.5\"x2\" screen), providing the baseline of the system's display. Thus on a 160dpi screen this density value will be 1; on a 120 dpi screen it would be .75; etc.\n" +
                        "\n" +
                        "This value does not exactly follow the real screen size (as given by xdpi and ydpi, but rather is used to scale the size of the overall UI in steps based on gross changes in the display dpi. For example, a 240x320 screen will have a density of 1 even if its width is 1.8\", 1.3\", etc. However, if the screen resolution is increased to 320x480 but the screen size remained 1.5\"x2\" then the density would be increased (probably to 1.5)."))
                .addChild(new ListItem().setLabel("scaledDensity").setValue(metrics.scaledDensity).setDescription("A scaling factor for fonts displayed on the display."))
                .addChild(new ListItem().setLabel("x Dpi").setValue(metrics.xdpi).setDescription("The exact physical pixels per inch of the screen in the X dimension."))
                .addChild(new ListItem().setLabel("y Dpi").setValue(metrics.ydpi).setDescription("The exact physical pixels per inch of the screen in the Y dimension.")));
    }

    // endregion
}
