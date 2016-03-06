package net.kibotu.android.deviceinfo.ui.list.binder;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.common.android.utils.ui.recyclerView.DataBindAdapter;
import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.model.ListItem;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Nyaruhodo on 22.02.2016.
 */
public class CardViewHorizontalListItemBinderWithTitle extends HorizontalListItemBinder {

    public CardViewHorizontalListItemBinderWithTitle(@NotNull DataBindAdapter<ListItem> dataBindAdapter) {
        super(dataBindAdapter);
    }

    @Override
    public int getLayout() {
        return R.layout.cardview_list_item_with_title;
    }

    @NotNull
    protected ViewHolder newViewHolder(@NotNull ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(getLayout(), parent, false));
    }
}