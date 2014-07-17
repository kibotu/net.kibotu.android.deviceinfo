package net.kibotu.android.deviceinfo;

import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.widget.FrameLayout;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.nio.IntBuffer;

public class GPU {

    // region OpenGLGles10

    public static class OpenGLGles10Info extends OpenGLInfo {

        public int GL_MAX_TEXTURE_UNITS;
        public int GL_MAX_LIGHTS;
        public int GL_SUBPIXEL_BITS;
        public int GL_MAX_ELEMENTS_INDICES;
        public int GL_MAX_ELEMENTS_VERTICES;
        public int GL_MAX_MODELVIEW_STACK_DEPTH;
        public int GL_MAX_PROJECTION_STACK_DEPTH;
        public int GL_MAX_TEXTURE_STACK_DEPTH;
        public int GL_MAX_TEXTURE_SIZE;
        public int GL_DEPTH_BITS;
        public int GL_STENCIL_BITS;

        public OpenGLGles10Info() {
            super(1);
        }

        protected void loadOncreate() {
            GL_MAX_TEXTURE_UNITS = glGetIntegerv(GLES10.GL_MAX_TEXTURE_UNITS);
            GL_MAX_LIGHTS = glGetIntegerv(GLES10.GL_MAX_LIGHTS);
            GL_SUBPIXEL_BITS = glGetIntegerv(GLES10.GL_SUBPIXEL_BITS);
            GL_MAX_ELEMENTS_INDICES = glGetIntegerv(GLES10.GL_MAX_ELEMENTS_INDICES);
            GL_MAX_ELEMENTS_VERTICES = glGetIntegerv(GLES10.GL_MAX_ELEMENTS_VERTICES);
            GL_MAX_MODELVIEW_STACK_DEPTH = glGetIntegerv(GLES10.GL_MAX_MODELVIEW_STACK_DEPTH);
            GL_MAX_PROJECTION_STACK_DEPTH = glGetIntegerv(GLES10.GL_MAX_PROJECTION_STACK_DEPTH);
            GL_MAX_TEXTURE_STACK_DEPTH = glGetIntegerv(GLES10.GL_MAX_TEXTURE_STACK_DEPTH);
            GL_MAX_TEXTURE_SIZE = glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE);
            GL_DEPTH_BITS = glGetIntegerv(GLES10.GL_DEPTH_BITS);
            GL_STENCIL_BITS = glGetIntegerv(GLES10.GL_STENCIL_BITS);
        }

        @Override
        public String toString() {
            return "MAX_TEXTURE_UNITS=" + GL_MAX_TEXTURE_UNITS + "\n" +
                    "MAX_LIGHTS=" + GL_MAX_LIGHTS + "\n" +
                    "SUBPIXEL_BITS=" + GL_SUBPIXEL_BITS + "\n" +
                    "MAX_ELEMENTS_INDICES=" + GL_MAX_ELEMENTS_INDICES + "\n" +
                    "MAX_ELEMENTS_VERTICES=" + GL_MAX_ELEMENTS_VERTICES + "\n" +
                    "MAX_MODELVIEW_STACK_DEPTH=" + GL_MAX_MODELVIEW_STACK_DEPTH + "\n" +
                    "MAX_PROJECTION_STACK_DEPTH=" + GL_MAX_PROJECTION_STACK_DEPTH + "\n" +
                    "MAX_TEXTURE_STACK_DEPTH=" + GL_MAX_TEXTURE_STACK_DEPTH + "\n" +
                    "MAX_TEXTURE_SIZE=" + GL_MAX_TEXTURE_SIZE + "\n" +
                    "DEPTH_BITS=" + GL_DEPTH_BITS + "\n" +
                    "STENCIL_BITS=" + GL_STENCIL_BITS;
        }
    }

    // endregion

    // region OpenGLGles20

    public static class OpenGLGles20Info extends OpenGLInfo {

        public String GL_RENDERER;
        public String GL_VENDOR;
        public String GL_VERSION;
        public int GL_MAX_VERTEX_ATTRIBS;
        public int GL_MAX_VERTEX_UNIFORM_VECTORS;
        public int GL_MAX_FRAGMENT_UNIFORM_VECTORS;
        public int GL_MAX_VARYING_VECTORS;
        public boolean Vertex_Texture_Fetch;
        public int GL_MAX_TEXTURE_IMAGE_UNITS;
        public int[] GL_MAX_VIEWPORT_DIMS;

        public OpenGLGles20Info() {
            super(Device.supportsOpenGLES2() ? 2 : 1);
        }

        public void loadOncreate() {
            GL_RENDERER = glGetString(GLES10.GL_RENDERER);
            GL_VENDOR = glGetString(GLES10.GL_VENDOR);
            GL_VERSION = glGetString(GLES10.GL_VERSION);
            GL_MAX_VERTEX_ATTRIBS = glGetIntegerv(GLES20.GL_MAX_VERTEX_ATTRIBS);
            GL_MAX_VERTEX_UNIFORM_VECTORS = glGetIntegerv(GLES20.GL_MAX_VERTEX_UNIFORM_VECTORS);
            GL_MAX_FRAGMENT_UNIFORM_VECTORS = glGetIntegerv(GLES20.GL_MAX_FRAGMENT_UNIFORM_VECTORS);
            GL_MAX_VARYING_VECTORS = glGetIntegerv(GLES20.GL_MAX_VARYING_VECTORS);
            Vertex_Texture_Fetch = isVTFSupported();
            GL_MAX_TEXTURE_IMAGE_UNITS = glGetIntegerv(GLES20.GL_MAX_TEXTURE_IMAGE_UNITS);
            GL_MAX_VIEWPORT_DIMS = glGetIntegerv(GLES10.GL_MAX_VIEWPORT_DIMS, 2);
        }

        @Override
        public String toString() {
            return "RENDERER='" + GL_RENDERER + '\'' + "\n" +
                    "VENDOR='" + GL_VENDOR + '\'' + "\n" +
                    "VERSION='" + GL_VERSION + '\'' + "\n" +
                    "MAX_VERTEX_ATTRIBS=" + GL_MAX_VERTEX_ATTRIBS + "\n" +
                    "MAX_VERTEX_UNIFORM_VECTORS=" + GL_MAX_VERTEX_UNIFORM_VECTORS + "\n" +
                    "MAX_FRAGMENT_UNIFORM_VECTORS=" + GL_MAX_FRAGMENT_UNIFORM_VECTORS + "\n" +
                    "MAX_VARYING_VECTORS=" + GL_MAX_VARYING_VECTORS + "\n" +
                    "Vertex_Texture_Fetch=" + Vertex_Texture_Fetch + "\n" +
                    "MAX_TEXTURE_IMAGE_UNITS=" + GL_MAX_TEXTURE_IMAGE_UNITS + "\n" +
                    "MAX_VIEWPORT_DIMS=" + GL_MAX_VIEWPORT_DIMS[0] + "x" + GL_MAX_VIEWPORT_DIMS[1];
        }
    }

    // endregion

    // region glGet

    private volatile static IntBuffer buffer = IntBuffer.allocate(1);
    private volatile static int[] arrayBuffer = new int[1];

    public synchronized static String glGetString(int value) {
        return GLES10.glGetString(value);
    }

    public synchronized static int glGetIntegerv(int value) {
        buffer = IntBuffer.allocate(1);
        GLES10.glGetIntegerv(value, buffer);
        return buffer.get(0);
    }

    public synchronized static int[] glGetIntegerv(int value, int size) {
        final IntBuffer buffer = IntBuffer.allocate(size);
        GLES10.glGetIntegerv(value, buffer);
        return buffer.array();
    }

    public synchronized static int glGetIntegerv(GL10 gl, int value) {
        buffer = IntBuffer.allocate(1);
        gl.glGetIntegerv(value, buffer);
        return buffer.get(0);
    }

    public static boolean isVTFSupported() {
        GLES10.glGetIntegerv(GLES20.GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS, arrayBuffer, 0);
        return arrayBuffer[0] != 0;
    }

    // endregion

    // region GPU

    public void loadOpenGLGles10Info(final OnCompleteCallback<OpenGLGles10Info> callback) {
        new InfoLoader<OpenGLGles10Info>(new OpenGLGles10Info()).loadInfo(callback);
    }

    public void loadOpenGLGles20Info(final OnCompleteCallback<OpenGLGles20Info> callback) {
        new InfoLoader<OpenGLGles20Info>(new OpenGLGles20Info()).loadInfo(callback);
    }

    // endregion

    // region interfaces

    public interface OnCompleteCallback<T> {
        void onComplete(final T result);
    }

    private static abstract class OpenGLInfo {
        public final int eGLContextClientVersion;
        public OpenGLInfo(final int eGLContextClientVersion) {
            this.eGLContextClientVersion = eGLContextClientVersion;
        }

        protected abstract void loadOncreate();
    }

    // endregion

    // region InfoLoader

    public static class InfoLoader<T extends OpenGLInfo> {

        private volatile GPURenderer<T> renderer;
        private T info;

        public InfoLoader(final T info) {
            this.info = info;
        }

        public void loadInfo(final OnCompleteCallback<T> callback) {

            Device.context().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // new view
                    final GLSurfaceView glView = new GLSurfaceView(Device.context());

                    // egl info
                    glView.setEGLConfigChooser(true);

                    // need to be on top to be rendered at least for one frame
                    glView.setZOrderOnTop(true);

                    // create new renderer; note: we only need the oncreate method for all the infos
                    renderer = new GPURenderer<T>(glView, info, callback);

                    // set opengl version
                    glView.setEGLContextClientVersion(info.eGLContextClientVersion);

                    // set renderer
                    glView.setRenderer(renderer);

                    // add opengl view to current active view
                    final FrameLayout layout = (FrameLayout) Device.context().findViewById(android.R.id.content); // Note: needs to be layout of current active view
                    layout.addView(glView);
                }
            });
        }
    }

    // endregion

    // region GPURenderer

    private static class GPURenderer<T extends OpenGLInfo> implements  GLSurfaceView.Renderer {

        private GLSurfaceView glSurfaceView;
        private T result;
        private OnCompleteCallback<T> callback;

        protected GPURenderer(final GLSurfaceView glSurfaceView, final T result, final OnCompleteCallback<T> callback) {
            this.result = result;
            this.glSurfaceView = glSurfaceView;
            this.callback = callback;
        }

        @Override
        public void onSurfaceCreated(final GL10 gl, final EGLConfig eglConfig) {

            // loadOncreate info
            result.loadOncreate();

            // remove view
            Device.context().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    final FrameLayout layout = (FrameLayout) Device.context().findViewById(android.R.id.content);
                    layout.removeView(glSurfaceView);

                    // call callback
                    if(callback != null) callback.onComplete(result);
                }
            });
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

    // endregion
}