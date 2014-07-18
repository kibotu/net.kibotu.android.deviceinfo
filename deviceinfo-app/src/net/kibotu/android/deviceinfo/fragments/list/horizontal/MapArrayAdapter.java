package net.kibotu.android.deviceinfo.fragments.list.horizontal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import net.kibotu.android.deviceinfo.R;

public class MapArrayAdapter extends ArrayAdapter<MapItem> {

    public MapArrayAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.deviceinfo, null);

        final MapItem item = getItem(position);

        return convertView;
    }
}
