package net.kibotu.android.deviceinfo;

import android.os.AsyncTask;
import org.json.JSONObject;

public class NetworkHelper {

    // utility class
    private NetworkHelper() throws IllegalAccessException {
        throw new IllegalAccessException("static class");
    }

    public static void request(final String url, final Device.AsyncCallback<JSONObject> callback) {

        new AsyncTask<String, Integer, JSONObject>() {

            @Override
            protected void onPreExecute() {
                Logger.v("Fetching from " + url);
            }

            @Override
            protected JSONObject doInBackground(final String... jsonUrls) {
                return JsonParser.readJson(jsonUrls[0]);
            }

            @Override
            protected void onProgressUpdate(final Integer... progress) {
                publishProgress(1);
            }

            @Override
            protected void onPostExecute(final JSONObject json) {
                callback.onComplete(json);
            }
        }.execute(url);
    }
}