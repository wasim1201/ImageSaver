package com.example.imagesaver.activity;

import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.imagesaver.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {

    private Camera mCamera;
    private boolean captureEnabled = true;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_camera);

        mCamera = getCameraInstance();
        mCamera.setDisplayOrientation(90);
        CameraPreview mCameraPreview = new CameraPreview(CameraActivity.this, mCamera);
        FrameLayout preview = findViewById(R.id.camera_preview);
        preview.addView(mCameraPreview);

        final Button captureButton = findViewById(R.id.button_capture);

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (captureEnabled)
                    mCamera.takePicture(null, null, mPicture);
                captureEnabled = false;
            }
        });
    }

    /**
     * Helper method to access the camera returns null if it cannot get the
     * camera or does not exist
     *
     * @return
     */
    private Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            // cannot get camera or does not exist
        }
        return camera;
    }

    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile();
            if (pictureFile == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                galleryAddPic(pictureFile.getPath());

                //Toast.makeText(CameraActivity.this, Thread.currentThread().getName(), Toast.LENGTH_SHORT).show();

                new Thread(new Runnable() {
                    Handler handler = new Handler();
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(CameraActivity.this, Thread.currentThread().getName(), Toast.LENGTH_SHORT).show();
                                CameraActivity.super.recreate();
                            }
                        });
                        Log.d("TAG", "Thread_Name = " + Thread.currentThread().getName());
                    }
                }).start();

//                new Handler(getMainLooper()).postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(CameraActivity.this, Thread.currentThread().getName(), Toast.LENGTH_SHORT).show();
//                        CameraActivity.super.recreate();
//
//                    }
//                }, 1000);

                //Thread.sleep(5000);

                //camera.startPreview();
                //CameraActivity.super.recreate();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private File getOutputMediaFile() {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory("Trade Show Images"), "");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("TradeShow Images", "failed to create directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

    private void galleryAddPic(String photoPath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(photoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
}