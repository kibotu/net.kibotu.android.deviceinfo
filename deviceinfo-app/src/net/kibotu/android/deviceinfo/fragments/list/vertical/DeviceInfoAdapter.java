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
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.deviceinfo, null);

        final DeviceInfoItem item = getItem(position);

        ((TextView) convertView.findViewById(R.id.row_title)).setText(Html.fromHtml("<b>" + item.tag + "</b>"));

        if (item.customView != null) {
            if (item.customView.getParent() != null)
                ((ViewGroup) item.customView.getParent()).removeView(item.customView);
            ((LinearLayout) convertView).addView(item.customView);
            convertView.findViewById(R.id.row_value).setVisibility(View.GONE);
        } else {
//            ((TextView) convertView.findViewById(R.id.row_value)).setText(Html.fromHtml(item.value));
            ((TextView) convertView.findViewById(R.id.row_value)).setText(item.value);
        }

        return convertView;
    }
}