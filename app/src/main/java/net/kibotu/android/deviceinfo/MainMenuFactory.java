package net.kibotu.android.deviceinfo;

import net.kibotu.android.deviceinfo.ui.build.BuildFragment;
import net.kibotu.android.deviceinfo.ui.menu.MenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nyaruhodo on 20.02.2016.
 */
public class MainMenuFactory {

    private MainMenuFactory() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static List<MenuItem> createMenu() {
        final List<MenuItem> items = new ArrayList<>();

        items.add(new MenuItem(R.string.menu_item_build, R.drawable.build, BuildFragment.class));
        items.add(new MenuItem(R.string.menu_item_configuration, R.drawable.config, BuildFragment.class));
        items.add(new MenuItem(R.string.menu_item_cpu, R.drawable.cpu, BuildFragment.class));
        items.add(new MenuItem(R.string.menu_item_gpu, R.drawable.gpu, BuildFragment.class));
        items.add(new MenuItem(R.string.menu_item_memory, R.drawable.memory, BuildFragment.class));
        items.add(new MenuItem(R.string.menu_item_battery, R.drawable.battery, BuildFragment.class));
        items.add(new MenuItem(R.string.menu_item_display, R.drawable.display, BuildFragment.class));
        items.add(new MenuItem(R.string.menu_item_network, R.drawable.network, BuildFragment.class));
        items.add(new MenuItem(R.string.menu_item_sensor, R.drawable.sensors, BuildFragment.class));
        items.add(new MenuItem(R.string.menu_item_java, R.drawable.java, BuildFragment.class));
        items.add(new MenuItem(R.string.menu_item_geolocation, R.drawable.geo, BuildFragment.class));
        items.add(new MenuItem(R.string.menu_item_app, R.drawable.info, BuildFragment.class));
        items.add(new MenuItem(R.string.menu_item_other, R.drawable.others, BuildFragment.class));

        return items;
    }
}
