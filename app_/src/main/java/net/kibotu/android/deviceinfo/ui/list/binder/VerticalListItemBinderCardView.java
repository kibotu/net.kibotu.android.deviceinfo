package net.kibotu.android.deviceinfo.ui.list.binder;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.common.android.utils.ui.recyclerView.DataBindAdapter;
import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.model.ListItem;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Nyaruhodo on 21.02.2016.
 */
public class VerticalListItemBinderCardView extends CardViewHorizontalListItemBinder {

    public VerticalListItemBinderCardView(@NotNull DataBindAdapter<ListItem> dataBindAdapter) {
        super(dataBindAdapter);
    }

    @Override
    public int getLayout() {
        return R.layout.cardview_vertical;
    }

    @NotNull
    protected ViewHolder newViewHolder(@NotNull ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(this.getLayout(), parent, false));
    }
}
