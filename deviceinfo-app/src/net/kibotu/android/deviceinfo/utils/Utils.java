package net.kibotu.android.deviceinfo.utils;

import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.view.Surface;
import net.kibotu.android.deviceinfo.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

final public class Utils {

    public static final long BYTES_TO_KB = 1024;
    public static final long BYTES_TO_MB = BYTES_TO_KB * 1024;
    public static final long BYTES_TO_GB = BYTES_TO_MB * 1024;
    public static final long BYTES_TO_TB = BYTES_TO_GB * 1024;
    public static final String BR = "<br>\n";

    private Utils() throws IllegalAccessException {
        throw new IllegalAccessException("'Utils' cannot be instantiated.");
    }

    public static String jsonArrayToString(final JSONArray array) {
        final StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < array.length(); ++i) {
            try {
                buffer.append(array.getString(i)).append("\n");
            } catch (final JSONException e) {
                Logger.e(e);
            }
        }

        return buffer.toString();
    }

    // alternatve to Formatter.formatFileSize which doesn't show bytes and rounds to int
    public static String formatBytes(final long bytes) {
        if (bytes <= 0)
            return "0 bytes";

        return bytes / BYTES_TO_TB > 0 ? String.format("%.2f TB", bytes / (float) BYTES_TO_TB) :
                bytes / BYTES_TO_GB > 0 ? String.format("%.2f GB", bytes / (float) BYTES_TO_GB) :
                        bytes / BYTES_TO_MB > 0 ? String.format("%.2f MB", bytes / (float) BYTES_TO_MB) :
                                bytes / BYTES_TO_KB > 0 ? String.format("%.2f KB", bytes / (float) BYTES_TO_KB) : bytes + " bytes";
    }

    public static String formatFrequency(final int clockHz) {
        return clockHz == 0 ? "Not available" : clockHz < 1000 * 1000 ? (clockHz / 1000) + " MHz" : (clockHz / 1000 / 1000) + "." + (clockHz / 1000 / 100) % 10 + " GHz";
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

    public static String getSensorName(final int type) {

        String name = "" + type;

        switch (type) {
            case Sensor.TYPE_ACCELEROMETER:
                name = "Accelerometer";
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                name = "Magnetic Field";
                break;
            case Sensor.TYPE_ORIENTATION:
                name = "Orientation";
                break;
            case Sensor.TYPE_GYROSCOPE:
                name = "Gyroscope";
                break;
            case Sensor.TYPE_LIGHT:
                name = "Light";
                break;
            case Sensor.TYPE_PRESSURE:
                name = "Pressure";
                break;
            case Sensor.TYPE_TEMPERATURE:
                name = "Temperature";
                break;
            case Sensor.TYPE_PROXIMITY:
                name = "Proximity";
                break;
            case Sensor.TYPE_GRAVITY:
                name = "Gravity";
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                name = "Linear Acceleration";
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                name = "Rotation Vector";
                break;
        }

        return name;
    }

    /**
     * Based PixelFormat and native http://stackoverflow.com/a/12068719
     */
    public synchronized static String nameForPixelFormat(final int pixelFormat) {
        String res;
        switch (pixelFormat) {
            case PixelFormat.TRANSLUCENT:
                res = "Translucent";
                break;
            case PixelFormat.TRANSPARENT:
                res = "Transparent";
                break;
            case PixelFormat.OPAQUE:
                res = "Opaque";
                break;
            case PixelFormat.RGBA_8888:
                res = "RGBA_8888";
                break;
            case PixelFormat.RGBX_8888:
                res = "RGBX_8888";
                break;
            case PixelFormat.RGB_888:
                res = "RGB_888";
                break;
            case PixelFormat.RGB_565:
                res = "RGB_565";
                break;
            case PixelFormat.RGBA_5551:
                res = "RGBA_5551";
                break;
            case PixelFormat.RGBA_4444:
                res = "RGBA_4444";
                break;
            case PixelFormat.A_8:
                res = "A_8";
                break;
            case PixelFormat.L_8:
                res = "L_8";
                break;
            case PixelFormat.LA_88:
                res = "LA_88";
                break;
            case PixelFormat.RGB_332:
                res = "RGB_332";
                break;
            case 0x13:
                res = "YCbCr_420_P";
                break;
            case 0x21:
                res = "YCbCr_420_SP";
                break;
            case 0x20:
            case 0x22:
                res = "YCrCb_420_SP_TILED";
                break;
            case ImageFormat.NV16:
                //case PixelFormat.YCbCr_422_SP: deprecated
            case 0x23:
            case 0x24:
            case 0x12:
                res = "YCbCr_422_P";
                break;
            case ImageFormat.NV21:
                //case PixelFormat.YCbCr_420_SP: deprecated
                res = "YCbCr_420_SP";
                break;
            case ImageFormat.YUY2:
                //case PixelFormat.YCbCr_422_I: deprecated
            case 0x16:
                res = "CbYCrY_422_I";
                break;
            case ImageFormat.JPEG:
                //case PixelFormat.JPEG: deprecated
                res = "Jpeg";
                break;
            case 0x15:
            case 0x17:
                res = "reserved";
                break;
            case 0x18:
                res = "range unavailable";
                break;
            case PixelFormat.UNKNOWN:
            default:
                res = "Unknown";
        }
        return res;
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
        buffer.append("Vertex Texture Fetch:").append(t(5)).append(formatBool(i.Vertex_Texture_Fetch)).append("\n");
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

    public static String nameForRotation(final int rotation) {
        String ret;
        switch (rotation) {
            case Surface.ROTATION_0:
                ret = "0°";
                break;
            case Surface.ROTATION_90:
                ret = "90°";
                break;
            case Surface.ROTATION_180:
                ret = "180°";
                break;
            case Surface.ROTATION_270:
                ret = "270°";
                break;
            default:
                ret = "" + rotation;
        }
        return ret;
    }

    public static String formatBool(final boolean isTrue) {
        return isTrue ? "Yes" : "No";
    }

    public static String formatInches(final double screenInches) {
        return String.format("%.2f inches", screenInches);
    }

    public static String inchToCm(final double screenInches) {
        return String.format("%.2f cm", screenInches * 2.54f);
    }

    public static String formatPixel(final double screenDiagonalPixel) {
        return String.format("%.2f px", screenDiagonalPixel);
    }

    public static Map<String, String> mapStorage(final Storage s) {
        final LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

        map.put("Path:", s.absolutePath);
        map.put("Total:", formatBytes(s.total));
        map.put("Available:", formatBytes(s.available));
        map.put("Free:", formatBytes(s.free));
        map.put("Used:", formatBytes(s.total - s.free));

        if (s.free - s.available > 0)
            map.put("Busy:", formatBytes(s.free - s.available));

        return map;
    }

    public static Map<String, String> parseTelize(final JSONObject geo) {
        final LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        try {
            map.put("Timezone", geo.getString("timezone"));
            map.put("ISP", geo.getString("isp"));
            map.put("Region Code", geo.getString("region_code"));
            map.put("Country", geo.getString("country"));
            map.put("DMA Code", geo.getString("dma_code"));
            map.put("Area Code", geo.getString("area_code"));
            map.put("Region", geo.getString("region"));
            map.put("IP", geo.getString("ip"));
            map.put("ASN", geo.getString("asn"));
            map.put("Continent Code", geo.getString("continent_code"));
            map.put("City", geo.getString("city"));
            map.put("Postal Code", geo.getString("postal_code"));
            map.put("Longitude", geo.getString("longitude"));
            map.put("Latitude", geo.getString("latitude"));
            map.put("Country Code", geo.getString("country_code"));
        } catch (final JSONException e) {
            Logger.e(e);
        }

        return map;
    }

    public static Map<String, String> parseRam(final String procMem) {
        final LinkedHashMap<String, String> ramMap = new LinkedHashMap<String, String>();

        final String lines[] = procMem.trim().split("\n");

        for (String line : lines) {
            final String[] token = line.split(" ");
            // proc mem output looks like this each line: "info         byte kB" with tons of non-utf8-spaces in between,
            // so split(" ") doesn't work properly, but we can just take the first and length-2 token index to get what we want
            ramMap.put(token[0], formatBytes(Integer.valueOf(token[token.length - 2])));
        }

        return ramMap;
    }

    public static JSONArray sort(final JSONArray jsonArray) {
        final List<String> jsonValues = new ArrayList<String>();
        for (int i = 0; i < jsonArray.length(); i++)
            try {
                jsonValues.add(jsonArray.getString(i));
            } catch (JSONException e) {
                Logger.e(e);
            }
        Collections.sort(jsonValues);
        return new JSONArray(jsonValues);
    }

    public static String formatKeyBoardHidden(final int hardKeyboardHidden) {
        switch (hardKeyboardHidden) {
            case Configuration.HARDKEYBOARDHIDDEN_YES:
                return formatBool(true);
            case Configuration.HARDKEYBOARDHIDDEN_NO:
                return formatBool(false);
            default:
                return "Undefined";
        }
    }

    public static String formatKeyboard(final int keyboard) {
        switch (keyboard) {
            case Configuration.KEYBOARD_NOKEYS:
                return "NOKEYS";
            case Configuration.KEYBOARD_QWERTY:
                return "QWERTY";
            case Configuration.KEYBOARD_12KEY:
                return "12KEY";
            default:
                return "Undefined";
        }
    }

    public static String formatScreenLayout(final int screenLayout) {

        String result;

        switch (screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                result = "SIZE_UNDEFINED";
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                result = "SIZE_SMALL";
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                result = "SIZE_NORMAL";
                break;
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                result = "SIZE_LARGE";
                break;
            case Configuration.SCREENLAYOUT_SIZE_UNDEFINED:
            default:
                result = "SIZE_UNDEFINED";
        }

        result += "\n";

        switch (screenLayout & Configuration.SCREENLAYOUT_LONG_MASK) {
            case Configuration.SCREENLAYOUT_LONG_MASK:
                result += "SIZE_XLARGE";
                break;
            case Configuration.SCREENLAYOUT_LONG_NO:
                result += "LONG_MASK";
                break;
            case Configuration.SCREENLAYOUT_LONG_YES:
                result += "LONG_NO";
                break;
            case Configuration.SCREENLAYOUT_LONG_UNDEFINED:
            default:
                result = "SIZE_UNDEFINED";
        }

        if (!Device.supportsApi(17)) {
            result += "\n(LAYOUTDIR_MASK Added in Api 17)";
        } else {
            result += "\n";

            final int SCREENLAYOUT_LAYOUTDIR_MASK = ReflectionHelper.getPublicStaticField(Configuration.class, "SCREENLAYOUT_LAYOUTDIR_MASK");
            final int SCREENLAYOUT_LAYOUTDIR_LTR = ReflectionHelper.getPublicStaticField(Configuration.class, "SCREENLAYOUT_LAYOUTDIR_LTR");
            final int SCREENLAYOUT_LAYOUTDIR_RTL = ReflectionHelper.getPublicStaticField(Configuration.class, "SCREENLAYOUT_LAYOUTDIR_RTL");

            if ((screenLayout & SCREENLAYOUT_LAYOUTDIR_MASK) == SCREENLAYOUT_LAYOUTDIR_LTR)
                result += "LAYOUTDIR_LTR";

            else if ((screenLayout & SCREENLAYOUT_LAYOUTDIR_MASK) == SCREENLAYOUT_LAYOUTDIR_RTL)
                result += "LAYOUTDIR_RTL";
            else
                result += "LAYOUTDIR_UNDEFINED";
        }

        return result;
    }

    public static String formatNavigationHidden(final int navigation) {

        String result;

        switch (navigation) {
            case Configuration.NAVIGATIONHIDDEN_NO:
                result = formatBool(false);
                break;
            case Configuration.NAVIGATIONHIDDEN_YES:
                result = formatBool(true);
                break;
            case Configuration.NAVIGATIONHIDDEN_UNDEFINED:
            default:
                result = "Undefined";
        }

        return result;
    }

    public static String formatNavigation(final int navigation) {

        String result;

        switch (navigation) {
            case Configuration.NAVIGATION_TRACKBALL:
                result = "Trackball";
                break;
            case Configuration.NAVIGATION_WHEEL:
                result = "Wheel";
                break;
            case Configuration.NAVIGATION_DPAD:
                result = "DPad";
                break;
            case Configuration.NAVIGATION_NONAV:
                result = "NONAV";
                break;
            case Configuration.NAVIGATION_UNDEFINED:
            default:
                result = "Undefined";
        }

        return result;
    }

    public static String formatOrientation(final int orientation) {
        String result;

        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                result = "Portrait";
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                result = "Landscape";
                break;
            case Configuration.ORIENTATION_SQUARE:
                result = "Square";
                break;
            case Configuration.ORIENTATION_UNDEFINED:
            default:
                result = "Undefined";
        }

        return result;
    }

    public static String formatTouchscreen(final int touchscreen) {

        String result;

        switch (touchscreen) {
            case Configuration.TOUCHSCREEN_NOTOUCH:
                result = "No touch";
                break;
            case Configuration.TOUCHSCREEN_STYLUS:
                result = "Stylus";
                break;
            case Configuration.TOUCHSCREEN_FINGER:
                result = "Finger";
                break;
            default:
                result = "Undefined";
        }

        return result;
    }

    public static String formatUIMode(final int uiMode) {

        String result;

        switch (uiMode & Configuration.UI_MODE_TYPE_MASK) {
            case Configuration.UI_MODE_TYPE_NORMAL:
                result = "NORMAL";
                break;
            case Configuration.UI_MODE_TYPE_DESK:
                result = "DESK";
                break;
            case Configuration.UI_MODE_TYPE_CAR:
                result = "CAR";
                break;
            case Configuration.UI_MODE_TYPE_UNDEFINED:
            default:
                result = "Type Undefined";
        }

        result += "\n";

        switch (uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_NO:
                result += "NIGHT_NO";
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                result = "NIGHT_YES";
                break;
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
            default:
                result = "NIGHT_UNDEFINED";
        }

        return result;
    }

    public static String formatKeyboardHidden(final int keyboardHidden) {

        String result;

        switch (keyboardHidden) {
            case Configuration.HARDKEYBOARDHIDDEN_NO:
                result = formatBool(false);
                break;
            case Configuration.HARDKEYBOARDHIDDEN_YES:
                result = formatBool(true);
                break;
            case Configuration.HARDKEYBOARDHIDDEN_UNDEFINED:
            default:
                result = "Undefined";
        }

        return result;
    }

    public static String formatLineSeparator(final String lineSeparator) {
        return lineSeparator.equals("\n") ? "\\n" : lineSeparator.equals("\n\r") ? "\\n\\r" : lineSeparator;

    }

    public static String formatPercent(final float usage) {
        return String.format("%.2f %s", usage, "%");
    }
}
