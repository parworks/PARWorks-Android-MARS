package com.parworks.mars;

import android.app.Application;
import android.content.Context;

import com.parworks.mars.cache.BitmapCache;
import com.parworks.mars.model.sync.SyncHelper;

/**
 * The entry point of the app
 * 
 * @author yusun
 */
public class Mars extends Application {
	
	private static Context mApplicationContext;
	
	@Override
	public void onCreate() {		
		super.onCreate();
		mApplicationContext = this;
		
		// initialize the account 
		SyncHelper.initAppAccount(this.getApplicationContext());
		
		// initialize the db
		
		// initialize the bitmap cache system
		BitmapCache.init(mApplicationContext);
	}
	
	public static Context getAppContext() {
		return mApplicationContext;
	}
}
