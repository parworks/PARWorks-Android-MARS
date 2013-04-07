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

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.parworks.androidlibrary.ar.ARErrorListener;
import com.parworks.androidlibrary.ar.ARListener;
import com.parworks.androidlibrary.ar.ARSite;
import com.parworks.androidlibrary.ar.ARSites;
import com.parworks.androidlibrary.response.SiteInfo;
import com.parworks.mars.utils.User;

public class NearbySitesInfoFinder {
	public static final String TAG = NearbySitesInfoFinder.class.getName();
	public interface NearbySitesInfoFinderListener {
		public void gotSite(SiteInfo site);
	}
	
	private final NearbySitesInfoFinderListener mListener;
	public NearbySitesInfoFinder(NearbySitesInfoFinderListener listener) {
		mListener = listener;
	}
	
	public void getNearbySiteInfo(LatLng location, int max, double radius) {
		Log.d(NearbyFragment.TAG_LOAD_MARKERS,"getNearbySiteInfo : calling nearby endpoint");
		ARSites sites = User.getARSites();
		sites.nearInfo(location.latitude,location.longitude,max,radius,new ARListener<List<SiteInfo>>() {
			
			@Override
			public void handleResponse(List<SiteInfo> sites) {
				for(SiteInfo info : sites) {
					mListener.gotSite(info);
				}
				
			}
		}, new ARErrorListener() {
			
			@Override
			public void handleError(Exception error) {
				// TODO Auto-generated method stub
				
			}
		});
//		sites.near(location.latitude, location.longitude, max, radius, new ARListener<List<ARSite>>() {
//			
//			@Override
//			public void handleResponse(List<ARSite> sites) {
//				Log.d(NearbyFragment.TAG_LOAD_MARKERS,"handleResponse: got list of sites");
//				for(ARSite site : sites) {
//					Log.d(NearbyFragment.TAG_LOAD_MARKERS,"calling get site info on site" + site.getSiteId());
//					getSiteInfo(site);
//				}
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
	}
	private void getSiteInfo(ARSite site) {
		Log.d(NearbyFragment.TAG_LOAD_MARKERS,"getNearbySiteInfo");
		site.getSiteInfo(new ARListener<SiteInfo>() {
			
			@Override
			public void handleResponse(SiteInfo info) {
				mListener.gotSite(info);
				
			}
		}, new ARErrorListener() {
			
			@Override
			public void handleError(Exception error) {
				Log.e(TAG,error.getMessage());
				
			}
		});
	}
	

}
