package net.kibotu.android.deviceinfo.fragments.list.vertical;

import android.view.View;

public class DeviceInfoItem implements Comparable<DeviceInfoItem>{

    public String tag;
    public String description;
    public String value = "0";
    public int order = Integer.MAX_VALUE;
    public View customView;

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
       this(tag,description,value, Integer.MAX_VALUE);
    }

    public DeviceInfoItem() {
        this("","","0",Integer.MAX_VALUE);
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
}