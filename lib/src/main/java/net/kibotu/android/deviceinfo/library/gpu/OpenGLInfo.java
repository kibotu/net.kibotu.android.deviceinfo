package net.kibotu.android.deviceinfo.library.gpu;

import java.util.ArrayList;

import static net.kibotu.android.deviceinfo.library.services.SystemService.getActivityManager;

public abstract class OpenGLInfo {

    final int eGLContextClientVersion;
    public ArrayList<Egl> eglconfigs;

    protected OpenGLInfo(final int eGLContextClientVersion) {
        this.eGLContextClientVersion = eGLContextClientVersion;
        eglconfigs = new ArrayList<>();
    }

    /**
     * Basically run this in a successfully running GLSurfaceView.Renderer.onSurfaceCreated.
     * Guaranties valid OpenGL context.
     */
    protected abstract void loadOnCreate();
}