package net.kibotu.android.deviceinformation;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import net.kibotu.android.deviceid.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoFragment extends ListFragment {

    private ArrayList<Row> mData;
    private SimpleAdapter mAdapter;
    public List<Map<String, String>> listOfMaps;

    public InfoFragment(final ArrayList<Row> mData) {
        this.mData = mData;
    }

    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * The Fragment's UI is just a simple text view showing its instance number.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_list, container, false);
        listOfMaps = generateListMap();
        mAdapter = new SimpleAdapter(getActivity(), listOfMaps, android.R.layout.simple_list_item_2, new String[]{"header", "data"}, new int[]{android.R.id.text1, android.R.id.text2});
        return result;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(mAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
    }

    protected List<Map<String, String>> generateListMap() {
        List<Map<String, String>> result = new ArrayList<Map<String,String>>();
        for (Row r : mData) {
            HashMap<String, String> map = new HashMap<String,String>();
            map.put("header", r.mHeader);
            map.put("data", r.mData);
            result.add(map);
        }
        return result;
    }
}
