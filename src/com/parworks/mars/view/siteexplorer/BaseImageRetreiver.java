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

import java.util.List;

import com.parworks.androidlibrary.ar.ARErrorListener;
import com.parworks.androidlibrary.ar.ARListener;
import com.parworks.androidlibrary.ar.ARSite;
import com.parworks.androidlibrary.ar.ARSites;
import com.parworks.androidlibrary.response.BaseImageInfo;
import com.parworks.mars.utils.ImageLoader;
import com.parworks.mars.utils.ImageLoader.ImageLoaderListener;
import com.parworks.mars.utils.User;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

public class BaseImageRetreiver {
	public interface BaseImageRetreiverListener {
		public void firstBaseImageUrl(String url);
	}
	public static final String TAG = BaseImageRetreiver.class.getName();

	public static void getFirstBaseImageUrl(final String siteId, final BaseImageRetreiverListener listener) {
		ARSites sites = new ARSites(User.getApiKey(),User.getSecretKey());
		sites.getExisting(siteId, new ARListener<ARSite>() {
			
			@Override
			public void handleResponse(ARSite resp) {
				resp.getBaseImages(new ARListener<List<BaseImageInfo>>() {
					
					@Override
					public void handleResponse(List<BaseImageInfo> resp) {
						if(resp.size() > 0 ) {
							listener.firstBaseImageUrl(resp.get(0).getGallerySize());
						}
						
					}
				}, new ARErrorListener() {
					
					@Override
					public void handleError(Exception error) {
						Log.e(TAG, "BaseImageRetreiver failed to get base images for site: " + siteId + ". " + error.getMessage());
						
					}
				});
				
			}
		}, new ARErrorListener() {
			
			@Override
			public void handleError(Exception error) {
				Log.e(TAG, "BaseImageRetreiver failed to find the site: " + siteId + ". " + error.getMessage());
				
			}
		});
	}

}
