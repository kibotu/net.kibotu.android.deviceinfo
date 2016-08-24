package net.kibotu.android.deviceinfo.ui.menu;

import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.recyclerviewpresenter.PresenterAdapter;

import java.util.List;

import butterknife.ButterKnife;

import static com.common.android.utils.ContextHelper.getActivity;
import static com.common.android.utils.ContextHelper.getAppCompatActivity;
import static com.common.android.utils.ContextHelper.getContext;
import static com.common.android.utils.extensions.ActivityExtensions.getSupportActionBar;
import static com.common.android.utils.extensions.ResourceExtensions.color;
import static com.common.android.utils.extensions.ViewExtensions.getContentRoot;
import static com.common.android.utils.extensions.ViewExtensions.inflate;

/**
 * Created by Nyaruhodo on 20.02.2016.
 */
public class MainMenu implements IMainMenu {

    @NonNull
    TextView leftDrawerTitle;

    @NonNull
    DrawerLayout drawerLayout;

    @NonNull
    NavigationView leftDrawer;

    @NonNull
    RecyclerView list;

    @NonNull
    Toolbar toolbar;

    ActionBarDrawerToggle drawerToggle;

    ActionbarBaseViewHolder actionbarViewHolder;

    PresenterAdapter<MenuItem> adapter;

    public MainMenu() {
        ButterKnife.bind(this, getContentRoot());

        leftDrawerTitle = (TextView) getContentRoot().findViewById(R.id.drawer_title);
        drawerLayout = (DrawerLayout) getContentRoot().findViewById(R.id.drawer_layout);
        leftDrawer = (NavigationView) getContentRoot().findViewById(R.id.left_drawer);
        list = (RecyclerView) getContentRoot().findViewById(R.id.main_menu_list);
        toolbar = (Toolbar) getContentRoot().findViewById(R.id.toolbar);

        adapter = new PresenterAdapter<>();
        list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        list.setAdapter(adapter);

        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        setupDefaultActionbar();
    }

    protected void setupDefaultActionbar() {
        getAppCompatActivity().setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setIcon(android.R.color.transparent);
        actionBar.setHomeButtonEnabled(false);

        actionbarViewHolder = new ActionbarBaseViewHolder(inflate(R.layout.custom_toolbar, toolbar));
        actionbarViewHolder.iconHitBox.setOnClickListener(v -> openLeftDrawer());

        final ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        actionBar.setCustomView(actionbarViewHolder.itemView, params);
    }

    @NonNull
    @Override
    public IMainMenu prepareDrawers() {
        if (drawerToggle != null)
            return this;

        drawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(final View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(final View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerSlide(final View drawerView, final float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };

        drawerLayout.post(() -> {
            drawerToggle.setHomeAsUpIndicator(null);
            drawerToggle.setDrawerIndicatorEnabled(false);
            drawerLayout.addDrawerListener(drawerToggle);
            drawerToggle.syncState();
        });

        return this;
    }

    @NonNull
    @Override
    public IMainMenu setLeftDrawerTitle(@NonNull final String title) {
        this.leftDrawerTitle.setText(title);
        return this;
    }

    @NonNull
    @Override
    public IMainMenu setTitle(@NonNull final String title) {
        getSupportActionBar().setTitle(title);
        actionbarViewHolder.title.setText("" + title);
        return this;
    }

    @NonNull
    @Override
    public IMainMenu showActionBar(final boolean isShowing) {
        if (isShowing)
            getSupportActionBar().show();
        else
            getSupportActionBar().hide();
        return this;
    }

    @NonNull
    @Override
    public IMainMenu setHomeIcon(@DrawableRes final int drawable) {
//        getSupportActionBar().setHomeAsUpIndicator(drawable(drawable));

        Glide.with(getContext())
                .load(drawable)
                .asBitmap()
                .fitCenter()
                .into(new BitmapImageViewTarget(actionbarViewHolder.homeIcon) {
                    @Override
                    public void onResourceReady(final Bitmap drawable, final GlideAnimation anim) {
                        super.onResourceReady(drawable, anim);
                        // colorizing icon for contrast also making sure we don't accidentally cache the colorized image with same name as the original
                        actionbarViewHolder.homeIcon.setColorFilter(color(android.R.color.white), PorterDuff.Mode.SRC_IN);
                    }
                });

        return this;
    }

    @NonNull
    @Override
    public IMainMenu setLeftDrawerLockMode(@LockMode final int lockMode) {
        drawerLayout.setDrawerLockMode(lockMode, leftDrawer);
        return this;
    }

    public void openLeftDrawer() {
        drawerLayout.openDrawer(Gravity.LEFT);
    }

    @NonNull
    @Override
    public IMainMenu setMenuItems(@NonNull final List<MenuItem> menu) {
        adapter.clear();
        for (final MenuItem item : menu)
            adapter.add(item, MenuItemBinder.class);
        adapter.notifyDataSetChanged();
        return this;
    }

    protected boolean isLeftDrawerOpen() {
        return drawerLayout.isDrawerOpen(Gravity.LEFT);
    }

    @Override
    public boolean isDrawerOpen() {
        return isLeftDrawerOpen();
    }

    @NonNull
    @Override
    public IMainMenu closeDrawers() {
        drawerLayout.closeDrawers();
        return this;
    }
}