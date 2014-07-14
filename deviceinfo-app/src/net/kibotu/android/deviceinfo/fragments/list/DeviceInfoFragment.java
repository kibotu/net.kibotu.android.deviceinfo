package net.kibotu.android.deviceinfo.fragments.list;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import net.kibotu.android.deviceinfo.Logger;
import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.Registry;

public class DeviceInfoFragment extends ListFragment {

    public DeviceInfoAdapter list;
    private Context context;

    public DeviceInfoFragment(Context context) {
        this.context = context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list, null);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(getFragmentListAdapter());
    }

    public DeviceInfoAdapter getFragmentListAdapter() {
        return (list == null) ? (list = new DeviceInfoAdapter(context)) : list;
    }

    public void addItem(String tag, String description, String value) {
        getFragmentListAdapter().add(new DeviceInfoItem(tag, description, value));
    }

    @Override
    public void onListItemClick(ListView lv, View v, int position, long id) {
        Logger.v(Registry.values()[position].name());
        super.onListItemClick(lv, v, position, id);
    }
}
