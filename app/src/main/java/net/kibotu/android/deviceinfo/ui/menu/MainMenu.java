package net.kibotu.android.deviceinfo.ui.menu;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.common.android.utils.extensions.ResourceExtensions;
import com.common.android.utils.ui.recyclerView.DataBindAdapter;
import net.kibotu.android.deviceinfo.R;

import java.util.List;

import static com.common.android.utils.ContextHelper.getContext;
import static com.common.android.utils.extensions.ResourceExtensions.drawable;
import static com.common.android.utils.extensions.ViewExtensions.getContentRoot;

/**
 * Created by Nyaruhodo on 20.02.2016.
 */
public class MainMenu implements IMainMenu {

    @NonNull
    @Bind(R.id.drawer_title)
    TextView leftDrawerTitle;

    @NonNull
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @NonNull
    @Bind(R.id.left_drawer)
    NavigationView leftDrawer;

    @NonNull
    @Bind(R.id.main_menu_list)
    RecyclerView list;

    @NonNull
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    ActionBarDrawerToggle drawerToggle;

    ActionbarViewHolder actionbarViewHolder;

    DataBindAdapter<MenuItem> adapter;

    public MainMenu() {
        ButterKnife.bind(this, getContentRoot());

        adapter = new DataBindAdapter<>();
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
        actionBar.setIcon(R.color.transparent);
        actionBar.setHomeButtonEnabled(false);

        final View viewActionBar = getContext().getLayoutInflater().inflate(R.layout.custom_toolbar, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        actionBar.setCustomView(viewActionBar, params);

        actionbarViewHolder = new ActionbarViewHolder(viewActionBar);

        actionbarViewHolder.homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLeftDrawer();
            }
        });
    }

    @Override
    public IMainMenu prepareDrawers() {
        if (drawerToggle != null)
            return this;

        drawerToggle = new ActionBarDrawerToggle(getContext(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };

        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                drawerToggle.setHomeAsUpIndicator(null);
                drawerToggle.setDrawerIndicatorEnabled(false);
                drawerLayout.addDrawerListener(drawerToggle);
                drawerToggle.syncState();
            }
        });

        return this;
    }

    @Override
    public IMainMenu setLeftDrawerTitle(@NonNull final String title) {
        this.leftDrawerTitle.setText(title);
        return this;
    }

    @Override
    public IMainMenu setTitle(@NonNull final String title) {
        getSupportActionBar().setTitle(title);
        actionbarViewHolder.title.setText("" + title);
        return this;
    }

    @NonNull
    protected AppCompatActivity getAppCompatActivity() {
        return (AppCompatActivity) getContext();
    }

    @NonNull
    protected android.support.v7.app.ActionBar getSupportActionBar() {
        return getAppCompatActivity().getSupportActionBar();
    }

    @Override
    public IMainMenu showActionBar(boolean isShowing) {
        if (isShowing)
            getSupportActionBar().show();
        else
            getSupportActionBar().hide();
        return this;
    }

    @Override
    public IMainMenu setHomeIcon(@DrawableRes int drawable) {
//        getSupportActionBar().setHomeAsUpIndicator(drawable(drawable));
        Glide.with(getContext()).load(drawable).fitCenter().into(actionbarViewHolder.homeIcon);
        return this;
    }

    @Override
    public IMainMenu setLeftDrawerLockMode(@LockMode int lockMode) {
        drawerLayout.setDrawerLockMode(lockMode, leftDrawer);
        return this;
    }

    public void openLeftDrawer() {
        drawerLayout.openDrawer(Gravity.LEFT);
    }

    @Override
    public IMainMenu setMenuItems(@NonNull final List<MenuItem> menu) {
        adapter.clear();
        for (MenuItem item : menu)
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

    @Override
    public IMainMenu closeDrawers() {
        drawerLayout.closeDrawers();
        return this;
    }
}
