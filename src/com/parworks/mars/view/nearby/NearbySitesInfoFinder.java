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
