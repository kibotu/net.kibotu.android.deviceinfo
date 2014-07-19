package net.kibotu.android.deviceinfo.fragments.list.vertical;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import net.kibotu.android.deviceinfo.Device;
import net.kibotu.android.deviceinfo.Logger;
import net.kibotu.android.deviceinfo.R;

import java.util.Comparator;

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

    public DeviceInfoItem addItem(String tag, String description, String value, int order) {
        if (value == null || value == "") return null;
        DeviceInfoItem item = new DeviceInfoItem(tag, description, value, order);
        getFragmentListAdapter().add(item);
        return item;
    }

    public DeviceInfoItem addItem(String tag, String description, String value) {
        if (value == null || value == "") return null;
        DeviceInfoItem item = new DeviceInfoItem(tag, description, value);
        getFragmentListAdapter().add(item);
        return item;
    }

    /**
     * @param delay       - Delay in seconds.
     */
    public Thread addItem(final String tag, final String description, final float delay, final boolean loop, final DeviceInfoItemAsync asyncValue) {

        Thread t =  new Thread(new Runnable() {
            @Override
            public void run() {

                asyncValue.tag = tag;
                asyncValue.description = description;
                Device.context().runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       getFragmentListAdapter().add(asyncValue);

                       // sort
                       list.sort(new Comparator<DeviceInfoItem>() {
                           @Override
                           public int compare(final DeviceInfoItem a, final DeviceInfoItem b) {
                               return a.order - b.order;
                           }
                       });
                   }
                });

                do {
                    try {
                        Thread.sleep((long) (delay * 1000));
                    } catch (final InterruptedException e) {
                        Logger.e(e);
                    }

                    asyncValue.run();

                } while (loop);
            }
        });

        t.start();

        return t;
    }

    public Thread addItem(final String tag, final String description, final DeviceInfoItemAsync asyncValue) {
        return addItem(tag, description, 0f, false, asyncValue);
    }

    @Override
    public void onListItemClick(ListView lv, View v, int position, long id) {
        super.onListItemClick(lv, v, position, id);

        final DeviceInfoItem item = list.getItem(position);

        if(item.tag.equalsIgnoreCase("RAM")){
            Runtime.getRuntime().gc();
        }

        if(!item.description.equals("description")){
            Logger.toast(item.description);
        }
        // Logger.toast(list.getItem(position).tag + " " +list.getItem(position).order);

        // CustomWebView webView = new CustomWebView(Device.context());
        // webView.showWebViewFullScreen("https://github.com/kibotu/net.kibotu.android.tworowitem/blob/master/tworowitem-lib/src/net/kibotu/android/tworowitem/Device.java#L96-L113");
    }
}