package net.kibotu.android.deviceinfo.ui;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;

import static com.common.android.utils.ContextHelper.getContext;

/**
 * Created by jan.rabe on 02/02/16.
 */
public abstract class ViewHolder {

    @NonNull
    public final View itemView;

    public ViewHolder(@NonNull final View itemView) {
        this.itemView = itemView;
        ButterKnife.bind(this, itemView);
    }

    public ViewHolder(@NonNull final ViewGroup parent, @LayoutRes final int layout) {
        itemView = LayoutInflater.from(getContext()).inflate(layout, parent, false);
        ButterKnife.bind(this, itemView);
    }
}