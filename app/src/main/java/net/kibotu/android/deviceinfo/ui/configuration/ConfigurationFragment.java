package net.kibotu.android.deviceinfo.ui.configuration;

import android.content.res.Configuration;
import android.os.Build;
import android.view.ViewConfiguration;

import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.model.ListItem;
import net.kibotu.android.deviceinfo.ui.list.ListFragment;

import static android.os.Build.VERSION_CODES.HONEYCOMB_MR2;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR1;
import static android.os.Build.VERSION_CODES.M;
import static com.common.android.utils.extensions.ResourceExtensions.configuration;
import static net.kibotu.android.deviceinfo.library.ViewHelper.formatBool;
import static net.kibotu.android.deviceinfo.library.ViewHelper.formatBytes;
import static net.kibotu.android.deviceinfo.library.ViewHelper.formatKeyBoardHidden;
import static net.kibotu.android.deviceinfo.library.ViewHelper.formatKeyboard;
import static net.kibotu.android.deviceinfo.library.ViewHelper.formatKeyboardHidden;
import static net.kibotu.android.deviceinfo.library.ViewHelper.formatNavigation;
import static net.kibotu.android.deviceinfo.library.ViewHelper.formatNavigationHidden;
import static net.kibotu.android.deviceinfo.library.ViewHelper.formatOrientation;
import static net.kibotu.android.deviceinfo.library.ViewHelper.formatScreenLayout;
import static net.kibotu.android.deviceinfo.library.ViewHelper.formatTouchscreen;
import static net.kibotu.android.deviceinfo.library.ViewHelper.formatUIMode;
import static net.kibotu.android.deviceinfo.library.display.Display.hasSoftKeys;
import static net.kibotu.android.deviceinfo.library.display.Display.isTablet;
import static net.kibotu.android.deviceinfo.library.version.Version.isAtLeastVersion;

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

        addConfiguration();

        addViewConfiguration();
    }

    private void addConfiguration() {

        addSubListItem(new ListItem().setLabel("android.content.res.Configuration").setDescription("http://developer.android.com/reference/android/content/res/Configuration.html"));

        final Configuration cfg = configuration();

        if (isAtLeastVersion(JELLY_BEAN_MR1))
            addHorizontallyCard("densityDpi", formatOrientation(cfg), "The target screen density being rendered to, corresponding to density resource qualifier.");

        addHorizontallyCard("Is Tablet", formatBool(isTablet()), "");
        addHorizontallyCard("Has Softkeys", formatBool(hasSoftKeys()), "");
        addHorizontallyCard("Font Scale", cfg.fontScale, "Current user preference for the scaling factor for fonts, relative to the base density scaling.");
        addHorizontallyCard("Hard Keyboard Hidden", formatKeyBoardHidden(cfg.hardKeyboardHidden), "A flag indicating whether the hard keyboard has been hidden. This will be set on a device with a mechanism to hide the keyboard from the user, when that mechanism is closed. One of: HARDKEYBOARDHIDDEN_NO, HARDKEYBOARDHIDDEN_YES.");
        addHorizontallyCard("Keyboard", formatKeyboard(cfg.keyboard), "The kind of keyboard attached to the device. One of: KEYBOARD_NOKEYS, KEYBOARD_QWERTY, KEYBOARD_12KEY.");
        addHorizontallyCard("Keyboard Hidden", formatKeyboardHidden(cfg.keyboardHidden), "A flag indicating whether any keyboard is available. Unlike hardKeyboardHidden, this also takes into account a soft keyboard, so if the hard keyboard is hidden but there is soft keyboard available, it will be set to NO. Value is one of: KEYBOARDHIDDEN_NO, KEYBOARDHIDDEN_YES.");
        addHorizontallyCard("Locale", cfg.locale.toString(), "Current user preference for the locale, corresponding to locale resource qualifier.");
        addHorizontallyCard("IMSI MCC", cfg.mcc, "IMSI MCC (Mobile Country Code), corresponding to mcc resource qualifier.");
        addHorizontallyCard("IMSI MNC", cfg.mnc, "IMSI MNC (Mobile Network Code), corresponding to mnc resource qualifier.");
        addHorizontallyCard("Navigation", formatNavigation(cfg.navigation), "The kind of navigation method available on the device. One of: NAVIGATION_NONAV, NAVIGATION_DPAD, NAVIGATION_TRACKBALL, NAVIGATION_WHEEL.");
        addHorizontallyCard("Navigation Hidden", formatNavigationHidden(cfg.navigationHidden), "A flag indicating whether any 5-way or DPAD navigation available.");
        addHorizontallyCard("Orientation", formatOrientation(cfg), "Overall orientation of the screen.");

        if (isAtLeastVersion(HONEYCOMB_MR2))
            addHorizontallyCard("ScreenHeightDp", formatOrientation(cfg), "The current height of the available screen space, in dp units, corresponding to screen height resource qualifier.");

        addHorizontallyCard("Screen Layout", formatScreenLayout(cfg.screenLayout), "Bit mask of overall layout of the screen. Currently there are two fields:\n" +
                "The SCREENLAYOUT_SIZE_MASK bits define the overall size of the screen. They may be one of SCREENLAYOUT_SIZE_SMALL, SCREENLAYOUT_SIZE_NORMAL, SCREENLAYOUT_SIZE_LARGE, or SCREENLAYOUT_SIZE_XLARGE.\n" +
                "The SCREENLAYOUT_LONG_MASK defines whether the screen is wider/taller than normal. They may be one of SCREENLAYOUT_LONG_NO or SCREENLAYOUT_LONG_YES.\n" +
                "The SCREENLAYOUT_LAYOUTDIR_MASK defines whether the screen layout is either LTR or RTL. They may be one of SCREENLAYOUT_LAYOUTDIR_LTR or SCREENLAYOUT_LAYOUTDIR_RTL.\n" +
                "See Supporting Multiple Screens for more information.");

        if (isAtLeastVersion(HONEYCOMB_MR2))
            addHorizontallyCard("screenWidthDp", formatOrientation(cfg), "The current width of the available screen space, in dp units, corresponding to screen width resource qualifier.");

        if (isAtLeastVersion(HONEYCOMB_MR2))
            addHorizontallyCard("smallestScreenWidthDp", formatOrientation(cfg), "The smallest screen size an application will see in normal operation, corresponding to smallest screen width resource qualifier.");

        addHorizontallyCard("Touchscreen", formatTouchscreen(cfg.touchscreen), "The kind of touch screen attached to the device. One of: TOUCHSCREEN_NOTOUCH, TOUCHSCREEN_FINGER.");
        addHorizontallyCard("UIMode", formatUIMode(cfg.uiMode), "Bit mask of the ui mode. Currently there are two fields:\n" +
                "The UI_MODE_TYPE_MASK bits define the overall ui mode of the device. They may be one of UI_MODE_TYPE_UNDEFINED, UI_MODE_TYPE_NORMAL, UI_MODE_TYPE_DESK, UI_MODE_TYPE_CAR, UI_MODE_TYPE_TELEVISION, UI_MODE_TYPE_APPLIANCE, or UI_MODE_TYPE_WATCH.\n" +
                "The UI_MODE_NIGHT_MASK defines whether the screen is in a special mode. They may be one of UI_MODE_NIGHT_UNDEFINED, UI_MODE_NIGHT_NO or UI_MODE_NIGHT_YES.");
    }

    private void addViewConfiguration() {

        addSubListItem(new ListItem().setLabel("android.view.ViewConfiguration").setDescription("http://developer.android.com/reference/android/view/ViewConfiguration.html"));

        final ViewConfiguration cfg = ViewConfiguration.get(getContext());

        if (isAtLeastVersion(M))
            addHorizontallyCard("Default Action Mode Hide Duration", ViewConfiguration.getDefaultActionModeHideDuration(), "the default duration in milliseconds for hide(long).");

        addHorizontallyCard("Double Tap Timeout", ViewConfiguration.getDoubleTapTimeout(), "The duration in milliseconds between the first tap's up event and the second tap's down event for an interaction to be considered a double-tap.");

        addHorizontallyCard("Scaled Fading Edge Length", cfg.getScaledFadingEdgeLength(), "The length of the fading edges in pixels.");
        addHorizontallyCard("Scaled Minimum Fling Velocity", cfg.getScaledMinimumFlingVelocity(), "Minimum velocity to initiate a fling, as measured in dips per second.");

        if (!isAtLeastVersion(Build.VERSION_CODES.KITKAT_WATCH))
            addHorizontallyCard("getGlobalActionKeyTimeout", ViewConfiguration.getGlobalActionKeyTimeout(), "The amount of time a user needs to press the relevant key to bring up the global actions dialog.");

        addHorizontallyCard("Jump Tap Timeout", ViewConfiguration.getJumpTapTimeout(), "The duration in milliseconds we will wait to see if a touch event is a jump tap. If the user does not move within this interval, it is considered to be a tap.");
        addHorizontallyCard("Key Repeat Delay", ViewConfiguration.getKeyRepeatDelay(), "The time between successive key repeats in milliseconds.");
        addHorizontallyCard("Key Repeat Timeout", ViewConfiguration.getKeyRepeatTimeout(), "The time before the first key repeat in milliseconds.");
        addHorizontallyCard("Long Press Timeout", ViewConfiguration.getLongPressTimeout(), "The duration in milliseconds before a press turns into a long press.");
        addHorizontallyCard("Scaled Minimum Fling Velocity", cfg.getScaledMinimumFlingVelocity(), "Minimum velocity to initiate a fling, as measured in pixels per second.");
        addHorizontallyCard("Scaled Maximum Fling Velocity", cfg.getScaledMaximumFlingVelocity(), "Maximum velocity to initiate a fling, as measured in pixels per second.");

        addHorizontallyCard("Pressed State Duration", ViewConfiguration.getPressedStateDuration(), "The duration in milliseconds of the pressed state in child components.");
        addHorizontallyCard("Scaled Edge Slop", cfg.getScaledEdgeSlop(), "Inset in pixels to look for touchable content when the user touches the edge of the screen.");
        addHorizontallyCard("Scaled Fading Edge Length", cfg.getScaledFadingEdgeLength(), "The length of the fading edges in pixels.");
        addHorizontallyCard("Scaled Maximum Drawing Cache Size", formatBytes(cfg.getScaledMaximumDrawingCacheSize()), "The maximum drawing cache size expressed in bytes.");
        addHorizontallyCard("Scaled Minimum Fling Velocity", cfg.getScaledMinimumFlingVelocity(), "Minimum velocity to initiate a fling, as measured in pixels per second.");
        addHorizontallyCard("Scaled Maximum Fling Velocity", cfg.getScaledMaximumFlingVelocity(), "Maximum velocity to initiate a fling, as measured in pixels per second.");
        addHorizontallyCard("Scaled Overfling Distance", cfg.getScaledOverflingDistance(), "The maximum distance a View should overfling by when showing edge effects (in pixels).");
        addHorizontallyCard("Scaled Overscroll Distance", cfg.getScaledOverscrollDistance(), "The maximum distance a View should overscroll by when showing edge effects (in pixels).");
        addHorizontallyCard("Scaled Paging Touch Slop", cfg.getScaledPagingTouchSlop(), "Distance in pixels a touch can wander before we think the user is scrolling a full page.");
        addHorizontallyCard("Scaled Scroll Bar Size", cfg.getScaledScrollBarSize(), "The width of the horizontal scrollbar and the height of the vertical scrollbar in pixels.");
        addHorizontallyCard("Scaled Touch Slop", cfg.getScaledTouchSlop(), "Distance in pixels a touch can wander before we think the user is scrolling.");
        addHorizontallyCard("Scaled Window Touch Slop", cfg.getScaledWindowTouchSlop(), "Distance in pixels a touch must be outside the bounds of a window for it to be counted as outside the window for purposes of dismissing that window.");
        addHorizontallyCard("Scroll Bar Fade Duration", ViewConfiguration.getScrollBarFadeDuration(), "Duration of the fade when scrollbars fade away in milliseconds.");
        addHorizontallyCard("Scaled Scroll Bar Size", cfg.getScaledScrollBarSize(), "The width of the horizontal scrollbar and the height of the vertical scrollbar in pixels.");
        addHorizontallyCard("Scroll Default Delay", ViewConfiguration.getScrollDefaultDelay(), "Default delay before the scrollbars fade in milliseconds.");
        addHorizontallyCard("Scroll Friction", ViewConfiguration.getScrollFriction(), "The amount of friction applied to scrolls and flings.");
        addHorizontallyCard("Tap Timeout", ViewConfiguration.getTapTimeout(), "The duration in milliseconds we will wait to see if a touch event is a tap or a scroll. If the user does not move within this interval, it is considered to be a tap.");
        addHorizontallyCard("Scaled Touch Slop", cfg.getScaledTouchSlop(), "Distance in pixels a touch can wander before we think the user is scrolling.");
        addHorizontallyCard("Scaled Window Touch Slop", cfg.getScaledWindowTouchSlop(), "Distance in pixels a touch must be outside the bounds of a window for it to be counted as outside the window for purposes of dismissing that window.");
        addHorizontallyCard("Zoom Controls Timeout", ViewConfiguration.getZoomControlsTimeout(), "The amount of time that the zoom controls should be displayed on the screen expressed in milliseconds.");
        addHorizontallyCard("Has Permanent Menu Key", formatBool(cfg.hasPermanentMenuKey()), "Report if the device has a permanent menu key available to the user.\n" +
                "\n" +
                "As of Android 3.0, devices may not have a permanent menu key available. Apps should use the action bar to present menu options to users. However, there are some apps where the action bar is inappropriate or undesirable. This method may be used to detect if a menu key is present. If not, applications should provide another on-screen affordance to access functionality.");
    }

    @Override
    protected int getHomeIcon() {
        return R.drawable.config;
    }
}
