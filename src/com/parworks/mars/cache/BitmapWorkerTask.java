package com.parworks.mars.cache;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

/**
 * Async task to the following things:
 * <p>
 * 1) download the image from the url
 * 2) put it into the cache 
 * 3) display in the given image view
 * 
 * @author yusun
 */
public class BitmapWorkerTask extends AsyncTask<Void, Void, Bitmap> {

	/** The URL of the image to be downloaded */
	private String imageUrl;
	/** The target ImageView to display the bitmap */
	private ImageView imageView;
	
	// TODO: We might need more configs here to control,
	// feel free to add more configuration trhough the constructor.
	// For example, you can add:
	//  1) the Bitmap sample
	//  2) the ImageView size and scale
	//  3) the Default image place holder 
	//  4) the progress bar

	public BitmapWorkerTask(String url, ImageView imageView) {
		this.imageUrl = url;
		this.imageView = imageView;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		// show an ImagePlacerHolder if provided
		// this is not required to have
	}

	@Override
	protected Bitmap doInBackground(Void... params) {  
		// download and put it into cache
		return BitmapCache.get().downloadImage(imageUrl);			
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		// update the image view
		if (result != null) {
			System.out.println("Show the image" + result.getRowBytes() * result.getHeight());			
			imageView.setImageBitmap(result);
		} else {
			Log.w("BitmapWorkerTask", "Failed to get the bitmap and display it: " + imageUrl);
		}
	}
}
