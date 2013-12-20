package net.kibotu.android.deviceinformation;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import javax.microedition.khronos.egl.EGL;

public class GLView extends GLSurfaceView {

    public GLView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GLView(final Context context) {
        super(context);
        init();
    }

    private void init() {
        GLRenderer renderer = new GLRenderer();
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        setRenderer(renderer);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        // Have to do this or else
        // GlSurfaceView wont be transparent.
        setZOrderOnTop(true);
    }
}
