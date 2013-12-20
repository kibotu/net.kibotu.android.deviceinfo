package net.kibotu.android.deviceinformation;

import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;
import android.widget.FrameLayout;

import java.io.IOException;
import java.util.LinkedList;

public class CameraHandler implements TextureView.SurfaceTextureListener {

    private Camera mCamera;
    private TextureView mTextureView;

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        // Update your view here!
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mCamera = Camera.open();

        Camera.Size previewSize = mCamera.getParameters().getPreviewSize();
        mTextureView.setLayoutParams(new FrameLayout.LayoutParams(previewSize.width, previewSize.height, Gravity.CENTER));

        mTextureView.setDrawingCacheEnabled(true);

        try {
            mCamera.setPreviewTexture(surface);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCamera.startPreview();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        // Ignored, the Camera does all the work for us
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mCamera.stopPreview();
        mCamera.release();
        return true;
    }



    /**
     * @see <a href="http://stackoverflow.com/a/12457895">save-bitmap-to-location</a>
     */
    public void startTextureView() {
        mTextureView = new TextureView(MainActivity.mActivity);
        mTextureView.setSurfaceTextureListener(this);
        mTextureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // saveBitmap(mTextureView.getBitmap(),Environment.getExternalStorageDirectory().getAbsolutePath(),"cameraScreenshot.png");
                explodeMemory();
            }
        });
        MainActivity.mActivity.setContentView(mTextureView);
    }

    public void explodeMemory() {

        final LinkedList<Bitmap> explodeMemory = new LinkedList<Bitmap>();

        long i = 0;
        while (true) {
            i++;
            Log.v("explode-counter", "allocating screenshot " + i);
            //explodeMemory.add(mTextureView.getBitmap());
            explodeMemory.add(Device.save(mTextureView));
        }
    }

}
