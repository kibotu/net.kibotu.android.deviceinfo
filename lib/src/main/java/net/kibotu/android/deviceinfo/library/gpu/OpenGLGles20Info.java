package net.kibotu.android.deviceinfo.library.gpu;

import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLES30;

import java.util.Arrays;

import static android.opengl.GLES20.*;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR2;
import static net.kibotu.android.deviceinfo.library.gpu.OpenGLExtensions.*;
import static net.kibotu.android.deviceinfo.library.version.Version.isAtLeastVersion;

public class OpenGLGles20Info extends OpenGLInfo {

    // general
    public String GL_RENDERER;
    public String GL_VERSION;
    public String GL_VENDOR;
    public String GL_SHADING_LANGUAGE_VERSION;

    // texture related
    public int GL_MAX_TEXTURE_SIZE;
    public int GL_MAX_TEXTURE_UNITS;
    public int GL_MAX_TEXTURE_IMAGE_UNITS;
    public int GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS;
    public int GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS;
    public int GL_MAX_CUBE_MAP_TEXTURE_SIZE;
    public int[] GL_MAX_VIEWPORT_DIMS;
    public int GL_MAX_RENDERBUFFER_SIZE;
    public boolean Vertex_Texture_Fetch;

    // shader constrains
    public int GL_MAX_VERTEX_UNIFORM_VECTORS;
    public int GL_MAX_VERTEX_ATTRIBS;
    public int GL_MAX_VARYING_VECTORS;
    public int GL_MAX_FRAGMENT_UNIFORM_VECTORS;

    // extensions
    public String GL_EXTENSIONS;

    // precision [] { -range, range, precision }
    public int[] GL_VERTEX_SHADER_GL_LOW_INT;
    public int[] GL_VERTEX_SHADER_GL_MEDIUM_INT;
    public int[] GL_VERTEX_SHADER_GL_HIGH_INT;
    public int[] GL_VERTEX_SHADER_GL_LOW_FLOAT;
    public int[] GL_VERTEX_SHADER_GL_MEDIUM_FLOAT;
    public int[] GL_VERTEX_SHADER_GL_HIGH_FLOAT;

    public int[] GL_FRAGMENT_SHADER_GL_LOW_INT;
    public int[] GL_FRAGMENT_SHADER_GL_MEDIUM_INT;
    public int[] GL_FRAGMENT_SHADER_GL_HIGH_INT;
    public int[] GL_FRAGMENT_SHADER_GL_LOW_FLOAT;
    public int[] GL_FRAGMENT_SHADER_GL_MEDIUM_FLOAT;
    public int[] GL_FRAGMENT_SHADER_GL_HIGH_FLOAT;

    public OpenGLGles20Info() {
        super(supportsOpenGLES2()
                ? 2
                : 1);
    }

    @Override
    protected void loadOnCreate() {

        GL_RENDERER = OpenGLExtensions.glGetString(GLES10.GL_RENDERER);
        GL_VERSION = OpenGLExtensions.glGetString(GLES10.GL_VERSION);
        GL_VENDOR = OpenGLExtensions.glGetString(GLES10.GL_VENDOR);
        GL_SHADING_LANGUAGE_VERSION = OpenGLExtensions.glGetString(GLES20.GL_SHADING_LANGUAGE_VERSION);

        GL_MAX_TEXTURE_SIZE = glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE);
        GL_MAX_TEXTURE_UNITS = glGetIntegerv(GLES10.GL_MAX_TEXTURE_UNITS);
        GL_MAX_VERTEX_ATTRIBS = glGetIntegerv(GLES20.GL_MAX_VERTEX_ATTRIBS);
        GL_MAX_VERTEX_UNIFORM_VECTORS = glGetIntegerv(GLES20.GL_MAX_VERTEX_UNIFORM_VECTORS);
        GL_MAX_FRAGMENT_UNIFORM_VECTORS = glGetIntegerv(GLES20.GL_MAX_FRAGMENT_UNIFORM_VECTORS);
        GL_MAX_VARYING_VECTORS = glGetIntegerv(GLES20.GL_MAX_VARYING_VECTORS);
        Vertex_Texture_Fetch = isVTFSupported();
        GL_MAX_TEXTURE_IMAGE_UNITS = glGetIntegerv(GLES20.GL_MAX_TEXTURE_IMAGE_UNITS);
        GL_MAX_VIEWPORT_DIMS = glGetIntegerv(GLES10.GL_MAX_VIEWPORT_DIMS, 2);

        if (isAtLeastVersion(JELLY_BEAN_MR2) && supportsOpenGLES3()) {
            int n = glGetIntegerv(GLES30.GL_NUM_EXTENSIONS);
            for (int i = 0; i < n; i++) {
                GL_EXTENSIONS += glGetStringi(GLES30.GL_EXTENSIONS, i);
            }
        } else
            GL_EXTENSIONS = OpenGLExtensions.glGetString(GLES10.GL_EXTENSIONS);

        GL_MAX_RENDERBUFFER_SIZE = glGetIntegerv(GLES20.GL_MAX_RENDERBUFFER_SIZE);
        GL_MAX_CUBE_MAP_TEXTURE_SIZE = glGetIntegerv(GLES20.GL_MAX_CUBE_MAP_TEXTURE_SIZE);
        GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS = glGetIntegerv(GLES20.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS);
        GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS = glGetIntegerv(GLES20.GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS);

        GL_VERTEX_SHADER_GL_LOW_INT = glGetShaderPrecisionFormat(GL_VERTEX_SHADER, GL_LOW_INT);
        GL_VERTEX_SHADER_GL_MEDIUM_INT = glGetShaderPrecisionFormat(GL_VERTEX_SHADER, GL_MEDIUM_INT);
        GL_VERTEX_SHADER_GL_HIGH_INT = glGetShaderPrecisionFormat(GL_VERTEX_SHADER, GL_HIGH_INT);

        GL_VERTEX_SHADER_GL_LOW_FLOAT = glGetShaderPrecisionFormat(GL_VERTEX_SHADER, GL_LOW_FLOAT);
        GL_VERTEX_SHADER_GL_MEDIUM_FLOAT = glGetShaderPrecisionFormat(GL_VERTEX_SHADER, GL_MEDIUM_FLOAT);
        GL_VERTEX_SHADER_GL_HIGH_FLOAT = glGetShaderPrecisionFormat(GL_VERTEX_SHADER, GL_HIGH_FLOAT);

        GL_FRAGMENT_SHADER_GL_LOW_INT = glGetShaderPrecisionFormat(GL_FRAGMENT_SHADER, GL_LOW_INT);
        GL_FRAGMENT_SHADER_GL_MEDIUM_INT = glGetShaderPrecisionFormat(GL_FRAGMENT_SHADER, GL_MEDIUM_INT);
        GL_FRAGMENT_SHADER_GL_HIGH_INT = glGetShaderPrecisionFormat(GL_FRAGMENT_SHADER, GL_HIGH_INT);

        GL_FRAGMENT_SHADER_GL_LOW_FLOAT = glGetShaderPrecisionFormat(GL_FRAGMENT_SHADER, GL_LOW_FLOAT);
        GL_FRAGMENT_SHADER_GL_MEDIUM_FLOAT = glGetShaderPrecisionFormat(GL_FRAGMENT_SHADER, GL_MEDIUM_FLOAT);
        GL_FRAGMENT_SHADER_GL_HIGH_FLOAT = glGetShaderPrecisionFormat(GL_FRAGMENT_SHADER, GL_HIGH_FLOAT);
    }

    @Override
    public String toString() {
        return "OpenGLGles20Info{" +
                "GL_RENDERER='" + GL_RENDERER + '\'' +
                ", GL_VERSION='" + GL_VERSION + '\'' +
                ", GL_VENDOR='" + GL_VENDOR + '\'' +
                ", GL_SHADING_LANGUAGE_VERSION='" + GL_SHADING_LANGUAGE_VERSION + '\'' +
                ", GL_MAX_TEXTURE_SIZE=" + GL_MAX_TEXTURE_SIZE +
                ", GL_MAX_TEXTURE_UNITS=" + GL_MAX_TEXTURE_UNITS +
                ", GL_MAX_TEXTURE_IMAGE_UNITS=" + GL_MAX_TEXTURE_IMAGE_UNITS +
                ", GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS=" + GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS +
                ", GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS=" + GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS +
                ", GL_MAX_CUBE_MAP_TEXTURE_SIZE=" + GL_MAX_CUBE_MAP_TEXTURE_SIZE +
                ", GL_MAX_VIEWPORT_DIMS=" + Arrays.toString(GL_MAX_VIEWPORT_DIMS) +
                ", GL_MAX_RENDERBUFFER_SIZE=" + GL_MAX_RENDERBUFFER_SIZE +
                ", Vertex_Texture_Fetch=" + Vertex_Texture_Fetch +
                ", GL_MAX_VERTEX_UNIFORM_VECTORS=" + GL_MAX_VERTEX_UNIFORM_VECTORS +
                ", GL_MAX_VERTEX_ATTRIBS=" + GL_MAX_VERTEX_ATTRIBS +
                ", GL_MAX_VARYING_VECTORS=" + GL_MAX_VARYING_VECTORS +
                ", GL_MAX_FRAGMENT_UNIFORM_VECTORS=" + GL_MAX_FRAGMENT_UNIFORM_VECTORS +
                ", GL_EXTENSIONS='" + GL_EXTENSIONS + '\'' +
                ", GL_VERTEX_SHADER_GL_LOW_INT=" + Arrays.toString(GL_VERTEX_SHADER_GL_LOW_INT) +
                ", GL_VERTEX_SHADER_GL_MEDIUM_INT=" + Arrays.toString(GL_VERTEX_SHADER_GL_MEDIUM_INT) +
                ", GL_VERTEX_SHADER_GL_HIGH_INT=" + Arrays.toString(GL_VERTEX_SHADER_GL_HIGH_INT) +
                ", GL_VERTEX_SHADER_GL_LOW_FLOAT=" + Arrays.toString(GL_VERTEX_SHADER_GL_LOW_FLOAT) +
                ", GL_VERTEX_SHADER_GL_MEDIUM_FLOAT=" + Arrays.toString(GL_VERTEX_SHADER_GL_MEDIUM_FLOAT) +
                ", GL_VERTEX_SHADER_GL_HIGH_FLOAT=" + Arrays.toString(GL_VERTEX_SHADER_GL_HIGH_FLOAT) +
                ", GL_FRAGMENT_SHADER_GL_LOW_INT=" + Arrays.toString(GL_FRAGMENT_SHADER_GL_LOW_INT) +
                ", GL_FRAGMENT_SHADER_GL_MEDIUM_INT=" + Arrays.toString(GL_FRAGMENT_SHADER_GL_MEDIUM_INT) +
                ", GL_FRAGMENT_SHADER_GL_HIGH_INT=" + Arrays.toString(GL_FRAGMENT_SHADER_GL_HIGH_INT) +
                ", GL_FRAGMENT_SHADER_GL_LOW_FLOAT=" + Arrays.toString(GL_FRAGMENT_SHADER_GL_LOW_FLOAT) +
                ", GL_FRAGMENT_SHADER_GL_MEDIUM_FLOAT=" + Arrays.toString(GL_FRAGMENT_SHADER_GL_MEDIUM_FLOAT) +
                ", GL_FRAGMENT_SHADER_GL_HIGH_FLOAT=" + Arrays.toString(GL_FRAGMENT_SHADER_GL_HIGH_FLOAT) +
                '}';
    }
}