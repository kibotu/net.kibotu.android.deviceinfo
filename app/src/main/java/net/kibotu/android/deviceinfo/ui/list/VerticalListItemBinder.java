package net.kibotu.android.deviceinfo.ui.list;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.common.android.utils.ui.recyclerView.DataBindAdapter;
import net.kibotu.android.deviceinfo.R;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Nyaruhodo on 21.02.2016.
 */
public class VerticalListItemBinder extends HorizontalListItemBinder {

    public VerticalListItemBinder(@NotNull DataBindAdapter<ListItem> dataBindAdapter) {
        super(dataBindAdapter);
    }

    @NotNull
    protected ViewHolder newViewHolder(@NotNull ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(this.getLayout(), parent, false));
    }

    @Override
    public int getLayout() {
        return R.layout.list_item_vertical;
    }
}
