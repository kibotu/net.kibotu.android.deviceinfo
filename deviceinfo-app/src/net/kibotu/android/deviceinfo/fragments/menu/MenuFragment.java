package net.kibotu.android.deviceinfo.fragments.menu;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import net.kibotu.android.deviceinfo.Device;
import net.kibotu.android.deviceinfo.MainActivity;
import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.Registry;

public class MenuFragment extends ListFragment {

    private volatile MenuAdapter list;
    private volatile Context context;
    public volatile Registry lastItemList;

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
    public void onListItemClick(final ListView lv, final View v, final int position, final long id) {
        super.onListItemClick(lv, v, position, id);

        if (lastItemList != null) lastItemList.stopRefreshing();
        final Registry currentItemList = Registry.values()[position];
        currentItemList.startRefreshingList(1);
//        currentItemList.resumeThreads();

        if (lastItemList != currentItemList)
            ((FragmentActivity) Device.context()).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, currentItemList.getFragmentList())
                    .commit();

        lastItemList = currentItemList;
        changeActionBar();
    }

    public void changeActionBar() {
        Device.context().setTitle(lastItemList.name());
        ((SlidingFragmentActivity) context).getSupportActionBar().setIcon(lastItemList.iconR_i);
        ((MainActivity)context).getSlidingMenu().showContent();
    }
}