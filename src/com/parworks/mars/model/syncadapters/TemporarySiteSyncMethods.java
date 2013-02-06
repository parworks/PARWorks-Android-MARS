package com.parworks.mars.model.syncadapters;

import java.util.List;

import android.content.Context;
import android.util.Log;

import com.parworks.androidlibrary.ar.ARErrorListener;
import com.parworks.androidlibrary.ar.ARListener;
import com.parworks.androidlibrary.ar.ARSite;
import com.parworks.androidlibrary.ar.ARSites;
import com.parworks.androidlibrary.response.SiteInfo;
import com.parworks.mars.Mars;
import com.parworks.mars.model.providers.SitesContentHelper;
import com.parworks.mars.utils.User;

public class TemporarySiteSyncMethods {
	private static final String TAG = TemporarySiteSyncMethods.class.getName();
	
	private static void syncSite(final ARSite site, Context context) {
		ARListener<SiteInfo> getSiteInfoListener = new ARListener<SiteInfo>() {

			@Override
			public void handleResponse(SiteInfo info) {
				SitesContentHelper helper = new SitesContentHelper(Mars.getAppContext());
				helper.storeSite(site.getSiteId(), info);
				
			}
			
		};
		ARErrorListener getSiteInfoErrorListener = new ARErrorListener() {

			@Override
			public void handleError(Exception error) {
				Log.e(TAG, "Failed to get site info: " + error.getMessage());
				
			}
			
		};
		site.getSiteInfo(getSiteInfoListener, getSiteInfoErrorListener);
	}
	public static void syncUserSites(final Context context) {
		ARSites sites = new ARSites(User.getApiKey(), User.getSecretKey());
		ARListener<List<ARSite>> getUserSitesListener = new ARListener<List<ARSite>>() {

			@Override
			public void handleResponse(List<ARSite> userSites) {
				for(ARSite site : userSites) {
					syncSite(site,context);
				}
				
			}
		};
		ARErrorListener getUserSitesErrorListener = new ARErrorListener() {

			@Override
			public void handleError(Exception error) {
				Log.e(TAG,"Failed to get user sites: " + error);
				
			}
			
		};
		sites.getUserSites(getUserSitesListener, getUserSitesErrorListener);
	}

}
