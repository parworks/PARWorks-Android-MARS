package com.parworks.mars.model.sync;

import com.parworks.androidlibrary.ar.ARErrorListener;
import com.parworks.androidlibrary.ar.ARListener;
import com.parworks.androidlibrary.response.SiteInfo;
import com.parworks.mars.utils.User;

public class SyncHandler {

	public static void syncSiteInfo(String siteId) {
		User.getARSites().getSiteInfo(siteId, new ARListener<SiteInfo>() {
			@Override
			public void handleResponse(SiteInfo resp) {
				
			}
		}, new ARErrorListener() {			
			@Override
			public void handleError(Exception error) {
				
			}
		});
	}
}
