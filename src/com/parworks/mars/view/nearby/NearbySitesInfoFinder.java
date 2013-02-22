package com.parworks.mars.view.nearby;

import java.util.List;

import android.location.Location;
import android.util.Log;

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
	
	public void getNearbySiteInfo(Location location, int max, double radius) {
		ARSites sites = User.getARSites();
		sites.near(location.getLatitude(), location.getLongitude(), max, radius, new ARListener<List<ARSite>>() {
			
			@Override
			public void handleResponse(List<ARSite> sites) {
				for(ARSite site : sites) {
					getSiteInfo(site);
				}
				
			}
		}, new ARErrorListener() {
			
			@Override
			public void handleError(Exception error) {
				Log.e(TAG,error.getMessage());
				
			}
		});
	}
	private void getSiteInfo(ARSite site) {
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
