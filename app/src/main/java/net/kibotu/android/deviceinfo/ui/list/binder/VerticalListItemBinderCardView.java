package net.kibotu.android.deviceinfo.ui.list.binder;

import androidx.annotation.NonNull;

import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.model.ListItem;
import net.kibotu.android.recyclerviewpresenter.PresenterAdapter;

/**
 * Created by Nyaruhodo on 21.02.2016.
 */
public class VerticalListItemBinderCardView extends CardViewHorizontalListItemBinder {

    public VerticalListItemBinderCardView(@NonNull PresenterAdapter<ListItem> dataBindAdapter) {
        super(dataBindAdapter);
    }

    @Override
    public int getLayout() {
        return R.layout.cardview_vertical;
    }
}
