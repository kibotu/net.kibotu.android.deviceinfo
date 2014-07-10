package net.kibotu.android.deviceinfo.fragments.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import net.kibotu.android.deviceinfo.R;

public class MenuAdapter extends ArrayAdapter<MenuItem> {

    public MenuAdapter(Context context) {
        super(context, 0);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, null);
        }
        ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
        icon.setImageResource(getItem(position).iconRes);
        TextView title = (TextView) convertView.findViewById(R.id.row_title);
        title.setText(getItem(position).tag);

        return convertView;
    }
}