package net.kibotu.android.deviceinformation;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.net.wifi.WifiManager;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.LinearLayout;
import net.kibotu.android.deviceid.R;
import org.apache.http.conn.util.InetAddressUtils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.IntBuffer;
import java.util.*;

import static android.opengl.GLSurfaceView.Renderer;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static net.kibotu.android.deviceinformation.MainActivity.mActivity;

/**
 * Variety ways to retrieve device id in Android.
 * <p/>
 * - Unique number (IMEI, MEID, ESN, IMSI)
 * - MAC Address
 * - Serial Number
 * - ANDROID_ID
 * <p/>
 * Further information @see http://developer.samsung.com/android/technical-docs/How-to-retrieve-the-Device-Unique-ID-from-android-device
 */
public class Device {

    private static String sharedLibraries;
    private static String features;
    private static Object openGLShaderConstraints;

    /**
     * Returns the unique device ID. for example,the IMEI for GSM and the MEID or ESN for CDMA phones.
     * <p/>
     * IMPORTANT! it requires READ_PHONE_STATE permission in AndroidManifest.xml
     * <p/>
     * Disadvantages:
     * - Android devices should have telephony services
     * - It doesn't work reliably
     * - Serial Number
     * - When it does work, that value survives device wipes (Factory resets)
     * and thus you could end up making a nasty mistake when one of your customers wipes their device
     * and passes it on to another person.
     */
    public static String getDeviceIdFromTelephonyManager() {
        if (mActivity == null)
            throw new IllegalStateException("'LinesActivity.mActivity' must not be null.");
        return ((TelephonyManager) mActivity.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }

    /**
     * Returns the unique subscriber ID, for example, the IMSI for a GSM phone.
     * <p/>
     * Disadvantages:
     * - Android devices should have telephony services
     * - It doesn't work reliably
     * - Serial Number
     * - When it does work, that value survives device wipes (Factory resets)
     * and thus you could end up making a nasty mistake when one of your customers wipes their device
     * and passes it on to another person.
     */
    public static String getSubscriberIdFromTelephonyManager() {
        if (mActivity == null)
            throw new IllegalStateException("'LinesActivity.mActivity' must not be null.");
        return ((TelephonyManager) mActivity.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
    }

    /**
     * Returns MAC Address.
     * <p/>
     * IMPORTANT! requires ACCESS_WIFI_STATE permission in AndroidManifest.xml
     * <p/>
     * Disadvantages:
     * - Device should have Wi-Fi (where not all devices have Wi-Fi)
     * - If Wi-Fi present in Device should be turned on otherwise does not report the MAC address
     */
    public static String getMacAdress() {
        if (mActivity == null)
            throw new IllegalStateException("'LinesActivity.mActivity' must not be null.");
        return ((WifiManager) mActivity.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getMacAddress();
    }


    /**
     * Returns MAC address of the given interface name.
     *
     * @param interfaceName eth0, wlan0 or NULL=use first interface
     * @return mac address or empty string
     */
    public static String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac == null) return "";
                StringBuilder buf = new StringBuilder();
                for (final byte aMac : mac) buf.append(String.format("%02X:", aMac));
                if (buf.length() > 0) buf.deleteCharAt(buf.length() - 1);
                return buf.toString();
            }
        } catch (Exception ignored) {
        } // for now eat exceptions
        return "";
       /*try {
           // this is so Linux hack
           return loadFileAsString("/sys/class/net/" +interfaceName + "/address").toUpperCase().trim();
       } catch (IOException ex) {
           return null;
       }*/
    }

    /**
     * Get IP address from first non-localhost interface
     *
     * @param useIPv4 true=return ipv4, false=return ipv6
     * @return address or empty string
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                                return delim < 0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        } // for now eat exceptions
        return "";
    }

    /**
     * System Property ro.serialno returns the serial number as unique number Works for Android 2.3 and above. Can return null.
     * <p/>
     * Disadvantages:
     * - Serial Number is not available with all android devices
     */
    public static String getSerialNummer() {
        String hwID = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            hwID = (String) (get.invoke(c, "ro.serialno", "unknown"));
        } catch (Exception ignored) {
        }
        if (hwID != null) return hwID;
        try {
            Class<?> myclass = Class.forName("android.os.SystemProperties");
            Method[] methods = myclass.getMethods();
            Object[] params = new Object[]{"ro.serialno", "Unknown"};
            hwID = (String) (methods[2].invoke(myclass, params));
        } catch (Exception ignored) {
        }
        return hwID;
    }

    /**
     * More specifically, Settings.Secure.ANDROID_ID. A 64-bit number (as a hex string)
     * that is randomly generated on the device's first boot and should remain constant
     * for the lifetime of the device (The value may change if a factory reset is performed on the device.)
     * ANDROID_ID seems a good choice for a unique device identifier.
     * <p/>
     * Disadvantages:
     * - Not 100% reliable of Android prior to 2.2 (�Froyo�) devices
     * - Also, there has been at least one widely-observed bug in a popular
     * handset from a major manufacturer, where every instance has the same ANDROID_ID.
     */
    public static String getAndroidId() {
        if (mActivity == null)
            throw new IllegalStateException("'LinesActivity.mActivity' must not be null.");
        return Settings.Secure.getString(mActivity.getContentResolver(), Settings.Secure.ANDROID_ID);
    }


    public static DisplayMetrics getDisplayMetrics() {
        DisplayMetrics metrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

    public static DisplayMetrics getRealDisplayMetrics() throws NoSuchMethodError {
        DisplayMetrics metrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        return metrics;
    }

    public static Point getSize() throws NoSuchMethodError{
        Point ret = new Point();
        mActivity.getWindowManager().getDefaultDisplay().getSize(ret);
        return ret;
    }

    static IntBuffer size = IntBuffer.allocate(1);

    public static int glGetIntegerv(int value) {
        size = IntBuffer.allocate(1);
        GLES10.glGetIntegerv(value, size);
        return size.get(0);
    }

    public static int glGetIntegerv(GL10 gl, int value) {
        size = IntBuffer.allocate(1);
        gl.glGetIntegerv(value, size);
        return size.get(0);
    }

    public static int getOpenGLVersion() {
        final ActivityManager activityManager = (ActivityManager) mActivity
                .getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager
                .getDeviceConfigurationInfo();
        return configurationInfo.reqGlEsVersion;
    }

    public static boolean supportsOpenGLES2() {
        return getOpenGLVersion() >= 0x20000;
    }

    public static int getVersionFromPackageManager() {
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

    public static String getExtensions() {
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
                .getSystemService(Context.ACTIVITY_SERVICE);
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
     * @return
     * @see <a href="http://stackoverflow.com/questions/2630158/detect-application-heap-size-in-android">detect-application-heap-size-in-android</a>
     */
    public static long getMaxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    public static int getMemoryClass() {
        return ((ActivityManager) mActivity.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
    }

    public static int getLargeMemoryClass() throws NoSuchMethodError {
        return ((ActivityManager) mActivity.getSystemService(Context.ACTIVITY_SERVICE)).getLargeMemoryClass();
    }

    /**
     * http://stackoverflow.com/a/18489243
     */
    public static Bitmap save(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }

    public static void saveBitmap(Bitmap b, String filepath, String filename) {
        try {
            FileOutputStream out = new FileOutputStream(new File(filepath, filename));
            b.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void enableHardwareAcceleration(Activity context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB)
            context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
    }

    public static void enableHardwareAcceleration(View view) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB)
            view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
    }

    public static TreeSet<String> getSharedLibraries() {
        PackageManager pm = mActivity.getPackageManager();
        String[] libraries = pm.getSystemSharedLibraryNames();
        TreeSet<String> l = new TreeSet<>();
        for (String lib : libraries) {
            if (lib != null) {
                l.add(lib);
            }
        }
        return l;
    }

    public static TreeSet<String> getFeatures() {
        PackageManager pm = mActivity.getPackageManager();
        FeatureInfo[] features = pm.getSystemAvailableFeatures();
        TreeSet<String> l = new TreeSet<>();
        for (FeatureInfo f : features) {
            if (f.name != null) {
                l.add(f.name);
            }
        }
        return l;
    }

    /**
     * http://stackoverflow.com/questions/18447875/print-gpu-info-renderer-version-vendor-on-textview-on-android
     * @return
     */
    public static TreeSet<String> getOpenGLShaderConstraints() {

        final TreeSet<String> l = new TreeSet<>();
        final GLSurfaceView gles10view = new GLSurfaceView(mActivity);
        final GLSurfaceView gles20view = new GLSurfaceView(mActivity);

        final Renderer gles10 = new Renderer() {

            @Override
            public void onSurfaceCreated(GL10 gl, EGLConfig config) {

                l.add("GL_MAX_TEXTURE_UNITS: "              + glGetIntegerv(GLES10.GL_MAX_TEXTURE_UNITS));
                l.add("GL_MAX_LIGHTS: "                     + glGetIntegerv(GLES10.GL_MAX_LIGHTS));
                l.add("GL_SUBPIXEL_BITS: "                  + glGetIntegerv(GLES10.GL_SUBPIXEL_BITS));
                l.add("GL_MAX_ELEMENTS_INDICES: "           + glGetIntegerv(GLES10.GL_MAX_ELEMENTS_INDICES));
                l.add("GL_MAX_ELEMENTS_VERTICES: "          + glGetIntegerv(GLES10.GL_MAX_ELEMENTS_VERTICES));
                l.add("GL_MAX_MODELVIEW_STACK_DEPTH: "      + glGetIntegerv(GLES10.GL_MAX_MODELVIEW_STACK_DEPTH));
                l.add("GL_MAX_PROJECTION_STACK_DEPTH: "     + glGetIntegerv(GLES10.GL_MAX_PROJECTION_STACK_DEPTH));
                l.add("GL_MAX_TEXTURE_STACK_DEPTH: "        + glGetIntegerv(GLES10.GL_MAX_TEXTURE_STACK_DEPTH));
                l.add("GL_MAX_TEXTURE_SIZE: "               + glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE));
                l.add("GL_DEPTH_BITS: "                     + glGetIntegerv(GLES10.GL_DEPTH_BITS));
                l.add("GL_STENCIL_BITS: "                   + glGetIntegerv(GLES10.GL_STENCIL_BITS));

//                l.add("GL_VERTEX_SHADER: " + glGetIntegerv(gl, GLES20.GL_VERTEX_SHADER));
//                l.add("GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS: " + glGetIntegerv(gl, GLES20.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS));
//                l.add("GL_SHADER_TYPE: " + glGetIntegerv(gl, GLES20.GL_SHADER_TYPE));
//                l.add("GL_DELETE_STATUS: " + glGetIntegerv(gl, GLES20.GL_DELETE_STATUS));
//                l.add("GL_LINK_STATUS: " + glGetIntegerv(gl, GLES20.GL_LINK_STATUS));
//                l.add("GL_VALIDATE_STATUS: " + glGetIntegerv(gl, GLES20.GL_VALIDATE_STATUS));
//                l.add("GL_ATTACHED_SHADERS: " + glGetIntegerv(gl, GLES20.GL_ATTACHED_SHADERS));
//                l.add("GL_ACTIVE_UNIFORMS: " + glGetIntegerv(gl, GLES20.GL_ACTIVE_UNIFORMS));
//                l.add("GL_ACTIVE_UNIFORM_MAX_LENGTH: " + glGetIntegerv(gl, GLES20.GL_ACTIVE_UNIFORM_MAX_LENGTH));
//                l.add("GL_ACTIVE_ATTRIBUTES: " + glGetIntegerv(gl, GLES20.GL_ACTIVE_ATTRIBUTES));
//                l.add("GL_ACTIVE_ATTRIBUTE_MAX_LENGTH: " + glGetIntegerv(gl, GLES20.GL_ACTIVE_ATTRIBUTE_MAX_LENGTH));
//                l.add("GL_CURRENT_PROGRAM: " + glGetIntegerv(gl, GLES20.GL_CURRENT_PROGRAM));

                mActivity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        List<Map<String, String>> listOfMaps = MainActivity.fragment.listOfMaps;

                        for(int i = 0; i < listOfMaps.size(); ++i) {
                            for( Map.Entry<String, ?> map: listOfMaps.get(i).entrySet()) {
                                if( map.getValue().equals("OpenGL Constraints")) {
                                    listOfMaps.get(i).put("data", buildLineItem("OpenGL Constraints", l).mData);
                                }
                            }
                        }

                        // remove view after getting all required infos
                        final LinearLayout layout = (LinearLayout) mActivity.findViewById(R.id.mainlayout);
                        layout.removeView(gles10view);
                        layout.findViewById(R.id.pager).setVisibility(VISIBLE);

                        // update fragment adapter about changes
                        final ViewPager mPager = (ViewPager) mActivity.findViewById(R.id.pager);
                        mPager.getAdapter().notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onSurfaceChanged(GL10 gl, int width, int height) {
            }

            @Override
            public void onDrawFrame(GL10 gl) {
            }
        };

        final Renderer gles20 = new Renderer() {

            @Override
            public void onSurfaceCreated(GL10 gl, EGLConfig config) {

                l.add("GL_RENDERER: "                       + gl.glGetString(GLES10.GL_RENDERER));
                l.add("GL_VENDOR: "                         + gl.glGetString(GLES10.GL_VENDOR));
                l.add("GL_VERSION: "                        + gl.glGetString(GLES10.GL_VERSION));
                l.add("GL_MAX_VERTEX_ATTRIBS: "             + glGetIntegerv(gl, GLES20.GL_MAX_VERTEX_ATTRIBS));
                l.add("GL_MAX_VERTEX_UNIFORM_VECTORS: "     + glGetIntegerv(gl, GLES20.GL_MAX_VERTEX_UNIFORM_VECTORS));
                l.add("GL_MAX_FRAGMENT_UNIFORM_VECTORS: "   + glGetIntegerv(gl, GLES20.GL_MAX_FRAGMENT_UNIFORM_VECTORS));
                l.add("GL_MAX_VARYING_VECTORS: "            + glGetIntegerv(gl, GLES20.GL_MAX_VARYING_VECTORS));
                l.add("Vertex Texture Fetch: "              + isVTFSupported(gl));
                l.add("GL_MAX_TEXTURE_IMAGE_UNITS: "        + glGetIntegerv(gl, GLES20.GL_MAX_TEXTURE_IMAGE_UNITS));
                int size[] = new int[2];
                gl.glGetIntegerv(GLES10.GL_MAX_VIEWPORT_DIMS,size, 0);
                l.add("GL_MAX_VIEWPORT_DIMS: " + size[0] + "x" + size[1]);


                List<Map<String, String>> listOfMaps = MainActivity.fragment.listOfMaps;

                for(int i = 0; i < listOfMaps.size(); ++i) {
                    for( Map.Entry<String, ?> map: listOfMaps.get(i).entrySet()) {
                        if( map.getValue().equals("OpenGL Constraints")) {
                            Map<String, String> extensions = new HashMap<>();
                            extensions.put("header", "OpenGL Extensions");
                            extensions.put("data", gl.glGetString(GLES10.GL_EXTENSIONS));
                            listOfMaps.add(i+1,extensions);
                        }
                    }
                }

                mActivity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        // remove view after getting all required infos
                        final LinearLayout layout = (LinearLayout) mActivity.findViewById(R.id.mainlayout);
                        layout.removeView(gles20view);

                        layout.addView(gles10view);
                    }
                });
            }

            @Override
            public void onSurfaceChanged(GL10 gl, int width, int height) {
            }

            @Override
            public void onDrawFrame(GL10 gl) {
            }
        };

        mActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                gles20view.setEGLConfigChooser(true);
                gles20view.setZOrderOnTop(true);

                gles10view.setEGLConfigChooser(true);
                gles10view.setZOrderOnTop(true);

                // Check if the system supports OpenGL ES 2.0.
                final ActivityManager activityManager = (ActivityManager) mActivity.getSystemService(Context.ACTIVITY_SERVICE);
                final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
                final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

                if (supportsEs2)
                    gles20view.setEGLContextClientVersion(2);
                gles10view.setEGLContextClientVersion(1);

                gles10view.setRenderer(gles10);
                gles20view.setRenderer(gles20);
                final LinearLayout layout = (LinearLayout) mActivity.findViewById(R.id.mainlayout);
                layout.addView(gles20view);
                layout.findViewById(R.id.pager).setVisibility(GONE);
            }
        });

        return l;
    }

    public static TreeSet<String> getFolder() {
        TreeSet<String> l = new TreeSet<>();
        l.add("Internal Storage Path\n" + mActivity.getFilesDir().getParent() + "/");
        l.add("APK Storage Path\n" + mActivity.getPackageCodePath());
        l.add("Root Directory\n" + Environment.getRootDirectory());
        l.add("Data Directory\n" + Environment.getDataDirectory());
        l.add("External Storage Directory\n" + Environment.getExternalStorageDirectory());
        l.add("Download Cache Directory\n" + Environment.getDownloadCacheDirectory());
        l.add("External Storage State\n" + Environment.getExternalStorageState());
        l.add("Directory Alarms\n" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS));
        l.add("Directory DCIM\n" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM));
        l.add("Directory Downloads\n" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
        l.add("Directory Movies\n" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES));
        l.add("Directory Music\n" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC));
        l.add("Directory Notifications\n" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS));
        l.add("Directory Pictures\n" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
        l.add("Directory Podcasts\n" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS));
        l.add("Directory Ringtones\n" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES));
        return l;
    }

    private String getOpenGLVersion2() {
        Context context = mActivity;
        PackageManager packageManager = context.getPackageManager();
        FeatureInfo[] featureInfos = packageManager.getSystemAvailableFeatures();
        if (featureInfos != null && featureInfos.length > 0) {
            for (FeatureInfo featureInfo : featureInfos) {
                // Null feature name means this feature is the open gl es version feature.
                if (featureInfo.name == null) {
                    if (featureInfo.reqGlEsVersion != FeatureInfo.GL_ES_VERSION_UNDEFINED) {
                        return String.valueOf((featureInfo.reqGlEsVersion & 0xFFFF0000) >> 16) + "." + String.valueOf((featureInfo.reqGlEsVersion & 0x0000FFFF));
                    } else {
                        return "1.0"; // Lack of property means OpenGL ES version 1
                    }
                }
            }
        }
        return "1.0";
    }

    public static boolean isVTFSupported(GL10 gl) {
        int[] arr = new int[1];
        gl.glGetIntegerv(GLES20.GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS, arr, 0);
        return arr[0] != 0;
    }

    public static String getUserAgent() {
        String result = "Unknown";
        WebView wv = new WebView(mActivity);
        result = wv.getSettings().getUserAgentString();
        return result;
    }

    public static long getDirectorySize(File directory, long blockSize) {
        File[] files = directory.listFiles();
        if (files == null) {
            return 0;
        }

        // space used by directory itself
        long size = directory.length();

        for (File file : files) {
            if (file.isDirectory()) {
                // space used by subdirectory
                size += getDirectorySize(file, blockSize);
            } else {
                // file size need to rounded up to full block sizes
                // (not a perfect function, it adds additional block to 0 sized files
                // and file who perfectly fill their blocks)
                size += (file.length() / blockSize + 1) * blockSize;
            }
        }
        return size;
    }

    public static long getFileSizeDir(String path) {
        File directory = new File(path);

        if(!directory.exists()) return 0;

        StatFs statFs = new StatFs(directory.getAbsolutePath());
        long blockSize;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//            blockSize = statFs.getBlockSizeLong();
//        } else {
            blockSize = statFs.getBlockSize();
//        }

        return getDirectorySize(directory, blockSize) / (1024*1024);
    }

    public static String getAvailableFileSize(String path) {
        try {
            StatFs fs = new StatFs(path);
            return Formatter.formatFileSize(mActivity, fs.getAvailableBlocks() * fs.getBlockSize());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final String TAG = Device.class.getSimpleName();

    /**
     * Checks if the phone is rooted.
     *
     * @return <code>true</code> if the phone is rooted, <code>false</code>
     * otherwise.
     *
     * @credits: http://stackoverflow.com/a/6425854
     */
    public static boolean isPhoneRooted() {

        // get from build info
        String buildTags = android.os.Build.TAGS;
        if (buildTags != null && buildTags.contains("test-keys")) {
            Log.v(TAG, "is rooted by build tag");
            return true;
        }

        // check if /system/app/Superuser.apk is present
        try {
            File file = new File("/system/app/Superuser.apk");
            if (file.exists()) {
                Log.v(TAG, "is rooted by /system/app/Superuser.apk");
                return true;
            }
        } catch (Throwable e1) {
            // ignore
        }

        // from excecuting shell command
        return executeShellCommand("which su");
    }

    /**
     * Executes a shell command.
     *
     * @param command - Unix shell command.
     *
     * @return <code>true</code> if shell command was successful.
     *
     * @credits  http://stackoverflow.com/a/15485210
     */
    public static boolean executeShellCommand(final String command){
        Process process = null;
        try{
            process = Runtime.getRuntime().exec(command);
            Log.v(TAG, "'" + command + "' successfully excecuted.");
            Log.v(TAG, "is rooted by su command");
            return true;
        } catch (final Exception e) {
            Log.e(TAG, "" + e.getMessage());
            return false;
        } finally {
            if(process != null){
                try {
                    process.destroy();
                } catch (final Exception e) {
                    Log.e(TAG, "" + e.getMessage());
                }
            }
        }
    }

    public static String getFileSize(String file) {
        return getFileSize(new File(file));
    }

    private static HashMap<File,String> cache;

    public static String getFileSize(File file) {
        if(cache == null) cache = new HashMap<>();
        if(cache.containsKey(file)) return cache.get(file);
        long size = Device.getFileSizeDir(file.toString());
        String ret = size == 0 ? file.toString() : file + " (" + size + " MB)";
        cache.put(file, ret);
        return ret;
    }

    protected static Row buildLineItem(String header, Object data) {
        return buildLineItem(header, false, String.valueOf(data));
    }

    protected static Row buildLineItem(String header, boolean newLine, String... data) {
        StringBuilder sb = new StringBuilder();
        for (String s : data) {
            sb.append(s);
            if (newLine) {
                sb.append("\n");
            }
        }
        return new Row(header, sb.toString());
    }

    protected static Row buildLineItem(String header, Set<String> data) {
        return buildLineItem(header, true, data.toArray(new String[data.size()]));
    }

    public static ArrayList<Row> generateDeviceInfoList(Activity context) {
        ArrayList<Row> result = new ArrayList<>();

        final long BYTES_TO_MB = 1024 * 1024;

        // memory
        result.add(buildLineItem("Rooted", Device.isPhoneRooted()));
        result.add(buildLineItem("Time", Calendar.getInstance().getTime()));
        result.add(buildLineItem("Total Memory by Environment", Device.getTotalMemoryByEnvironment() + "  Bytes (" + Device.getTotalMemoryByEnvironment() / BYTES_TO_MB + " MB)"));
        result.add(buildLineItem("Available Memory by ActivityService", Device.getFreeMemoryByActivityService() + "  Bytes (" + Device.getFreeMemoryByActivityService() / BYTES_TO_MB + " MB)"));
        result.add(buildLineItem("Available Memory by Environment", Device.getFreeMemoryByEnvironment() + "  Bytes (" + Device.getFreeMemoryByEnvironment() / BYTES_TO_MB + " MB)"));
        result.add(buildLineItem("Max Heap Memory", Device.getMaxMemory() + " Bytes (" + Device.getMaxMemory() / BYTES_TO_MB + " MB)"));
        result.add(buildLineItem("Memory Class", Device.getMemoryClass() + " MB"));

        try {
            result.add(buildLineItem("Large Memory Class", Device.getLargeMemoryClass() + " MB"));
        } catch (Error error) {
            error.printStackTrace();
        }

        result.add(buildLineItem("Total Memory by this App", Device.getRuntimeTotalMemory() + "  Bytes (" + Device.getRuntimeTotalMemory() / BYTES_TO_MB + " MB)"));
        result.add(buildLineItem("Used Memory by this App", Device.getUsedMemorySize() + "  Bytes (" + Device.getUsedMemorySize() / BYTES_TO_MB + " MB)"));
        result.add(buildLineItem("Free Runtime Memory by this App", Device.getRuntimeFreeMemory() + "  Bytes (" + Device.getRuntimeFreeMemory() / BYTES_TO_MB + " MB)"));

        // display
        result.add(buildLineItem("Density", context.getString(R.string.density) + " (" + Device.getDisplayMetrics().density +")"));
        result.add(buildLineItem("DensityDpi", Device.getDisplayMetrics().densityDpi + " (" + Device.getDisplayMetrics().scaledDensity + ")"));

        try {
            result.add(buildLineItem("DPI X/Y", Device.getRealDisplayMetrics().xdpi + " / " + Device.getRealDisplayMetrics().ydpi));
        } catch (Error error) {
            error.printStackTrace();
        }

        result.add(buildLineItem("Screen size", context.getString(R.string.screen_size)));

        try {
            result.add(buildLineItem("Screen resolution", Device.getSize().x + "x" + Device.getSize().y));
        } catch (Error error) {
            error.printStackTrace();
        }

        result.add(buildLineItem("Orientation", context.getString(R.string.orientation)));
        result.add(buildLineItem("Rotation", mActivity.getWindowManager().getDefaultDisplay().getRotation()));
        result.add(buildLineItem("PixelFormat", mActivity.getWindowManager().getDefaultDisplay().getPixelFormat()));
        result.add(buildLineItem("RefreshRate", mActivity.getWindowManager().getDefaultDisplay().getRefreshRate()));
        result.add(buildLineItem("Locale", context.getResources().getConfiguration().locale.toString()));
        result.add(buildLineItem("Mobile County/Network code", context.getResources().getConfiguration().mcc + "/" + context.getResources().getConfiguration().mnc));
        result.add(buildLineItem("UserAgent", Device.getUserAgent()));

        // opengl
        // http://developer.apple.com/library/ios/#documentation/3DDrawing/Conceptual/OpenGLES_ProgrammingGuide/DeterminingOpenGLESCapabilities/DeterminingOpenGLESCapabilities.html
//        result.add(buildLineItem(""getVersionFromPackageManager: " + getVersionFromPackageManager());
//        result.add(buildLineItem("supportsOpenGLES2: " + supportsOpenGLES2());
        result.add(buildLineItem("OpenGL Version", Device.getOpenGLVersion()));

        result.add(buildLineItem("OpenGL Constraints", Device.getOpenGLShaderConstraints()));

        // hardware
        result.add(buildLineItem("SDK", Build.VERSION.SDK));
        result.add(buildLineItem("ID", mActivity.getWindowManager().getDefaultDisplay().getDisplayId()));
        result.add(buildLineItem("SDK_INT", android.os.Build.VERSION.SDK_INT));
        result.add(buildLineItem("CODENAME", android.os.Build.VERSION.CODENAME));
        result.add(buildLineItem("INCREMENTAL", android.os.Build.VERSION.INCREMENTAL));
        result.add(buildLineItem("RELEASE", android.os.Build.VERSION.RELEASE));
        result.add(buildLineItem("Manufacturer", Build.MANUFACTURER));
        result.add(buildLineItem("Model", Build.MODEL));
        result.add(buildLineItem("Device", Build.DEVICE));
        result.add(buildLineItem("Product", Build.PRODUCT));
        result.add(buildLineItem("Brand", Build.BRAND));
        result.add(buildLineItem("CPU+ABI", Build.CPU_ABI));
        result.add(buildLineItem("Build (Tags)", Build.DISPLAY + " (" + Build.TAGS + ")"));
        result.add(buildLineItem("Features", Device.getFeatures()));
        result.add(buildLineItem("Shared Libraries", Device.getSharedLibraries()));

        // address
        result.add(buildLineItem("IMEI No", Device.getDeviceIdFromTelephonyManager()));
        result.add(buildLineItem("IMSI No", Device.getSubscriberIdFromTelephonyManager()));
        result.add(buildLineItem("hwID", Device.getSerialNummer()));
        result.add(buildLineItem("AndroidID", Device.getAndroidId()));
        result.add(buildLineItem("MAC Address (wlan0)", Device.getMACAddress("wlan0")));
        result.add(buildLineItem("MAC Address (eth0)", Device.getMACAddress("eth0")));
        result.add(buildLineItem("IP4 Address", Device.getIPAddress(true)));
        result.add(buildLineItem("IP6 Address", Device.getIPAddress(false)));

        // build
        result.add(buildLineItem("BOARD", android.os.Build.BOARD));
        result.add(buildLineItem("BOOTLOADER", android.os.Build.BOOTLOADER));
        result.add(buildLineItem("BRAND", android.os.Build.BRAND));
        result.add(buildLineItem("CPU_ABI", android.os.Build.CPU_ABI));
        result.add(buildLineItem("CPU_ABI2", android.os.Build.CPU_ABI2));
        result.add(buildLineItem("DEVICE", android.os.Build.DEVICE));
        result.add(buildLineItem("DISPLAY", android.os.Build.DISPLAY));
        result.add(buildLineItem("FINGERPRINT", android.os.Build.FINGERPRINT));
        result.add(buildLineItem("HARDWARE", android.os.Build.HARDWARE));
        result.add(buildLineItem("HOST", android.os.Build.HOST));
        result.add(buildLineItem("ID", android.os.Build.ID));
        result.add(buildLineItem("MANUFACTURER", android.os.Build.MANUFACTURER));
        result.add(buildLineItem("MODEL", android.os.Build.MODEL));
        result.add(buildLineItem("PRODUCT", android.os.Build.PRODUCT));
        result.add(buildLineItem("RADIO", android.os.Build.RADIO));
        result.add(buildLineItem("SERIAL", android.os.Build.SERIAL));
        result.add(buildLineItem("TAGS",  android.os.Build.TAGS));
        result.add(buildLineItem("TIME", android.os.Build.TIME));
        result.add(buildLineItem("TYPE", android.os.Build.TYPE));
        result.add(buildLineItem("UNKNOWN", android.os.Build.UNKNOWN));
        result.add(buildLineItem("USER", android.os.Build.USER));

        // internal storage
        result.add(buildLineItem("External Storage State", Environment.getExternalStorageState()));
        result.add(buildLineItem("Internal Storage Path", getFileSize(mActivity.getFilesDir().getParent())));
        result.add(buildLineItem("APK Storage Path", getFileSize(mActivity.getPackageCodePath())));
        result.add(buildLineItem("Root Directory", getFileSize(Environment.getRootDirectory())));
        result.add(buildLineItem("Data Directory", getFileSize(Environment.getDataDirectory())));
        result.add(buildLineItem("External Storage Director", getFileSize(Environment.getExternalStorageDirectory())));
        result.add(buildLineItem("Download Cache Directory", getFileSize(Environment.getDownloadCacheDirectory())));
        result.add(buildLineItem("Directory Alarms", getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS))));
        result.add(buildLineItem("Directory DCIM", getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM))));
        result.add(buildLineItem("Directory Downloads", getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))));
        result.add(buildLineItem("Directory Movies", getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES))));
        result.add(buildLineItem("Directory Music", getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC))));
        result.add(buildLineItem("Directory Notifications", getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS))));
        result.add(buildLineItem("Directory Pictures", getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES))));
        result.add(buildLineItem("Directory Podcasts", getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS))));
        result.add(buildLineItem("Directory Ringtones", getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES))));

        return result;
    }

}
