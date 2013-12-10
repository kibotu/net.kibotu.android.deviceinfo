package net.kibotu.android.deviceinformation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ConfigurationInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.graphics.*;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.*;
import net.kibotu.android.deviceid.R;

import javax.microedition.khronos.opengles.GL10;
import java.io.*;
import java.nio.IntBuffer;
import java.util.Calendar;
import java.util.LinkedList;

import static android.view.ViewGroup.*;


public class MainActivity extends Activity implements TextureView.SurfaceTextureListener {

    private Camera mCamera;
    private TextureView mTextureView;

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mCamera = Camera.open();

        Camera.Size previewSize = mCamera.getParameters().getPreviewSize();
        mTextureView.setLayoutParams(new FrameLayout.LayoutParams(previewSize.width, previewSize.height, Gravity.CENTER));

        mTextureView.setDrawingCacheEnabled(true);

        try {
            mCamera.setPreviewTexture(surface);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCamera.startPreview();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        // Ignored, the Camera does all the work for us
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mCamera.stopPreview();
        mCamera.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        // Update your view here!
    }

    public static MainActivity mActivity;
    private static final long UPDATE_INTERVAL = 750L;
    private static final long BYTES_TO_MB = 1024 * 1024;
    private static final String BR = "-------------------------------------------------------";
    private boolean updateThreadIsRunning = true;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Device", "starting");
        mActivity = this;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout);
    }

    @Override
    public void onStart() {
        super.onStart();

        startDeviceInfoUpdates();
        //startWebViewWithFaq2();
        //ExpensionHelper.checkExpension();
        //startTextureView();
    }

    public void saveBitmap(Bitmap b, String filepath, String filename) {
        try {
            FileOutputStream out = new FileOutputStream(new File(filepath, filename));
            b.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void explodeMemory() {

        explodeMemory = new LinkedList<Bitmap>();

        long i = 0;
        while(true) {
            i++;
            Log.v("explode-counter", "allocating screenshot " + i);
            //explodeMemory.add(mTextureView.getBitmap());
            explodeMemory.add(save(mTextureView));
        }
    }

    private LinkedList<Bitmap> explodeMemory;

    /**
     * http://stackoverflow.com/a/18489243
     */
    private Bitmap save(View v)
    {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }

    /**
     * @see <a href="http://stackoverflow.com/a/12457895">save-bitmap-to-location</a>
     */
    public void startTextureView() {
        mTextureView = new TextureView(this);
        mTextureView.setSurfaceTextureListener(this);
        mTextureView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                // saveBitmap(mTextureView.getBitmap(),Environment.getExternalStorageDirectory().getAbsolutePath(),"cameraScreenshot.png");
                explodeMemory();
            }
        });
        setContentView(mTextureView);
    }

    public static void startWebViewWithFaq2() {
        startWebView2("http://www.woogamops.com/support/?game=js");
    }

    public static void startWebView2(final String url) {
        if(mActivity == null) return;
        DisplayMetrics dm = mActivity.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        width = 480;
        height = (int)(height-(0.3f*height));
        Log.d("WebView", "Loading " + url + " [width=" + width + "|height=" + height + "]");
        startWebView2(mActivity, url, 0, 0, width, height);
    }

    public static void startWebView2(final Activity activity, final String url, final int x, final int y, final int width, final int height) {

        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        RelativeLayout viewGroup = new RelativeLayout(activity);
        RelativeLayout.LayoutParams viewGroupParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        viewGroup.setLayoutParams(viewGroupParams);

        final ImageView topShadow = new ImageView(activity);
        Bitmap topShadowBitmap = BitmapFactory.decodeResource(activity.getResources(),R.drawable.imgfaqtopcut);
        topShadow.setImageBitmap(topShadowBitmap);
        topShadow.setAdjustViewBounds(false);
        topShadow.setScaleType(ImageView.ScaleType.CENTER_CROP);
        topShadow.bringToFront();
        RelativeLayout.LayoutParams topShadowParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        topShadowParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        topShadow.setLayoutParams(topShadowParams);

        final ImageView bottomShadow = new ImageView(activity);
        Bitmap bottomShadowBitmap = BitmapFactory.decodeResource(activity.getResources(),R.drawable.imgfaqbottomcut);
        bottomShadow.setImageBitmap(bottomShadowBitmap);
        bottomShadow.setAdjustViewBounds(false);
        bottomShadow.bringToFront();
        bottomShadow.setScaleType(ImageView.ScaleType.CENTER_CROP);
        RelativeLayout.LayoutParams bottomShadowParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        bottomShadowParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        bottomShadow.setLayoutParams(bottomShadowParams);

        WebView webView = new WebView(activity);
        RelativeLayout.LayoutParams webViewParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        webView.setLayoutParams(webViewParams);
        webView.loadUrl(url);
        setWebViewSettings(activity,webView);

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

        dialog.setView(viewGroup, 0,0,0,0);
        dialog.show();

        // important! call after dialog.show()
        dialog.getWindow().setLayout(width,height);
    }

    public void testImageViewSize() {
        final ImageView iv = (ImageView) findViewById(R.id.imageView);
        final TextView tv = (TextView) findViewById(R.id.textView);
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

    public static void displayDialog() {

        // http://stackoverflow.com/questions/9172805/android-webview-inside-dialog-or-popup
        AlertDialog.Builder alert = new AlertDialog.Builder(mActivity);

        alert.setTitle("Title here");

        String website = "https://github.com/kibotu/net.kibotu.android.deviceinfo";

        WebView wv = new WebView(mActivity);

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
        ImageView imgView = (ImageView) mActivity.findViewById(imageViewId);
        imgView.setImageBitmap(BitmapFactory.decodeFile("pathToImageFile"));
        imgView.setImageResource(backgroundImageResourceId);

        // webview
        WebView webView = (WebView) activity.findViewById(webViewResourceId);
        webView.setWebViewClient(new WebViewClient());
        setWebViewSettings(activity, webView);
        webView.loadUrl(url);
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
        s.setPluginState(PluginState.ON_DEMAND);
        s.setRenderPriority(RenderPriority.HIGH);
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

    public static void startDeviceInfoUpdates() {
        mActivity.setContentView(R.layout.activity_main);
        final Button bt = (Button) mActivity.findViewById(R.id.button1);
        final TextView idView = (TextView) mActivity
                .findViewById(R.id.textView1);
        idView.setMovementMethod(new ScrollingMovementMethod());

        final OnClickListener clicker = new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    idView.setText("");
                    idView.append(Calendar.getInstance().getTime().toString() + "\n");
                    idView.append("Total Memory by Environment\n" + getTotalMemoryByEnvironment() + "  Bytes (" + getTotalMemoryByEnvironment() / BYTES_TO_MB + " MB)\n");
                    idView.append("Available Memory by ActivityService\n" + getFreeMemoryByActivityService() + "  Bytes (" + getFreeMemoryByActivityService() / BYTES_TO_MB + " MB)\n");
                    idView.append("Available Memory by Environment\n" + getFreeMemoryByEnvironment() + "  Bytes (" + getFreeMemoryByEnvironment() / BYTES_TO_MB + " MB)\n");
                    idView.append(BR);
                    idView.append("Max Memory\n" + getMaxMemory() + " Bytes (" + getMaxMemory() / BYTES_TO_MB + " MB)\n");
                    idView.append("Memory Class\n" + getMemoryClass() + " MB \n");
                    idView.append("Large Memory Class\n" + getLargeMemoryClass() + " MB \n");
                    idView.append(BR);
                    idView.append("Total Memory by this App\n" + getRuntimeTotalMemory() + "  Bytes (" + getRuntimeTotalMemory() / BYTES_TO_MB + " MB)\n");
                    idView.append("Used Memory by this App\n" + getUsedMemorySize() + "  Bytes (" + getUsedMemorySize() / BYTES_TO_MB + " MB)\n");
                    idView.append("Free Runtime Memory by this App\n" + getRuntimeFreeMemory() + "  Bytes (" + getRuntimeFreeMemory() / BYTES_TO_MB + " MB)\n");
                    // http://developer.apple.com/library/ios/#documentation/3DDrawing/Conceptual/OpenGLES_ProgrammingGuide/DeterminingOpenGLESCapabilities/DeterminingOpenGLESCapabilities.html
                    idView.append(BR);
                    idView.append("GL_VERSION: " + getOpenGLVersion() + "\n");
                    idView.append("getVersionFromPackageManager: " + getVersionFromPackageManager() + "\n");
                    idView.append("supportsOpenGLES2: " + supportsOpenGLES2() + "\n");
                    idView.append("GL_MAX_TEXTURE_SIZE: " + glGetIntegerv(GL10.GL_MAX_TEXTURE_SIZE) + "\n");
                    idView.append("GL_DEPTH_BITS: " + glGetIntegerv(GL10.GL_DEPTH_BITS) + "\n");
                    idView.append("GL_STENCIL_BITS: " + glGetIntegerv(GL10.GL_STENCIL_BITS) + "\n");
                    idView.append("GL_MAX_VERTEX_ATTRIBS: " + glGetIntegerv(GLES20.GL_MAX_VERTEX_ATTRIBS) + "\n");
                    idView.append("GL_MAX_VERTEX_UNIFORM_VECTORS: " + glGetIntegerv(GLES20.GL_MAX_VERTEX_UNIFORM_VECTORS) + "\n");
                    idView.append("GL_MAX_FRAGMENT_UNIFORM_VECTORS: " + glGetIntegerv(GLES20.GL_MAX_FRAGMENT_UNIFORM_VECTORS) + "\n");
                    idView.append("GL_MAX_VARYING_VECTORS: " + glGetIntegerv(GLES20.GL_MAX_VARYING_VECTORS) + "\n");
                    idView.append("GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS: " + glGetIntegerv(GLES20.GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS) + "\n");
                    idView.append("GL_MAX_TEXTURE_IMAGE_UNITS: " + glGetIntegerv(GLES20.GL_MAX_TEXTURE_IMAGE_UNITS) + "\n");
                    idView.append("GL_MAX_TEXTURE_UNITS: " + glGetIntegerv(GLES10.GL_MAX_TEXTURE_UNITS) + "\n");
                    idView.append(BR);
                    idView.append("IMEI No: " + UIDeviceId.getDeviceIdFromTelephonyManager() + "\n");
                    idView.append("IMSI No: " + UIDeviceId.getSubscriberIdFromTelephonyManager() + "\n");
                    idView.append("hwID: " + UIDeviceId.getSerialNummer() + "\n");
                    idView.append("AndroidID: " + UIDeviceId.getAndroidId() + "\n");
                    idView.append("MAC Adress (wlan0): " + UIDeviceId.getMACAddress("wlan0") + "\n");
                    idView.append("MAC Adress (eth0): " + UIDeviceId.getMACAddress("eth0") + "\n");
                    idView.append("IP4 Adress: " + UIDeviceId.getIPAddress(true) + "\n");
                    idView.append("IP6 Adress: " + UIDeviceId.getIPAddress(false) + "\n");
                    idView.append(BR);
                    idView.append(Html.fromHtml("<h1>Build</h1>"));
                    idView.append("CODENAME: " + android.os.Build.VERSION.CODENAME + "\n");
                    idView.append("INCREMENTAL: " + android.os.Build.VERSION.INCREMENTAL + "\n");
                    idView.append("RELEASE: " + android.os.Build.VERSION.RELEASE + "\n");
                    idView.append("SDK: " + android.os.Build.VERSION.SDK + "\n");
                    idView.append("SDK_INT: " + android.os.Build.VERSION.SDK_INT + "\n");
                    idView.append(BR);
                    idView.append("BOARD: " + android.os.Build.BOARD + "\n");
                    idView.append("BOOTLOADER: " + android.os.Build.BOOTLOADER + "\n");
                    idView.append("BRAND: " + android.os.Build.BRAND + "\n");
                    idView.append("CPU_ABI: " + android.os.Build.CPU_ABI + "\n");
                    idView.append("CPU_ABI2: " + android.os.Build.CPU_ABI2 + "\n");
                    idView.append("DEVICE: " + android.os.Build.DEVICE + "\n");
                    idView.append("DISPLAY: " + android.os.Build.DISPLAY + "\n");
                    idView.append("FINGERPRINT: " + android.os.Build.FINGERPRINT + "\n");
                    idView.append("HARDWARE: " + android.os.Build.HARDWARE + "\n");
                    idView.append("HOST: " + android.os.Build.HOST + "\n");
                    idView.append("ID: " + android.os.Build.ID + "\n");
                    idView.append("MANUFACTURER: " + android.os.Build.MANUFACTURER + "\n");
                    idView.append("MODEL: " + android.os.Build.MODEL + "\n");
                    idView.append("PRODUCT: " + android.os.Build.PRODUCT + "\n");
                    idView.append("RADIO: " + android.os.Build.RADIO + "\n");
                    idView.append("SERIAL: " + android.os.Build.SERIAL + "\n");
                    idView.append("TAGS: " + android.os.Build.TAGS + "\n");
                    idView.append("TIME: " + android.os.Build.TIME + "\n");
                    idView.append("TYPE: " + android.os.Build.TYPE + "\n");
                    idView.append("UNKNOWN: " + android.os.Build.UNKNOWN + "\n");
                    idView.append("USER: " + android.os.Build.USER + "\n");
                    //idView.append("getRadioVersion: " + android.os.Build.getRadioVersion() + "\n");
                    idView.append(BR);
                    idView.append(Html.fromHtml("<h1>GL_EXTENSIONS</h1>"));
                    idView.append(mActivity.getExtensions() + "\n");
                    idView.append(BR);
                    idView.append(Html.fromHtml("<h1>Environment</h1>"));
                    idView.append("Internal Storage Path\n" + mActivity.getFilesDir().getParent() + "/\n");
                    idView.append("APK Storage Path\n" + mActivity.getPackageCodePath() + "\n");
                    idView.append("Root Directory\n" + Environment.getRootDirectory() + "\n");
                    idView.append("Data Directory\n" + Environment.getDataDirectory() + "\n");
                    idView.append("External Storage Directory\n" + Environment.getExternalStorageDirectory() + "\n");
                    idView.append("Download Cache Directory\n" + Environment.getDownloadCacheDirectory() + "\n");
                    idView.append("External Storage State\n" + Environment.getExternalStorageState() + "\n");
                    idView.append("Directory Alarms\n" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS) + "\n");
                    idView.append("Directory DCIM\n" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "\n");
                    idView.append("Directory Downloads\n" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "\n");
                    idView.append("Directory Movies\n" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "\n");
                    idView.append("Directory Music\n" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) + "\n");
                    idView.append("Directory Notifications\n" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS) + "\n");
                    idView.append("Directory Pictures\n" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "\n");
                    idView.append("Directory Podcasts\n" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS) + "\n");
                    idView.append("Directory Ringtones\n" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES) + "\n");
                    idView.append(BR);
                    idView.append(Html.fromHtml("<h1>Display</h1>"));
                    idView.append("ID: " + mActivity.getWindowManager().getDefaultDisplay().getDisplayId() + "\n");
                    idView.append("Width: " + getSize().x + "\n");
                    idView.append("Height: " + getSize().y + "\n");
                    idView.append("Rotation: " + mActivity.getWindowManager().getDefaultDisplay().getRotation() + "\n");
                    idView.append("PixelFormat: " + mActivity.getWindowManager().getDefaultDisplay().getPixelFormat() + "\n");
                    idView.append("RefreshRate: " + mActivity.getWindowManager().getDefaultDisplay().getRefreshRate() + "\n");
                    idView.append("Density\n" + getDisplayMetrics().density + "\n");
                    idView.append("DensityDpi\n" + getDisplayMetrics().densityDpi + "\n");
                    idView.append("ScaledDensity\n" + getDisplayMetrics().scaledDensity + "\n");
                    idView.append("WidthPixels\n" + getDisplayMetrics().widthPixels + "\n");
                    idView.append("HeightPixels\n" + getDisplayMetrics().heightPixels + "\n");
                    idView.append("Density: " + getRealDisplayMetrics().density + "\n");
                    idView.append("DensityDpi: " + getRealDisplayMetrics().densityDpi + "\n");
                    idView.append("ScaledDensity: " + getRealDisplayMetrics().scaledDensity + "\n");
                    idView.append("WidthPixels: " + getRealDisplayMetrics().widthPixels + "\n");
                    idView.append("HeightPixels: " + getRealDisplayMetrics().heightPixels + "\n");
                    idView.append("X DPI: " + getRealDisplayMetrics().xdpi + "\n");
                    idView.append("Y DPI: " + getRealDisplayMetrics().ydpi + "\n");

                } catch (Exception e) {
                    Log.e("Device", e.getMessage());
                }
            }
        };

        bt.setOnClickListener(clicker);

        final Thread updateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (mActivity.updateThreadIsRunning) {
                    try {
                        Thread.sleep(UPDATE_INTERVAL);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            clicker.onClick(idView);
                        }
                    });
                }
            }
        });
        updateThread.start();
    }

    public static DisplayMetrics getDisplayMetrics() {
        DisplayMetrics metrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

    public static DisplayMetrics getRealDisplayMetrics() {
        DisplayMetrics metrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        return metrics;
    }

    public static Point getSize() {
        Point ret = new Point();
        mActivity.getWindowManager().getDefaultDisplay().getSize(ret);
        return ret;
    }

    public void onPause() {
        super.onPause();
        updateThreadIsRunning = false;
    }

    static IntBuffer size = IntBuffer.allocate(1);

    private static int glGetIntegerv(int value) {
        size = IntBuffer.allocate(1);
        GLES10.glGetIntegerv(value, size);
        return size.get(0);
    }

    private static int getOpenGLVersion() {
        final ActivityManager activityManager = (ActivityManager) mActivity
                .getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager
                .getDeviceConfigurationInfo();
        return configurationInfo.reqGlEsVersion;
    }

    private static boolean supportsOpenGLES2() {
        return getOpenGLVersion() >= 0x20000;
    }

    private static int getVersionFromPackageManager() {
        PackageManager packageManager = mActivity.getPackageManager();
        FeatureInfo[] featureInfos = packageManager
                .getSystemAvailableFeatures();
        if (featureInfos != null && featureInfos.length > 0) {
            for (FeatureInfo featureInfo : featureInfos) {
                // Null feature name means this feature is the open gl es
                // version feature.
                if (featureInfo.name == null) {
                    if (featureInfo.reqGlEsVersion != FeatureInfo.GL_ES_VERSION_UNDEFINED) {
                        return getMajorVersion(featureInfo.reqGlEsVersion);
                    } else {
                        return 1; // Lack of property means OpenGL ES version 1
                    }
                }
            }
        }
        return 1;
    }

    /**
     * @see FeatureInfo#getGlEsVersion()
     */
    private static int getMajorVersion(int glEsVersion) {
        return ((glEsVersion & 0xffff0000) >> 16);
    }

    public String getExtensions() {
        return GLES10.glGetString(GL10.GL_EXTENSIONS);
    }

    /**
     * credits:
     * http://stackoverflow.com/questions/3170691/how-to-get-current-memory
     * -usage-in-android
     */
    public static long getFreeMemoryByActivityService() {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) mActivity
                .getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        return mi.availMem;
    }

    public static long getFreeMemoryByEnvironment() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    public static long getTotalMemoryByEnvironment() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getBlockCount();
        return availableBlocks * blockSize;
    }

    public static long getUsedMemorySize() {
        long freeSize = 0L;
        long totalSize = 0L;
        long usedSize = -1L;
        try {
            Runtime info = Runtime.getRuntime();
            freeSize = info.freeMemory();
            totalSize = info.totalMemory();
            usedSize = totalSize - freeSize;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usedSize;
    }

    public static long getRuntimeTotalMemory() {
        long memory = 0L;
        try {
            Runtime info = Runtime.getRuntime();
            memory = info.totalMemory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return memory;
    }

    public static long getRuntimeFreeMemory() {
        long memory = 0L;
        try {
            Runtime info = Runtime.getRuntime();
            memory = info.freeMemory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return memory;
    }

    /**
     * @see <a href="http://stackoverflow.com/questions/2630158/detect-application-heap-size-in-android">detect-application-heap-size-in-android</a>
     * @return
     */
    public static long getMaxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    public static int getMemoryClass() {
        return ((ActivityManager) mActivity.getSystemService(ACTIVITY_SERVICE)).getMemoryClass();
    }

    public static int getLargeMemoryClass() {
        return ((ActivityManager) mActivity.getSystemService(ACTIVITY_SERVICE)).getLargeMemoryClass();
    }
}