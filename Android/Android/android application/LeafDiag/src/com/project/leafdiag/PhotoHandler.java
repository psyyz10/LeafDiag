package com.project.leafdiag;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class PhotoHandler extends Activity implements PictureCallback {

	private final Context context;

	public PhotoHandler(Context context) {
		this.context = context;
	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {

		File pictureFileDir = getDir();
	
		if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {

			Log.d(DisplayImage.DEBUG_TAG,
					"Can't create directory to save image.");
			Toast.makeText(context, "Can't create directory to save image.",
					Toast.LENGTH_LONG).show();
			return;

		}	

		//format of the filename, using the current date and time
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
		String date = dateFormat.format(new Date());
		String photoFile = "Picture_" + date + ".jpg";

		String filename = pictureFileDir.getPath() + File.separator + photoFile;

		File pictureFile = new File(filename);
		
		// write data to file
		try {
			FileOutputStream fos = new FileOutputStream(pictureFile);
			fos.write(data);
			fos.close();

			ImageDetails imgD = ImageDetails.getSingletonObject();
			imgD.fillInInfo(pictureFile);

		} catch (Exception error) {
			Toast.makeText(context, "Error- Failed to contact server",
					Toast.LENGTH_LONG).show();
		}
	}
	
	// get directory to save image into
	private File getDir() {
		File sdDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		return new File(sdDir, "AndroidAdReader");
	}
}