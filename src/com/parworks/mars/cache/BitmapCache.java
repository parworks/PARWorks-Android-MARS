package com.parworks.mars.cache;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.parworks.androidlibrary.utils.BitmapUtils;
import com.parworks.mars.cache.BitmapWorkerTask.BitmapWorkerListener;


/**
 * The memory cache used to store all the bitmaps.
 * 
 * @author yusun
 */
public class BitmapCache {

	private static final String TAG = BitmapCache.class.getName();
	/** Disk cache name */
	private static final String DISK_CACHE_NAME = "mars";
	/** Bitmap sample */
	private static final int BITMAP_SAMPLE = 1;
	/** Disk cache size */
	private static final int DISK_CACHE_SIZE = 1024 * 1024 * 256; //256MB

	/** Memory Cache */
	private LruCache<String, Bitmap> mMemoryCache;
	/** Disk Cache */
	private static DiskLruImageCache mImageDiskCache;
	/** The singleton instance holder */
	private static BitmapCache INSTANCE;

	/**
	 * Get the singleton instance of the bitmap cache
	 * @return
	 */
	public static BitmapCache get() {
		return INSTANCE;
	}

	/**
	 * Always trigger initialize first before any actions
	 *  
	 * @param context
	 */
	public static void init(Context context) {
		if (INSTANCE == null) {
			INSTANCE = new BitmapCache(context);
		}
		// initialize disk cache
		mImageDiskCache = new DiskLruImageCache(context, DISK_CACHE_NAME, DISK_CACHE_SIZE, 100);
		Log.i(TAG, "Created disk cache at: " + mImageDiskCache.getCacheFolder().getAbsolutePath()
				+ " with size: " + DISK_CACHE_SIZE);
	}	

	private BitmapCache(Context context) {
		// Get memory class of this device, exceeding this amount 
		// will throw an OutOfMemory exception
		final int memClass = ((ActivityManager) context.getSystemService(
				Context.ACTIVITY_SERVICE)).getMemoryClass();

		// Use 1/4th of the available memory for this memory cache
		final int cacheSize = 1024 * 1024 * memClass / 4;	    

		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				// The cache size will be measured in bytes rather than number of items.
				return bitmap.getRowBytes() * bitmap.getHeight();
			}
		};
		Log.i(TAG, "Created mem cache with size: " + cacheSize);		
	}

	/**
	 * Get the bitmap from cache system.
	 * <p>
	 * The key is first looked in mem cache, if not found,
	 * disk cache will be looked. If still not found, null
	 * will be returned.
	 * <p>
	 * Users can trigger {@link #downloadImage(String, String)}
	 * to download the bitmap with a given URL.
	 * 
	 * @param key the bitmap key to look for
	 * @return the bitmap
	 */
	public Bitmap getBitmap(String key) {
		// check mem cache first
		Bitmap bitmap = mMemoryCache.get(key);
		if (bitmap == null) {
			// check disk cache if not present in mem cache
			bitmap = mImageDiskCache.getBitmap(key, BITMAP_SAMPLE);
			if (bitmap != null) {
				mMemoryCache.put(key, bitmap);
			}
		}
		return bitmap;
	}
	
	public void getBitmapAsync(final String url, final BitmapWorkerListener onCompleteListener) {
		String key = getImageKeyFromURL(url);								
		Bitmap bitmap = getBitmap(key);
		if (bitmap == null) {
			new BitmapWorkerTask(url, new BitmapWorkerListener() {					
				@Override
				public void bitmapLoaded(Bitmap bitmap) {	
					if (onCompleteListener != null) {
						onCompleteListener.bitmapLoaded(bitmap);
					}
				}
			}).execute();
		} else {
			if (onCompleteListener != null) {
				onCompleteListener.bitmapLoaded(bitmap);
			}
		}
	}
	
	public boolean containsKey(String key) {
		return mImageDiskCache.containsKey(key);
	}

	/**
	 * Download the image from web with the given URL. Once it
	 * is downloaded, the bitmap will be automatically put into
	 * both mem and disk caches.
	 * <p>
	 * Note, this method should be called in the background thread.
	 * 
	 * @param key
	 * @param url
	 * @return bitmap
	 */
	public Bitmap downloadImage(String url) {
		Bitmap bitmap = null;
		try {			
			// download the image
			BitmapUtils bitmapUtils = new BitmapUtils();
			bitmap = bitmapUtils.getBitmap(url, BITMAP_SAMPLE);
			String key = getImageKeyFromURL(url);
			// put into disk cache
			mImageDiskCache.put(key, bitmap);
			// put into mem cache
			mMemoryCache.put(key, bitmap);	
			Log.d(TAG, "Finished downloading link for " + url);
		} catch (Exception e) {
			Log.w(TAG, "Failed to download the image: " + url + " : " + e.getMessage());
		}
		return bitmap;
	}
	
	public static String getImageKeyFromURL(String imageUrl) {
		String key = Integer.toString(imageUrl.hashCode());
		key = key.replaceAll("-", "_");
		return key;
	}
}
