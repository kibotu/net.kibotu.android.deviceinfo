package net.kibotu.android.deviceinfo.ui.menu;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.support.annotation.DrawableRes;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.ui.app.AppFragment;
import net.kibotu.android.deviceinfo.ui.battery.BatteryFragment;
import net.kibotu.android.deviceinfo.ui.buildinfo.BuildFragment;
import net.kibotu.android.deviceinfo.ui.configuration.ConfigurationFragment;
import net.kibotu.android.deviceinfo.ui.cpu.CpuFragment;
import net.kibotu.android.deviceinfo.ui.display.DisplayFragment;
import net.kibotu.android.deviceinfo.ui.geolocation.GeolocationFragment;
import net.kibotu.android.deviceinfo.ui.gpu.GpuFragment;
import net.kibotu.android.deviceinfo.ui.java.JavaFragment;
import net.kibotu.android.deviceinfo.ui.memory.MemoryFragment;
import net.kibotu.android.deviceinfo.ui.network.NetworkFragment;
import net.kibotu.android.deviceinfo.ui.other.MiscellaneousFragment;
import net.kibotu.android.deviceinfo.ui.sensor.SensorFragment;

import static com.common.android.utils.ContextHelper.getAppCompatActivity;
import static com.common.android.utils.ContextHelper.getContext;
import static com.common.android.utils.extensions.ActivityExtensions.getSupportActionBar;
import static com.common.android.utils.extensions.FragmentExtensions.replaceToBackStackBySlidingHorizontally;
import static com.common.android.utils.extensions.ResourceExtensions.color;
import static com.common.android.utils.extensions.ViewExtensions.getContentRoot;
import static com.common.android.utils.extensions.ViewExtensions.inflate;


/**
 * Created by Nyaruhodo on 04.09.2016.
 */

public enum Menu {

    instance;

    private static final String TAG = Menu.class.getSimpleName();
    private Drawer drawer;

    private ActionbarBaseViewHolder actionbarViewHolder;

    public static boolean isDrawerOpen() {
        return instance.drawer.isDrawerOpen();
    }

    public static void closeDrawer() {
        instance.drawer.closeDrawer();
    }

    public static void with(Activity activity) {
        instance.drawer = new DrawerBuilder()
                .withTranslucentStatusBar(true)
                .withActivity(activity)
                .addDrawerItems(createDebugMenuItems())
                .withOnDrawerItemClickListener(createDebugMenuClickListener())
                .build();

        instance.setupDefaultActionbar();
    }

    private void setupDefaultActionbar() {
        Toolbar toolbar = (Toolbar) getContentRoot().findViewById(R.id.toolbar);
        actionbarViewHolder = new ActionbarBaseViewHolder(inflate(R.layout.custom_toolbar, toolbar));
        actionbarViewHolder.iconHitBox.setOnClickListener(v -> drawer.openDrawer());
        getAppCompatActivity().setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setIcon(android.R.color.transparent);
        actionBar.setHomeButtonEnabled(false);

        final ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        actionBar.setCustomView(actionbarViewHolder.itemView, params);
    }

    private static IDrawerItem[] createDebugMenuItems() {
        return new IDrawerItem[]{
                new SecondaryDrawerItem().withIdentifier(R.string.menu_item_build).withName(R.string.menu_item_build).withIcon(R.drawable.build),
                new SecondaryDrawerItem().withIdentifier(R.string.menu_item_configuration).withName(R.string.menu_item_configuration).withIcon(R.drawable.config),
                new SecondaryDrawerItem().withIdentifier(R.string.menu_item_cpu).withName(R.string.menu_item_cpu).withIcon(R.drawable.cpu),
                new SecondaryDrawerItem().withIdentifier(R.string.menu_item_gpu).withName(R.string.menu_item_gpu).withIcon(R.drawable.gpu),
                new SecondaryDrawerItem().withIdentifier(R.string.menu_item_memory).withName(R.string.menu_item_memory).withIcon(R.drawable.memory),
                new SecondaryDrawerItem().withIdentifier(R.string.menu_item_battery).withName(R.string.menu_item_battery).withIcon(R.drawable.battery),
                new SecondaryDrawerItem().withIdentifier(R.string.menu_item_display).withName(R.string.menu_item_display).withIcon(R.drawable.display),
                new SecondaryDrawerItem().withIdentifier(R.string.menu_item_network).withName(R.string.menu_item_network).withIcon(R.drawable.network),
                new SecondaryDrawerItem().withIdentifier(R.string.menu_item_sensor).withName(R.string.menu_item_sensor).withIcon(R.drawable.sensors),
                new SecondaryDrawerItem().withIdentifier(R.string.menu_item_java).withName(R.string.menu_item_java).withIcon(R.drawable.java),
                new SecondaryDrawerItem().withIdentifier(R.string.menu_item_geolocation).withName(R.string.menu_item_geolocation).withIcon(R.drawable.geo),
                new SecondaryDrawerItem().withIdentifier(R.string.menu_item_app).withName(R.string.menu_item_app).withIcon(R.drawable.info),
                new SecondaryDrawerItem().withIdentifier(R.string.menu_item_other).withName(R.string.menu_item_other).withIcon(R.drawable.others)
        };
    }

    private static Drawer.OnDrawerItemClickListener createDebugMenuClickListener() {

        return (view, position, drawerItem) -> {

            final int identifier = (int) drawerItem.getIdentifier();
            switch (identifier) {
                case R.string.menu_item_build:
                    replaceToBackStackBySlidingHorizontally(new BuildFragment());
                    break;
                case R.string.menu_item_configuration:
                    replaceToBackStackBySlidingHorizontally(new ConfigurationFragment());
                    break;
                case R.string.menu_item_cpu:
                    replaceToBackStackBySlidingHorizontally(new CpuFragment());
                    break;
                case R.string.menu_item_gpu:
                    replaceToBackStackBySlidingHorizontally(new GpuFragment());
                    break;
                case R.string.menu_item_memory:
                    replaceToBackStackBySlidingHorizontally(new MemoryFragment());
                    break;
                case R.string.menu_item_battery:
                    replaceToBackStackBySlidingHorizontally(new BatteryFragment());
                    break;
                case R.string.menu_item_display:
                    replaceToBackStackBySlidingHorizontally(new DisplayFragment());
                    break;
                case R.string.menu_item_network:
                    replaceToBackStackBySlidingHorizontally(new NetworkFragment());
                    break;
                case R.string.menu_item_sensor:
                    replaceToBackStackBySlidingHorizontally(new SensorFragment());
                    break;
                case R.string.menu_item_java:
                    replaceToBackStackBySlidingHorizontally(new JavaFragment());
                    break;
                case R.string.menu_item_geolocation:
                    replaceToBackStackBySlidingHorizontally(new GeolocationFragment());
                    break;
                case R.string.menu_item_app:
                    replaceToBackStackBySlidingHorizontally(new AppFragment());
                    break;
                case R.string.menu_item_other:
                    replaceToBackStackBySlidingHorizontally(new MiscellaneousFragment());
                    break;
            }

            instance.drawer.closeDrawer();

            return true;
        };
    }

    public static void setActionBarIcon(@DrawableRes final int drawable) {
        Glide.with(getContext())
                .load(drawable)
                .asBitmap()
                .fitCenter()
                .into(new BitmapImageViewTarget(instance.actionbarViewHolder.homeIcon) {
                    @Override
                    public void onResourceReady(final Bitmap drawable, final GlideAnimation anim) {
                        super.onResourceReady(drawable, anim);
                        // colorizing icon for contrast also making sure we don't accidentally cache the colorized image with same name as the original
                        instance.actionbarViewHolder.homeIcon.setColorFilter(color(android.R.color.white), PorterDuff.Mode.SRC_IN);
                    }
                });
    }

    public static void setActionBarTitle(String title) {
        instance.actionbarViewHolder.title.setText("" + title);
    }
}
