package net.kibotu.android.deviceinfo.fragments.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import net.kibotu.android.deviceinfo.R;

public class DeviceInfoAdapter extends ArrayAdapter<DeviceInfoItem> {

    public DeviceInfoAdapter(Context context) {
        super(context, 0);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.deviceinfo, null);
        }

        TextView title = (TextView) convertView.findViewById(R.id.row_title);
        title.setText(getItem(position).tag);

        TextView value = (TextView) convertView.findViewById(R.id.row_value);
        value.setText(getItem(position).value);

        return convertView;
    }
}
