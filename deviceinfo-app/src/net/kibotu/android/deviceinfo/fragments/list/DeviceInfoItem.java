package net.kibotu.android.deviceinfo.fragments.list;

public class DeviceInfoItem {

    public String tag;
    public String description;
    public String value = "0";

    public DeviceInfoItem(String tag, String description, String value) {
        this.tag = tag;
        this.description = description;
        this.value = value;
    }

    public DeviceInfoItem() {
    }
}