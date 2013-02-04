package com.parworks.mars.cache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.jakewharton.DiskLruCache;
import com.parworks.mars.BuildConfig;

public class DiskLruImageCache {

	private DiskLruCache mDiskCache;
	private CompressFormat mCompressFormat = CompressFormat.JPEG;
	private int mCompressQuality = 100;
	private static final int APP_VERSION = 1;
	private static final int VALUE_COUNT = 1;
	private static final String TAG = "DiskLruImageCache";

	public DiskLruImageCache(Context context,String uniqueName, int diskCacheSize,
			int quality) {
		try {
			final File diskCacheDir = getDiskCacheDir(context, uniqueName );
			mDiskCache = DiskLruCache.open(diskCacheDir, APP_VERSION, VALUE_COUNT, diskCacheSize);                
			mCompressQuality = quality;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean writeBitmapToFile(Bitmap bitmap, DiskLruCache.Editor editor)
			throws IOException, FileNotFoundException {
		OutputStream out = null;
		try {
			out = new BufferedOutputStream( editor.newOutputStream( 0 ), CacheUtils.IO_BUFFER_SIZE );
			return bitmap.compress( mCompressFormat, mCompressQuality, out );
		} finally {
			if ( out != null ) {
				out.close();
			}
		}
	}
	
	private boolean writeInputStreamToFile(InputStream inputStream, DiskLruCache.Editor editor) throws IOException {
		OutputStream out = null;
		try {
			out = new BufferedOutputStream( editor.newOutputStream( 0 ), CacheUtils.IO_BUFFER_SIZE );
			IOUtils.copy(inputStream, out);
			return true;
		} catch (IOException e) {
			Log.w(TAG, "Failed to convert inputstream to outputstream");
			return false;
		} finally {
			if ( out != null ) {
				out.close();
			}
		}
	}

	private File getDiskCacheDir(Context context, String uniqueName) {

		// Check if media is mounted or storage is built-in, if so, try and use external cache dir
		// otherwise use internal cache dir
		final String cachePath =
				Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED ||
				!CacheUtils.isExternalStorageRemovable() ?
						CacheUtils.getExternalCacheDir(context).getPath() :
							context.getCacheDir().getPath();

		return new File(cachePath + File.separator + uniqueName);
	}
	
	public void put( String key, InputStream inputStream ) {

		DiskLruCache.Editor editor = null;
		try {
			editor = mDiskCache.edit( key );
			if ( editor == null ) {
				return;
			}

			if (writeInputStreamToFile(inputStream, editor)) {
				mDiskCache.flush();
				editor.commit();
				if ( BuildConfig.DEBUG ) {
					Log.d( "cache_test_DISK_", "image put on disk cache " + key );
				}
			} else {
				editor.abort();
				if ( BuildConfig.DEBUG ) {
					Log.d( "cache_test_DISK_", "ERROR on: image put on disk cache " + key );
				}
			}   
		} catch (IOException e) {
			if ( BuildConfig.DEBUG ) {
				Log.d( "cache_test_DISK_", "ERROR on: image put on disk cache " + key );
			}
			try {
				if ( editor != null ) {
					editor.abort();
				}
			} catch (IOException ignored) {
			}           
		}
	}


	public void put( String key, Bitmap data ) {

		DiskLruCache.Editor editor = null;
		try {
			editor = mDiskCache.edit( key );
			if ( editor == null ) {
				return;
			}

			if( writeBitmapToFile( data, editor ) ) {   
				mDiskCache.flush();
				editor.commit();
				if ( BuildConfig.DEBUG ) {
					Log.d( "cache_test_DISK_", "image put on disk cache " + key );
				}
			} else {
				editor.abort();
				if ( BuildConfig.DEBUG ) {
					Log.d( "cache_test_DISK_", "ERROR on: image put on disk cache " + key );
				}
			}   
		} catch (IOException e) {
			if ( BuildConfig.DEBUG ) {
				Log.d( "cache_test_DISK_", "ERROR on: image put on disk cache " + key );
			}
			try {
				if ( editor != null ) {
					editor.abort();
				}
			} catch (IOException ignored) {
			}           
		}
	}

	public Bitmap getBitmap( String key, Integer sampleSize) {
		Bitmap bitmap = null;
		DiskLruCache.Snapshot snapshot = null;
		try {
			snapshot = mDiskCache.get( key );
			if ( snapshot == null ) {
				return null;
			}
			final InputStream in = snapshot.getInputStream( 0 );
			if ( in != null ) {								
				final BufferedInputStream buffIn = 
						new BufferedInputStream( in, CacheUtils.IO_BUFFER_SIZE );
				if (sampleSize == null) {
					sampleSize = 1;
				}
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = sampleSize;
				Log.d(TAG, "Applying sample size: " + sampleSize);
				bitmap = BitmapFactory.decodeStream( buffIn, null, options );
				Log.d(TAG, "Decode bitmap size: " + bitmap.getRowBytes() * bitmap.getHeight());			              
			}   
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			if ( snapshot != null ) {
				snapshot.close();
			}
		}

		if ( BuildConfig.DEBUG ) {
			Log.d( "cache_test_DISK_", bitmap == null ? "" : "image read from disk " + key);
		}

		return bitmap;
	}

	public boolean containsKey( String key ) {

		boolean contained = false;
		DiskLruCache.Snapshot snapshot = null;
		try {
			snapshot = mDiskCache.get( key );
			contained = snapshot != null;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if ( snapshot != null ) {
				snapshot.close();
			}
		}

		return contained;

	}

	public void clearCache() {
		if ( BuildConfig.DEBUG ) {
			Log.d( "cache_test_DISK_", "disk cache CLEARED");
		}
		try {
			mDiskCache.delete();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}

	public File getCacheFolder() {
		return mDiskCache.getDirectory();
	}

}