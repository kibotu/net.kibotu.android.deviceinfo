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

import static android.text.Html.fromHtml;

/**
 * Created by Nyaruhodo on 22.02.2016.
 */
public class HorizontalListItemBinderWithTitle extends DataBinder<ListItem, HorizontalListItemBinderWithTitle.ViewHolder> {

    public HorizontalListItemBinderWithTitle(@NotNull DataBindAdapter<ListItem> dataBindAdapter) {
        super(dataBindAdapter);
    }

    @Override
    public void bindViewHolder(@NotNull ViewHolder viewHolder, int position) {
        final ListItem item = get(position);
        viewHolder.label.setText(fromHtml(item.getLabel()));
        viewHolder.key.setText(fromHtml(item.getKey()));
        viewHolder.value.setText(fromHtml(item.getValue()));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.toast(item.getDescription());
            }
        });
    }

    @Override
    public int getLayout() {
        return R.layout.list_item_with_title;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        @Bind(R.id.label)
        TextView label;

        @NonNull
        @Bind(R.id.key)
        TextView key;

        @NonNull
        @Bind(R.id.value)
        TextView value;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
