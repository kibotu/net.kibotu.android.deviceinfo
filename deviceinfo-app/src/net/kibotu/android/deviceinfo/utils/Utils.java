package net.kibotu.android.deviceinfo.utils;

import net.kibotu.android.deviceinfo.GPU;
import net.kibotu.android.deviceinfo.Logger;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

final public class Utils {

    public static final long BYTES_TO_KB = 1024;
    public static final long BYTES_TO_MB = BYTES_TO_KB * 1024;
    public static final long BYTES_TO_GB = BYTES_TO_MB * 1024;
    public static final long BYTES_TO_TB = BYTES_TO_GB * 1024;

    private Utils() throws IllegalAccessException {
        throw new IllegalAccessException("'Utils' cannot be instantiated.");
    }

    public static String jsonArrayToString(final JSONArray array) {
        final StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < array.length(); ++i) {
            try {
                buffer.append(array.getString(i)).append("\n");
            } catch (JSONException e) {
                Logger.e("" + e.getMessage(), e);
            }
        }

        return buffer.toString();
    }

    public static String formatBytes(long bytes) {
        if (bytes == 0)
            return "";

        return bytes / BYTES_TO_TB > 0 ? String.format("%.2f TB [%d bytes]", bytes / (float) BYTES_TO_TB, bytes) :
                bytes / BYTES_TO_GB > 0 ? String.format("%.2f GB [%d bytes]", bytes / (float) BYTES_TO_GB, bytes) :
                        bytes / BYTES_TO_MB > 0 ? String.format("%.2f MB [%d bytes]", bytes / (float) BYTES_TO_MB, bytes) :
                                bytes / BYTES_TO_KB > 0 ? String.format("%.2f KB [%d bytes]", bytes / (float) BYTES_TO_KB, bytes) : bytes + " bytes";
    }

    public static String formatFrequency(int clockHz) {
        return clockHz < 1000 * 1000 ? (clockHz / 1000) + " MHz" : (clockHz / 1000 / 1000) + "." + (clockHz / 1000 / 100) % 10 + " GHz";
    }


    public static void killCpu() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int x = 0;
                for (int i = 0; i < Integer.MAX_VALUE; ++i) {
                    ++x;
                }
            }
        }).start();
    }

    public static String getSensorName(int type) {

        String name = "" + type;

        switch (type) {
            case 1:
                name = "TYPE_ACCELEROMETER";
            case 2:
                name = "TYPE_MAGNETIC_FIELD";
            case 3:
                name = "TYPE_ORIENTATION";
            case 4:
                name = "TYPE_GYROSCOPE";
            case 5:
                name = "TYPE_LIGHT";
            case 6:
                name = "TYPE_PRESSURE";
            case 7:
                name = "TYPE_TEMPERATURE";
            case 8:
                name = "TYPE_PROXIMITY";
            case 9:
                name = "TYPE_GRAVITY";
            case 10:
                name = "TYPE_LINEAR_ACCELERATION";
            case 11:
                name = "TYPE_ROTATION_VECTOR";
        }

        return name;
    }

    public synchronized static <T extends Comparable<? super T>> List<T> asSortedList(final Collection<T> c) {
        final List<T> list = new ArrayList<T>(c);
        Collections.sort(list);
        return list;
    }

    public static String t(final int amountTabs) {
        if (amountTabs < 1) return "";
        String ret = "\t";
        for (int i = 1; i < amountTabs; ++i)
            ret += "\t";
        return ret;
    }

    public synchronized static String formatOpenGles20info(final GPU.OpenGLGles20Info i) {
        final StringBuffer buffer = new StringBuffer();

        // general
        buffer.append("General\n\n");
        buffer.append("Renderer:").append(t(4)).append(i.GL_RENDERER).append("\n");
        buffer.append("Version:").append(t(5)).append(i.GL_VERSION).append("\n");
        buffer.append("Vendor:").append(t(5)).append(i.GL_VENDOR).append("\n");
        buffer.append("GLSL Version:").append(t(2)).append(i.GL_SHADING_LANGUAGE_VERSION).append("\n\n");

        // texture related
        buffer.append("Textures\n\n");
        buffer.append("Max Texture Size:").append(t(6)).append(i.GL_MAX_TEXTURE_SIZE).append("x").append(i.GL_MAX_TEXTURE_SIZE).append("\n");
        buffer.append("Max Texture Units:").append(t(6)).append(i.GL_MAX_TEXTURE_UNITS).append("\n");
        buffer.append("Max Texture Image Units:").append(t(3)).append(i.GL_MAX_TEXTURE_IMAGE_UNITS).append("\n");
        buffer.append("Max Combined Texture Units:").append(t(1)).append(i.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS).append("\n");
        buffer.append("Max Cube Map Texture Size:").append(t(1)).append(i.GL_MAX_CUBE_MAP_TEXTURE_SIZE).append("x").append(i.GL_MAX_CUBE_MAP_TEXTURE_SIZE).append("\n");
        buffer.append("Max Vertex Texture Images:").append(t(1)).append(i.GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS).append("\n");
        buffer.append("Vertex Texture Fetch:").append(t(5)).append(i.Vertex_Texture_Fetch ? "Yes" : "No").append("\n");
        buffer.append("Max Viewport Dimension:").append(t(3)).append(i.GL_MAX_VIEWPORT_DIMS[0]).append("x").append(i.GL_MAX_VIEWPORT_DIMS[0]).append("\n");
        buffer.append("Max Renderbuffer Size:").append(t(4)).append(i.GL_MAX_RENDERBUFFER_SIZE).append("x").append(i.GL_MAX_RENDERBUFFER_SIZE).append("\n\n");

        // shader constrains
        buffer.append("Attributes\n\n");
        buffer.append("Max Vertex Uniform Vectors:").append(t(3)).append(i.GL_MAX_VERTEX_UNIFORM_VECTORS).append("\n");
        buffer.append("Max Vertex Attributes:").append(t(6)).append(i.GL_MAX_VERTEX_ATTRIBS).append("\n");
        buffer.append("Max Varying Vectors:").append(t(7)).append(i.GL_MAX_VARYING_VECTORS).append("\n");
        buffer.append("Max Fragment Uniform Vectors:").append(t(2)).append(i.GL_MAX_FRAGMENT_UNIFORM_VECTORS).append("\n\n");

        // compressed texture formats
        buffer.append("Compressed Texture Formats\n\n");

        final String[] token = i.GL_EXTENSIONS.split("_");
        for (int j = 0; j < token.length; ++j)
            if (token[j].equalsIgnoreCase("compressed"))
                buffer.append(firstLetterToUpperCase(token[j + 1])).append("\n");

        // extensions
        buffer.append("\nGL Extensions\n\n").append(i.GL_EXTENSIONS).append("\n\n");

        // precision [] { -range, range, precision }
        buffer.append("Vertex Numeric Precision\n\n");
        buffer.append(appendGLInfoArray("Low Int" + t(4), i.GL_VERTEX_SHADER_GL_LOW_INT)).append("\n");
        buffer.append(appendGLInfoArray("Medium Int" + t(2), i.GL_VERTEX_SHADER_GL_MEDIUM_INT)).append("\n");
        buffer.append(appendGLInfoArray("High Int" + t(4), i.GL_VERTEX_SHADER_GL_HIGH_INT)).append("\n");
        buffer.append(appendGLInfoArray("Low Float" + t(3), i.GL_VERTEX_SHADER_GL_LOW_FLOAT)).append("\n");
        buffer.append(appendGLInfoArray("Medium Float" + t(1), i.GL_VERTEX_SHADER_GL_MEDIUM_FLOAT)).append("\n");
        buffer.append(appendGLInfoArray("High Float" + t(3), i.GL_VERTEX_SHADER_GL_HIGH_FLOAT)).append("\n\n");

        buffer.append("Fragment Numeric Precision\n\n");
        buffer.append(appendGLInfoArray("Low Int" + t(4), i.GL_FRAGMENT_SHADER_GL_LOW_INT)).append("\n");
        buffer.append(appendGLInfoArray("Medium Int" + t(2), i.GL_FRAGMENT_SHADER_GL_MEDIUM_INT)).append("\n");
        buffer.append(appendGLInfoArray("High Int" + t(4), i.GL_FRAGMENT_SHADER_GL_HIGH_INT)).append("\n");
        buffer.append(appendGLInfoArray("Low Float" + t(3), i.GL_FRAGMENT_SHADER_GL_LOW_FLOAT)).append("\n");
        buffer.append(appendGLInfoArray("Medium Float" + t(1), i.GL_FRAGMENT_SHADER_GL_MEDIUM_FLOAT)).append("\n");
        buffer.append(appendGLInfoArray("High Float" + t(3), i.GL_FRAGMENT_SHADER_GL_HIGH_FLOAT)).append("\n");

        return buffer.toString();
    }

    private synchronized static String appendGLInfoArray(final String name, final int[] a) {
        return a[0] + a[1] + a[2] == 0 ? name + "Not Available." : name + "[-2^" + a[0] + ", 2^" + a[1] + "]" + (a[2] > 0 ? ", 2^" + a[2] : "");
    }

    public synchronized static String formatOpenGles10info(final GPU.OpenGLGles10Info i) {
        final StringBuffer buffer = new StringBuffer();

        // general
        buffer.append("General\n\n");
        buffer.append("Renderer:").append(t(4)).append(i.GL_RENDERER).append("\n");
        buffer.append("Version:").append(t(5)).append(i.GL_VERSION).append("\n");
        buffer.append("Vendor:").append(t(5)).append(i.GL_VENDOR).append("\n\n");

        // texture related
        buffer.append("Textures\n\n");
        buffer.append("Max Texture Size:").append(t(6)).append(i.GL_MAX_TEXTURE_SIZE).append("x").append(i.GL_MAX_TEXTURE_SIZE).append("\n");
        buffer.append("Max Texture Units:").append(t(6)).append(i.GL_MAX_TEXTURE_UNITS).append("\n");
        buffer.append("Max Viewport Dimension:").append(t(3)).append(i.GL_MAX_VIEWPORT_DIMS[0]).append("x").append(i.GL_MAX_VIEWPORT_DIMS[0]).append("\n\n");

        // fixed function pipeline constrains
        buffer.append("Fixed Function Pipeline Constrains\n\n");
        buffer.append("Max ModelView Stack Depth:").append(t(2)).append(i.GL_MAX_MODELVIEW_STACK_DEPTH).append("\n");
        buffer.append("Max Projection Stack Depth:").append(t(2)).append(i.GL_MAX_PROJECTION_STACK_DEPTH).append("\n");
        buffer.append("Max Texture Stack Depth:").append(t(3)).append(i.GL_MAX_TEXTURE_STACK_DEPTH).append("\n");
        buffer.append("Max Lights:").append(t(10)).append(i.GL_MAX_LIGHTS).append("\n");
        buffer.append("Max Depth Bits:").append(t(8)).append(i.GL_DEPTH_BITS).append("\n");
        buffer.append("Max Stencil Bits:").append(t(8)).append(i.GL_STENCIL_BITS).append("\n");
        buffer.append("Max Subpixel Bits:").append(t(7)).append(i.GL_SUBPIXEL_BITS).append("\n");
        buffer.append("Max Element Indices:").append(t(5)).append(i.GL_MAX_ELEMENTS_INDICES).append("\n");
        buffer.append("Max Element Vertices:").append(t(5)).append(i.GL_MAX_ELEMENTS_VERTICES).append("\n\n");

        // compressed texture formats
        buffer.append("Compressed Texture Formats\n\n");

        final String[] token = i.GL_EXTENSIONS.split("_");
        for (int j = 0; j < token.length; ++j)
            if (token[j].equalsIgnoreCase("compressed"))
                buffer.append(firstLetterToUpperCase(token[j + 1])).append("\n");

        // extensions
        buffer.append("\nGL Extensions\n\n").append(i.GL_EXTENSIONS).append("\n\n");

        return buffer.toString();
    }

    public synchronized static String firstLetterToUpperCase(final String word) {
        return Character.toString(word.charAt(0)).toUpperCase() + word.substring(1);
    }
}
