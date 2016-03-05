package net.kibotu.android.deviceinfo.ui.list;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.Bind;
import com.common.android.utils.ui.recyclerView.DataBindAdapter;
import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.ui.BaseFragment;

import static android.text.TextUtils.isEmpty;

/**
 * Created by Nyaruhodo on 20.02.2016.
 */
public abstract class ListFragment extends BaseFragment {

    @NonNull
    @Bind(R.id.list)
    RecyclerView list;
    private DataBindAdapter<ListItem> adapter;

    @Override
    public int getLayout() {
        return R.layout.fragment_list;
    }

    @CallSuper
    @Override
    protected void onViewCreated() {

        adapter = new DataBindAdapter<>();
        list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        list.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    protected void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

    protected void clear() {
        adapter.clear();
    }

    protected void addListItemHorizontally(String label, Object value, String description) {
        final String content = String.valueOf(value);

        if (isEmpty(content))
            return;

        ((DataBindAdapter<ListItem>) list.getAdapter()).add(new ListItem()
                        .setLabel(label)
                        .setValue(content)
                        .setDescription(description),
                HorizontalListItemBinder.class);
    }

    protected void addListItemVertically(String label, Object value, String description) {
        final String content = String.valueOf(value);

        if (isEmpty(content))
            return;

        ((DataBindAdapter<ListItem>) list.getAdapter()).add(new ListItem()
                        .setLabel(label)
                        .setValue(String.valueOf(value))
                        .setDescription(description),
                VerticalListItemBinder.class);
    }

    public void addListItemWithTitle(String label, String keys, String value, String description) {
        final String content = String.valueOf(value);

        if (isEmpty(content))
            return;

        ((DataBindAdapter<ListItem>) list.getAdapter()).add(new ListItem()
                        .setLabel(label)
                        .setKey(String.valueOf(keys))
                        .setValue(String.valueOf(value))
                        .setDescription(description),
                HorizontalListItemBinderWithTitle.class);
    }
}
