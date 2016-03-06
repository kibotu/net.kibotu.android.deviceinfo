package net.kibotu.android.deviceinfo.library.hardware.gpu;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.view.ViewGroup;
import net.kibotu.android.deviceinfo.library.Callback;
import net.kibotu.android.deviceinfo.library.Device;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GPURenderer<T extends OpenGLInfo> implements GLSurfaceView.Renderer {

    private GLSurfaceView glSurfaceView;
    private T result;
    private Callback<T> callback;

    protected GPURenderer(final GLSurfaceView glSurfaceView, final T result, final Callback<T> callback) {
        this.result = result;
        this.glSurfaceView = glSurfaceView;
        this.callback = callback;
    }

    @Override
    public void onSurfaceCreated(final GL10 gl, final EGLConfig eglConfig) {

        // loadOnCreate info
        result.loadOnCreate();

        ((Activity) Device.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                callback.onComplete(result);
            }
        });

        final ViewGroup layout = (ViewGroup) Device.getContentRootView();
        layout.removeView(glSurfaceView);
    }

    @Override
    public void onSurfaceChanged(final GL10 gl10, final int width, final int height) {
        // do nothing
    }

    @Override
    public void onDrawFrame(final GL10 gl) {
        // do nothing
    }
}