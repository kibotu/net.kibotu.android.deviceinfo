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

    public static String formatFrequency(int clockHz) { return clockHz < 1000 * 1000 ? (clockHz / 1000) + " MHz" : (clockHz / 1000 / 1000) + "." + (clockHz / 1000 / 100) % 10 + " GHz";  }


    public static void killCpu() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int x = 0;
                for(int i = 0; i < Integer.MAX_VALUE; ++i) {
                    ++x;
                }
            }
        }).start();
    }
}
