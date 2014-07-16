package net.kibotu.android.deviceinfo.fragments.menu;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import net.kibotu.android.deviceinfo.Device;
import net.kibotu.android.deviceinfo.MainActivity;
import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.Registry;

public class MenuFragment extends ListFragment {

    private MenuAdapter list;
    private Context context;
    private Registry currentItemList;

    public MenuFragment(Context context) {
        this.context = context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list, null);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(getFragmentListAdapter());
    }

    public MenuAdapter getFragmentListAdapter() {
        return (list == null) ? (list = new MenuAdapter(context)) : list;
    }

    public void addItem(final String name, int iconRId) {
        getFragmentListAdapter().add(new MenuItem(name, iconRId));
    }

    @Override
    public void onListItemClick(ListView lv, View v, int position, long id) {
        if(currentItemList != null)
            currentItemList.stopRefreshing();
        currentItemList = Registry.values()[position];
        currentItemList.startRefreshingList(1);

        ((FragmentActivity) Device.context()).getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, currentItemList.getFragmentList())
                .commit();

        super.onListItemClick(lv, v, position, id);
        MainActivity.menu.showContent();
        Device.context().setTitle(currentItemList.name());
    }
}
