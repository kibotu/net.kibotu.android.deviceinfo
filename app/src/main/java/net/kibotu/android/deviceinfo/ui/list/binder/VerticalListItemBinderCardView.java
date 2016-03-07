package net.kibotu.android.deviceinfo.ui.list.binder;

import android.support.annotation.NonNull;
import com.common.android.utils.ui.recyclerView.DataBindAdapter;
import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.model.ListItem;

/**
 * Created by Nyaruhodo on 21.02.2016.
 */
public class VerticalListItemBinderCardView extends CardViewHorizontalListItemBinder {

    public VerticalListItemBinderCardView(@NonNull DataBindAdapter<ListItem> dataBindAdapter) {
        super(dataBindAdapter);
    }

    @Override
    public int getLayout() {
        return R.layout.cardview_vertical;
    }
}
