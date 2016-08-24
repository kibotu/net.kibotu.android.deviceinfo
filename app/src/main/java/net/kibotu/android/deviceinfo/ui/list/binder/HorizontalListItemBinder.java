package net.kibotu.android.deviceinfo.ui.list.binder;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.android.utils.logging.Logger;
import com.common.android.utils.ui.BaseViewHolder;
import com.common.android.utils.ui.recyclerView.DataBindAdapter;
import com.common.android.utils.ui.recyclerView.DataBinder;

import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.model.ListItem;

import butterknife.Bind;

import static android.text.Html.fromHtml;
import static android.text.TextUtils.isEmpty;

/**
 * Created by Nyaruhodo on 06.03.2016.
 */
public class HorizontalListItemBinder extends DataBinder<ListItem, HorizontalListItemBinder.ViewHolder> {

    public HorizontalListItemBinder(@NonNull DataBindAdapter<ListItem> dataBindAdapter) {
        super(dataBindAdapter);
    }

    @NonNull
    @Override
    protected ViewHolder createViewHolder(int i, ViewGroup viewGroup) {
        return new ViewHolder(i, viewGroup);
    }

    @Override
    public int getLayout() {
        return R.layout.item_horizontal;
    }


    @Override
    public void bindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final ListItem item = get(position);

        if (!isEmpty(item.getLabel())) {
            viewHolder.label.setVisibility(View.VISIBLE);
            viewHolder.label.setText(fromHtml(item.getLabel()));
        } else {
            viewHolder.label.setVisibility(View.GONE);
        }

        if (!isEmpty(item.getValue())) {
            viewHolder.value.setVisibility(View.VISIBLE);
            viewHolder.value.setText(fromHtml(item.getValue()));
        } else {
            viewHolder.value.setVisibility(View.GONE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.toast(item.getDescription());
            }
        });

        Logger.v(tag(), "label: " +  viewHolder.label.getText() + " value: " +  viewHolder.value.getText());

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
