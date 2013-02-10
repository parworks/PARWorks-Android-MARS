package com.parworks.mars.view.siteexplorer;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parworks.mars.R;
import com.parworks.mars.cache.BitmapCache;
import com.parworks.mars.cache.BitmapWorkerTask;
import com.parworks.mars.cache.BitmapWorkerTask.BitmapWorkerListener;
import com.parworks.mars.model.db.SiteInfoTable;
import com.parworks.mars.view.siteexplorer.BaseImageRetreiver.BaseImageRetreiverListener;

public class ImageViewManager {
	public interface ImageLoadedListener {
		public void onImageLoaded();
	}
	
	public static final String TAG = ImageViewManager.class.getName();
	
	
	public void setImageView(String url, final int width, final ImageView imageView, final ImageLoadedListener listener) {
		if(url != null) {
			Bitmap posterImageBitmap = BitmapCache.get().getBitmap(
					BitmapCache.getImageKeyFromURL(url));
			if (posterImageBitmap == null) {
				Log.d(TAG, "Bitmap not found in cache, start to download it.");
				new BitmapWorkerTask(url, imageView, new BitmapWorkerListener() {
					
					@Override
					public void bitmapLoaded() {
						setImageSizeMaintainAspectRatio(imageView,width);
						listener.onImageLoaded();
						
					}
				}).execute();
			} else {
				imageView.setImageBitmap(posterImageBitmap);
				setImageSizeMaintainAspectRatio(imageView,width);
				listener.onImageLoaded();
			}
			
		}  else {
			Log.e(TAG, "mapURL was null.");
		}
	}
	
	
	private void setImageSizeMaintainAspectRatio(ImageView imageView,int width) {
		Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
		int height = ViewDimensionCalculator.calculateHeightToMaintainAspectRatio(width, bitmap);
		bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
		imageView.setImageBitmap(bitmap);
		
	}

}
