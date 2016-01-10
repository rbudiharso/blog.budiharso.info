package com.myapp;

import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.myapp.R;
import com.myapp.Viewport;
import com.myapp.CameraView;

public class MyActivity extends AppCompatActivity {

    private FrameLayout preview1;
    private Camera mCamera;

    private CameraView cameraPreview1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_activity);

        mCamera = getCameraInstance();
        preview1 = (FrameLayout) findViewById(R.id.preview1);
        cameraPreview1 = new CameraView(this, mCamera);
        preview1.addView(cameraPreview1);

        mCamera.startPreview();
    }

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
}
