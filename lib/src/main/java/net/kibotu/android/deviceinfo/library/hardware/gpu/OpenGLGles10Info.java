package net.kibotu.android.deviceinfo.library.hardware.gpu;

import android.opengl.GLES10;

import java.util.Arrays;

import static net.kibotu.android.deviceinfo.library.hardware.gpu.OpenGLExtensions.glGetIntegerv;
import static net.kibotu.android.deviceinfo.library.hardware.gpu.OpenGLExtensions.glGetString;

public class OpenGLGles10Info extends OpenGLInfo {

    // general
    public String GL_RENDERER;
    public String GL_VERSION;
    public String GL_VENDOR;

    // texture related
    public int GL_MAX_TEXTURE_SIZE;
    public int GL_MAX_TEXTURE_UNITS;
    public int[] GL_MAX_VIEWPORT_DIMS;

    // fixed function pipeline constrains
    public int GL_MAX_MODELVIEW_STACK_DEPTH;
    public int GL_MAX_PROJECTION_STACK_DEPTH;
    public int GL_MAX_TEXTURE_STACK_DEPTH;
    public int GL_MAX_LIGHTS;
    public int GL_SUBPIXEL_BITS;
    public int GL_DEPTH_BITS;
    public int GL_STENCIL_BITS;
    public int GL_MAX_ELEMENTS_INDICES;
    public int GL_MAX_ELEMENTS_VERTICES;

    // extensions
    public String GL_EXTENSIONS;

    public OpenGLGles10Info() {
        super(1);
    }

    @Override
    protected void loadOnCreate() {
        GL_RENDERER = glGetString(GLES10.GL_RENDERER);
        GL_VERSION = glGetString(GLES10.GL_VERSION);
        GL_VENDOR = glGetString(GLES10.GL_VENDOR);
        GL_MAX_TEXTURE_SIZE = OpenGLExtensions.glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE);
        GL_MAX_TEXTURE_UNITS = OpenGLExtensions.glGetIntegerv(GLES10.GL_MAX_TEXTURE_UNITS);
        GL_MAX_LIGHTS = OpenGLExtensions.glGetIntegerv(GLES10.GL_MAX_LIGHTS);
        GL_SUBPIXEL_BITS = OpenGLExtensions.glGetIntegerv(GLES10.GL_SUBPIXEL_BITS);
        GL_MAX_ELEMENTS_VERTICES = OpenGLExtensions.glGetIntegerv(GLES10.GL_MAX_ELEMENTS_VERTICES);
        GL_MAX_ELEMENTS_INDICES = OpenGLExtensions.glGetIntegerv(GLES10.GL_MAX_ELEMENTS_INDICES);
        GL_MAX_MODELVIEW_STACK_DEPTH = OpenGLExtensions.glGetIntegerv(GLES10.GL_MAX_MODELVIEW_STACK_DEPTH);
        GL_MAX_PROJECTION_STACK_DEPTH = OpenGLExtensions.glGetIntegerv(GLES10.GL_MAX_PROJECTION_STACK_DEPTH);
        GL_MAX_TEXTURE_STACK_DEPTH = OpenGLExtensions.glGetIntegerv(GLES10.GL_MAX_TEXTURE_STACK_DEPTH);
        GL_DEPTH_BITS = OpenGLExtensions.glGetIntegerv(GLES10.GL_DEPTH_BITS);
        GL_STENCIL_BITS = OpenGLExtensions.glGetIntegerv(GLES10.GL_STENCIL_BITS);
        GL_EXTENSIONS = glGetString(GLES10.GL_EXTENSIONS);
        GL_MAX_VIEWPORT_DIMS = glGetIntegerv(GLES10.GL_MAX_VIEWPORT_DIMS, 2);
    }

    @Override
    public String toString() {
        return "OpenGLGles10Info{" + '\n' +
                "GL_RENDERER='" + GL_RENDERER + '\'' + '\n' +
                ", GL_VERSION='" + GL_VERSION + '\'' + '\n' +
                ", GL_VENDOR='" + GL_VENDOR + '\'' + '\n' +
                ", GL_MAX_TEXTURE_SIZE=" + GL_MAX_TEXTURE_SIZE + '\n' +
                ", GL_MAX_TEXTURE_UNITS=" + GL_MAX_TEXTURE_UNITS + '\n' +
                ", GL_MAX_VIEWPORT_DIMS=" + Arrays.toString(GL_MAX_VIEWPORT_DIMS) + '\n' +
                ", GL_MAX_MODELVIEW_STACK_DEPTH=" + GL_MAX_MODELVIEW_STACK_DEPTH + '\n' +
                ", GL_MAX_PROJECTION_STACK_DEPTH=" + GL_MAX_PROJECTION_STACK_DEPTH + '\n' +
                ", GL_MAX_TEXTURE_STACK_DEPTH=" + GL_MAX_TEXTURE_STACK_DEPTH +
                ", GL_MAX_LIGHTS=" + GL_MAX_LIGHTS + '\n' +
                ", GL_SUBPIXEL_BITS=" + GL_SUBPIXEL_BITS + '\n' +
                ", GL_DEPTH_BITS=" + GL_DEPTH_BITS + '\n' +
                ", GL_STENCIL_BITS=" + GL_STENCIL_BITS + '\n' +
                ", GL_MAX_ELEMENTS_INDICES=" + GL_MAX_ELEMENTS_INDICES + '\n' +
                ", GL_MAX_ELEMENTS_VERTICES=" + GL_MAX_ELEMENTS_VERTICES + '\n' +
                ", GL_EXTENSIONS='" + GL_EXTENSIONS + '\'' + '\n' +
                '}';
    }
}