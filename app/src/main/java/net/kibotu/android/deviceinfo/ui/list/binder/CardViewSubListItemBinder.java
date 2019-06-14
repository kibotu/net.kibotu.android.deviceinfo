package net.kibotu.android.deviceinfo.ui.list.binder;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.android.utils.logging.Logger;

import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.model.ListItem;
import net.kibotu.android.recyclerviewpresenter.BaseViewHolder;
import net.kibotu.android.recyclerviewpresenter.Presenter;
import net.kibotu.android.recyclerviewpresenter.PresenterAdapter;

import butterknife.BindView;

import static com.common.android.utils.ContextHelper.getContext;

/**
 * Created by Nyaruhodo on 05.03.2016.
 */
public class CardViewSubListItemBinder extends Presenter<ListItem, CardViewSubListItemBinder.ViewHolder> {

    public CardViewSubListItemBinder(@NonNull PresenterAdapter<ListItem> dataBindAdapter) {
        super(dataBindAdapter);
    }

    @Override
    public int getLayout() {
        return R.layout.sub_list_item;
    }

    @NonNull
    @Override
    protected ViewHolder createViewHolder(int layout, @NonNull ViewGroup viewGroup) {
        return new ViewHolder(layout, viewGroup);
    }

    @Override
    public void bindViewHolder(@NonNull ViewHolder viewHolder, @NonNull ListItem item, int position) {

        viewHolder.label.setText(item.getLabel());

        viewHolder.adapter.clear();

        if (item.hasChildren())
            for (ListItem child : item.getChildren())
                viewHolder.adapter.add(child, HorizontalListItemBinder.class);

        viewHolder.adapter.notifyDataSetChanged();

        viewHolder.itemView.setOnClickListener(v -> {
            Logger.toast(item.getDescription());

            if (presenterAdapter.getOnItemClickListener() != null)
                presenterAdapter.getOnItemClickListener().onItemClick(item, viewHolder.itemView, position);
        });

        Logger.v(tag(), "label: " + viewHolder.label.getText());
    }

    public static class ViewHolder extends BaseViewHolder {

        @NonNull
        @BindView(R.id.label)
        TextView label;

        @NonNull
        @BindView(R.id.list)
        RecyclerView list;

        final PresenterAdapter<Object> adapter;

        public ViewHolder(@LayoutRes int layout, ViewGroup parent) {
            super(layout, parent);

            adapter = new PresenterAdapter<>();
            list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            list.setAdapter(adapter);
        }
    }
}
