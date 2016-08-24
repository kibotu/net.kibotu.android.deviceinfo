package net.kibotu.android.deviceinfo.ui.list.binder;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.android.utils.logging.Logger;

import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.model.ListItem;
import net.kibotu.android.recyclerviewpresenter.BaseViewHolder;
import net.kibotu.android.recyclerviewpresenter.Presenter;
import net.kibotu.android.recyclerviewpresenter.PresenterAdapter;

import butterknife.BindView;

import static android.text.Html.fromHtml;
import static android.text.TextUtils.isEmpty;

/**
 * Created by Nyaruhodo on 06.03.2016.
 */
public class HorizontalListItemBinder extends Presenter<ListItem, HorizontalListItemBinder.ViewHolder> {

    public HorizontalListItemBinder(@NonNull PresenterAdapter<ListItem> dataBindAdapter) {
        super(dataBindAdapter);
    }

    @Override
    public int getLayout() {
        return R.layout.item_horizontal;
    }

    @NonNull
    @Override
    protected ViewHolder createViewHolder(@LayoutRes int layout, @NonNull ViewGroup parent) {
        return new ViewHolder(layout, parent);
    }

    @Override
    public void bindViewHolder(@NonNull ViewHolder viewHolder, @NonNull ListItem item, int position) {

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

        viewHolder.itemView.setOnClickListener(v -> Logger.toast(item.getDescription()));

        Logger.v(tag(), "label: " + viewHolder.label.getText() + " value: " + viewHolder.value.getText());
    }

    public static class ViewHolder extends BaseViewHolder {

        @NonNull
        @BindView(R.id.label)
        TextView label;

        @NonNull
        @BindView(R.id.value)
        TextView value;

        public ViewHolder(@LayoutRes int layout, @Nullable ViewGroup parent) {
            super(layout, parent);
        }
    }
}
