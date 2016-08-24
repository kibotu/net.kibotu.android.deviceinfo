package net.kibotu.android.deviceinfo.ui.list.binder;

import android.support.annotation.NonNull;

import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.model.ListItem;
import net.kibotu.android.recyclerviewpresenter.PresenterAdapter;

/**
 * Created by Nyaruhodo on 22.02.2016.
 */
public class CardViewHorizontalListItemBinderWithTitle extends HorizontalListItemBinder {

    public CardViewHorizontalListItemBinderWithTitle(@NonNull PresenterAdapter<ListItem> dataBindAdapter) {
        super(dataBindAdapter);
    }

    @Override
    public int getLayout() {
        return R.layout.cardview_list_item_with_title;
    }
}