package net.kibotu.android.deviceinfo.ui.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.common.android.utils.logging.Logger;
import com.common.android.utils.ui.recyclerView.DataBindAdapter;
import com.common.android.utils.ui.recyclerView.DataBinder;
import net.kibotu.android.deviceinfo.R;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Nyaruhodo on 21.02.2016.
 */
public class HorizontalListItemBinder extends DataBinder<ListItem, HorizontalListItemBinder.ViewHolder> {

    public HorizontalListItemBinder(@NotNull DataBindAdapter<ListItem> dataBindAdapter) {
        super(dataBindAdapter);
    }

    @Override
    public int getLayout() {
        return R.layout.list_item_horizontal;
    }

    @Override
    public void bindViewHolder(@NotNull ViewHolder viewHolder, int position) {
        final ListItem item = get(position);

        viewHolder.label.setText(item.getLabel());
        viewHolder.value.setText(item.getValue());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.toast(item.getDescription());
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        @Bind(R.id.label)
        TextView label;

        @NonNull
        @Bind(R.id.value)
        TextView value;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
