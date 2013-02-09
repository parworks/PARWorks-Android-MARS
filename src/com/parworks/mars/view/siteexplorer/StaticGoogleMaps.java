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
