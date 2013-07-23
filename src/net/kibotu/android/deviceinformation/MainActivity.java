package net.kibotu.android.deviceinformation;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import net.kibotu.android.deviceid.R;

import javax.microedition.khronos.opengles.GL10;
import java.io.File;
import java.nio.IntBuffer;
import java.util.Calendar;

public class MainActivity extends Activity {

    public static Activity mActivity;
    private static final long UPDATE_INTERVAL = 750L;
    private static final long BYTES_TO_MB = 1024 * 1024;
    private static final String BR = "---------------------------------------------------";
    private boolean updateThreadIsRunning = true;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mActivity = this;
        final Button bt = (Button) findViewById(R.id.button1);
        final TextView idView = (TextView) findViewById(R.id.textView1);
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
                    idView.append("getRadioVersion: " + android.os.Build.getRadioVersion() + "\n");
                    idView.append(BR);
                    idView.append("GL_EXTENSIONS\n" + getExtensions() + "\n");
                } catch (Exception e) {
                    Log.e("Device", e.getMessage());
                }
            }
        };

        bt.setOnClickListener(clicker);

        final Thread updateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (updateThreadIsRunning) {
                    try {
                        Thread.sleep(UPDATE_INTERVAL);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
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
        final ActivityManager activityManager = (ActivityManager) mActivity.getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        return configurationInfo.reqGlEsVersion;
    }

    private static boolean supportsOpenGLES2() {
        return getOpenGLVersion() >= 0x20000;
    }

    private static int getVersionFromPackageManager() {
        PackageManager packageManager = mActivity.getPackageManager();
        FeatureInfo[] featureInfos = packageManager.getSystemAvailableFeatures();
        if (featureInfos != null && featureInfos.length > 0) {
            for (FeatureInfo featureInfo : featureInfos) {
                // Null feature name means this feature is the open gl es version feature.
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
     * credits:  http://stackoverflow.com/questions/3170691/how-to-get-current-memory-usage-in-android
     */
    public static long getFreeMemoryByActivityService() {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) mActivity.getSystemService(ACTIVITY_SERVICE);
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
}