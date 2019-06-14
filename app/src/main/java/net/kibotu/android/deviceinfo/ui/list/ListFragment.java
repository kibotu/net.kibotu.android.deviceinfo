package net.kibotu.android.deviceinfo.ui.list;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.common.android.utils.interfaces.TitleProvider;

import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.model.ListItem;
import net.kibotu.android.deviceinfo.ui.BaseFragment;
import net.kibotu.android.deviceinfo.ui.list.binder.CardViewHorizontalListItemBinder;
import net.kibotu.android.deviceinfo.ui.list.binder.CardViewSubListItemBinder;
import net.kibotu.android.deviceinfo.ui.list.binder.VerticalListItemBinderCardView;
import net.kibotu.android.deviceinfo.ui.menu.Menu;
import net.kibotu.android.recyclerviewpresenter.PresenterAdapter;

import butterknife.BindView;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

import static android.text.TextUtils.isEmpty;

/**
 * Created by Nyaruhodo on 20.02.2016.
 */
public abstract class ListFragment extends BaseFragment implements TitleProvider {

    @NonNull
    @BindView(R.id.list)
    RecyclerView list;
    protected PresenterAdapter<ListItem> adapter;

    @Override
    public int getLayout() {
        return R.layout.fragment_list;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Menu.setActionBarIcon(getHomeIcon());

        list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        setListItemAnimator();

        adapter = new PresenterAdapter<>();
        list.setAdapter(injectAdapterAnimation(adapter));
    }

    protected RecyclerView.Adapter injectAdapterAnimation(RecyclerView.Adapter adapter) {
        final ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(adapter, 0.90f);
        animationAdapter.setDuration(200);
        animationAdapter.setFirstOnly(false);
        animationAdapter.setInterpolator(new OvershootInterpolator(1f));
        return animationAdapter;
    }

    private void setListItemAnimator() {
        OverScrollDecoratorHelper.setUpOverScroll(list, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        list.setItemAnimator(new LandingAnimator(new OvershootInterpolator(1f)));
        list.getItemAnimator().setAddDuration(125);
        list.getItemAnimator().setRemoveDuration(125);
        list.getItemAnimator().setMoveDuration(125);
        list.getItemAnimator().setChangeDuration(125);
    }

    protected void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

    protected void clear() {
        adapter.clear();
    }

    protected void addHorizontallyCard(String label, Object value, String description) {
        final String content = String.valueOf(value);

        if (isEmpty(content))
            return;

        adapter.add(new ListItem()
                        .setLabel(label)
                        .setValue(content)
                        .setDescription(description),
                CardViewHorizontalListItemBinder.class);
    }

    protected void addVerticallyCard(String label, Object value, String description) {
        final String content = String.valueOf(value);

        if (isEmpty(content))
            return;

        adapter.add(new ListItem()
                        .setLabel(label)
                        .setValue(String.valueOf(value))
                        .setDescription(description),
                VerticalListItemBinderCardView.class);
    }

    public void addSubListItem(ListItem item) {
        if (item == null)
            return;

        adapter.add(item, CardViewSubListItemBinder.class);
    }

    protected abstract int getHomeIcon();

    @Override
    protected void onActiveAfterBackStackChanged() {
        super.onActiveAfterBackStackChanged();

        Menu.setActionBarIcon(getHomeIcon());
        Menu.setActionBarTitle(getTitle());
    }
}
