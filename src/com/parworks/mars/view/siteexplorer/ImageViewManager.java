/*******************************************************************************
 * Copyright 2013 PAR Works, Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.parworks.mars.view.siteexplorer;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.parworks.mars.cache.BitmapCache;
import com.parworks.mars.cache.BitmapWorkerTask;
import com.parworks.mars.cache.BitmapWorkerTask.BitmapWorkerListener;

public class ImageViewManager {
	
	public interface ImageLoadedListener {
		public void onImageLoaded(Bitmap bitmap);
	}
	public static final String TAG = ImageViewManager.class.getName();	
	
	public void setImageView(final String url, final ImageView imageView, final ImageLoadedListener listener) {
		if(url != null) {
			Bitmap posterImageBitmap = BitmapCache.get().getBitmap(
					BitmapCache.getImageKeyFromURL(url));
			if (posterImageBitmap == null) {
				Log.d(TAG, "Bitmap not found in cache, start to download it. Url was: " + url);
				new BitmapWorkerTask(url, new BitmapWorkerListener() {					
					@Override
					public void bitmapLoaded(Bitmap bitmap) {
						imageView.setImageBitmap(bitmap);
						if (listener != null) {
							listener.onImageLoaded(bitmap);
						}
					}
				}).execute();
			} else {
				Log.d(TAG,"Bitmap was already in the cache! Setting the image. Url was: " + url);
				imageView.setImageBitmap(posterImageBitmap);
				if (listener != null) {
					listener.onImageLoaded(posterImageBitmap);
				}
			}
			
		}  else {
			Log.e(TAG, "mapURL was null.");
		}
	}
}
