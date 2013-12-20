package net.kibotu.android.deviceinformation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;


public class Helper {

    // static
    private Helper() {
    }

    @SuppressLint("SetJavaScriptEnabled")
    public static void setWebViewSettings(final Activity activtiy, final WebView webView) {
        final String internalFilePath = activtiy.getFilesDir().getPath();
        // http://stackoverflow.com/questions/10097233/optimal-webview-settings-for-html5-support
        final WebSettings s = webView.getSettings();
        webView.setFocusable(true);
        webView.setFocusableInTouchMode(true);
        s.setJavaScriptEnabled(true);
        s.setJavaScriptCanOpenWindowsAutomatically(true);
        s.setPluginState(WebSettings.PluginState.ON_DEMAND);
        s.setRenderPriority(WebSettings.RenderPriority.HIGH);
        s.setCacheMode(WebSettings.LOAD_NO_CACHE); // WebSettings.LOAD_DEFAULT
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setDatabasePath(internalFilePath + "databases/");
        s.setAppCacheEnabled(true);
        s.setAppCachePath(internalFilePath + "cache/");
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // Configure the webview https://code.google.com/p/html5webview/source/browse/trunk/HTML5WebView/src/org/itri/html5webview/HTML5WebView.java
        s.setBuiltInZoomControls(true);
        s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        s.setUseWideViewPort(true);
        s.setLoadWithOverviewMode(true);
        s.setSavePassword(true);
        s.setSaveFormData(true);
    }

    public static void logUrlInfo(String url) {
        Log.i("Device getAuthority", "" + Uri.parse(url).getAuthority());
        Log.i("Device getEncodedFragment", "" + Uri.parse(url).getEncodedFragment());
        Log.i("Device getEncodedPath", "" + Uri.parse(url).getEncodedPath());
        Log.i("Device getEncodedQuery", "" + Uri.parse(url).getEncodedQuery());
        Log.i("Device getEncodedSchemeSpecificPart", "" + Uri.parse(url).getEncodedSchemeSpecificPart());
        Log.i("Device getEncodedUserInfo", "" + Uri.parse(url).getEncodedUserInfo());
        Log.i("Device getFragment", "" + Uri.parse(url).getFragment());
        Log.i("Device getHost", "" + Uri.parse(url).getHost());
        Log.i("Device getLastPathSegment", "" + Uri.parse(url).getLastPathSegment());
        Log.i("Device getPath", "" + Uri.parse(url).getPath());
        Log.i("Device getPort", "" + Uri.parse(url).getPort());
        Log.i("Device getQuery", "" + Uri.parse(url).getQuery());
        Log.i("Device getPathSegments", "" + Uri.parse(url).getPathSegments());
        Log.i("Device getQueryParameterNames", "" + Uri.parse(url).getQueryParameterNames());
    }
}
