package net.kibotu.android.deviceinfo.fragments.list.vertical;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import net.kibotu.android.deviceinfo.R;

public class DeviceInfoAdapter extends ArrayAdapter<DeviceInfoItem> {

    public DeviceInfoAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final DeviceInfoItem item = getItem(position);
        if (convertView == null)
            convertView = item.customView != null ? item.customView : LayoutInflater.from(getContext()).inflate(item.viewId, null);

        final TextView tag = (TextView) convertView.findViewById(R.id.row_title);
        if(tag != null) {
            tag.setTextAppearance(getContext(), item.textAppearance);
            tag.setText(Html.fromHtml(item.tag));
        }

        final TextView rowValue = (TextView) convertView.findViewById(R.id.row_value);
        if(rowValue != null) {
            rowValue.setText(item.value);
        }

        return convertView;
    }
}