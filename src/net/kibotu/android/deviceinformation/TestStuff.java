package net.kibotu.android.deviceinformation;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import net.kibotu.android.deviceid.R;

public class TestStuff  {

    public static void displayDialog() {

        // http://stackoverflow.com/questions/9172805/android-webview-inside-dialog-or-popup
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.mActivity);

        alert.setTitle("Title here");

        String website = "https://github.com/kibotu/net.kibotu.android.deviceinfo";

        WebView wv = new WebView(MainActivity.mActivity);

        wv.loadUrl(website);

        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
        });

        alert.setView(wv);
        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        alert.show();
    }

    public static void startWebView(Activity activity, String url, int webViewLayoutId, int webViewResourceId, int imageViewId, int backgroundImageResourceId) {

        // set active content view
        activity.setContentView(webViewLayoutId);

        // imageview
        ImageView imgView = (ImageView) MainActivity.mActivity.findViewById(imageViewId);
        imgView.setImageBitmap(BitmapFactory.decodeFile("pathToImageFile"));
        imgView.setImageResource(backgroundImageResourceId);

        // webview
        WebView webView = (WebView) activity.findViewById(webViewResourceId);
        webView.setWebViewClient(new WebViewClient());
        Helper.setWebViewSettings(activity, webView);
        webView.loadUrl(url);
    }

    public void testImageViewSize() {
        final ImageView iv = (ImageView) MainActivity.mActivity.findViewById(R.id.imageView);
        final TextView tv = (TextView)MainActivity.mActivity.findViewById(R.id.textView);
        ViewTreeObserver vto = iv.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                final int finalHeight = iv.getMeasuredHeight();
                final int finalWidth = iv.getMeasuredWidth();
                tv.setText("Height: " + finalHeight + " Width: " + finalWidth);
                return true;
            }
        });
    }


    public static void startWebView2(final String url) {
        if (MainActivity.mActivity == null) return;
        DisplayMetrics dm = MainActivity.mActivity.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        width = 480;
        height = (int) (height - (0.3f * height));
        Log.d("WebView", "Loading " + url + " [width=" + width + "|height=" + height + "]");
        startWebView2(MainActivity.mActivity, url, 0, 0, width, height);
    }

    public static void startWebView2(final Activity activity, final String url, final int x, final int y, final int width, final int height) {

        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        RelativeLayout viewGroup = new RelativeLayout(activity);
        RelativeLayout.LayoutParams viewGroupParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        viewGroup.setLayoutParams(viewGroupParams);

        final ImageView topShadow = new ImageView(activity);
        Bitmap topShadowBitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.imgfaqtopcut);
        topShadow.setImageBitmap(topShadowBitmap);
        topShadow.setAdjustViewBounds(false);
        topShadow.setScaleType(ImageView.ScaleType.CENTER_CROP);
        topShadow.bringToFront();
        RelativeLayout.LayoutParams topShadowParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        topShadowParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        topShadow.setLayoutParams(topShadowParams);

        final ImageView bottomShadow = new ImageView(activity);
        Bitmap bottomShadowBitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.imgfaqbottomcut);
        bottomShadow.setImageBitmap(bottomShadowBitmap);
        bottomShadow.setAdjustViewBounds(false);
        bottomShadow.bringToFront();
        bottomShadow.setScaleType(ImageView.ScaleType.CENTER_CROP);
        RelativeLayout.LayoutParams bottomShadowParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bottomShadowParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        bottomShadow.setLayoutParams(bottomShadowParams);

        WebView webView = new WebView(activity);
        RelativeLayout.LayoutParams webViewParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        webView.setLayoutParams(webViewParams);
        webView.loadUrl(url);
        Helper.setWebViewSettings(activity, webView);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen).create();

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        params.gravity = Gravity.CENTER;
        params.x = x;
        params.y = y;
        dialog.getWindow().setAttributes(params);

        viewGroup.addView(webView);
        viewGroup.addView(topShadow);
        viewGroup.addView(bottomShadow);

        dialog.setView(viewGroup, 0, 0, 0, 0);
        dialog.show();

        // important! call after dialog.show()
        dialog.getWindow().setLayout(width, height);
    }


    public static void startWebViewWithFaq2() {
        startWebView2("http://www.woogamops.com/support/?game=js");
    }
}
