package com.project.leafdiag;

import com.g52grp.androidadreader.R;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static final String DEBUG_TAG = null;
	private FrameLayout preview;
	private Preview mPreview;

	private Camera mCamera;
	int numberOfCameras;

	private ProgressDialog progress;
	private Toast flashToast;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Hide the window title.
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// Create a FrameLayout container that will hold a SurfaceView (the
		// Camera preview),
		// and set it as the content of the activity.
		setContentView(R.layout.activity_main);
		mPreview = new Preview(this);
		preview = (FrameLayout) findViewById(R.id.camera_preview);
	}

	
	@SuppressLint("ShowToast")
	@Override
	protected void onResume() {
		dismissLoadingDialog();
		super.onResume();

		flashToast = Toast.makeText(getApplicationContext(), "",
				Toast.LENGTH_SHORT);

		// Find the total number of cameras available
		numberOfCameras = Camera.getNumberOfCameras();

		// Find the ID of the default camera
		CameraInfo cameraInfo = new CameraInfo();
		for (int i = 0; i < numberOfCameras; i++) {
			Camera.getCameraInfo(i, cameraInfo);
			if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
				mCamera = Camera.open(i);
			}
		}

		if (mCamera == null) {
			mCamera = Camera.open();
		}
		// set the camera to use auto-focus
		Camera.Parameters cp = mCamera.getParameters();
		cp.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
		cp.setPictureSize(1920, 1080);
		mCamera.setParameters(cp);

		preview.addView(mPreview);
		mPreview.setCamera(mCamera);
	}

	@Override
	protected void onPause() {
		super.onPause();

		// release the camera when the activity is paused.
		if (mCamera != null) {
			mPreview.pause();
			mCamera.release();
			mPreview.setCamera(null);
			mCamera = null;
		}
		preview.removeView(mPreview);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// create a new photoHandler object to save a picture file
	public void takePicture(View view) {
		showLoadingDialog();
		PhotoHandler pH = new PhotoHandler(getApplicationContext());
		mCamera.takePicture(null, null, pH);

		long waitingTime = System.currentTimeMillis() + 250;

		while (System.currentTimeMillis() < waitingTime) {
			synchronized (this) {
				try {
					wait(waitingTime - System.currentTimeMillis());
				} catch (Exception e) {
				}
			}
		}
		// start display company details intent
		Intent i = new Intent(this, DisplayImage.class);
		startActivity(i);
	}

	// method to handle toggling of the flash, sets parameters and displays
	// message to user
	public void toggleFlash(View view) {
		Camera.Parameters cp = mCamera.getParameters();
		String flashStatus = mCamera.getParameters().getFlashMode();

		if (flashStatus.equals(Camera.Parameters.FLASH_MODE_OFF)) {
			flashToast.setText("Flash : ON");
			flashToast.show();
			cp.setFlashMode(Parameters.FLASH_MODE_ON);
		}
		if (flashStatus.equals(Camera.Parameters.FLASH_MODE_ON)) {
			flashToast.setText("Flash : OFF");
			flashToast.show();
			cp.setFlashMode(Parameters.FLASH_MODE_OFF);
		}
		mCamera.setParameters(cp);

	}


	// show loading dialog box while image is being processed by server
	public void showLoadingDialog() {

		if (progress == null) {
			progress = new ProgressDialog(this);
			progress.setTitle("Loading");
			progress.setMessage("wait");
		}
		progress.show();
	}

	public void dismissLoadingDialog() {

		if (progress != null && progress.isShowing()) {
			progress.dismiss();
		}
	}
}
