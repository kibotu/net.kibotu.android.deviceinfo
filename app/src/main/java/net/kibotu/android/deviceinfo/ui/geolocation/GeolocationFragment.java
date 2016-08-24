package net.kibotu.android.deviceinfo.ui.geolocation;

import com.common.android.utils.network.GeoRequestProvider;

import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.model.ListItem;
import net.kibotu.android.deviceinfo.ui.list.ListFragment;

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

        addIpApiCom();

        addFreeGeoIp();

        addIpInfo();

        // TODO: 06.03.2016 google maps
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
    }

    private void addFreeGeoIp() {
        final ListItem item = new ListItem().setLabel("freegeoip.net").setDescription("source: https://freegeoip.net");

        addSubListItem(item);

        GeoRequestProvider.freeGeoIpService().getGeoIp().subscribe(responseModel -> {

            item
                    .addChild(new ListItem().setLabel("Ip").setValue(responseModel.getIp()))
                    .addChild(new ListItem().setLabel("Country Name").setValue(responseModel.getCountryName()))
                    .addChild(new ListItem().setLabel("Country Code").setValue(responseModel.getCountryCode()))
                    .addChild(new ListItem().setLabel("Region Name").setValue(responseModel.getRegionName()))
                    .addChild(new ListItem().setLabel("Region Code").setValue(responseModel.getRegionCode()))
                    .addChild(new ListItem().setLabel("City").setValue(responseModel.getCity()))
                    .addChild(new ListItem().setLabel("Postal Code").setValue(responseModel.getZipCode()))
                    .addChild(new ListItem().setLabel("Timezone").setValue(responseModel.getTimeZone()))
                    .addChild(new ListItem().setLabel("Latitude").setValue(responseModel.getLatitude()))
                    .addChild(new ListItem().setLabel("Longitude").setValue(responseModel.getLongitude()))
                    .addChild(new ListItem().setLabel("Metro Code").setValue(responseModel.getMetroCode()));

            notifyDataSetChanged();
        }, Throwable::printStackTrace);
    }

    private void addIpInfo() {
        final ListItem item = new ListItem().setLabel("ipinfo.io").setDescription("source: http://ipinfo.io");

        addSubListItem(item);


        GeoRequestProvider.ipApiComService().getGeoIp().subscribe(responseModel -> {

            item
                    .addChild(new ListItem().setLabel("Ip").setValue(responseModel.getIp()))
                    .addChild(new ListItem().setLabel("Hostname").setValue(responseModel.getHostname()))
                    .addChild(new ListItem().setLabel("City").setValue(responseModel.getCity()))
                    .addChild(new ListItem().setLabel("Region").setValue(responseModel.getRegion()))
                    .addChild(new ListItem().setLabel("Country").setValue(responseModel.getCountry()))
                    .addChild(new ListItem().setLabel("Location").setValue(responseModel.getLoc()))
                    .addChild(new ListItem().setLabel("Org").setValue(responseModel.getOrg()))
                    .addChild(new ListItem().setLabel("Postal Code").setValue(responseModel.getPostal()));

            notifyDataSetChanged();
        }, Throwable::printStackTrace);
    }

    private void addIpApiCom() {
        final ListItem item = new ListItem().setLabel("ip-api.com/json").setDescription("source: http://ip-api.com/json");

        addSubListItem(item);

        GeoRequestProvider.ipInfoIoService().getGeoIp().subscribe(responseModel -> {

            item
//                     .addChild(new ListItem().setLabel("As").setValue(responseModel.getAs()))
                    .addChild(new ListItem().setLabel("City").setValue(responseModel.getCity()))
                    .addChild(new ListItem().setLabel("Country").setValue(responseModel.getCountry()))
//                    .addChild(new ListItem().setLabel("Country Code").setValue(responseModel.getCountryCode()))
//                    .addChild(new ListItem().setLabel("Isp").setValue(responseModel.getIsp()))
//                    .addChild(new ListItem().setLabel("Latitude").setValue(responseModel.getLatitude()))
//                    .addChild(new ListItem().setLabel("Longitude").setValue(responseModel.getLongitude()))
                    .addChild(new ListItem().setLabel("Org").setValue(responseModel.getOrg()))
//                    .addChild(new ListItem().setLabel("Query").setValue(responseModel.getQuery()))
                    .addChild(new ListItem().setLabel("Region").setValue(responseModel.getRegion()));
//                    .addChild(new ListItem().setLabel("RegionName").setValue(responseModel.getRegionName()))
//                    .addChild(new ListItem().setLabel("Status").setValue(responseModel.getStatus()))
//                    .addChild(new ListItem().setLabel("Timezone").setValue(responseModel.getTimezone()));

            notifyDataSetChanged();
        }, Throwable::printStackTrace);
    }

    @Override
    protected int getHomeIcon() {
        return R.drawable.geo;
    }
}
