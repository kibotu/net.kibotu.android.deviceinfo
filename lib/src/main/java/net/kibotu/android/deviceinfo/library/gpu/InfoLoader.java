package net.kibotu.android.deviceinfo.library.gpu;

import android.opengl.GLSurfaceView;
import android.view.ViewGroup;
import net.kibotu.android.deviceinfo.library.Device;
import net.kibotu.android.deviceinfo.library.misc.Callback;

import static net.kibotu.android.deviceinfo.library.Device.getContext;

public class InfoLoader<T extends OpenGLInfo> {

    private volatile GPURenderer<T> renderer;
    private T info;

    public InfoLoader(final T info) {
        this.info = info;
    }

    public void loadInfo(final Callback<T> callback) {

        // new view
        final GLSurfaceView glView = new GLSurfaceView(getContext());

        // egl info
        glView.setEGLConfigChooser(new EglChooser<T>(info));

        // need to be on top to be rendered at least for one frame
        glView.setZOrderOnTop(true);

        // create new renderer; note: we only need the oncreate method for all the infos
        renderer = new GPURenderer<T>(glView, info, callback);

        // set opengl version
        glView.setEGLContextClientVersion(info.eGLContextClientVersion);

        // set renderer
        glView.setRenderer(renderer);

        // add opengl view to current active view
        final ViewGroup layout = (ViewGroup) Device.getContentRootView(); // Note: needs to be layout of current active view
        layout.addView(glView);
    }
}