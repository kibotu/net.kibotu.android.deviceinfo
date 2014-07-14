package net.kibotu.android.deviceinfo;

import android.annotation.TargetApi;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.*;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.webkit.WebView;
import org.apache.http.conn.util.InetAddressUtils;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.io.*;
import java.lang.Process;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.IntBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

import static android.os.Build.*;

/**
 * Variety ways to retrieve device id in Android.
 * <p>
 * - Unique number (IMEI, MEID, ESN, IMSI)
 * - MAC Address
 * - Serial Number
 * - ANDROID_ID
 * <p>
 * Further information @see http://developer.samsung.com/android/technical-docs/How-to-retrieve-the-Device-Unique-ID-from-android-device
 */
public class Device {

    public static final int HONEYCOMB = 11;
    public static final String TAG = Device.class.getSimpleName();
    public static final int ANDROID_MIN_SDK_VERSION = 9;
    public static final boolean ACTIVATE_TB = false;
    public static final int mTB_Y = 2012;
    public static final int mTB_M = 11;
    public static final int mTB_D = 30;
    public static final Class MAIN_CLASS = Activity.class;
    private static final String PREFS_NAME = "Tracking";
    static IntBuffer size = IntBuffer.allocate(1);
    private static String sharedLibraries;
    private static String features;
    private static Object openGLShaderConstraints;
    private static DisplayHelper displayHelper;
    private static Context context;
    private static volatile HashMap<File, String> cache;
    private static String uuid;
    public int x = 0;


    // static
    public Device() {
    }

    public static void setContext(Context context){
        Device.context = context;
    }

    public static Activity context() {
        if (context == null) context = null;
        return (Activity) context;
    }

    public static DisplayHelper getDisplayHelper() {
        if (displayHelper == null) displayHelper = new DisplayHelper();
        return displayHelper;
    }

    public static JSONArray getPermissions() {
        final PackageManager pm = context().getPackageManager();
        final ArrayList<String> permissions = new ArrayList<String>();
        final List<PermissionGroupInfo> lstGroups = pm.getAllPermissionGroups(0);
        for (final PermissionGroupInfo pgi : lstGroups) {
            // permissions.add(pgi.name);
            try {
                final List<PermissionInfo> lstPermissions = pm.queryPermissionsByGroup(pgi.name, 0);
                for (final PermissionInfo pi : lstPermissions) {
                    if (context().checkCallingOrSelfPermission(pi.name) == PackageManager.PERMISSION_GRANTED)
                        permissions.add(pi.name);
                }
            } catch (final Exception ex) {
                Logger.e("" + ex.getMessage(), ex);
            }
        }
        return new JSONArray(permissions);
    }

    /**
     * Returns the unique device ID. for example,the IMEI for GSM and the MEID or ESN for CDMA phones.
     * <p>
     * IMPORTANT! it requires READ_PHONE_STATE permission in AndroidManifest.xml
     * <p>
     * Disadvantages:
     * - Android devices should have telephony services
     * - It doesn't work reliably
     * - Serial Number
     * - When it does work, that value survives device wipes (Factory resets)
     * and thus you could end up making a nasty mistake when one of your customers wipes their device
     * and passes it on to another person.
     */
    public static String getDeviceIdFromTelephonyManager() {
        return ((TelephonyManager) context().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }

    /**
     * Returns the unique subscriber ID, for example, the IMSI for a GSM phone.
     * <p>
     * Disadvantages:
     * - Android devices should have telephony services
     * - It doesn't work reliably
     * - Serial Number
     * - When it does work, that value survives device wipes (Factory resets)
     * and thus you could end up making a nasty mistake when one of your customers wipes their device
     * and passes it on to another person.
     */
    public static String getSubscriberIdFromTelephonyManager() {
        return ((TelephonyManager) context().getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
    }

    /**
     * Returns MAC Address.
     * <p>
     * IMPORTANT! requires ACCESS_WIFI_STATE permission in AndroidManifest.xml
     * <p>
     * Disadvantages:
     * - Device should have Wi-Fi (where not all devices have Wi-Fi)
     * - If Wi-Fi present in Device should be turned on otherwise does not report the MAC address
     */
    public static String getMacAdress() {
        return ((WifiManager) context().getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getMacAddress();
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
     * <p>
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
     * <p>
     * Disadvantages:
     * - Not 100% reliable of Android prior to 2.2 (�Froyo�) devices
     * - Also, there has been at least one widely-observed bug in a popular
     * handset from a major manufacturer, where every instance has the same ANDROID_ID.
     */
    public static String getAndroidId() {
        return Settings.Secure.getString(context().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static int[] GetRealDimension() {
        final Display display = context().getWindowManager().getDefaultDisplay();
        final int[] dimension = new int[2];

        if (VERSION.SDK_INT >= 17) {
            try {
                //new pleasant way to get real metrics
                final DisplayMetrics realMetrics = new DisplayMetrics();
                final Point p = new Point();
                // display.getRealMetrics(realMetrics);
                Display.class.getMethod("getRealSize").invoke(p);

                dimension[0] = p.x;
                dimension[1] = p.y;
            } catch (final IllegalAccessException e) {
                Logger.e("" + e.getMessage(), e);
            } catch (final InvocationTargetException e) {
                Logger.e("" + e.getMessage(), e);
            } catch (final NoSuchMethodException e) {
                Logger.e("" + e.getMessage(), e);
            } catch (final NoClassDefFoundError e) {
                Logger.e("" + e.getMessage(), e);
            }

        } else if (VERSION.SDK_INT >= 14) {
            //reflection for this weird in-between time
            try {
                Method mGetRawH = Display.class.getMethod("getRawHeight");
                Method mGetRawW = Display.class.getMethod("getRawWidth");
                dimension[0] = (Integer) mGetRawW.invoke(display);
                dimension[1] = (Integer) mGetRawH.invoke(display);
            } catch (final Exception e) {
                //this may not be 100% accurate, but it's all we've got
                dimension[0] = display.getWidth();
                dimension[1] = display.getHeight();
                Logger.e("Couldn't use reflection to get the real display metrics.", e);
            }

        } else {
            //This should be close, as lower API devices should not have window navigation bars
            dimension[0] = display.getWidth();
            dimension[1] = display.getHeight();
        }

        return dimension;
    }

    public static DisplayMetrics getDisplayMetrics() {
        DisplayMetrics metrics = new DisplayMetrics();
        context().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

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
        final ActivityManager activityManager = (ActivityManager) context()
                .getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager
                .getDeviceConfigurationInfo();
        return configurationInfo.reqGlEsVersion;
    }

    public static boolean supportsOpenGLES2() {
        return getOpenGLVersion() >= 0x20000;
    }

    public static int getVersionFromPackageManager() {
        PackageManager packageManager = context().getPackageManager();
        FeatureInfo[] featureInfos = packageManager.getSystemAvailableFeatures();
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
     * @see android.content.pm.FeatureInfo#getGlEsVersion()
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
        ActivityManager activityManager = (ActivityManager) context().getSystemService(Context.ACTIVITY_SERVICE);
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
        return ((ActivityManager) context().getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
    }

    public static void screenshot(final String filepath, final String filename) {
        saveBitmap(save(context().getWindow().findViewById(android.R.id.content)), filepath, filename);
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
            b.recycle();
        } catch (final Exception e) {
            Logger.e("" + e.getMessage(), e);
        }
    }

    public static TreeSet<String> getSharedLibraries2() {
        PackageManager pm = context().getPackageManager();
        String[] libraries = pm.getSystemSharedLibraryNames();
        TreeSet<String> l = new TreeSet<String>();
        for (String lib : libraries) {
            if (lib != null) {
                l.add(lib);
            }
        }
        return l;
    }

    public static JSONArray getSharedLibraries() {
        PackageManager pm = context().getPackageManager();
        String[] libraries = pm.getSystemSharedLibraryNames();
        JSONArray l = new JSONArray();
        for (String lib : libraries) {
            if (lib != null) {
                l.put(lib);
            }
        }
        return l;
    }

    public static TreeSet<String> getFeatures2() {
        PackageManager pm = context().getPackageManager();
        FeatureInfo[] features = pm.getSystemAvailableFeatures();
        TreeSet<String> l = new TreeSet<String>();
        for (FeatureInfo f : features) {
            if (f.name != null) {
                l.add(f.name);
            }
        }
        return l;
    }

    public static JSONArray getFeatures() {
        PackageManager pm = context().getPackageManager();
        FeatureInfo[] features = pm.getSystemAvailableFeatures();
        JSONArray l = new JSONArray();
        for (FeatureInfo f : features) {
            if (f.name != null) {
                l.put(f.name);
            }
        }
        return l;
    }

    public static JSONArray getOpenGLES() {
        JSONArray json = new JSONArray();

        // enableHardwareAcceleration(context());
        // enableHardwareAcceleration(context().findViewById(android.R.id.content));

        json.put("GL_MAX_TEXTURE_UNITS: " + glGetIntegerv(GLES10.GL_MAX_TEXTURE_UNITS));
        json.put("GL_MAX_LIGHTS: " + glGetIntegerv(GLES10.GL_MAX_LIGHTS));
        json.put("GL_SUBPIXEL_BITS: " + glGetIntegerv(GLES10.GL_SUBPIXEL_BITS));
        json.put("GL_MAX_ELEMENTS_INDICES: " + glGetIntegerv(GLES10.GL_MAX_ELEMENTS_INDICES));
        json.put("GL_MAX_ELEMENTS_VERTICES: " + glGetIntegerv(GLES10.GL_MAX_ELEMENTS_VERTICES));
        json.put("GL_MAX_MODELVIEW_STACK_DEPTH: " + glGetIntegerv(GLES10.GL_MAX_MODELVIEW_STACK_DEPTH));
        json.put("GL_MAX_PROJECTION_STACK_DEPTH: " + glGetIntegerv(GLES10.GL_MAX_PROJECTION_STACK_DEPTH));
        json.put("GL_MAX_TEXTURE_STACK_DEPTH: " + glGetIntegerv(GLES10.GL_MAX_TEXTURE_STACK_DEPTH));
        json.put("GL_MAX_TEXTURE_SIZE: " + glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE));
        json.put("GL_DEPTH_BITS: " + glGetIntegerv(GLES10.GL_DEPTH_BITS));
        json.put("GL_STENCIL_BITS: " + glGetIntegerv(GLES10.GL_STENCIL_BITS));

        json.put("GL_RENDERER: " + GLES20.glGetString(GLES10.GL_RENDERER));
        json.put("GL_VENDOR: " + GLES20.glGetString(GLES10.GL_VENDOR));
        json.put("GL_VERSION: " + GLES20.glGetString(GLES10.GL_VERSION));
        json.put("GL_MAX_VERTEX_ATTRIBS: " + glGetIntegerv(GLES20.GL_MAX_VERTEX_ATTRIBS));
        json.put("GL_MAX_VERTEX_UNIFORM_VECTORS: " + glGetIntegerv(GLES20.GL_MAX_VERTEX_UNIFORM_VECTORS));
        json.put("GL_MAX_FRAGMENT_UNIFORM_VECTORS: " + glGetIntegerv(GLES20.GL_MAX_FRAGMENT_UNIFORM_VECTORS));
        json.put("GL_MAX_VARYING_VECTORS: " + glGetIntegerv(GLES20.GL_MAX_VARYING_VECTORS));

        int[] arr = new int[1];
        GLES20.glGetIntegerv(GLES20.GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS, arr, 0);
        json.put("Vertex Texture Fetch: " + (arr[0] != 0));
        json.put("GL_MAX_TEXTURE_IMAGE_UNITS: " + glGetIntegerv(GLES20.GL_MAX_TEXTURE_IMAGE_UNITS));
        int size[] = new int[2];
        GLES20.glGetIntegerv(GLES10.GL_MAX_VIEWPORT_DIMS, size, 0);
        json.put("GL_MAX_VIEWPORT_DIMS: " + size[0] + "x" + size[1]);

        return json;
    }

    /**
     * http://stackoverflow.com/questions/18447875/print-gpu-info-renderer-version-vendor-on-textview-on-android
     *
     * @return
     */
    public static TreeSet<String> getOpenGLShaderConstraints() {

        final TreeSet<String> l = new TreeSet<String>();
        final GLSurfaceView gles10view = new GLSurfaceView(context());
        final GLSurfaceView gles20view = new GLSurfaceView(context());

        final GLSurfaceView.Renderer gles10 = new GLSurfaceView.Renderer() {

            @Override
            public void onSurfaceCreated(GL10 gl, EGLConfig config) {

                l.add("GL_MAX_TEXTURE_UNITS: " + glGetIntegerv(GLES10.GL_MAX_TEXTURE_UNITS));
                l.add("GL_MAX_LIGHTS: " + glGetIntegerv(GLES10.GL_MAX_LIGHTS));
                l.add("GL_SUBPIXEL_BITS: " + glGetIntegerv(GLES10.GL_SUBPIXEL_BITS));
                l.add("GL_MAX_ELEMENTS_INDICES: " + glGetIntegerv(GLES10.GL_MAX_ELEMENTS_INDICES));
                l.add("GL_MAX_ELEMENTS_VERTICES: " + glGetIntegerv(GLES10.GL_MAX_ELEMENTS_VERTICES));
                l.add("GL_MAX_MODELVIEW_STACK_DEPTH: " + glGetIntegerv(GLES10.GL_MAX_MODELVIEW_STACK_DEPTH));
                l.add("GL_MAX_PROJECTION_STACK_DEPTH: " + glGetIntegerv(GLES10.GL_MAX_PROJECTION_STACK_DEPTH));
                l.add("GL_MAX_TEXTURE_STACK_DEPTH: " + glGetIntegerv(GLES10.GL_MAX_TEXTURE_STACK_DEPTH));
                l.add("GL_MAX_TEXTURE_SIZE: " + glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE));
                l.add("GL_DEPTH_BITS: " + glGetIntegerv(GLES10.GL_DEPTH_BITS));
                l.add("GL_STENCIL_BITS: " + glGetIntegerv(GLES10.GL_STENCIL_BITS));

//                context.runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//
//                        List<Map<String, String>> listOfMaps = context.fragment.listOfMaps;
//
//                        for (int i = 0; i < listOfMaps.size(); ++i) {
//                            for (Map.Entry<String, ?> map : listOfMaps.get(i).entrySet()) {
//                                if (map.getValue().equals("OpenGL Constraints")) {
//                                    listOfMaps.get(i).put("data", buildLineItem("OpenGL Constraints", l).mData);
//                                }
//                            }
//                        }
//
//                        // remove view after getting all required infos
//                        final LinearLayout layout = (LinearLayout) context.findViewById(R.id.mainlayout);
//                        layout.removeView(gles10view);
//                        layout.findViewById(R.id.pager).setVisibility(VISIBLE);
//
//                        // update fragment adapter about changes
//                        final ViewPager mPager = (ViewPager) context.findViewById(R.id.pager);
//                        mPager.getAdapter().notifyDataSetChanged();
//                    }
//                });
            }

            @Override
            public void onSurfaceChanged(GL10 gl, int width, int height) {
            }

            @Override
            public void onDrawFrame(GL10 gl) {
            }
        };

        final GLSurfaceView.Renderer gles20 = new GLSurfaceView.Renderer() {

            @Override
            public void onSurfaceCreated(GL10 gl, EGLConfig config) {

                l.add("GL_RENDERER: " + gl.glGetString(GLES10.GL_RENDERER));
                l.add("GL_VENDOR: " + gl.glGetString(GLES10.GL_VENDOR));
                l.add("GL_VERSION: " + gl.glGetString(GLES10.GL_VERSION));
                l.add("GL_MAX_VERTEX_ATTRIBS: " + glGetIntegerv(gl, GLES20.GL_MAX_VERTEX_ATTRIBS));
                l.add("GL_MAX_VERTEX_UNIFORM_VECTORS: " + glGetIntegerv(gl, GLES20.GL_MAX_VERTEX_UNIFORM_VECTORS));
                l.add("GL_MAX_FRAGMENT_UNIFORM_VECTORS: " + glGetIntegerv(gl, GLES20.GL_MAX_FRAGMENT_UNIFORM_VECTORS));
                l.add("GL_MAX_VARYING_VECTORS: " + glGetIntegerv(gl, GLES20.GL_MAX_VARYING_VECTORS));
                l.add("Vertex Texture Fetch: " + isVTFSupported(gl));
                l.add("GL_MAX_TEXTURE_IMAGE_UNITS: " + glGetIntegerv(gl, GLES20.GL_MAX_TEXTURE_IMAGE_UNITS));
                int size[] = new int[2];
                gl.glGetIntegerv(GLES10.GL_MAX_VIEWPORT_DIMS, size, 0);
                l.add("GL_MAX_VIEWPORT_DIMS: " + size[0] + "x" + size[1]);

//
//                List<Map<String, String>> listOfMaps = context.fragment.listOfMaps;
//
//                for (int i = 0; i < listOfMaps.size(); ++i) {
//                    for (Map.Entry<String, ?> map : listOfMaps.get(i).entrySet()) {
//                        if (map.getValue().equals("OpenGL Constraints")) {
//                            Map<String, String> extensions = new HashMap<String, String>();
//                            extensions.put("header", "OpenGL Extensions");
//                            extensions.put("data", gl.glGetString(GLES10.GL_EXTENSIONS));
//                            listOfMaps.add(i + 1, extensions);
//                        }
//                    }
//                }
//
//                context.runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//
//                        // remove view after getting all required infos
//                        final LinearLayout layout = (LinearLayout) context.findViewById(R.id.mainlayout);
//                        layout.removeView(gles20view);
//
//                        layout.addView(gles10view);
//                    }
//                });
            }

            @Override
            public void onSurfaceChanged(GL10 gl, int width, int height) {
            }

            @Override
            public void onDrawFrame(GL10 gl) {
            }
        };

//        context.runOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                gles20view.setEGLConfigChooser(true);
//                gles20view.setZOrderOnTop(true);
//
//                gles10view.setEGLConfigChooser(true);
//                gles10view.setZOrderOnTop(true);
//
//                // Check if the system supports OpenGL ES 2.0.
//                final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//                final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
//                final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
//
//                if (supportsEs2)
//                    gles20view.setEGLContextClientVersion(2);
//                gles10view.setEGLContextClientVersion(1);
//
//                gles10view.setRenderer(gles10);
//                gles20view.setRenderer(gles20);
//                final LinearLayout layout = (LinearLayout) context.findViewById(R.id.mainlayout);
//                layout.addView(gles20view);
//                layout.findViewById(R.id.pager).setVisibility(View.GONE);
//            }
//        });

        return l;
    }

    public static String getInternalStoragePath() {
        return context().getFilesDir().getParent();
    }

    public static String getCachingPath() {
        return context().getCacheDir().getAbsolutePath();
    }

    public static TreeSet<String> getFolder() {
        TreeSet<String> l = new TreeSet<String>();
        l.add("Internal Storage Path\n" + context().getFilesDir().getParent() + "/");
        l.add("APK Storage Path\n" + context().getPackageCodePath());
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

    public static String getOsBuildModel() {
        return Build.MODEL;
    }

    public static float getScreenDensity() {
        return context().getResources().getDisplayMetrics().density;
    }

    public static int getApiLevel() {
        return VERSION.SDK_INT;
    }

    public static Object getManufacturer() {
        return Build.MANUFACTURER;
    }

    public static String getLocale() {
        return Locale.getDefault().toString();
    }

    public static boolean isVTFSupported(GL10 gl) {
        int[] arr = new int[1];
        gl.glGetIntegerv(GLES20.GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS, arr, 0);
        return arr[0] != 0;
    }

    public static String getUserAgent() {
        String result = "Unknown";
        WebView wv = new WebView(context());
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

        if (!directory.exists()) return 0;

        StatFs statFs = new StatFs(directory.getAbsolutePath());
        long blockSize;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//            blockSize = statFs.getBlockSizeLong();
//        } else {
        blockSize = statFs.getBlockSize();
//        }

        return getDirectorySize(directory, blockSize) / (1024 * 1024);
    }

    public static String getAvailableFileSize(String path) {
        try {
            StatFs fs = new StatFs(path);
            return android.text.format.Formatter.formatFileSize(context(), fs.getAvailableBlocks() * fs.getBlockSize());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getSystemOSVersion() {
        return System.getProperty("os.version");
    }

    /**
     * Checks if the phone is rooted.
     *
     * @return <code>true</code> if the phone is rooted, <code>false</code>
     * otherwise.
     * @credits: http://stackoverflow.com/a/6425854
     */
    public static boolean isPhoneRooted() {

        // get from build info
        String buildTags = TAGS;
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
     * @return <code>true</code> if shell command was successful.
     * @credits http://stackoverflow.com/a/15485210
     */
    public static boolean executeShellCommand(final String command) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            Log.v(TAG, "'" + command + "' successfully excecuted.");
            Log.v(TAG, "is rooted by su command");
            return true;
        } catch (final Exception e) {
            Log.e(TAG, "" + e.getMessage());
            return false;
        } finally {
            if (process != null) {
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

    public static String getFileSize(File file) {
        if (cache == null) cache = new HashMap<File, String>();
        if (cache.containsKey(file)) return cache.get(file);
        long size = Device.getFileSizeDir(file.toString());
        String ret = size == 0 ? file.toString() : file + " (" + size + " MB)";
        cache.put(file, ret);
        return ret;
    }

    public static ArrayList<String> generateDeviceInfoList(Activity context) {
        ArrayList<String> result = new ArrayList<String>();
//
//        final long BYTES_TO_MB = 1024 * 1024;
//
//        // memory
//        result.add(buildLineItem("Rooted", Device.isPhoneRooted()));
//        result.add(buildLineItem("Time", Calendar.getInstance().getTime()));
//        result.add(buildLineItem("Total Memory by Environment", Device.getTotalMemoryByEnvironment() + "  Bytes (" + Device.getTotalMemoryByEnvironment() / BYTES_TO_MB + " MB)"));
//        result.add(buildLineItem("Available Memory by ActivityService", Device.getFreeMemoryByActivityService() + "  Bytes (" + Device.getFreeMemoryByActivityService() / BYTES_TO_MB + " MB)"));
//        result.add(buildLineItem("Available Memory by Environment", Device.getFreeMemoryByEnvironment() + "  Bytes (" + Device.getFreeMemoryByEnvironment() / BYTES_TO_MB + " MB)"));
//        result.add(buildLineItem("Max Heap Memory", Device.getMaxMemory() + " Bytes (" + Device.getMaxMemory() / BYTES_TO_MB + " MB)"));
//        result.add(buildLineItem("Memory Class", Device.getMemoryClass() + " MB"));
//
//        try {
//            result.add(buildLineItem("Large Memory Class", Device.getLargeMemoryClass() + " MB"));
//        } catch (Error error) {
//            error.printStackTrace();
//        }
//
//        result.add(buildLineItem("Total Memory by this App", Device.getRuntimeTotalMemory() + "  Bytes (" + Device.getRuntimeTotalMemory() / BYTES_TO_MB + " MB)"));
//        result.add(buildLineItem("Used Memory by this App", Device.getUsedMemorySize() + "  Bytes (" + Device.getUsedMemorySize() / BYTES_TO_MB + " MB)"));
//        result.add(buildLineItem("Free Runtime Memory by this App", Device.getRuntimeFreeMemory() + "  Bytes (" + Device.getRuntimeFreeMemory() / BYTES_TO_MB + " MB)"));
//
//        // display
//        result.add(buildLineItem("Density", context.getString(R.string.density) + " (" + Device.getDisplayMetrics().density + ")"));
//        result.add(buildLineItem("DensityDpi", Device.getDisplayMetrics().densityDpi + " (" + Device.getDisplayMetrics().scaledDensity + ")"));
//
//        try {
//            result.add(buildLineItem("DPI X/Y", Device.getRealDisplayMetrics().xdpi + " / " + Device.getRealDisplayMetrics().ydpi));
//        } catch (Error error) {
//            error.printStackTrace();
//        }
//
//        result.add(buildLineItem("Screen size", context.getString(R.string.screen_size)));
//
//        try {
//            result.add(buildLineItem("Screen resolution", Device.getSize().x + "x" + Device.getSize().y));
//        } catch (Error error) {
//            error.printStackTrace();
//        }
//
//        result.add(buildLineItem("Orientation", context.getString(R.string.orientation)));
//        result.add(buildLineItem("Rotation", Device.context.getWindowManager().getDefaultDisplay().getRotation()));
//        result.add(buildLineItem("PixelFormat", Device.context.getWindowManager().getDefaultDisplay().getPixelFormat()));
//        result.add(buildLineItem("RefreshRate", Device.context.getWindowManager().getDefaultDisplay().getRefreshRate()));
//        result.add(buildLineItem("Locale", context.getResources().getConfiguration().locale.toString()));
//        result.add(buildLineItem("Mobile County/Network code", context.getResources().getConfiguration().mcc + "/" + context.getResources().getConfiguration().mnc));
//        result.add(buildLineItem("UserAgent", Device.getUserAgent()));
//
//        // opengl
//        // http://developer.apple.com/library/ios/#documentation/3DDrawing/Conceptual/OpenGLES_ProgrammingGuide/DeterminingOpenGLESCapabilities/DeterminingOpenGLESCapabilities.html
////        result.add(buildLineItem(""getVersionFromPackageManager: " + getVersionFromPackageManager());
////        result.add(buildLineItem("supportsOpenGLES2: " + supportsOpenGLES2());
//        result.add(buildLineItem("OpenGL Version", Device.getOpenGLVersion()));
//
//        result.add(buildLineItem("OpenGL Constraints", Device.getOpenGLShaderConstraints()));
//
//        // hardware
//        result.add(buildLineItem("SDK", Build.VERSION.SDK));
//        result.add(buildLineItem("ID", Device.context.getWindowManager().getDefaultDisplay().getDisplayId()));
//        result.add(buildLineItem("SDK_INT", android.os.Build.VERSION.SDK_INT));
//        result.add(buildLineItem("CODENAME", android.os.Build.VERSION.CODENAME));
//        result.add(buildLineItem("INCREMENTAL", android.os.Build.VERSION.INCREMENTAL));
//        result.add(buildLineItem("RELEASE", android.os.Build.VERSION.RELEASE));
//        result.add(buildLineItem("Manufacturer", Build.MANUFACTURER));
//        result.add(buildLineItem("Model", Build.MODEL));
//        result.add(buildLineItem("Device", Build.DEVICE));
//        result.add(buildLineItem("Product", Build.PRODUCT));
//        result.add(buildLineItem("Brand", Build.BRAND));
//        result.add(buildLineItem("CPU+ABI", Build.CPU_ABI));
//        result.add(buildLineItem("Build (Tags)", Build.DISPLAY + " (" + Build.TAGS + ")"));
//        result.add(buildLineItem("Features", Device.getFeatures()));
//        result.add(buildLineItem("Shared Libraries", Device.getSharedLibraries()));
//
//        // address
//        result.add(buildLineItem("IMEI No", Device.getDeviceIdFromTelephonyManager()));
//        result.add(buildLineItem("IMSI No", Device.getSubscriberIdFromTelephonyManager()));
//        result.add(buildLineItem("hwID", Device.getSerialNummer()));
//        result.add(buildLineItem("AndroidID", Device.getAndroidId()));
//        result.add(buildLineItem("MAC Address (wlan0)", Device.getMACAddress("wlan0")));
//        result.add(buildLineItem("MAC Address (eth0)", Device.getMACAddress("eth0")));
//        result.add(buildLineItem("IP4 Address", Device.getIPAddress(true)));
//        result.add(buildLineItem("IP6 Address", Device.getIPAddress(false)));
//
//        // build
//        result.add(buildLineItem("BOARD", android.os.Build.BOARD));
//        result.add(buildLineItem("BOOTLOADER", android.os.Build.BOOTLOADER));
//        result.add(buildLineItem("BRAND", android.os.Build.BRAND));
//        result.add(buildLineItem("CPU_ABI", android.os.Build.CPU_ABI));
//        result.add(buildLineItem("CPU_ABI2", android.os.Build.CPU_ABI2));
//        result.add(buildLineItem("DEVICE", android.os.Build.DEVICE));
//        result.add(buildLineItem("DISPLAY", android.os.Build.DISPLAY));
//        result.add(buildLineItem("FINGERPRINT", android.os.Build.FINGERPRINT));
//        result.add(buildLineItem("HARDWARE", android.os.Build.HARDWARE));
//        result.add(buildLineItem("HOST", android.os.Build.HOST));
//        result.add(buildLineItem("ID", android.os.Build.ID));
//        result.add(buildLineItem("MANUFACTURER", android.os.Build.MANUFACTURER));
//        result.add(buildLineItem("MODEL", android.os.Build.MODEL));
//        result.add(buildLineItem("PRODUCT", android.os.Build.PRODUCT));
//        result.add(buildLineItem("RADIO", android.os.Build.RADIO));
//        result.add(buildLineItem("SERIAL", android.os.Build.SERIAL));
//        result.add(buildLineItem("TAGS", android.os.Build.TAGS));
//        result.add(buildLineItem("TIME", android.os.Build.TIME));
//        result.add(buildLineItem("TYPE", android.os.Build.TYPE));
//        result.add(buildLineItem("UNKNOWN", android.os.Build.UNKNOWN));
//        result.add(buildLineItem("USER", android.os.Build.USER));
//
//        // internal storage
//        result.add(buildLineItem("External Storage State", Environment.getExternalStorageState()));
//        result.add(buildLineItem("Internal Storage Path", getFileSize(Device.context.getFilesDir().getParent())));
//        result.add(buildLineItem("APK Storage Path", getFileSize(Device.context.getPackageCodePath())));
//        result.add(buildLineItem("Root Directory", getFileSize(Environment.getRootDirectory())));
//        result.add(buildLineItem("Data Directory", getFileSize(Environment.getDataDirectory())));
//        result.add(buildLineItem("External Storage Director", getFileSize(Environment.getExternalStorageDirectory())));
//        result.add(buildLineItem("Download Cache Directory", getFileSize(Environment.getDownloadCacheDirectory())));
//        result.add(buildLineItem("Directory Alarms", getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS))));
//        result.add(buildLineItem("Directory DCIM", getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM))));
//        result.add(buildLineItem("Directory Downloads", getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))));
//        result.add(buildLineItem("Directory Movies", getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES))));
//        result.add(buildLineItem("Directory Music", getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC))));
//        result.add(buildLineItem("Directory Notifications", getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS))));
//        result.add(buildLineItem("Directory Pictures", getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES))));
//        result.add(buildLineItem("Directory Podcasts", getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS))));
//        result.add(buildLineItem("Directory Ringtones", getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES))));

        return result;
    }

    public static String getReleaseVersion() {
        return VERSION.RELEASE;
    }

    public static String getOsName() {

        Field[] fields = VERSION_CODES.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            int fieldValue = -1;

            try {
                fieldValue = field.getInt(new Object());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            if (fieldValue == VERSION.SDK_INT) {
                return fieldName;
            }
        }

        return "android";
    }

    /**
     * Returns MAC address of the given interface name.
     * <p>
     * credits to http://stackoverflow.com/questions/6064510/how-to-get-ip-address-of-the-device/13007325#13007325
     * <p>
     * Note: requires  <uses-permission android:name="android.permission.INTERNET " /> and
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE " />
     *
     * @param interfaceName eth0, wlan0 or NULL=use first interface
     * @return mac address or empty string
     */
    @TargetApi(VERSION_CODES.GINGERBREAD)
    public static String AndroidMacAddress(String interfaceName) {
        String macaddress = "";
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac == null) return "";
                StringBuilder buf = new StringBuilder();
                for (byte aMac : mac) buf.append(String.format("%02X:", aMac));
                if (buf.length() > 0) buf.deleteCharAt(buf.length() - 1);
                macaddress = buf.toString();
            }
        } catch (Exception ignore) {
        } // for now eat exceptions
        return macaddress;
    }

    @TargetApi(11)
    public static void enableHardwareAcceleration(Activity context) {
        if (VERSION.SDK_INT >= 11)
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // context().getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
                    context().getWindow().setFlags(0x01000000, 0x01000000);
                }
            });
    }

    @TargetApi(11)
    public static void enableHardwareAcceleration(final View view) {
        if (VERSION.SDK_INT >= 11)
            context().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // view.setLayerType(View.LAYER_TYPE_HARDWARE, null); todo find way for api 10
                }
            });

    }

    public static String HashIdentifier(int prefix, String addressAndDeviceName) {
        byte[] hash = new byte[0];
        try {
            hash = md5(addressAndDeviceName).getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        long strippedMD548bit =
                (long) hash[0] |
                        (long) hash[1] << 8 |
                        (long) hash[2] << 16 |
                        (long) hash[3] << 24 |
                        (long) hash[4] << 32 |
                        (long) hash[5] << 40;
        // add prefix
        strippedMD548bit += ((long) prefix * 1000000000000000L);
        return String.valueOf(strippedMD548bit);
    }

    public static String AndroidUniqueIdentifier() {
        // to compute the identifier for android
        // 7778${ANDROID_ID}${MAC_ADDRESS}:${DEVICE_NAME}' running Apportable'
        return HashIdentifier(7778, AndroidId() + AndroidMacAddress("wlan0") + ":" + MODEL + " running Apportable");
    }

    public static String UniqueIdentifier() {
        return HashIdentifier(1111, AndroidUniqueIdentifier());
    }

    @TargetApi(VERSION_CODES.CUPCAKE)
    public static String AndroidId() {
        return Settings.Secure.getString(context().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * String to Md5 conversion.
     *
     * @param s Raw string
     * @return Returns md5 hash.
     * @credits http://stackoverflow.com/questions/4846484/md5-or-other-hashing-in-android
     */
    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    // We return the lowest disk space out of the internal and sd card storage
    public static Long getFreeDiskSpace() {
        Long diskSpace = null;

        try {
            StatFs externalStat = new StatFs(Environment.getExternalStorageDirectory().getPath());
            long externalBytesAvailable = (long) externalStat.getBlockSize() * (long) externalStat.getBlockCount();

            StatFs internalStat = new StatFs(Environment.getDataDirectory().getPath());
            long internalBytesAvailable = (long) internalStat.getBlockSize() * (long) internalStat.getBlockCount();

            diskSpace = Math.min(internalBytesAvailable, externalBytesAvailable);
        } catch (Exception e) {
            Log.w(Device.TAG, e);
        }

        return diskSpace;
    }

    public static Boolean getCharging() {
        Boolean isCharging = null;
        try {
            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = context().registerReceiver(null, ifilter);

            int status = batteryStatus.getIntExtra("status", -1);
            isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;
        } catch (Exception e) {
            Log.w(Device.TAG, e);
        }

        return isCharging;
    }

    public static Float getChargeLevel() {
        Float chargeLevel = null;
        try {
            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = context().registerReceiver(null, ifilter);

            int level = batteryStatus.getIntExtra("level", -1);
            int scale = batteryStatus.getIntExtra("scale", -1);

            chargeLevel = level / (float) scale;
        } catch (Exception e) {
            Log.w(Device.TAG, e);
        }

        return chargeLevel;
    }

    public static int getOrientation() {
        return context().getResources().getConfiguration().orientation;
    }

    public static String getOrientationName() {
        String orientation = null;

        try {
            switch (context().getResources().getConfiguration().orientation) {
                case android.content.res.Configuration.ORIENTATION_LANDSCAPE:
                    orientation = "landscape";
                    break;
                case android.content.res.Configuration.ORIENTATION_PORTRAIT:
                    orientation = "portrait";
                    break;
            }
        } catch (Exception e) {
            Log.w(Device.TAG, e);
        }

        return orientation;
    }

    public static String getResolution() {
        return String.format("%dx%d", Math.max(getDisplayHelper().mScreenWidth, getDisplayHelper().mScreenHeight), Math.min(getDisplayHelper().mScreenWidth, getDisplayHelper().mScreenHeight));
    }

    // http://developer.android.com/reference/java/lang/System.html#getProperty(java.lang.String)
    public static String getJavaVmVersion() {
        return System.getProperty("java.vm.version");
    }

    public static String getOsArch() {
        return System.getProperty("os.arch");
    }

    public static String getJavaClassPath() {
        return System.getProperty("java.class.path");
    }

    public static String getOsName2() {
        return System.getProperty("os.name");
    }

    // This returns the maximum memory the VM can allocate which != the total
    // memory on the phone.
    public static Long totalMemoryAvailable() {
        Long totalMemory = null;

        try {
            if (Runtime.getRuntime().maxMemory() != Long.MAX_VALUE) {
                totalMemory = Runtime.getRuntime().maxMemory();
            } else {
                totalMemory = Runtime.getRuntime().totalMemory();
            }
        } catch (Exception e) {
            Log.w(Device.TAG, e);
        }

        return totalMemory;
    }

    // This is the amount of memory remaining that the VM can allocate.
    public static Long totalFreeMemory() {
        Long freeMemory = null;

        try {
            freeMemory = totalMemoryAvailable() - memoryUsedByApp();
        } catch (Exception e) {
            Log.w(Device.TAG, e);
        }

        return freeMemory;
    }

    // This is the actual memory used by the VM (which may not be the total used
    // by the app in the case of NDK usage).
    public static Long memoryUsedByApp() {
        Long memoryUsedByApp = null;

        try {
            memoryUsedByApp = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        } catch (Exception e) {
            Log.w(Device.TAG, e);
        }

        return memoryUsedByApp;
    }

    public static Boolean lowMemoryState() {
        Boolean lowMemory = null;
        try {
            ActivityManager activityManager = (ActivityManager) context().getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
            activityManager.getMemoryInfo(memInfo);

            lowMemory = memInfo.lowMemory;
        } catch (Exception e) {
            Log.w(Device.TAG, e);
        }
        return lowMemory;
    }

    // We might be able to improve this by checking for su, but i have seen
    // some reports that su is on non rooted phones too
    public static boolean checkIsRooted() {
        return checkTestKeysBuild() || checkSuperUserAPK() || executeShellCommand("which su");
    }

    protected static boolean checkTestKeysBuild() {
        String buildTags = TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }

    protected static boolean checkSuperUserAPK() {
        try {
            File file = new File("/system/app/Superuser.apk");
            return file.exists();
        } catch (Exception e) {
            return false;
        }
    }

    // Requires android.permission.ACCESS_NETWORK_STATE
    public static String getNetworkStatus() {
        String networkStatus = null;

        try {
            // Get the network information
            ConnectivityManager cm = (ConnectivityManager) context().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                if (activeNetwork.getType() == 1) {
                    networkStatus = "wifi";
                } else if (activeNetwork.getType() == 9) {
                    networkStatus = "ethernet";
                } else {
                    // We default to cellular as the other enums are all cellular in some
                    // form or another
                    networkStatus = "cellular";
                }
            } else {
                networkStatus = "none";
            }
        } catch (Exception e) {
            Log.w(Device.TAG, e);
        }

        return networkStatus;
    }

    @TargetApi(VERSION_CODES.CUPCAKE)
    public static String getGpsAllowed() {
        String gpsAllowed = null;

        try {
            ContentResolver cr = context().getContentResolver();
            String providersAllowed = Settings.Secure.getString(cr, Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (providersAllowed != null && providersAllowed.length() > 0) {
                gpsAllowed = "allowed";
            } else {
                gpsAllowed = "disallowed";
            }
        } catch (Exception e) {
            Log.w(Device.TAG, e);
        }

        return gpsAllowed;
    }

    public static String getPackageName() {
        try {
            return context().getPackageName();
        } catch (Exception e) {
            Log.w(Device.TAG, e);
        }
        return null;
    }

    public static String getAppName() {
        Resources appR = context().getResources();
        CharSequence txt = appR.getText(appR.getIdentifier("app_name", "string", context().getPackageName()));
        return txt.toString();
    }

    public static String getAppversion() {
        try {
            return context().getPackageManager().getPackageInfo(context().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getAppVersionCode() {
        try {
            return context().getPackageManager().getPackageInfo(context().getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static synchronized String getUUID() {
        if (uuid != null) return uuid;

        final SharedPreferences settings = context().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        uuid = settings.getString("userId", null);
        if (uuid == null) {
            uuid = UUID.randomUUID().toString();

            // Save if for future
            final String finalUuid = uuid;

            Async.safeAsync(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("userId", finalUuid);
                    editor.commit();
                }
            });
        }
        return uuid;
    }

    public static int getRotation() {
        return context().getWindowManager().getDefaultDisplay().getPixelFormat();
    }

    public static int getPixelFormat() {
        return context().getWindowManager().getDefaultDisplay().getPixelFormat();
    }

    public static float getRefreshRate() {
        return context().getWindowManager().getDefaultDisplay().getRefreshRate();
    }

    public static Object getMobileCountryCode() {
        return context().getResources().getConfiguration().mcc;
    }

    public static Object getMobileNetworkCode() {
        return context().getResources().getConfiguration().mnc;
    }

    // region Supported Version

    public static String getTimeUTC(@Nullable final String format) {
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat(format == null ? "dd:MM:yyyy HH:mm:ss" : format);
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormatGmt.format(new Date());
    }

    public static float getDensityDpi() {
        if (supportsApi(14)) return getDisplayMetrics().densityDpi;
        else return context().getResources().getDisplayMetrics().density * 160f;
    }

    public static DisplayMetrics getRealDisplayMetrics() {
        return null;
    }

    // endregion


    // region timebomb

    public static boolean supportsApi(int apiLevel) {
        return VERSION.SDK_INT >= apiLevel;
    }

    public static boolean checkAndroidVersion() {
        if (supportsApi(ANDROID_MIN_SDK_VERSION)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context());
            builder.setMessage("Your Android OS is not officially supported. Please update your OS.")
                    .setCancelable(false).setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    context().finish();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
            return false;
        }
        return true;
    }

    public static void checkTimebombDialog() {
        if (context() == null) return;
        if (ACTIVATE_TB) {
            Date today = new Date();
            Date tb = new Date();
            tb.setYear(mTB_Y - 1900);
            tb.setMonth(mTB_M - 1);
            tb.setDate(mTB_D);
            if (today.compareTo(tb) > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context());
                builder.setMessage("This version has expired. The Application will exit.")
                        .setCancelable(false).setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        context().finish();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    public static void restartScheduled() {
        // We restart the application and force to reload everything in order to clean all memory variables, etc..
        Intent mStartActivity = new Intent(context(), MAIN_CLASS);
        int mPendingIntentId = 51459;
        PendingIntent mPendingIntent = PendingIntent.getActivity(context(), mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) context().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, mPendingIntent);
        System.exit(0);
    }

    public static void Restart() {
        Intent intent = context().getIntent();
        context().finish();
        context().startActivity(intent);
    }

    // region experimental
    public static void stuff() {
        //clearLog();
        //saveLog();

        // setContentView(R.layout.crashreport);

        // getThombstones(this);
        //readLogcat(this);

//        b.postDelayed(new Runnable() {
//            public void run() {
//                if (task.getStatus() == AsyncTask.Status.FINISHED) return;
//                // It's probably one of these devices where some fool broke logcat.
//                progress.dismiss();
//                task.cancel(true);
//                new AlertDialog.Builder(JNICrashHandlerActivity.this)
//                        .setMessage(MessageFormat.format(getString(R.string.get_log_failed), getString(R.string.author_email)))
//                        .setCancelable(true)
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .show();
//            }
//        }, 3000);

//        finish();


//        Logger.v("start bugreport");
//        try {
//            ShellUtils.doShellCommand(new String[]{"bugreport > /mnt/sdcard/bugreport.txt"}, new ShellUtils.ShellCallback() {
//                @Override
//                public void shellOut(String shellLine) {
//                    Logger.v("doShellCommand shellOut: " + shellLine);
//                }
//
//                @Override
//                public void processComplete(int exitValue) {
//                    Logger.v("doShellCommand processComplete exitValue: " + exitValue);
//                }
//            },false,true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        Logger.v("end bugreport");
    }
    // endregion

    // region Restarting app

    public static void clearLog() {
        try {
            ShellUtils.doShellCommand(new String[]{"logcat -c"}, new ShellUtils.ShellCallback() {
                @Override
                public void shellOut(String shellLine) {
                    Logger.v("shellOut " + shellLine);
                }

                @Override
                public void processComplete(int exitValue) {
                    Logger.v("shellOut " + exitValue);
                }
            }, false, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

 /*   public static void readLogcat(final Activity context) {

        final TextView textView = (TextView) context.findViewById(R.id.reportTextView);
        if (textView == null)
            Logger.v("textview null");

        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Progress");
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();

        final AsyncTask task = new AsyncTask<Void, Void, Void>() {

            String log;
            Process process;

            @Override
            protected Void doInBackground(Void... v) {
                try {
                    // Debug logs are compiled in but stripped at runtime. Error, warning and info logs are always kept.
                    // @see http://developer.android.com/reference/android/util/Log.html
                    process = Runtime.getRuntime().exec(new String[]{"logcat", "-d", "-v", "threadtime"});
                    log = readAllOf(process.getInputStream());

                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "" + log, Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (final IOException e) {
                    e.printStackTrace();
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
                return null;
            }

            @Override
            protected void onCancelled() {
                process.destroy();
            }

            @Override
            protected void onPostExecute(Void v) {
                progress.setMessage("Sending report to backend.");
                if (textView != null)
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(log);
                        }
                    });
//                boolean ok = SGTPuzzles.tryEmailAuthor(JNICrashHandlerActivity.this, true, getString(R.string.crash_preamble) + "\n\n\n\nLog:\n" + log);
                progress.dismiss();
//                if (ok) {
//                    if (cl.isChecked()) clearState();
//                    finish();
//                }
            }
        }.execute();
    }*/

    // endregion

    public static String readAllOf(InputStream s) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(s), 8096);
        String line;
        final StringBuilder log = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            log.append(line);
            log.append("\n");
        }
        return log.toString();
    }

    // http://bytesthink.com/blog/?p=133
    // http://stackoverflow.com/questions/1083154/how-can-i-catch-sigsegv-segmentation-fault-and-get-a-stack-trace-under-jni-on
    // http://blog.httrack.com/blog/2013/08/23/catching-posix-signals-on-android/
    public static void runAsync(final AsyncCallback callback) {

        final String cmd = "mv /data/tombstones/* " + getCachingPath(); // mv or cp
        final String chmod = "chmod -R 777 " + getCachingPath();
        Logger.v("su " + cmd);

        try {
            ShellUtils.doShellCommand(new String[]{cmd, chmod}, new ShellUtils.ShellCallback() {
                @Override
                public void shellOut(String shellLine) {
                    Logger.v("Shell shellOut: " + shellLine);
                }

                @Override
                public void processComplete(int exitValue) {
                    Logger.v("Shell exitValue: " + exitValue);
                    callback.callback(loadThombstones());
                }
            }, true, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<File> loadThombstones() {
        final List<File> thombstones = new ArrayList<File>();
//        File thombstonesFolder = new File("/data/tombstones");
        File thombstonesFolder = new File(getCachingPath());
        Logger.v("Exists: " + thombstonesFolder.exists() + " is directory: " + thombstonesFolder.isDirectory() + " can be read: " + thombstonesFolder.canRead());
        if (thombstonesFolder.exists() && thombstonesFolder.isDirectory() && thombstonesFolder.canRead())
            Collections.addAll(thombstones, thombstonesFolder.listFiles());
        return thombstones;
    }

    public static void saveLog() {
        /*try {
            File logfile = new File(SBSErrorTrackingCore.getCachePath() + "logfile.log");
            logfile.createNewFile();
            String cmd = "logcat -d -v threadtime -f " + logfile.getAbsolutePath();
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        Logger.v("start logcat");

        String fileName = "logcat.txt";
        File outputFile = new File("/mnt/sdcard/", fileName);
        try {
            Process process = Runtime.getRuntime().exec("logcat -d -v threadtime -f " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            Logger.e("" + e.getMessage(), e);
        }

        Logger.v("end logcat");
    }

    /*public static void getThombstones(final Activity context) {

        final ArrayList<String> thombstones = new ArrayList<String>();
        final TextView textView = (TextView) context.findViewById(R.id.reportTextView);
        if (textView == null)
            Logger.v("textview null");

        runAsync(new AsyncCallback() {
            @Override
            public void callback(Object o) {
                if (o instanceof List<?>) {
                    List<File> list = (List<File>) o;
                    Logger.v("Files: " + ((List) o).size());
                    for (File errorFile : list) {

                        if (!errorFile.getName().startsWith("tombstone_"))
                            continue;
                        try {
                            final Scanner in = new Scanner(new BufferedReader(new InputStreamReader(new FileInputStream(errorFile), Charset.forName("UTF-8").newEncoder().charset())));
                            final StringBuffer buffer = new StringBuffer();

                            while (in.hasNextLine()) {
                                buffer.append(in.nextLine());
                            }


                            if (buffer.length() < 0)
                                continue;

                            thombstones.add(buffer.toString());
                            Logger.v(buffer.toString());

                        } catch (final Exception e) {
                            Logger.w("Problem sending unsent error from disk", e);
                        }
                    }


                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            if(textView == null)
//                                Logger.v("textview null again");
//                            else

//                            textView.setText(thombstones.get(0));
                            textView.invalidate();
                        }
                    });
                }
            }
        });
    }*/

    private String getOpenGLVersion2() {
        Context context = Device.context();
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

    protected String getPackageVersion(String packageName) {
        String packageVersion = null;

        try {
            PackageInfo pi = context().getPackageManager().getPackageInfo(packageName, 0);
            packageVersion = pi.versionName;
        } catch (Exception e) {
            Log.w(Device.TAG, "Could not get package version", e);
        }

        return packageVersion;
    }

    protected String guessReleaseStage(String packageName) {
        String releaseStage = "production";

        try {
            ApplicationInfo ai = context().getPackageManager().getApplicationInfo(packageName, 0);
            boolean debuggable = (ai.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
            if (debuggable) {
                releaseStage = "development";
            }
        } catch (Exception e) {
            Log.w(Device.TAG, "Could not guess release stage", e);
        }

        return releaseStage;
    }

    public static class DisplayHelper {

        public int mScreenWidth;
        public int mScreenHeight;
        public int mScreenDpi;
        public boolean mHasSoftKeys;

        public int absScreenWidth;
        public int absScreenHeight;

        public DisplayHelper() {
            mScreenWidth = -1;
            mScreenHeight = -1;
            mScreenDpi = -1;
            mHasSoftKeys = false;
            init();
        }

        private void init() {

            DisplayMetrics dm = new DisplayMetrics();
            Display display = context().getWindowManager().getDefaultDisplay();
            display.getMetrics(dm);

            mScreenDpi = (int) dm.densityDpi;

            if (dm.heightPixels <= dm.widthPixels) {
                mScreenWidth = dm.heightPixels;
                mScreenHeight = dm.widthPixels;
            } else {
                mScreenWidth = dm.widthPixels;
                mScreenHeight = dm.heightPixels;
            }

            // to determine if we have softkeys: try to read absolute display size and compare to useable screen size
            absScreenWidth = mScreenWidth;
            absScreenHeight = mScreenHeight;
            if (VERSION.SDK_INT >= 14 && VERSION.SDK_INT < 17) {
                try {
                    absScreenWidth = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                    absScreenHeight = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
                } catch (Exception ignored) {
                }
            }
            if (VERSION.SDK_INT >= 17) {
                try {
                    Point realSize = new Point();
                    Display.class.getMethod("getRealSize", Point.class).invoke(display, realSize);
                    absScreenWidth = realSize.x;
                    absScreenHeight = realSize.y;
                } catch (Exception ignored) {
                }
            }
        }

        public boolean hasSoftKeys() {
            return mScreenWidth < absScreenWidth || mScreenHeight < absScreenHeight;
        }
    }

    // endregion
}
