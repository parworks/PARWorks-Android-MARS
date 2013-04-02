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
package com.parworks.mars;

import android.app.Application;
import android.content.Context;

import com.parworks.arcameraview.CameraInitHelper;
import com.parworks.mars.cache.BitmapCache;
import com.parworks.mars.model.sync.SyncHandler;

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
		
		// initialize the db
		
		// initialize the bitmap cache system
		BitmapCache.init(mApplicationContext.getApplicationContext());
		
		// initialize the sync handler
		SyncHandler.initSyncHandler(mApplicationContext);
		
		// initialize the camera config
		CameraInitHelper.initCamera(mApplicationContext);
	}
	
	public static Context getAppContext() {
		return mApplicationContext;
	}
}
