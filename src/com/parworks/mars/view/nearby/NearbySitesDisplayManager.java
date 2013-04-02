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
package com.parworks.mars.view.nearby;

import java.util.List;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parworks.androidlibrary.ar.ARErrorListener;
import com.parworks.androidlibrary.ar.ARListener;
import com.parworks.androidlibrary.ar.ARSite;
import com.parworks.androidlibrary.response.SiteInfo;

//import com.parworks.mars.view.nearby.NearbySitesFinder.NearbySitesListener;

public class NearbySitesDisplayManager {
	
//	public static final String TAG = NearbySitesDisplayManager.class.getName();
//
//	private final GoogleMap mMap;
//	
//	public NearbySitesDisplayManager(GoogleMap map) {
//		mMap = map;
//		
//	}
//	public void displayNearbySites(Location location, int maxSites, double radiusInMeters) {
//		NearbySitesFinder sitesFinder = new NearbySitesFinder();
//		sitesFinder.getSites(location, maxSites, radiusInMeters, new NearbySitesListener() {
//			
//			@Override
//			public void gotSites(List<ARSite> sites) {
//				displaySites(sites);
//				
//			}
//		});
//	}
//	
//	private void displaySites(List<ARSite> sites ) {
//		for(ARSite site: sites) {
//			displaySite(site);
//		}
//	}
//	
//	private void displaySite(ARSite site) {
//		site.getSiteInfo(new ARListener<SiteInfo>() {
//			
//			@Override
//			public void handleResponse(SiteInfo info) {
//				createSiteMarker(info);
//				
//			}
//		}, new ARErrorListener() {
//			
//			@Override
//			public void handleError(Exception error) {
//				Log.e(TAG,error.getMessage());
//				
//			}
//		});
//		
//	}
//	private void createSiteMarker(SiteInfo info) {
//		MarkerOptions markerOptions = new MarkerOptions();
//		String latString = info.getLat();
//		String lngString = info.getLon();
//		double latDouble = Double.parseDouble(latString);
//		double lngDouble = Double.parseDouble(lngString);
//		markerOptions.position(new LatLng(latDouble,lngDouble));
//		markerOptions.title(info.getId());
//		markerOptions.snippet(info.getDescription());
//		mMap.addMarker(markerOptions);
//	}

}
