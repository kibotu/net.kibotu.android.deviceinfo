package net.kibotu.android.deviceinfo.library.hardware.gpu;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

public class EglChooser<T extends OpenGLInfo> implements GLSurfaceView.EGLConfigChooser {

    public T info;

    public EglChooser(final T info) {
        this.info = info;
    }

    @Override
    public EGLConfig chooseConfig(final EGL10 egl, final EGLDisplay display) {

        //Querying number of configurations
        final int[] num_conf = new int[1];
        egl.eglGetConfigs(display, null, 0, num_conf); //if configuration array is null it still returns the number of configurations
        final int configurations = num_conf[0];

        //Querying actual configurations
        final EGLConfig[] conf = new EGLConfig[configurations];
        egl.eglGetConfigs(display, conf, configurations, num_conf);

        EGLConfig result = null;

        for (int i = 0; i < configurations; i++) {
            result = better(result, conf[i], egl, display);

            final Egl config = new Egl(egl, display, conf[i]);
            if (config.EGL_RED_SIZE + config.EGL_BLUE_SIZE + config.EGL_GREEN_SIZE + config.EGL_ALPHA_SIZE >= 16)
                info.eglconfigs.add(config);
        }

        return result;
    }

    /**
     * Returns the best of the two EGLConfig passed according to depth and colours
     *
     * @param a The first candidate
     * @param b The second candidate
     * @return The chosen candidate
     */
    private EGLConfig better(EGLConfig a, EGLConfig b, EGL10 egl, EGLDisplay display) {
        if (a == null) return b;

        EGLConfig result;

        int[] value = new int[1];

        egl.eglGetConfigAttrib(display, a, EGL10.EGL_DEPTH_SIZE, value);
        int depthA = value[0];

        egl.eglGetConfigAttrib(display, b, EGL10.EGL_DEPTH_SIZE, value);
        int depthB = value[0];

        if (depthA > depthB)
            result = a;
        else if (depthA < depthB)
            result = b;
        else //if depthA == depthB
        {
            egl.eglGetConfigAttrib(display, a, EGL10.EGL_RED_SIZE, value);
            int redA = value[0];

            egl.eglGetConfigAttrib(display, b, EGL10.EGL_RED_SIZE, value);
            int redB = value[0];

            if (redA > redB)
                result = a;
            else if (redA < redB)
                result = b;
            else //if redA == redB
            {
                //Don't care
                result = a;
            }
        }

        return result;
    }
}