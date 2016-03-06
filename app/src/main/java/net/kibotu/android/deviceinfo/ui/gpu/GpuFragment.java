package net.kibotu.android.deviceinfo.ui.gpu;

import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.library.Callback;
import net.kibotu.android.deviceinfo.library.Device;
import net.kibotu.android.deviceinfo.library.hardware.gpu.Egl;
import net.kibotu.android.deviceinfo.library.hardware.gpu.OpenGLGles10Info;
import net.kibotu.android.deviceinfo.library.hardware.gpu.OpenGLGles20Info;
import net.kibotu.android.deviceinfo.model.ListItem;
import net.kibotu.android.deviceinfo.ui.list.ListFragment;

import static net.kibotu.android.deviceinfo.ui.ViewHelper.appendGLInfoArray;
import static net.kibotu.android.deviceinfo.ui.ViewHelper.firstLetterToUpperCase;

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

        Device.loadOpenGLGles20Info(new Callback<OpenGLGles20Info>() {
            @Override
            public void onComplete(final OpenGLGles20Info openGLGles20Info) {
                addOpenGLGles20Info(openGLGles20Info);
            }
        });

        Device.loadOpenGLGles10Info(new Callback<OpenGLGles10Info>() {
            @Override
            public void onComplete(final OpenGLGles10Info openGLGles10Info) {
                addGLES10Constraints(openGLGles10Info);
            }
        });
    }

    private void addGLES10Constraints(OpenGLGles10Info info) {
        ListItem item = new ListItem().setLabel("Fixed Function Pipeline")
                .addChild(new ListItem().setLabel("Version").setValue(info.GL_VERSION))
                .addChild(new ListItem().setLabel("Max ModelView Stack Depth").setValue(info.GL_MAX_MODELVIEW_STACK_DEPTH))
                .addChild(new ListItem().setLabel("Max Projection Stack Depth").setValue(info.GL_MAX_PROJECTION_STACK_DEPTH))
                .addChild(new ListItem().setLabel("Max Lights").setValue(info.GL_MAX_LIGHTS))
                .addChild(new ListItem().setLabel("Max Depth Bits").setValue(info.GL_DEPTH_BITS))
                .addChild(new ListItem().setLabel("Max Stencil Bits").setValue(info.GL_STENCIL_BITS))
                .addChild(new ListItem().setLabel("Max Subpixel Bits").setValue(info.GL_SUBPIXEL_BITS))
                .addChild(new ListItem().setLabel("Max Element Indices").setValue(info.GL_MAX_ELEMENTS_INDICES))
                .addChild(new ListItem().setLabel("Max Element Vertices").setValue(info.GL_MAX_ELEMENTS_VERTICES));
        addSubListItem(item);

        notifyDataSetChanged();
    }

    private void addOpenGLGles20Info(OpenGLGles20Info info) {

        addGeneral(info);

        addCompressedTextures(info);

        addTexture(info);

        addShaderAttributes(info);

        addVertexNumericPrecision(info);

        addFragmentNumericPrecision(info);

        addEglConfigs(info);

        addExtensions(info);

        notifyDataSetChanged();
    }

    private void addEglConfigs(OpenGLGles20Info info) {
        ListItem item = new ListItem().setLabel("Graphic Modes");
        for (final Egl egl : info.eglconfigs)
            item.addChild(new ListItem().setLabel(egl.toString()));

        addSubListItem(item);
    }

    private void addFragmentNumericPrecision(OpenGLGles20Info info) {
        // precision [] { -range, range, precision }
        ListItem item = new ListItem().setLabel("Fragment Numeric Precision");
        item.addChild(new ListItem().setLabel("Low Int").setValue(appendGLInfoArray(info.GL_FRAGMENT_SHADER_GL_LOW_INT)));
        item.addChild(new ListItem().setLabel("Medium Int").setValue(appendGLInfoArray(info.GL_FRAGMENT_SHADER_GL_MEDIUM_INT)));
        item.addChild(new ListItem().setLabel("High Int").setValue(appendGLInfoArray(info.GL_FRAGMENT_SHADER_GL_HIGH_INT)));
        item.addChild(new ListItem().setLabel("Low Float").setValue(appendGLInfoArray(info.GL_FRAGMENT_SHADER_GL_LOW_FLOAT)));
        item.addChild(new ListItem().setLabel("Medium Float").setValue(appendGLInfoArray(info.GL_FRAGMENT_SHADER_GL_MEDIUM_FLOAT)));
        item.addChild(new ListItem().setLabel("High Float").setValue(appendGLInfoArray(info.GL_FRAGMENT_SHADER_GL_HIGH_FLOAT)));
        addSubListItem(item);
    }

    private void addVertexNumericPrecision(OpenGLGles20Info info) {
        // precision [] { -range, range, precision }
        ListItem item = new ListItem().setLabel("Vertex Numeric Precision");
        item.addChild(new ListItem().setLabel("Low Int").setValue(appendGLInfoArray(info.GL_VERTEX_SHADER_GL_LOW_INT)));
        item.addChild(new ListItem().setLabel("Medium Int").setValue(appendGLInfoArray(info.GL_VERTEX_SHADER_GL_MEDIUM_INT)));
        item.addChild(new ListItem().setLabel("High Int").setValue(appendGLInfoArray(info.GL_VERTEX_SHADER_GL_HIGH_INT)));
        item.addChild(new ListItem().setLabel("Low Float").setValue(appendGLInfoArray(info.GL_VERTEX_SHADER_GL_LOW_FLOAT)));
        item.addChild(new ListItem().setLabel("Medium Float").setValue(appendGLInfoArray(info.GL_VERTEX_SHADER_GL_MEDIUM_FLOAT)));
        item.addChild(new ListItem().setLabel("High Float").setValue(appendGLInfoArray(info.GL_VERTEX_SHADER_GL_HIGH_FLOAT)));
        addSubListItem(item);
    }

    private void addCompressedTextures(OpenGLGles20Info info) {
        ListItem item = new ListItem().setLabel("Compressed Texture Formats");
        final String[] token = info.GL_EXTENSIONS.split("_");
        for (int j = 0; j < token.length; ++j)
            if (token[j].equalsIgnoreCase("compressed"))
                item.addChild(new ListItem().setLabel(firstLetterToUpperCase(token[j + 1])));

        addSubListItem(item);
    }

    private void addShaderAttributes(OpenGLGles20Info info) {
        ListItem item = new ListItem().setLabel("Shader Attributes")
                .addChild(new ListItem().setLabel("Max Vertex Uniform Vectors").setValue(info.GL_MAX_VERTEX_UNIFORM_VECTORS))
                .addChild(new ListItem().setLabel("Max Fragment Uniform Vectors").setValue(info.GL_MAX_FRAGMENT_UNIFORM_VECTORS))
                .addChild(new ListItem().setLabel("Max Vertex Attributes").setValue(info.GL_MAX_VERTEX_ATTRIBS))
                .addChild(new ListItem().setLabel("Max Varying Vectors").setValue(info.GL_MAX_VARYING_VECTORS));
        addSubListItem(item);
    }

    private void addTexture(OpenGLGles20Info info) {
        ListItem item = new ListItem().setLabel("Texture Attributes")
                .addChild(new ListItem().setLabel("Max Texture Size").setValue(info.GL_MAX_TEXTURE_SIZE + "x" + info.GL_MAX_TEXTURE_SIZE))
                .addChild(new ListItem().setLabel("Max Texture Units").setValue(info.GL_MAX_TEXTURE_UNITS))
                .addChild(new ListItem().setLabel("Max Texture Image Units").setValue(info.GL_MAX_TEXTURE_IMAGE_UNITS))
                .addChild(new ListItem().setLabel("Max Combined Texture Units").setValue(info.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS))
                .addChild(new ListItem().setLabel("Max Cube Map Texture Size").setValue(info.GL_MAX_CUBE_MAP_TEXTURE_SIZE + "x" + info.GL_MAX_CUBE_MAP_TEXTURE_SIZE))
                .addChild(new ListItem().setLabel("Max Vertex Texture Image").setValue(info.GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS))
                .addChild(new ListItem().setLabel("Vertex Texture Fetch").setValue(info.Vertex_Texture_Fetch))
                .addChild(new ListItem().setLabel("Max Viewport Dimension").setValue(info.GL_MAX_VIEWPORT_DIMS[0] + "x" + info.GL_MAX_VIEWPORT_DIMS[0]))
                .addChild(new ListItem().setLabel("Max Renderbuffer Size").setValue(info.GL_MAX_RENDERBUFFER_SIZE + "x" + info.GL_MAX_RENDERBUFFER_SIZE));
        addSubListItem(item);
    }

    private void addGeneral(OpenGLGles20Info info) {
        ListItem item = new ListItem().setLabel("General")
                .addChild(new ListItem().setLabel("GLSL Version").setValue(info.GL_SHADING_LANGUAGE_VERSION))
                .addChild(new ListItem().setLabel("Version").setValue(info.GL_VERSION))
                .addChild(new ListItem().setLabel("Vendor").setValue(info.GL_VENDOR))
                .addChild(new ListItem().setLabel("Renderer").setValue(info.GL_RENDERER));
        addSubListItem(item);
    }

    private void addExtensions(OpenGLGles20Info info) {
        final ListItem item = new ListItem().setLabel("GL Extensions");
        item.addChild(new ListItem().setLabel("GL Extensions"));
        final String[] extensions = info.GL_EXTENSIONS.replace("GL_", "-GL_").split("-");
        for (int j = 0; j < extensions.length; ++j) {
            final String extension = extensions[j];
            if (extension.equalsIgnoreCase("null"))
                continue;

            item.addChild(new ListItem().setLabel(extension));
        }

        addSubListItem(item);
    }

    @Override
    protected int getHomeIcon() {
        return R.drawable.gpu_i;
    }
}
