package com.project.leafdiag;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.g52grp.androidadreader.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class DisplayImage extends Activity {

	private static int RESULT_LOAD_IMAGE = 1;
	int loader;
	String image_url = "http://10.154.135.169:8080/WebApplication1/identification.png";	
	ImageView mImageView;


	public static String DEBUG_TAG;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_disp_comp_details);
		Button takePicture = (Button)findViewById(R.id.button3);
		takePicture.setOnClickListener(new View.OnClickListener(){
			public void onClick(View arg0){
				Intent i = new Intent(DisplayImage.this, MainActivity.class);
				startActivity(i);

			}
		});
		
		Button chooser = (Button)findViewById(R.id.button2);
		chooser.setOnClickListener(new View.OnClickListener(){
			public void onClick(View arg0){
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
	            intent.setType("image/*");

	            Intent chooser = Intent.createChooser(intent, "Choose a Picture");

	            startActivityForResult(chooser, 1);
	            
	            Intent i = new Intent(
	            Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);	 
	            startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});
		
		loader = R.drawable.loader;
        
        // Imageview to show
		mImageView = (ImageView) findViewById(R.id.imageView1);
		super.onStart();
		loadimage();
	}

	//if incoming intent is hisotry, then this method will be invoked
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

 
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
 
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
             
            mImageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            
            File pictureFile = new File(picturePath);
    		
    		// write data to file
    		try {
    			ImageDetails imgD = ImageDetails.getSingletonObject();
    			imgD.fillInInfo(pictureFile);

    		} catch (Exception error) {
    		}
    		
    		long waitingTime = System.currentTimeMillis() + 250;

    		while (System.currentTimeMillis() < waitingTime) {
    			synchronized (this) {
    				try {
    					wait(waitingTime - System.currentTimeMillis());
    				} catch (Exception e) {
    				}
    			}
    		}
    		
    		Intent intent = getIntent();
    		finish();
    		startActivity(intent);
        }    
     
    }
    
    private void loadimage(){
        //image_url = "http://i.stack.imgur.com/H5E28.png";
        // ImageLoader class instance
        ImageLoader imgLoader = new ImageLoader(getApplicationContext());
    	imgLoader.DisplayImage(image_url, loader, mImageView);
    }

}
