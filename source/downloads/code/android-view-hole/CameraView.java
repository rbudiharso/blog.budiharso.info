package com.myapp.viewport;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CameraView extends SurfaceView {

    private Camera mCamera;
    private SurfaceHolder mHolder;

    public CameraView(Context context, @Nullable Camera camera) {
        super(context);
        mCamera = camera;
        mHolder = getHolder();
        mHolder.addCallback(new MyCallback());
    }

    private class MyCallback implements SurfaceHolder.Callback {
        public void surfaceCreated(SurfaceHolder surface) {
            if (mHolder == null) {
                mHolder = surface;
            }
            if (mCamera != null) {
                try {
                    mCamera.setPreviewDisplay(surface);
                } catch (IOException e) {
                    // Something bad happened
                }
            }
        }

        public void surfaceChanged(SurfaceHolder surface, int format, int width, int height) {
            // Ignored, Camera does all the work for us
        }

        public void surfaceDestroyed(SurfaceHolder surface) {
        }
    }
}
