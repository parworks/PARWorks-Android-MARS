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
	
	public interface BitmapWorkerListener {
		public void bitmapLoaded();
	}

	/** The URL of the image to be downloaded */
	private String imageUrl;
	/** The target ImageView to display the bitmap */
	private ImageView imageView;
	/** The callback listener */
	private BitmapWorkerListener listener;
	
	// TODO: We might need more configs here to control,
	// feel free to add more configuration trhough the constructor.
	// For example, you can add:
	//  1) the Bitmap sample
	//  2) the ImageView size and scale
	//  3) the Default image place holder 
	//  4) the progress bar

	public BitmapWorkerTask(String url, ImageView imageView, BitmapWorkerListener listener) {
		this.imageUrl = url;
		this.imageView = imageView;
		this.listener = listener;
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
			imageView.setImageBitmap(result);
			listener.bitmapLoaded();
		} else {
			Log.w("BitmapWorkerTask", "Failed to get the bitmap and display it: " + imageUrl);
		}
	}
}
