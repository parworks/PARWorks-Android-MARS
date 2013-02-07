package com.parworks.mars.staticmaps.utils;

import java.io.File;

import android.content.Context;

public class Utilities {
	
	public static File GetCacheDir(Context context)
	{
	    File cacheDir;
	    if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
	        cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), "com.parworks.mars");
	    else
	        cacheDir = context.getCacheDir();
	    if (!cacheDir.exists())
	        cacheDir.mkdirs();

	    return cacheDir;
	}

}
