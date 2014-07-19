package net.kibotu.android.deviceinfo.fragments.list.vertical;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import net.kibotu.android.deviceinfo.R;

import java.util.HashMap;
import java.util.Map;

import static net.kibotu.android.deviceinfo.Device.context;

public class DeviceInfoItem implements Comparable<DeviceInfoItem> {

    public String tag;
    public String description;
    public String value = "0";
    public String keys;
    public int order = Integer.MAX_VALUE;
    public volatile View customView;
    public int textAppearance = android.R.style.TextAppearance_Large;
    public volatile int viewId = R.layout.tworowitem;

    public DeviceInfoItem(final String tag, final String description, final String value, final int order) {
        this.tag = tag;
        this.description = description;
        this.value = value;
        this.order = order;
    }

    public DeviceInfoItem(final int order) {
        this("", "", "0", order);
    }

    public DeviceInfoItem(final String tag, final String description, final String value) {
        this(tag, description, value, Integer.MAX_VALUE);
    }

    public DeviceInfoItem() {
        this("", "", "0", Integer.MAX_VALUE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceInfoItem that = (DeviceInfoItem) o;

        if (order != that.order) return false;
        if (!description.equals(that.description)) return false;
        if (!tag.equals(that.tag)) return false;
        if (!value.equals(that.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = tag.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + value.hashCode();
        result = 31 * result + order;
        return result;
    }

    @Override
    public int compareTo(final DeviceInfoItem other) {
        assert other != null;
        return order - other.order;
    }

    public void setHorizontal() {
        final LinearLayout l = (LinearLayout) LayoutInflater.from(context()).inflate(R.layout.table, null);
        final TextView keys = ((TextView) l.findViewById(R.id.key));
        final TextView values = ((TextView) l.findViewById(R.id.value));
        keys.setText(tag);
        values.setText(value);
        customView = l;
    }

    public View getView() {
        return customView = customView != null ? customView : LayoutInflater.from(context()).inflate(viewId, null);
    }

    public void setMap(final Map<String,String> map) {
        final StringBuffer keyBuffer = new StringBuffer();
        final StringBuffer valueBuffer = new StringBuffer();

        for (String key : map.keySet()) {
            keyBuffer.append(key).append("\n");
            valueBuffer.append(map.get(key)).append("\n");
        }

        keys = keyBuffer.toString();
        value = valueBuffer.toString();
    }
}