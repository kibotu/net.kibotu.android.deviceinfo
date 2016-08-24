package net.kibotu.android.deviceinfo.ui.menu;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.recyclerviewpresenter.BaseViewHolder;
import net.kibotu.android.recyclerviewpresenter.Presenter;
import net.kibotu.android.recyclerviewpresenter.PresenterAdapter;

import static com.common.android.utils.ContextHelper.getContext;
import static com.common.android.utils.ContextHelper.getFragmentActivity;

/**
 * Created by Nyaruhodo on 20.02.2016.
 */
public class MenuItemBinder extends Presenter<MenuItem, MenuItemBinder.ViewHolder> {

    public MenuItemBinder(@NonNull final PresenterAdapter<MenuItem> dataBindAdapter) {
        super(dataBindAdapter);
    }

    @Override
    public int getLayout() {
        return R.layout.menu_item;
    }

    @NonNull
    @Override
    protected ViewHolder createViewHolder(final int layout, final ViewGroup parent) {
        return new ViewHolder(layout, parent);
    }

    @Override
    public void bindViewHolder(@NonNull ViewHolder viewHolder, @NonNull MenuItem item, int position) {

        Glide.with(getContext())
                .load(item.icon)
                .into(viewHolder.icon);

        viewHolder.label.setText(getContext().getString(item.name));

        viewHolder.itemView.setOnClickListener(v -> {
            final Fragment fragment = item.getFragment();
            getFragmentActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(fragment.getTag())
                    .commit();
            MainMenuProvider.provide().closeDrawers();
        });
    }

    public static class ViewHolder extends BaseViewHolder {

        @NonNull
        ImageView icon;

        @NonNull
        TextView label;

        public ViewHolder(@LayoutRes final int layout, @Nullable final ViewGroup parent) {
            super(layout, parent);

            icon = (ImageView) itemView.findViewById(R.id.icon);
            label = (TextView) itemView.findViewById(R.id.label);
        }
    }
}