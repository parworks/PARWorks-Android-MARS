package com.parworks.mars.cache;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.parworks.androidlibrary.utils.BitmapUtils;


/**
 * The memory cache used to store all the bitmaps.
 * 
 * @author yusun
 */
public class BitmapCache {

	private static final String TAG = "BitmapCache";
	/** Disk cache name */
	private static final String DISK_CACHE_NAME = "MarsBitmapCache";
	/** Bitmap sample */
	private static final int BITMAP_SAMPLE = 1;
	/** Disk cache size */
	private static final int DISK_CACHE_SIZE = 1024 * 1024 * 256; //256MB

	/** Memory Cache */
	private LruCache<String, Bitmap> mMemoryCache;
	/** Disk Cache */
	private DiskLruImageCache mImageDiskCache;
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
	}	

	public BitmapCache(Context context) {
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

		// initialize disk cache
		mImageDiskCache = new DiskLruImageCache(context, DISK_CACHE_NAME, DISK_CACHE_SIZE, 100);
		Log.i(TAG, "Created disk cache at: " + mImageDiskCache.getCacheFolder().getAbsolutePath()
				+ " with size: " + DISK_CACHE_SIZE);
	}

	/**
	 * Get the bitmap from cache system.
	 * <p>
	 * The key is first looked in mem cache, if not found,
	 * disk cache will be looked. If still not found, null
	 * will be returned.
	 * <p>
	 * Users can trigger {{@link #downloadImage(String, String)}
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
		}
		return bitmap;
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
		} catch (Exception e) {
			Log.w(TAG, "Failed to download the image: " + url);
		}
		return bitmap;
	}
	
	public static String getImageKeyFromURL(String imageUrl) {
		String key = imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.lastIndexOf("."));
		key = key.replaceAll("-", "");
		return key;
	}
}
