package net.kibotu.android.deviceinfo.fragments.list.vertical;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import net.kibotu.android.deviceinfo.R;

public class DeviceInfoAdapter extends ArrayAdapter<DeviceInfoItem> {

    public DeviceInfoAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public synchronized View getView(int position, View convertView, ViewGroup parent) {

        final DeviceInfoItem item = getItem(position);
        convertView = item.getView();

        final TextView tag = (TextView) convertView.findViewById(R.id.row_title);
        if (tag != null) {
            tag.setTextAppearance(getContext(), item.textAppearance);
            tag.setText(Html.fromHtml(item.tag));
        }

        final TextView keys = (TextView) convertView.findViewById(R.id.key);
        if (keys != null) {
//            keys.setText(item.keys != null ? item.keys : item.tag);
            final String v = item.keys != null ? item.keys : item.tag;
            keys.setText(item.useHtml ? Html.fromHtml(v) : v);
        }

        final TextView value = (TextView) convertView.findViewById(R.id.value);
        if (value != null) {
            value.setText(item.useHtml ? Html.fromHtml(item.value) : item.value);
        }

        return convertView;
    }
}