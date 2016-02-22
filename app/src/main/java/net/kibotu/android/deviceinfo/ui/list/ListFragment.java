package net.kibotu.android.deviceinfo.ui.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.Bind;
import com.common.android.utils.ui.recyclerView.DataBindAdapter;
import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.ui.BaseFragment;

/**
 * Created by Nyaruhodo on 20.02.2016.
 */
public abstract class ListFragment extends BaseFragment {

    @NonNull
    @Bind(R.id.list)
    RecyclerView list;

    @Override
    public int getLayout() {
        return R.layout.fragment_list;
    }


    @Override
    protected void onViewCreated() {

        final DataBindAdapter<ListItem> adapter = new DataBindAdapter<>();
        list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        list.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    protected void addListItemHorizontally(String label, Object value, String description) {
        ((DataBindAdapter<ListItem>) list.getAdapter()).add(new ListItem()
                        .setLabel(label)
                        .setValue(String.valueOf(value))
                        .setDescription(description),
                HorizontalListItemBinder.class);
    }

    protected void addListItemVertically(String label, Object value, String description) {
        ((DataBindAdapter<ListItem>) list.getAdapter()).add(new ListItem()
                        .setLabel(label)
                        .setValue(String.valueOf(value))
                        .setDescription(description),
                VerticalListItemBinder.class);
    }
}
