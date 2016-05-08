package net.kibotu.android.deviceinfo.ui.list.binder;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.android.utils.logging.Logger;
import com.common.android.utils.ui.BaseViewHolder;
import com.common.android.utils.ui.recyclerView.DataBindAdapter;
import com.common.android.utils.ui.recyclerView.DataBinder;

import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.model.ListItem;

import butterknife.Bind;

import static com.common.android.utils.ContextHelper.getContext;

/**
 * Created by Nyaruhodo on 05.03.2016.
 */
public class CardViewSubListItemBinder extends DataBinder<ListItem, CardViewSubListItemBinder.ViewHolder> {

    public CardViewSubListItemBinder(@NonNull DataBindAdapter<ListItem> dataBindAdapter) {
        super(dataBindAdapter);
    }

    @NonNull
    @Override
    protected ViewHolder createViewHolder(int i, ViewGroup viewGroup) {
        return new ViewHolder(i, viewGroup);
    }

    @Override
    public int getLayout() {
        return R.layout.sub_list_item;
    }


    @Override
    public void bindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        final ListItem item = get(position);

        viewHolder.label.setText(item.getLabel());

        viewHolder.adapter.clear();

        if (item.hasChildren())
            for (ListItem child : item.getChildren())
                viewHolder.adapter.add(child, HorizontalListItemBinder.class);

        viewHolder.adapter.notifyDataSetChanged();

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.toast(item.getDescription());

                if (dataBindAdapter.getOnItemClickListener() != null)
                    dataBindAdapter.getOnItemClickListener().onItemClick(item, viewHolder.itemView, position);
            }
        });
    }

    public static class ViewHolder extends BaseViewHolder {

        @NonNull
        @Bind(R.id.label)
        TextView label;

        @NonNull
        @Bind(R.id.list)
        RecyclerView list;

        final DataBindAdapter<Object> adapter;

        public ViewHolder(@LayoutRes int layout, @Nullable ViewGroup parent) {
            super(layout, parent);

            adapter = new DataBindAdapter<>();
            list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            list.setAdapter(adapter);
        }
    }
}
