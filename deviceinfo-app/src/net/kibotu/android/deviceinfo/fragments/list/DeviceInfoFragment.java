package net.kibotu.android.deviceinfo.fragments.list;

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

    public DeviceInfoItem addItem(String tag, String description, String value) {
        if (value == null || value == "") return null;
        DeviceInfoItem item = new DeviceInfoItem(tag, description, value);
        getFragmentListAdapter().add(item);
        return item;
    }

    /**
     * @param tag
     * @param description
     * @param delay       - Delay in seconds.
     * @param loop
     * @param asyncValue
     */
    public void addItem(final String tag, final String description, final float delay, final boolean loop, final DeviceInfoItemAsync asyncValue) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                asyncValue.tag = tag;
                asyncValue.description = description;
                getFragmentListAdapter().add(asyncValue);

                do {
                    try {
                        Thread.sleep((long) (delay * 1000));
                    } catch (final InterruptedException e) {
                        Logger.e("" + e.getMessage(), e);
                    }

                    asyncValue.run();

                    Device.context().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            list.notifyDataSetChanged();
                        }
                    });

                } while (loop);
            }
        }).start();
    }

    public void addItem(final String tag, final String description, final DeviceInfoItemAsync asyncValue) {
        addItem(tag, description, 0f, false, asyncValue);
    }

    @Override
    public void onListItemClick(ListView lv, View v, int position, long id) {
        super.onListItemClick(lv, v, position, id);

        // CustomWebView webView = new CustomWebView(Device.context());
        // webView.showWebViewFullScreen("https://github.com/kibotu/net.kibotu.android.deviceinfo/blob/master/deviceinfo-lib/src/net/kibotu/android/deviceinfo/Device.java#L96-L113");
    }
}