package net.kibotu.android.deviceinfo.ui.display;

import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.library.Device;
import net.kibotu.android.deviceinfo.library.display.DisplayHelper;
import net.kibotu.android.deviceinfo.ui.list.ListFragment;

import static net.kibotu.android.deviceinfo.library.Device.*;
import static net.kibotu.android.deviceinfo.library.display.DisplayHelper.getScreenDiagonalInches;
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

        addVerticallyCard("Display Screen Resolution", getResolution() + " px | " + getResolutionDp() + " dp", "");
        addVerticallyCard("Usable Screen Resolution", getUsableResolution() + " px | " + getUsableResolutionDp() + " dp", "");
        addVerticallyCard("Screen Diagonal Length", inchToCm(getScreenDiagonalInches()) + " | " + formatInches(getScreenDiagonalInches()) + " | " + formatPixel(DisplayHelper.getScreenDiagonalPixel()), "");
        addHorizontallyCard("Refresh Rate", Device.getRefreshRate() + " FPS", "");
        addHorizontallyCard("Is Tablet", formatBool(DisplayHelper.isTablet()), "");
        addHorizontallyCard("Has Softkeys", formatBool(DisplayHelper.hasSoftKeys()), "");
        addHorizontallyCard("Screen Class", getString(R.string.screen_size), "");
        addHorizontallyCard("Density", getString(R.string.density) + " | " + getDisplayMetrics().densityDpi + " | " + getDisplayMetrics().density, "");
        addHorizontallyCard("DPI", DisplayHelper.xDpi + " x " + DisplayHelper.yDpi, "");
        addHorizontallyCard("Orientation", getString(R.string.orientation), "");
        addHorizontallyCard("Rotation", nameForRotation(getDefaultDisplay().getRotation()), "");
        addHorizontallyCard("Display ID", getDefaultDisplay().getDisplayId(), "Each logical display has a unique id. The default display has id DEFAULT_DISPLAY. (" + android.view.Display.DEFAULT_DISPLAY + ")");
        addHorizontallyCard("PixelFormat", nameForPixelFormat(getDefaultDisplay().getPixelFormat()), "");
    }

    @Override
    protected int getHomeIcon() {
        return R.drawable.display;
    }
}
