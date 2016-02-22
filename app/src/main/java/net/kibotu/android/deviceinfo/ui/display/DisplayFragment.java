package net.kibotu.android.deviceinfo.ui.display;

import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.library.Device;
import net.kibotu.android.deviceinfo.library.legacy.DisplayHelper;
import net.kibotu.android.deviceinfo.ui.list.ListFragment;

import static net.kibotu.android.deviceinfo.library.Device.*;
import static net.kibotu.android.deviceinfo.library.legacy.DisplayHelper.getScreenDiagonalInches;
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

        addListItemVertically("Display Screen Resolution", getResolution() + " px | " + getResolutionDp() + " dp", "");
        addListItemVertically("Usable Screen Resolution", getUsableResolution() + " px | " + getUsableResolutionDp() + " dp", "");
        addListItemVertically("Screen Diagonal Length", inchToCm(getScreenDiagonalInches()) + " | " + formatInches(getScreenDiagonalInches()) + " | " + formatPixel(DisplayHelper.getScreenDiagonalPixel()), "");
        addListItemHorizontally("Refresh Rate", Device.getRefreshRate() + " FPS", "");
        addListItemHorizontally("Is Tablet", formatBool(DisplayHelper.isTablet()), "");
        addListItemHorizontally("Has Softkeys", formatBool(DisplayHelper.hasSoftKeys()), "");
        addListItemHorizontally("Screen Class", getString(R.string.screen_size), "");
        addListItemHorizontally("Density", getString(R.string.density) + " | " + getDisplayMetrics().densityDpi + " | " + getDisplayMetrics().density, "");
        addListItemHorizontally("DPI", DisplayHelper.xDpi + " x " + DisplayHelper.yDpi, "");
        addListItemHorizontally("Orientation", getString(R.string.orientation), "");
        addListItemHorizontally("Rotation", nameForRotation(getDefaultDisplay().getRotation()), "");
        addListItemHorizontally("Display ID", getDefaultDisplay().getDisplayId(), "Each logical display has a unique id. The default display has id DEFAULT_DISPLAY. (" + android.view.Display.DEFAULT_DISPLAY + ")");
        addListItemHorizontally("PixelFormat", nameForPixelFormat(getDefaultDisplay().getPixelFormat()), "");
    }
}
