package net.kibotu.android.deviceinfo.fragments.list;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import net.kibotu.android.deviceinfo.MainActivity;
import net.kibotu.android.deviceinfo.R;

public class DeviceInfoFragment extends ListFragment {

    public DeviceInfoAdapter list;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list, null);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(getFragmentListAdapter());
    }

    public DeviceInfoAdapter getFragmentListAdapter() {
        return (list == null) ? (list = new DeviceInfoAdapter(MainActivity.context)) : list;
    }

    public void addItem(String tag, String description, String value) {
        getFragmentListAdapter().add(new DeviceInfoItem(tag, description, value));
    }
}
