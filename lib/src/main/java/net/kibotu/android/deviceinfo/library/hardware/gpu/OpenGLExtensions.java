package net.kibotu.android.deviceinfo.library.hardware.gpu;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.os.Build;
import net.kibotu.android.deviceinfo.library.Device;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;
import java.nio.IntBuffer;

import static net.kibotu.android.deviceinfo.library.Device.getContext;

/**
 * Created by Nyaruhodo on 05.03.2016.
 */
public class OpenGLExtensions {

    private volatile static IntBuffer buffer = IntBuffer.allocate(1);
    private volatile static IntBuffer buffer2 = IntBuffer.allocate(2);
    private volatile static int[] arrayBuffer = new int[1];

    public synchronized static int glGetIntegerv(int value) {
        buffer.clear();
        GLES10.glGetIntegerv(value, buffer);
        return buffer.get(0);
    }

    public synchronized static int[] glGetIntegerv(int value, int size) {
        final IntBuffer buffer = IntBuffer.allocate(size);
        GLES10.glGetIntegerv(value, buffer);
        return buffer.array();
    }

    public synchronized static int[] glGetShaderPrecisionFormat(int shaderType, int precisionType) {
        if (!Device.supportsOpenGLES2()) return new int[]{0, 0, 0};
        buffer2.clear();
        GLES20.glGetShaderPrecisionFormat(shaderType, precisionType, buffer2, buffer);
        return new int[]{buffer2.get(0), buffer2.get(1), buffer.get(0)};
    }

    public synchronized static int eglGetConfigAttrib(int eglType, final EGL10 egl, final EGLDisplay display, final EGLConfig eglConfig) {
        egl.eglGetConfigAttrib(display, eglConfig, eglType, arrayBuffer);
        return arrayBuffer[0];
    }

    public static boolean isVTFSupported() {
        GLES10.glGetIntegerv(GLES20.GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS, arrayBuffer, 0);
        return arrayBuffer[0] != 0;
    }

    static int getOpenGLVersion() {
        final ActivityManager activityManager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        return configurationInfo.reqGlEsVersion;
    }

    static boolean supportsOpenGLES2() {
        return getOpenGLVersion() >= 0x20000;
    }

    public static String glGetString(int value) {
        return GLES10.glGetString(value);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String glGetStringi(int value, int index) {
        return GLES30.glGetStringi(value, index);
    }


}
