package com.parworks.mars.cache;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

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
		public void bitmapLoaded(Bitmap bitmap);
	}

	/** The URL of the image to be downloaded */
	private String imageUrl;
	/** The callback listener */
	private BitmapWorkerListener listener;

	public BitmapWorkerTask(String url, BitmapWorkerListener listener) {
		this.imageUrl = url;
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
		// lock on the URL to download in order to avoid
		// the same URL being downloaded by multiple threads
		
		// lock first
		// TODO double check if there will be a problem of using
		// string as the lock object
		synchronized (imageUrl) {
			// get the lock, double check the existence
			String key = BitmapCache.getImageKeyFromURL(imageUrl);
			Bitmap bitmap = BitmapCache.get().getBitmap(key);
			if (bitmap != null) {
				return bitmap;
			} else {
				return BitmapCache.get().downloadImage(imageUrl);
			}
		}					
		// release the lock after downloading it and putting it
		// into the cache
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		if (result != null) {	
			listener.bitmapLoaded(result);
		} else {
			Log.e("BitmapWorkerTask", "Failed to get the bitmap and display it: " + imageUrl);
		}
	}
}