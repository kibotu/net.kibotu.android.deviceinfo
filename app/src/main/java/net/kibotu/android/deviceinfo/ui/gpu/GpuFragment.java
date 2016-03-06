package net.kibotu.android.deviceinfo.ui.gpu;

import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.library.Callback;
import net.kibotu.android.deviceinfo.library.Device;
import net.kibotu.android.deviceinfo.library.hardware.gpu.OpenGLGles10Info;
import net.kibotu.android.deviceinfo.library.hardware.gpu.OpenGLGles20Info;
import net.kibotu.android.deviceinfo.model.ListItem;
import net.kibotu.android.deviceinfo.ui.list.ListFragment;

import static net.kibotu.android.deviceinfo.ui.ViewHelper.*;

/**
 * Created by Nyaruhodo on 21.02.2016.
 */
public class GpuFragment extends ListFragment {

    @Override
    protected String getTitle() {
        return getString(R.string.menu_item_gpu);
    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();

        Device.loadOpenGLGles10Info(new Callback<OpenGLGles10Info>() {
            @Override
            public void onComplete(final OpenGLGles10Info openGLGles10Info) {
                addOpenGLGles10Info(openGLGles10Info);
            }
        });

        Device.loadOpenGLGles20Info(new Callback<OpenGLGles20Info>() {
            @Override
            public void onComplete(final OpenGLGles20Info openGLGles20Info) {
                addOpenGLGles20Info(openGLGles20Info);
            }
        });

//        cachedList.addItem("OpenGL ES 2.0", "description", new DeviceInfoItemAsync() {
//            @Override
//            protected void async() {
//                gpu.loadOpenGLGles20Info(new Gpu.OnCompleteCallback<OpenGLGles20Info>() {
//
//                    @Override
//                    public void onComplete(final OpenGLGles20Info info) {
//                        value = formatOpenGles20info(info);
//                    }
//                });
//            }
//        });
//
//        cachedList.addItem("OpenGL ES-CM 1.1", "description", new DeviceInfoItemAsync() {
//            @Override
//            protected void async() {
//                gpu.loadOpenGLGles10Info(new Gpu.OnCompleteCallback<OpenGLGles10Info>() {
//
//                    @Override
//                    public void onComplete(final OpenGLGles10Info info) {
//                        value = formatOpenGles10info(info);
//                        cachedList.addItem("Graphic Modes", "description", new DeviceInfoItemAsync() {
//
//                            @Override
//                            protected void async() {
//                                value = "";
//                                for (final Gpu.Egl egl : info.eglconfigs)
//                                    value += egl.toString() + "\n";
//                            }
//                        });
//                    }
//                });
//            }
//        });
    }

    private void addOpenGLGles10Info(OpenGLGles10Info info) {

        // general
        String keys = "General" + BR;
        String values = BR;
        keys += "Renderer" + BR;
        values += info.GL_RENDERER + BR;
        keys += "Version" + BR;
        values += info.GL_VERSION + BR;
        keys += "Vendor" + BR + BR;
        values += info.GL_VENDOR + BR + BR;

//        addListItemWithTitle("OpenGLGles10", keys, values, "");

        notifyDataSetChanged();
    }

    private void addOpenGLGles20Info(OpenGLGles20Info info) {

        addSubListItem(new ListItem()
                .setLabel("OpenGLGles20")

                .addChild(new ListItem().setLabel("General"))
                .addChild(new ListItem().setLabel("GLSL Version").setValue(info.GL_SHADING_LANGUAGE_VERSION))
                .addChild(new ListItem().setLabel("Version").setValue(info.GL_VERSION))
                .addChild(new ListItem().setLabel("Vendor").setValue(info.GL_VENDOR))
                .addChild(new ListItem().setLabel("Renderer").setValue(info.GL_RENDERER))

                .addChild(new ListItem().setLabel("Textures"))
                .addChild(new ListItem().setLabel("Max Texture Size").setValue(info.GL_MAX_TEXTURE_SIZE + "x" + info.GL_MAX_TEXTURE_SIZE))
                .addChild(new ListItem().setLabel("Max Texture Units").setValue(info.GL_MAX_TEXTURE_UNITS))
                .addChild(new ListItem().setLabel("Max Texture Image Units").setValue(info.GL_MAX_TEXTURE_IMAGE_UNITS))
                .addChild(new ListItem().setLabel("Max Combined Texture Units").setValue(info.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS))
                .addChild(new ListItem().setLabel("Max Cube Map Texture Size").setValue(info.GL_MAX_CUBE_MAP_TEXTURE_SIZE + "x" + info.GL_MAX_CUBE_MAP_TEXTURE_SIZE))
                .addChild(new ListItem().setLabel("Max Vertex Texture Image").setValue(info.GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS))
                .addChild(new ListItem().setLabel("Vertex Texture Fetch").setValue(info.Vertex_Texture_Fetch))
                .addChild(new ListItem().setLabel("Max Viewport Dimension").setValue(info.GL_MAX_VIEWPORT_DIMS[0] + "x" + info.GL_MAX_VIEWPORT_DIMS[0]))
                .addChild(new ListItem().setLabel("Max Renderbuffer Size").setValue(info.GL_MAX_RENDERBUFFER_SIZE + "x" + info.GL_MAX_RENDERBUFFER_SIZE))

        );

        notifyDataSetChanged();
    }

    public synchronized static String formatOpenGles20info(final OpenGLGles20Info i) {
        final StringBuffer buffer = new StringBuffer();
//
//        // general
//        buffer.append("General").append(BR).append(BR);
//        buffer.append("Renderer:").append(t(4)).append(i.GL_RENDERER).append("\n");
//        buffer.append("Version:").append(t(5)).append(i.GL_VERSION).append("\n");
//        buffer.append("Vendor:").append(t(5)).append(i.GL_VENDOR).append("\n");
//        buffer.append("GLSL Version:").append(t(2)).append(i.GL_SHADING_LANGUAGE_VERSION).append("\n\n");
//
//        // texture related
//        buffer.append("Textures\n\n");
//        buffer.append("Max Texture Size:").append(t(6)).append(i.GL_MAX_TEXTURE_SIZE).append("x").append(i.GL_MAX_TEXTURE_SIZE).append("\n");
//        buffer.append("Max Texture Units:").append(t(6)).append(i.GL_MAX_TEXTURE_UNITS).append("\n");
//        buffer.append("Max Texture Image Units:").append(t(3)).append(i.GL_MAX_TEXTURE_IMAGE_UNITS).append("\n");
//        buffer.append("Max Combined Texture Units:").append(t(1)).append(i.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS).append("\n");
//        buffer.append("Max Cube Map Texture Size:").append(t(1)).append(i.GL_MAX_CUBE_MAP_TEXTURE_SIZE).append("x").append(i.GL_MAX_CUBE_MAP_TEXTURE_SIZE).append("\n");
//        buffer.append("Max Vertex Texture Images:").append(t(1)).append(i.GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS).append("\n");
//        buffer.append("Vertex Texture Fetch:").append(t(5)).append(formatBool(i.Vertex_Texture_Fetch)).append("\n");
//        buffer.append("Max Viewport Dimension:").append(t(3)).append(i.GL_MAX_VIEWPORT_DIMS[0]).append("x").append(i.GL_MAX_VIEWPORT_DIMS[0]).append("\n");
//        buffer.append("Max Renderbuffer Size:").append(t(4)).append(i.GL_MAX_RENDERBUFFER_SIZE).append("x").append(i.GL_MAX_RENDERBUFFER_SIZE).append("\n\n");

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
}
