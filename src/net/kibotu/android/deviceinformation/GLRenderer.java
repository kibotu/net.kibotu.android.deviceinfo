package net.kibotu.android.deviceinformation;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRenderer implements GLSurfaceView.Renderer {

    @Override
    public void onSurfaceCreated(final GL10 gl, final EGLConfig config) {
    }

    @Override
    public void onSurfaceChanged(final GL10 gl, final int width, final int height) {
    }

    @Override
    public void onDrawFrame(final GL10 gl) {
        gl.glClearColor(1, 0, 0, 1);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
    }
}
