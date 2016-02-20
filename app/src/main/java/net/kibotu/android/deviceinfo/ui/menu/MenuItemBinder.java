package net.kibotu.android.deviceinfo.ui.menu;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.common.android.utils.ui.recyclerView.DataBindAdapter;
import com.common.android.utils.ui.recyclerView.DataBinder;
import net.kibotu.android.deviceinfo.R;
import org.jetbrains.annotations.NotNull;

import static com.common.android.utils.ContextHelper.getContext;

/**
 * Created by Nyaruhodo on 20.02.2016.
 */
public class MenuItemBinder extends DataBinder<MenuItem, MenuItemBinder.ViewHolder> {

    public MenuItemBinder(@NotNull DataBindAdapter<MenuItem> dataBindAdapter) {
        super(dataBindAdapter);
    }

    @Override
    public void bindViewHolder(@NotNull final ViewHolder viewHolder, final int position) {
        final MenuItem item = get(position);

        Glide.with(getContext())
                .load(item.icon)
                .into(viewHolder.icon);

        viewHolder.label.setText(getContext().getString(item.name));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataBindAdapter.getOnItemClickListener() != null)
                    dataBindAdapter.getOnItemClickListener().onItemClick(item, viewHolder.itemView, position);
            }
        });
    }

    @Override
    public int getLayout() {
        return R.layout.menu_item;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        @Bind(R.id.icon)
        ImageView icon;

        @NonNull
        @Bind(R.id.label)
        TextView label;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
