package net.kibotu.android.deviceinfo.ui.list.binder;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
 * Created by Nyaruhodo on 21.02.2016.
 */
public class CardViewHorizontalListItemBinder extends Presenter<ListItem, CardViewHorizontalListItemBinder.ViewHolder> {

    public CardViewHorizontalListItemBinder(@NonNull PresenterAdapter<ListItem> dataBindAdapter) {
        super(dataBindAdapter);
    }

    @Override
    public int getLayout() {
        return R.layout.cardview_horizontal;
    }

    @NonNull
    @Override
    protected ViewHolder createViewHolder(@LayoutRes final int layout, @NonNull ViewGroup viewGroup) {
        return new ViewHolder(getLayout(), viewGroup);
    }

    @Override
    public void bindViewHolder(@NonNull ViewHolder viewHolder, @NonNull ListItem item, int position) {

        if (!isEmpty(item.getLabel()))
            viewHolder.label.setText(item.getLabel());

        if (!isEmpty(item.getValue()))
            viewHolder.value.setText(fromHtml(item.getValue()));

        Logger.v(tag(), "label: " + viewHolder.label.getText() + " value: " + viewHolder.value.getText());

        viewHolder.itemView.setOnClickListener(v -> Logger.toast(item.getDescription()));
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
