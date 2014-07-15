package net.kibotu.android.deviceinfo.utils;

import net.kibotu.android.deviceinfo.Logger;
import org.json.JSONArray;
import org.json.JSONException;

final public class Utils {

    private Utils() throws IllegalAccessException {
        throw new IllegalAccessException("'Utils' cannot be instantiated.");
    }

    public static String jsonArrayToString(final JSONArray array) {
        final StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < array.length(); ++i) {
            try {
                buffer.append(array.getString(i)).append("\n");
            } catch (JSONException e) {
                Logger.e("" + e.getMessage(), e);
            }
        }

        return buffer.toString();
    }
}
