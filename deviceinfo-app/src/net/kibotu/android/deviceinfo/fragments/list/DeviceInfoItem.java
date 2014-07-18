package net.kibotu.android.deviceinfo.fragments.list;

public class DeviceInfoItem implements Comparable<DeviceInfoItem>{

    public String tag;
    public String description;
    public String value = "0";
    public int order;

    public DeviceInfoItem(String tag, String description, String value) {
        this.tag = tag;
        this.description = description;
        this.value = value;
    }

    public DeviceInfoItem() {
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