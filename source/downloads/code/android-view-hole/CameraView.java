package com.myapp.ui;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class CameraView extends SurfaceView implements SurfaceHolder.Callback {

	private Camera mCamera;
	private SurfaceHolder mHolder;

	public CameraView(Context context, Camera camera) {
		super(context);
		mCamera = camera;
		mHolder = getHolder();
		mHolder.addCallback(this);
	}

	public void surfaceCreated(SurfaceHolder surface) {
		try {
			mCamera.setPreviewDisplay(surface);
		} catch (IOException e) {
			// Something bad happened
		}
	}

	public void surfaceChanged(SurfaceHolder surface, int format, int width, int height) {
	}

	public void surfaceDestroyed(SurfaceHolder surface) {
	}
}
