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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.util.Log;



public class StaticGoogleMaps {
	
	public static String TAG = StaticGoogleMaps.class.getName();
	
	private static int ZOOM_LEVEL = 16;  //level 0 shows entire earth, level 21+ shows individual buildings
	
	private static String BASE_URL = "https://maps.googleapis.com/maps/api/staticmap";
	
	public static String getMapUrl(String lat, String lon, int width, int height) {
		Map<String,String> queryString = new HashMap<String,String>();
		queryString.put("center", lat+","+lon);
		queryString.put("zoom", ""+ZOOM_LEVEL);
		queryString.put("size", width+"x"+height);
		queryString.put("sensor", ""+false);
		
		String markersString = "color:red|" +lat+","+lon;
		queryString.put("markers",markersString);
		
		String url = appendQueryStringToUrl(BASE_URL,queryString);
		return url;
	}
	
	private static String appendQueryStringToUrl(String url,
			Map<String, String> queryString) {
		url += "?";
		Iterator<Entry<String, String>> it = queryString.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> pairs = (Map.Entry<String, String>) it
					.next();
			try {
				url += "&" + pairs.getKey() + "=" + URLEncoder.encode(pairs.getValue(),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				Log.e(TAG,e.getMessage()); 
			}
			it.remove();
		}

		return url;
	}

}
