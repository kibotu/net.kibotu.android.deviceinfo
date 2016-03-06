package net.kibotu.android.deviceinfo.library.gpu;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

import static net.kibotu.android.deviceinfo.library.gpu.OpenGLExtensions.eglGetConfigAttrib;

public final class Egl {

    public final int EGL_NON_CONFORMANT_CONFIG;
    public final int EGL_SAMPLES;
    public final int EGL_STENCIL_SIZE;
    public final int EGL_DEPTH_SIZE;
    public final int EGL_ALPHA_SIZE;
    public final int EGL_BLUE_SIZE;
    public final int EGL_GREEN_SIZE;
    public final int EGL_RED_SIZE;

    public Egl(final EGL10 egl, final EGLDisplay display, final EGLConfig eglConfig) {
        EGL_RED_SIZE = eglGetConfigAttrib(EGL10.EGL_RED_SIZE, egl, display, eglConfig);
        EGL_BLUE_SIZE = eglGetConfigAttrib(EGL10.EGL_BLUE_SIZE, egl, display, eglConfig);
        EGL_GREEN_SIZE = eglGetConfigAttrib(EGL10.EGL_GREEN_SIZE, egl, display, eglConfig);
        EGL_ALPHA_SIZE = eglGetConfigAttrib(EGL10.EGL_ALPHA_SIZE, egl, display, eglConfig);
        EGL_DEPTH_SIZE = eglGetConfigAttrib(EGL10.EGL_DEPTH_SIZE, egl, display, eglConfig);
        EGL_STENCIL_SIZE = eglGetConfigAttrib(EGL10.EGL_STENCIL_SIZE, egl, display, eglConfig);
        EGL_SAMPLES = eglGetConfigAttrib(EGL10.EGL_SAMPLES, egl, display, eglConfig);
        EGL_NON_CONFORMANT_CONFIG = eglGetConfigAttrib(EGL10.EGL_NON_CONFORMANT_CONFIG, egl, display, eglConfig);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Egl egl = (Egl) o;

        if (EGL_ALPHA_SIZE != egl.EGL_ALPHA_SIZE) return false;
        if (EGL_BLUE_SIZE != egl.EGL_BLUE_SIZE) return false;
        if (EGL_DEPTH_SIZE != egl.EGL_DEPTH_SIZE) return false;
        if (EGL_GREEN_SIZE != egl.EGL_GREEN_SIZE) return false;
        if (EGL_NON_CONFORMANT_CONFIG != egl.EGL_NON_CONFORMANT_CONFIG) return false;
        if (EGL_RED_SIZE != egl.EGL_RED_SIZE) return false;
        if (EGL_SAMPLES != egl.EGL_SAMPLES) return false;
        if (EGL_STENCIL_SIZE != egl.EGL_STENCIL_SIZE) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = EGL_NON_CONFORMANT_CONFIG;
        result = 31 * result + EGL_SAMPLES;
        result = 31 * result + EGL_STENCIL_SIZE;
        result = 31 * result + EGL_DEPTH_SIZE;
        result = 31 * result + EGL_ALPHA_SIZE;
        result = 31 * result + EGL_BLUE_SIZE;
        result = 31 * result + EGL_GREEN_SIZE;
        result = 31 * result + EGL_RED_SIZE;
        return result;
    }

    @Override
    public String toString() {

        // rgba (rgba) depth stencil samples non comfort
        return "RGB" + (EGL_ALPHA_SIZE > 0 ? "A" : "") +
                " " + (EGL_RED_SIZE + EGL_GREEN_SIZE + EGL_BLUE_SIZE + EGL_ALPHA_SIZE) + " bit" +
                " (" + EGL_RED_SIZE + "" + EGL_GREEN_SIZE + EGL_BLUE_SIZE + "" + (EGL_ALPHA_SIZE > 0 ? EGL_ALPHA_SIZE : "") + ")" +
                (EGL_DEPTH_SIZE > 0 ? " Depth " + EGL_DEPTH_SIZE + "bit" : "") +
                (EGL_STENCIL_SIZE > 0 ? " Stencil " + EGL_STENCIL_SIZE : "") +
                (EGL_SAMPLES > 0 ? " Samples x" + EGL_SAMPLES : "") +
                (EGL_NON_CONFORMANT_CONFIG > 0 ? " Non-Conformant" : "");
    }
}