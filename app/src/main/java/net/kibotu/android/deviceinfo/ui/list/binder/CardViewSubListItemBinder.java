package net.kibotu.android.deviceinfo.ui.list.binder;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.common.android.utils.ui.recyclerView.DataBindAdapter;
import com.common.android.utils.ui.recyclerView.DataBinder;
import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.model.ListItem;
import org.jetbrains.annotations.NotNull;

import static com.common.android.utils.ContextHelper.getContext;

/**
 * Created by Nyaruhodo on 05.03.2016.
 */
public class CardViewSubListItemBinder extends DataBinder<ListItem, CardViewSubListItemBinder.ViewHolder> {

    public CardViewSubListItemBinder(@NotNull DataBindAdapter<ListItem> dataBindAdapter) {
        super(dataBindAdapter);
    }

    @Override
    public int getLayout() {
        return R.layout.sub_list_item;
    }

    @NotNull
    protected ViewHolder newViewHolder(@NotNull ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(getLayout(), parent, false));
    }

    @Override
    public void bindViewHolder(@NotNull ViewHolder viewHolder, int position) {
        final ListItem item = get(position);

        viewHolder.label.setText(item.getLabel());

        viewHolder.adapter.clear();

        for (ListItem child : item.getChildren())
            viewHolder.adapter.add(child, HorizontalListItemBinder.class);

        viewHolder.adapter.notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        @Bind(R.id.label)
        TextView label;

        @NonNull
        @Bind(R.id.list)
        RecyclerView list;

        final DataBindAdapter<Object> adapter;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            adapter = new DataBindAdapter<>();
            list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            list.setAdapter(adapter);
        }
    }
}
