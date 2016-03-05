package net.kibotu.android.deviceinfo.ui.gpu;

import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.ui.list.ListFragment;

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

//        final GPU gpu = new GPU(DeviceOld.context());
//
//        cachedList.addItem("OpenGL ES 2.0", "description", new DeviceInfoItemAsync() {
//            @Override
//            protected void async() {
//                gpu.loadOpenGLGles20Info(new GPU.OnCompleteCallback<OpenGLGles20Info>() {
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
//                gpu.loadOpenGLGles10Info(new GPU.OnCompleteCallback<OpenGLGles10Info>() {
//
//                    @Override
//                    public void onComplete(final OpenGLGles10Info info) {
//                        value = formatOpenGles10info(info);
//                        cachedList.addItem("Graphic Modes", "description", new DeviceInfoItemAsync() {
//
//                            @Override
//                            protected void async() {
//                                value = "";
//                                for (final GPU.Egl egl : info.eglconfigs)
//                                    value += egl.toString() + "\n";
//                            }
//                        });
//                    }
//                });
//            }
//        });

    }
}
