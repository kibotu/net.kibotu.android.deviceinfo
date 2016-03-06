package net.kibotu.android.deviceinfo.ui.geolocation;

import android.view.LayoutInflater;
import android.webkit.WebView;
import android.widget.LinearLayout;
import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.ui.list.ListFragment;
import org.json.JSONObject;

/**
 * Created by Nyaruhodo on 21.02.2016.
 */
public class GeolocationFragment extends ListFragment {

    @Override
    protected String getTitle() {
        return getString(R.string.menu_item_geolocation);
    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();

        // todo freegeoip.net

//        NetworkHelper.request("http://www.telize.com/geoip", new DeviceOld.AsyncCallback<JSONObject>() {
//            @Override
//            public void onComplete(final JSONObject result) {
//
//                final LinearLayout l = (LinearLayout) LayoutInflater.from(context()).inflate(R.layout.tablewithtag, null);
//                final Map<String, String> geoMap = parseTelize(result);
//
//                cachedList.addItem("<b>Geolocation</b>", "description", new DeviceInfoItemAsync(0) {
//
//                    @Override
//                    protected void async() {
//                        customView = l;
//                        setMap(geoMap);
//                    }
//                });
//
//                cachedList.addItem("<b>Google Maps</b>", "description", new DeviceInfoItemAsync(1) {
//
//                    @Override
//                    protected void async() {
//                        context().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                final LinearLayout linearWeb = (LinearLayout) LayoutInflater.from(context()).inflate(R.layout.linearlayoutwithtag, null);
//                                final WebView webView = CustomWebView.createWebView(context());
//                                linearWeb.addView(webView);
//                                // final String url2 = "https://www.google.com/maps/embed/v1/view?key="+API_KEY+"&center=" + geoMap.get("Latitude") + "," + geoMap.get("Longitude");
//                                final String url = "http://www.google.de/maps/?q=" + geoMap.get("Latitude") + "," + geoMap.get("Longitude") + "&t=h&z=17";
//                                webView.loadUrl(url);
//                                customView = linearWeb;
//                            }
//                        });
//                    }
//                });
//            }
//        });
    }

    @Override
    protected int getHomeIcon() {
        return R.drawable.geo;
    }
}
