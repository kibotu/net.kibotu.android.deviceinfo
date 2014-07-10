package net.kibotu.android.deviceinfo;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import net.kibotu.android.deviceinfo.fragments.list.DeviceInfoFragment;
import net.kibotu.android.deviceinfo.fragments.menu.MenuFragment;

public class MainActivity extends FragmentActivity {

    private SlidingMenu menu;
    private MenuFragment arcList;
    private DeviceInfoFragment displayList;

    public static Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        arcList = new MenuFragment();
        displayList = new DeviceInfoFragment();

        setTitle(R.string.attach);

        // set the Above View
        setContentView(R.layout.content_frame);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, displayList)
                .commit();

        // configure the SlidingMenu
        menu = new SlidingMenu(this);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.menu_frame);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.menu_frame, arcList)
                .commit();

        arcList.addItem("General", android.R.drawable.ic_menu_search);
        arcList.addItem("Hardware", android.R.drawable.ic_menu_search);
        arcList.addItem("Software", android.R.drawable.ic_menu_search);
        arcList.addItem("Display", android.R.drawable.ic_menu_search);
        arcList.addItem("Sensor", android.R.drawable.ic_menu_search);
        arcList.addItem("Battery", android.R.drawable.ic_menu_search);
        arcList.addItem("Network", android.R.drawable.ic_menu_search);

        displayList.addItem("Width", "800", "Screen Width");
        displayList.addItem("Height", "480", "Screen Height");
    }
}
