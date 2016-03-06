package net.kibotu.android.deviceinfo.ui.menu;


import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.ui.ViewHolder;

public class ActionbarViewHolder extends ViewHolder {

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