package net.kibotu.android.deviceinfo.fragments.menu;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import net.kibotu.android.deviceinfo.MainActivity;
import net.kibotu.android.deviceinfo.R;

public class MenuFragment extends ListFragment {

    public MenuAdapter list;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list, null);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(getFragmentListAdapter());
    }

    public MenuAdapter getFragmentListAdapter() {
        return (list == null) ? (list = new MenuAdapter(MainActivity.context)) : list;
    }

    public void addItem(final String name, int iconRId) {
        getFragmentListAdapter().add(new MenuItem(name, iconRId));
    }
}
