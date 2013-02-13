package com.parworks.mars.model.sync;

import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.parworks.androidlibrary.ar.ARErrorListener;
import com.parworks.androidlibrary.ar.ARListener;
import com.parworks.androidlibrary.response.SiteInfo;
import com.parworks.mars.model.db.SiteInfoTable;
import com.parworks.mars.model.provider.MarsContentProvider;
import com.parworks.mars.utils.SiteTags;
import com.parworks.mars.utils.User;

public class SyncHandler {
	
	private static final String TAG = "SyncHandler";
	
	private static ContentResolver mContentResolver;
	
	public static void initSyncHandler(Context context) {
		mContentResolver = context.getContentResolver();
	}

	public static void syncSiteInfo(String siteId) {
		User.getARSites().getSiteInfo(siteId, new ARListener<SiteInfo>() {
			@Override
			public void handleResponse(SiteInfo resp) {
				storeSiteInfo(resp);
			}
		}, new ARErrorListener() {			
			@Override
			public void handleError(Exception error) {
				Log.e(TAG, "Failed to get the site info", error);
			}
		});
	}	
	
	public static void syncListSiteInfo(List<String> siteIds) {
		// TODO: this should be done with thread control
		for(String id : siteIds) {
			syncSiteInfo(id);
		}
	}
	
	private static void storeSiteInfo(SiteInfo info) {
		ContentValues values = new ContentValues();
		values.put(SiteInfoTable.COLUMN_ADDRESS, info.getAddress());
		values.put(SiteInfoTable.COLUMN_CHANNEL, info.getChannel());
		values.put(SiteInfoTable.COLUMN_DESC, info.getDescription());
		values.put(SiteInfoTable.COLUMN_FEATURE_DESC, info.getFeatureType());
		values.put(SiteInfoTable.COLUMN_LAT, info.getLat());
		values.put(SiteInfoTable.COLUMN_LON, info.getLon());
		values.put(SiteInfoTable.COLUMN_NAME, info.getName());
		values.put(SiteInfoTable.COLUMN_POSTER_IMAGE_CONTENT, info.getPosterImageOverlayContent());
		values.put(SiteInfoTable.COLUMN_POSTER_IMAGE_URL, info.getPosterImageURL());
		values.put(SiteInfoTable.COLUMN_PROFILE, info.getProcessingProfile());
		values.put(SiteInfoTable.COLUMN_SITE_ID, info.getId());
		values.put(SiteInfoTable.COLUMN_STATE, info.getSiteState().name());
		values.put(SiteInfoTable.COLUMN_TAG_LIST, SiteTags.toJson(info.getTags()));
		
		// update or insert if not exist
		// FIXME: not thread-safe here
		if (mContentResolver.update(MarsContentProvider.getSiteUri(info.getId()), 
				values, null, null) == 0) {		
			mContentResolver.insert(MarsContentProvider.CONTENT_URI_ALL_SITES, values);
		}
	}
}
