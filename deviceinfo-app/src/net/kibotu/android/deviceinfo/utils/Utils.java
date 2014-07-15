package net.kibotu.android.deviceinfo.utils;

import net.kibotu.android.deviceinfo.Logger;
import org.json.JSONArray;
import org.json.JSONException;

final public class Utils {

    public static final int BYTES_TO_MB = 1024 * 1024;

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

    public static String formatBytes(long bytes) {
        return bytes / BYTES_TO_MB + " MB \t" + "[" + bytes + " bytes]";
    }
}
