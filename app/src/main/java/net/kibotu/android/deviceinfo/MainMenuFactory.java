package net.kibotu.android.deviceinfo;

import net.kibotu.android.deviceinfo.ui.app.AppFragment;
import net.kibotu.android.deviceinfo.ui.battery.BatteryFragment;
import net.kibotu.android.deviceinfo.ui.build.BuildFragment;
import net.kibotu.android.deviceinfo.ui.configuration.ConfigurationFragment;
import net.kibotu.android.deviceinfo.ui.cpu.CpuFragment;
import net.kibotu.android.deviceinfo.ui.display.DisplayFragment;
import net.kibotu.android.deviceinfo.ui.geolocation.GeolocationFragment;
import net.kibotu.android.deviceinfo.ui.gpu.GpuFragment;
import net.kibotu.android.deviceinfo.ui.java.JavaFragment;
import net.kibotu.android.deviceinfo.ui.memory.MemoryFragment;
import net.kibotu.android.deviceinfo.ui.menu.MenuItem;
import net.kibotu.android.deviceinfo.ui.network.NetworkFragment;
import net.kibotu.android.deviceinfo.ui.other.MiscellaneousFragment;
import net.kibotu.android.deviceinfo.ui.sensor.SensorFragment;

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
        items.add(new MenuItem(R.string.menu_item_configuration, R.drawable.config, ConfigurationFragment.class));
        items.add(new MenuItem(R.string.menu_item_cpu, R.drawable.cpu, CpuFragment.class));
        items.add(new MenuItem(R.string.menu_item_gpu, R.drawable.gpu, GpuFragment.class));
        items.add(new MenuItem(R.string.menu_item_memory, R.drawable.memory, MemoryFragment.class));
        items.add(new MenuItem(R.string.menu_item_battery, R.drawable.battery, BatteryFragment.class));
        items.add(new MenuItem(R.string.menu_item_display, R.drawable.display, DisplayFragment.class));
        items.add(new MenuItem(R.string.menu_item_network, R.drawable.network, NetworkFragment.class));
        items.add(new MenuItem(R.string.menu_item_sensor, R.drawable.sensors, SensorFragment.class));
        items.add(new MenuItem(R.string.menu_item_java, R.drawable.java, JavaFragment.class));
        items.add(new MenuItem(R.string.menu_item_geolocation, R.drawable.geo, GeolocationFragment.class));
        items.add(new MenuItem(R.string.menu_item_app, R.drawable.info, AppFragment.class));
        items.add(new MenuItem(R.string.menu_item_other, R.drawable.others, MiscellaneousFragment.class));

        return items;
    }
}
