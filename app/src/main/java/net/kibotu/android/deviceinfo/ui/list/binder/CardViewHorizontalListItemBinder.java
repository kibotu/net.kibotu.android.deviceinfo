package net.kibotu.android.deviceinfo.ui.list.binder;

import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.model.ListItem;
import net.kibotu.android.recyclerviewpresenter.Presenter;
import net.kibotu.android.recyclerviewpresenter.RecyclerViewHolder;
import net.kibotu.logger.Logger;
import org.jetbrains.annotations.NotNull;

import static android.text.Html.fromHtml;
import static android.text.TextUtils.isEmpty;

/**
 * Created by Nyaruhodo on 21.02.2016.
 */
public class CardViewHorizontalListItemBinder extends Presenter<ListItem> {

    @Override
    public int getLayout() {
        return R.layout.cardview_horizontal;
    }

    @Override
    public void bindViewHolder(@NotNull RecyclerView.ViewHolder viewHolder, @NotNull ListItem item, int i) {
//        if (!isEmpty(item.getLabel()))
//            viewHolder.label.setText(item.getLabel());
//
//        if (!isEmpty(item.getValue()))
//            viewHolder.value.setText(fromHtml(item.getValue()));
//
//        Logger.v("label: " + viewHolder.label.getText() + " value: " + viewHolder.value.getText());
//
//        viewHolder.itemView.setOnClickListener(v -> Logger.toast(item.getDescription()));
    }

    public static class ViewHolder extends RecyclerViewHolder {

        @NonNull
        @BindView(R.id.label)
        TextView label;

        @NonNull
        @BindView(R.id.value)
        TextView value;

        public ViewHolder(@LayoutRes int layout, @Nullable ViewGroup parent) {
            super(parent, layout);
        }
    }
}
