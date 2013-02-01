package com.parworks.mars;

import android.app.Application;
import android.content.Context;

/**
 * A placeholder class
 * 
 * @author yusun
 */
public class Mars extends Application {
	
	private static Context mApplicationContext;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		mApplicationContext = this;
		
	}
	
	public static Context getAppContext() {
		return mApplicationContext;
	}

}
