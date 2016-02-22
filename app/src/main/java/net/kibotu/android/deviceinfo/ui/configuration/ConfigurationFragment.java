package net.kibotu.android.deviceinfo.ui.configuration;

import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.ui.list.ListFragment;

import static android.os.Build.VERSION_CODES.HONEYCOMB_MR2;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR1;
import static net.kibotu.android.deviceinfo.library.Device.isAtLeastVersion;
import static net.kibotu.android.deviceinfo.ui.ViewHelper.*;
import static net.kibotu.android.deviceinfo.utils.Extensions.configuration;

/**
 * Created by Nyaruhodo on 21.02.2016.
 */
public class ConfigurationFragment extends ListFragment {

    @Override
    protected String getTitle() {
        return getString(R.string.menu_item_configuration);
    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();

        // http://developer.android.com/reference/android/content/res/Configuration.html
        if (isAtLeastVersion(JELLY_BEAN_MR1))
            addListItemHorizontally("densityDpi", formatOrientation(configuration().densityDpi), "The target screen density being rendered to, corresponding to density resource qualifier.");

        addListItemHorizontally("Font Scale", configuration().fontScale, "Current user preference for the scaling factor for fonts, relative to the base density scaling.");
        addListItemHorizontally("Hard Keyboard Hidden", formatKeyBoardHidden(configuration().hardKeyboardHidden), "A flag indicating whether the hard keyboard has been hidden. This will be set on a device with a mechanism to hide the keyboard from the user, when that mechanism is closed. One of: HARDKEYBOARDHIDDEN_NO, HARDKEYBOARDHIDDEN_YES.");
        addListItemHorizontally("Keyboard", formatKeyboard(configuration().keyboard), "The kind of keyboard attached to the device. One of: KEYBOARD_NOKEYS, KEYBOARD_QWERTY, KEYBOARD_12KEY.");
        addListItemHorizontally("Keyboard Hidden", formatKeyboardHidden(configuration().keyboardHidden), "A flag indicating whether any keyboard is available. Unlike hardKeyboardHidden, this also takes into account a soft keyboard, so if the hard keyboard is hidden but there is soft keyboard available, it will be set to NO. Value is one of: KEYBOARDHIDDEN_NO, KEYBOARDHIDDEN_YES.");
        addListItemHorizontally("Locale", configuration().locale.toString(), "Current user preference for the locale, corresponding to locale resource qualifier.");
        addListItemHorizontally("IMSI MCC", configuration().mcc, "IMSI MCC (Mobile Country Code), corresponding to mcc resource qualifier.");
        addListItemHorizontally("IMSI MNC", configuration().mnc, "IMSI MNC (Mobile Network Code), corresponding to mnc resource qualifier.");
        addListItemHorizontally("Navigation", formatNavigation(configuration().navigation), "The kind of navigation method available on the device. One of: NAVIGATION_NONAV, NAVIGATION_DPAD, NAVIGATION_TRACKBALL, NAVIGATION_WHEEL.");
        addListItemHorizontally("Navigation Hidden", formatNavigationHidden(configuration().navigationHidden), "A flag indicating whether any 5-way or DPAD navigation available.");
        addListItemHorizontally("Orientation", formatOrientation(configuration().orientation), "Overall orientation of the screen.");

        if (isAtLeastVersion(HONEYCOMB_MR2))
            addListItemHorizontally("ScreenHeightDp", formatOrientation(configuration().screenHeightDp), "The current height of the available screen space, in dp units, corresponding to screen height resource qualifier.");

        addListItemHorizontally("Screen Layout", formatScreenLayout(configuration().screenLayout), "Bit mask of overall layout of the screen. Currently there are two fields:\n" +
                "The SCREENLAYOUT_SIZE_MASK bits define the overall size of the screen. They may be one of SCREENLAYOUT_SIZE_SMALL, SCREENLAYOUT_SIZE_NORMAL, SCREENLAYOUT_SIZE_LARGE, or SCREENLAYOUT_SIZE_XLARGE.\n" +
                "The SCREENLAYOUT_LONG_MASK defines whether the screen is wider/taller than normal. They may be one of SCREENLAYOUT_LONG_NO or SCREENLAYOUT_LONG_YES.\n" +
                "The SCREENLAYOUT_LAYOUTDIR_MASK defines whether the screen layout is either LTR or RTL. They may be one of SCREENLAYOUT_LAYOUTDIR_LTR or SCREENLAYOUT_LAYOUTDIR_RTL.\n" +
                "See Supporting Multiple Screens for more information.");

        if (isAtLeastVersion(HONEYCOMB_MR2))
            addListItemHorizontally("screenWidthDp", formatOrientation(configuration().screenWidthDp), "The current width of the available screen space, in dp units, corresponding to screen width resource qualifier.");

        if (isAtLeastVersion(HONEYCOMB_MR2))
            addListItemHorizontally("smallestScreenWidthDp", formatOrientation(configuration().smallestScreenWidthDp), "The smallest screen size an application will see in normal operation, corresponding to smallest screen width resource qualifier.");

        addListItemHorizontally("Touchscreen", formatTouchscreen(configuration().touchscreen), "The kind of touch screen attached to the device. One of: TOUCHSCREEN_NOTOUCH, TOUCHSCREEN_FINGER.");
        addListItemHorizontally("UIMode", formatUIMode(configuration().uiMode), "Bit mask of the ui mode. Currently there are two fields:\n" +
                "The UI_MODE_TYPE_MASK bits define the overall ui mode of the device. They may be one of UI_MODE_TYPE_UNDEFINED, UI_MODE_TYPE_NORMAL, UI_MODE_TYPE_DESK, UI_MODE_TYPE_CAR, UI_MODE_TYPE_TELEVISION, UI_MODE_TYPE_APPLIANCE, or UI_MODE_TYPE_WATCH.\n" +
                "The UI_MODE_NIGHT_MASK defines whether the screen is in a special mode. They may be one of UI_MODE_NIGHT_UNDEFINED, UI_MODE_NIGHT_NO or UI_MODE_NIGHT_YES.");

    }
}
