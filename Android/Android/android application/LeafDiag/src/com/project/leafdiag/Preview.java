package com.project.leafdiag;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Display;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.List;
import java.io.IOException;

/**
 * 
 * @author cmetrolis
 * 
 * http://stackoverflow.com/questions/7629624/
 * how-can-i-set-the-camera-preview-size-to-fullscreen-with-android-api
 * 
 * Code adapted and extended by Martin Bounden and Duc Huy Mai
 * specifically the addition of an orientation event listener, 
 * new onOrientationChanged method and modification of surfaceChanged()
 */
class Preview extends ViewGroup implements SurfaceHolder.Callback {

	private final String TAG = "Preview";
	SurfaceView mSurfaceView;
	SurfaceHolder mHolder;
	Size mPreviewSize;
	List<Size> mSupportedPreviewSizes;
	Camera mCamera;
	Display display;
	int currentZoomLevel = 0, maxZoomLevel = 0;
	Camera.Parameters parameters;
	OrientationEventListener oel;
	private int orientation = 1;
	private boolean previewOn = false;

	@SuppressWarnings("deprecation")
	Preview(Context context) {

		super(context);
		oel = new OrientationEventListener(context,
				SensorManager.SENSOR_DELAY_NORMAL) {

			@Override
			public void onOrientationChanged(int arg0) {

				// TODO Auto-generated method stub
				if (previewOn == true) {
					if ((display.getRotation() == Surface.ROTATION_0)
							&& (orientation != 0) && (previewOn == true)) {
						mCamera.stopPreview();
						orientation = 0;
						parameters = mCamera.getParameters();
						parameters.setRotation(90);
						mCamera.setDisplayOrientation(90);
						mCamera.setParameters(parameters);
						mCamera.startPreview();
					} else if ((display.getRotation() == Surface.ROTATION_90)
							&& (orientation != 90) && (previewOn == true)) {
						mCamera.stopPreview();
						orientation = 90;
						parameters = mCamera.getParameters();
						parameters.setRotation(0);
						mCamera.setDisplayOrientation(0);
						mCamera.setParameters(parameters);
						mCamera.startPreview();
					} else if ((display.getRotation() == Surface.ROTATION_270)
							&& (orientation != 270) && (previewOn == true)) {
						mCamera.stopPreview();
						orientation = 270;
						parameters = mCamera.getParameters();
						parameters.setRotation(180);
						mCamera.setDisplayOrientation(180);
						mCamera.setParameters(parameters);
						mCamera.startPreview();
					}
				}
			}
		};
		if (oel.canDetectOrientation()) {
			oel.enable();
		}

		display = ((WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		mSurfaceView = new SurfaceView(context);
		addView(mSurfaceView);

		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		mHolder = mSurfaceView.getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	public void setCamera(Camera camera) {
		mCamera = camera;
		if (mCamera != null) {
			mSupportedPreviewSizes = mCamera.getParameters()
					.getSupportedPreviewSizes();
			requestLayout();
		}
	}

	public void switchCamera(Camera camera) {
		setCamera(camera);
		try {
			camera.setPreviewDisplay(mHolder);
		} catch (IOException exception) {
			Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
		}
		Camera.Parameters parameters = camera.getParameters();
		parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
		requestLayout();

		camera.setParameters(parameters);
	}

	/**
	 * Called to determine the size requirements for this view and all of its
	 * children.
	 * 
	 * @param widthMeasureSpec
	 * @param heightMeasureSpec
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// We purposely disregard child measurements because act as a
		// Wrapper to a SurfaceView that centers the camera preview instead of
		// stretching it.
		final int width = resolveSize(getSuggestedMinimumWidth(),
				widthMeasureSpec);
		final int height = resolveSize(getSuggestedMinimumHeight(),
				heightMeasureSpec);
		setMeasuredDimension(width, height);

		if (mSupportedPreviewSizes != null) {
			mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width,
					height);
		}
	}

	/**
	 * Called when this view should assign a size and position to all of its
	 * children.
	 * 
	 * @param changed
	 * @param l
	 * @param t
	 * @param r
	 * @param b
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (changed && getChildCount() > 0) {
			final View child = getChildAt(0);

			final int width = r - l;
			final int height = b - t;

			int previewWidth = width;
			int previewHeight = height;

			// Center the child SurfaceView within the parent.
			if (width * previewHeight > height * previewWidth) {
				final int scaledChildWidth = previewWidth * height
						/ previewHeight;
				child.layout((width - scaledChildWidth) / 2, 0,
						(width + scaledChildWidth) / 2, height);
			} else {
				final int scaledChildHeight = previewHeight * width
						/ previewWidth;
				child.layout(0, (height - scaledChildHeight) / 2, width,
						(height + scaledChildHeight) / 2);
			}
		}
	}

	/**
	 * This is called immediately after the surface is first created
	 * 
	 * @param holder
	 */
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			if (mCamera != null) {
				mCamera.setPreviewDisplay(holder);
				parameters = mCamera.getParameters();
			}
		} catch (IOException e) {
			Log.e(TAG, "IOException caused by setPreviewDisplay()", e);
		}

	}

	/**
	 * This is called immediately before a surface is being destroyed
	 * 
	 * @param holder
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {

	}

	public void pause() {
		oel.disable();
		mCamera.stopPreview();
		previewOn = false;
	}

	/**
	 * This is called immediately after any structural changes (format or size)
	 * have been made to the surface
	 * 
	 * @param holder
	 * @param format
	 * @param w
	 * @param h
	 */
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

		parameters = mCamera.getParameters();
		parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
		if (display.getRotation() == Surface.ROTATION_0) {
			mCamera.setDisplayOrientation(90);
			parameters.setRotation(90);
		} else if (display.getRotation() == Surface.ROTATION_90) {
			mCamera.setDisplayOrientation(0);
			parameters.setRotation(0);
		} else if (display.getRotation() == Surface.ROTATION_270) {
			mCamera.setDisplayOrientation(180);
			parameters.setRotation(180);
		}
		requestLayout();

		mCamera.setParameters(parameters);
		mCamera.startPreview();
		previewOn = true;
	}

	/**
	 * Returns the best preview size
	 * 
	 * @param sizes
	 * @param w
	 * @param h
	 * @return Size
	 */
	private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
		final double ASPECT_TOLERANCE = 0.1;
		double targetRatio = (double) w / h;
		if (sizes == null)
			return null;

		Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;

		int targetHeight = h;

		// Try to find an size match aspect ratio and size
		for (Size size : sizes) {
			double ratio = (double) size.width / size.height;
			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
				continue;
			if (Math.abs(size.height - targetHeight) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}

		// Cannot find the one match the aspect ratio, ignore the requirement
		if (optimalSize == null) {
			minDiff = Double.MAX_VALUE;
			for (Size size : sizes) {
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}

		return optimalSize;
	}

}