package com.parworks.mars.utils;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

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
	
	public static void CopyStream(InputStream is, OutputStream os)
	{
	    final int buffer_size = 1024;
	    try
	    {
	        byte[] bytes = new byte[buffer_size];
	        for (;;)
	        {
	            int count = is.read(bytes, 0, buffer_size);
	            if (count == -1)
	                break;
	            os.write(bytes, 0, count);
	        }
	    } catch (Exception ex)
	    {
	    }
	}

}
