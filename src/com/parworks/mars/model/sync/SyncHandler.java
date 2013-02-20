package com.parworks.mars.model.sync;

import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.parworks.androidlibrary.ar.ARErrorListener;
import com.parworks.androidlibrary.ar.ARListener;
import com.parworks.androidlibrary.ar.ARSite;
import com.parworks.androidlibrary.response.AugmentedImage;
import com.parworks.androidlibrary.response.SiteComment;
import com.parworks.androidlibrary.response.SiteInfo;
import com.parworks.mars.model.db.AugmentedImagesTable;
import com.parworks.mars.model.db.CommentsTable;
import com.parworks.mars.model.db.SiteInfoTable;
import com.parworks.mars.model.provider.MarsContentProvider;
import com.parworks.mars.utils.SiteTags;
import com.parworks.mars.utils.User;

public class SyncHandler {
	
	private static final String TAG = "MarsSyncHandler";	
	private static ContentResolver mContentResolver;
	
	public static void initSyncHandler(Context context) {
		mContentResolver = context.getContentResolver();
	}

	public static void syncSiteInfo(final String siteId, final boolean syncAllRelatedInfo) {
		User.getARSites().getSiteInfo(siteId, new ARListener<SiteInfo>() {
			@Override
			public void handleResponse(SiteInfo resp) {
				// store the SiteInfo locally
				storeSiteInfo(resp);
				
				// we might also need to sync other related info
				// such as AugmentedImages, Comments, etc.
				if (syncAllRelatedInfo) {
					syncSiteAugmentedImages(siteId);
					syncSiteComments(siteId);
				}
			}
		}, new ARErrorListener() {			
			@Override
			public void handleError(Exception error) {
				Log.e(TAG, "Failed to get the site info", error);
			}
		});
	}	
		
	/**
	 * Sync the SiteInfo for a list of siteIds
	 * 
	 * @param siteIds
	 */
	public static void syncListSiteInfo(List<String> siteIds) {
		// TODO: this should be done with thread control
		for(String id : siteIds) {
			syncSiteInfo(id, false);
		}
	}

	/**
	 * Sync all the recently augmented images for a given site
	 * 
	 * @param siteId
	 */
	public static void syncSiteAugmentedImages(String siteId) {		
		User.getARSites().getExisting(siteId, new ARListener<ARSite>() {
			@Override
			public void handleResponse(ARSite resp) {		
				resp.getAugmentedImages(new ARListener<List<AugmentedImage>>() {
					@Override
					public void handleResponse(List<AugmentedImage> resp) {
						storeAugmentedImages(resp);				
					}
				}, new ARErrorListener() {					
					@Override
					public void handleError(Exception error) {
						Log.e(TAG, "Failed to get the augmented images", error);		
					}
				});
			}
		}, new ARErrorListener() {			
			@Override
			public void handleError(Exception error) {
				Log.e(TAG, "Failed to get site when getting augmented images", error);
			}
		});
	}
	
	/**
	 * Sync site comments
	 * 
	 * @param siteId
	 */
	public static void syncSiteComments(final String siteId) {
		User.getARSites().getExisting(siteId, new ARListener<ARSite>() {
			@Override
			public void handleResponse(ARSite resp) {
				resp.getSiteComments(siteId, new ARListener<List<SiteComment>>() {
					@Override
					public void handleResponse(List<SiteComment> resp) {
						storeSiteComments(resp);
					}
				}, new ARErrorListener() {					
					@Override
					public void handleError(Exception error) {
						Log.e(TAG, "Failed to load site comments", error);
					}
				});
			}
		}, new ARErrorListener() {			
			@Override
			public void handleError(Exception error) {
				Log.e(TAG, "Failed to get the site when loading site comments", error);
			}
		});
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
	
	private static void storeAugmentedImages(List<AugmentedImage> augmentedImages) {
		for(AugmentedImage image : augmentedImages) {
			ContentValues values = new ContentValues();			
			values.put(AugmentedImagesTable.COLUMN_SITE_ID, image.getSiteId());
			values.put(AugmentedImagesTable.COLUMN_IMAGE_ID, image.getImgId());
			values.put(AugmentedImagesTable.COLUMN_USER_ID, image.getUserId());
			values.put(AugmentedImagesTable.COLUMN_WIDTH, image.getFullSizeWidth());
			values.put(AugmentedImagesTable.COLUMN_HIEGHT, image.getFullSizeHeight());
			values.put(AugmentedImagesTable.COLUMN_FULL_SIZE_URL, image.getImgPath());
			values.put(AugmentedImagesTable.COLUMN_GALLERY_SIZE_URL, image.getImgGalleryPath());
			values.put(AugmentedImagesTable.COLUMN_CONTENT_SIZE_URL, image.getImgContentPath());
			values.put(AugmentedImagesTable.COLUMN_TIMESTAMP, image.getTime());
			values.put(AugmentedImagesTable.COLUMN_CONTENT, image.getOutput());
			
			// update or insert if not exist
			// FIXME: not thread-safe here
			if (mContentResolver.update(MarsContentProvider.getAugmentedImageUri(image.getImgId()),	
					values, null, null) == 0) {
				mContentResolver.insert(MarsContentProvider.CONTENT_URI_ALL_AUGMENTED_IMAGES, values);
			}		
			
			// TODO: Cut the records if there are too many records for the site
		}
	}
	
	private static void storeSiteComments(List<SiteComment> comments) {
		for(SiteComment comment : comments) {
			ContentValues values = new ContentValues();			
			values.put(CommentsTable.COLUMN_SITE_ID, comment.getSiteId());
			values.put(CommentsTable.COLUMN_COMMENT, comment.getComment());
			values.put(CommentsTable.COLUMN_USER_ID, comment.getUserId());
			values.put(CommentsTable.COLUMN_TIMESTAMP, comment.getTimeStamp());
			values.put(CommentsTable.COLUMN_USER_NAME, comment.getUserName());
			
			// update or insert if not exist
			// FIXME: not thread-safe here
			if (mContentResolver.update(MarsContentProvider.getCommentsUri(comment.getSiteId()),	
					values, null, null) == 0) {
				mContentResolver.insert(MarsContentProvider.CONTENT_URI_ALL_COMMENTS, values);
			}
			
			// TODO: Cut the records if there are too many records for the site
		}
	}
}
