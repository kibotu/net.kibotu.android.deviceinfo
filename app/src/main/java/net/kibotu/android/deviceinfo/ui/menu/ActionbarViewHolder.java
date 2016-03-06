package net.kibotu.android.deviceinfo.ui.menu;


import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.ui.ViewHolder;

import static com.common.android.utils.ContextHelper.getContext;

public class ActionbarViewHolder extends ViewHolder {

    @NonNull
    @Bind(R.id.icon_layout)
    View iconHitBox;

    @NonNull
    @Bind(R.id.home_icon)
    ImageView homeIcon;

    @NonNull
    @Bind(R.id.actionbar_title)
    TextView title;

    public ActionbarViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}