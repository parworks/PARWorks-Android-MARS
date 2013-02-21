package com.parworks.mars.view.nearby;

import java.util.List;

import android.location.Location;
import android.util.Log;

import com.parworks.androidlibrary.ar.ARErrorListener;
import com.parworks.androidlibrary.ar.ARListener;
import com.parworks.androidlibrary.ar.ARSite;
import com.parworks.androidlibrary.ar.ARSites;
import com.parworks.mars.utils.User;

public class NearbySitesFinder {
	public interface NearbySitesListener {
		public void gotSites(List<ARSite> sites);
	}
	
	public static final String TAG = NearbySitesFinder.class.getName();
	
	public NearbySitesFinder() {
		
	}
	
	public void getSites(Location location, int maxSites, double radius, final NearbySitesListener listener ) {
		ARSites sites = User.getARSites();
		ARListener<List<ARSite>> sitesListener = new ARListener<List<ARSite>>() {

			@Override
			public void handleResponse(List<ARSite> sites) {
				listener.gotSites(sites);
				
			}
			
		};
		ARErrorListener errorListener = new ARErrorListener() {

			@Override
			public void handleError(Exception error) {
				Log.e(TAG,error.getMessage());
				
			}
			
		};
		sites.near(location.getLatitude(),location.getLongitude(),maxSites, radius, sitesListener, errorListener);
	}

}
