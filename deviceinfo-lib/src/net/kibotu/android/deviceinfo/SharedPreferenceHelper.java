package net.kibotu.android.deviceinfo;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static net.kibotu.android.deviceinfo.DeviceOld.context;

public enum SharedPreferenceHelper {

    shared;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SharedPreferences prefs() {
        if (prefs == null) prefs = PreferenceManager.getDefaultSharedPreferences(context());
        return prefs;
    }

    public SharedPreferences.Editor editor() {
        if (editor == null) editor = prefs().edit();
        return editor;
    }
}
