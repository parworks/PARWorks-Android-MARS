package com.parworks.mars;

import com.parworks.mars.account.MarsAccountHelper;
import com.parworks.mars.cache.BitmapCache;

import android.app.Application;
import android.content.Context;

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
		MarsAccountHelper.initAppAccount(this.getApplicationContext());
		
		// initialize the db
		
		// initialize the bitmap cache system
		// BitmapCache.init(mApplicationContext);
	}
	
	public static Context getAppContext() {
		return mApplicationContext;
	}
}
