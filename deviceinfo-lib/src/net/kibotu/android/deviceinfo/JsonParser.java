package net.kibotu.android.deviceinfo;

import net.kibotu.android.error.tracking.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

final public class JsonParser {

    private JsonParser() throws IllegalAccessException {
        throw new IllegalAccessException("static class");
    }

    /**
     * reads json by url
     *
     * @param url
     * @return JSONObject
     */
    @Nullable
    static public JSONObject readJson(final @NotNull String url) {
        final StringBuilder builder = new StringBuilder();
        final HttpClient client = new DefaultHttpClient();
        final HttpGet httpGet = new HttpGet(url);
        JSONObject finalResult = null;
        try {
            final HttpResponse response = client.execute(httpGet);
            final StatusLine statusLine = response.getStatusLine();
            final int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append("\n");
                }
                finalResult = new JSONObject(new JSONTokener(builder.toString()));
            } else {
                Logger.e("Failed to download status file.");
            }
        } catch (final JSONException e) {
            Logger.e(e);
        } catch (final ClientProtocolException e) {
            Logger.e(e);
        } catch (final IOException e) {
            Logger.e(e);
        }
        return finalResult;
    }
}