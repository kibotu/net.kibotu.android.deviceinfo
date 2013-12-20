package net.kibotu.android.deviceinformation;

import java.io.Serializable;

public class Row implements Serializable {

    private static final long serialVersionUID = 1L;

    public String mHeader;
    public String mData;

    public Row(String header, String data) {
        this.mHeader = header;
        this.mData = data;
    }
}