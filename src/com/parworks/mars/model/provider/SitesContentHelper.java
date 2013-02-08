package com.parworks.mars.model.provider;

import android.content.ContentValues;
import android.content.Context;

import com.parworks.androidlibrary.response.SiteInfo;
import com.parworks.mars.model.db.SiteInfoTable;
import com.parworks.mars.utils.SiteTags;

public class SitesContentHelper {
	
	private Context mContext;
	
	public SitesContentHelper(Context context) {
		mContext = context;
	}
	
	public void storeSite(String siteId, SiteInfo info) {
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
		mContext.getContentResolver().insert(MarsContentProvider.ALL_SITES_CONTENT_URI, values);
	}

}
