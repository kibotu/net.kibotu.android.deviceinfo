package net.kibotu.android.deviceinfo.ui.list.binder;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import com.common.android.utils.logging.Logger;
import com.common.android.utils.ui.BaseViewHolder;
import com.common.android.utils.ui.recyclerView.DataBindAdapter;
import com.common.android.utils.ui.recyclerView.DataBinder;
import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.model.ListItem;

import static android.text.Html.fromHtml;
import static android.text.TextUtils.isEmpty;

/**
 * Created by Nyaruhodo on 21.02.2016.
 */
public class CardViewHorizontalListItemBinder extends DataBinder<ListItem, CardViewHorizontalListItemBinder.ViewHolder> {

    public CardViewHorizontalListItemBinder(@NonNull DataBindAdapter<ListItem> dataBindAdapter) {
        super(dataBindAdapter);
    }

    @Override
    public int getLayout() {
        return R.layout.cardview_horizontal;
    }

    @NonNull
    @Override
    protected ViewHolder createViewHolder(@LayoutRes final int layout, ViewGroup viewGroup) {
        return new ViewHolder(getLayout(), viewGroup);
    }

    @Override
    public void bindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final ListItem item = get(position);

        if (!isEmpty(item.getLabel()))
            viewHolder.label.setText(item.getLabel());

        if (!isEmpty(item.getValue()))
            viewHolder.value.setText(fromHtml(item.getValue()));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.toast(item.getDescription());
            }
        });
    }

    public static class ViewHolder extends BaseViewHolder {

        @NonNull
        @Bind(R.id.label)
        TextView label;

        @NonNull
        @Bind(R.id.value)
        TextView value;

        public ViewHolder(@LayoutRes int layout, @Nullable ViewGroup parent) {
            super(layout, parent);
        }
    }
}
