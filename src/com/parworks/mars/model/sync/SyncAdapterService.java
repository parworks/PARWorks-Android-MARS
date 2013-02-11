package com.parworks.mars.model.sync;

import java.util.ArrayList;
import java.util.List;

import android.accounts.Account;
import android.accounts.OperationCanceledException;
import android.app.Service;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.parworks.androidlibrary.response.AugmentedImage;
import com.parworks.androidlibrary.response.SiteInfo;
import com.parworks.androidlibrary.response.SiteInfoOverview;
import com.parworks.mars.model.db.AugmentedImagesTable;
import com.parworks.mars.model.db.SiteInfoTable;
import com.parworks.mars.model.db.TrendingSitesTable;
import com.parworks.mars.model.provider.MarsContentProvider;
import com.parworks.mars.utils.SiteTags;
import com.parworks.mars.utils.User;

public class SyncAdapterService extends Service {

	private static final String TAG = "MarsSyncAdapterService";

	private static SyncAdapterImpl sSyncAdapter = null;
	private static ContentResolver mContentResolver = null;

	public SyncAdapterService() {
		super();
	}

	private static class SyncAdapterImpl extends AbstractThreadedSyncAdapter {
		private Context mContext;

		public SyncAdapterImpl(Context context) {
			super(context, true);
			mContext = context;
		}

		@Override
		public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
			try {
				SyncAdapterService.performSync(mContext, account, extras, authority, provider, syncResult);
			} catch (OperationCanceledException e) {
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		IBinder ret = null;
		ret = getSyncAdapter().getSyncAdapterBinder();
		return ret;
	}

	private SyncAdapterImpl getSyncAdapter() {
		if (sSyncAdapter == null)
			sSyncAdapter = new SyncAdapterImpl(this);
		return sSyncAdapter;
	}

	private static void performSync(Context context, Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult)
			throws OperationCanceledException {
		Log.i(TAG, "performSync: " + account.toString());
		mContentResolver = context.getContentResolver();

		String siteId = extras.getString("siteId");
		if (siteId != null) {
			try {
				Log.i(TAG, "performSync for SiteID: " + siteId);
				// TODO: Avoid use getExisint(). This is useless and costs one extra HTTP call
				// Sync the general SiteInfo
				SiteInfo siteInfo = User.getARSites().getExisting(siteId).getSiteInfo();
				storeSiteInfo(siteInfo);
				// Sync the augmented images
				List<AugmentedImage> augmentedImages = User.getARSites().getExisting(siteId).getAugmentedImages();
				storeAugmentedImages(augmentedImages);
			} catch (Exception e) {
				Log.e(TAG, "Failed to sync site with ID: " + siteId, e);
			}
		} else {
			try {
				// periodically sync and update trending sites
				Log.i(TAG, "performSync for TrendingSites: ");
				List<SiteInfoOverview> trendingSites = User.getARSites().getTrendingSites();
				updateTrendingSite(trendingSites);
			} catch (Exception e) {
				Log.e(TAG, "Failed to sync trending sites", e);
			}
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
			
			Log.d(TAG,"CONTENT SIZE URL: " + image.getImgContentPath());
			Log.d(TAG,"SITE ID: " + image.getSiteId());
			Log.d(TAG,"FULL SIZE IMAGE: " + image.getImgPath());
			
			// update or insert if not exist
			// FIXME: not thread-safe here
			if (mContentResolver.update(MarsContentProvider.getAugmentedImageUri(image.getImgId()),	
					values, null, null) == 0) {
				System.out.println("Inserted augmented image ");
				mContentResolver.insert(MarsContentProvider.CONTENT_URI_ALL_AUGMENTED_IMAGES, values);
			}		
			
			// TODO: Cut the records if there are too many records for the site
		}
	}

	private static void updateTrendingSite(List<SiteInfoOverview> sites) 
			throws RemoteException, OperationApplicationException {
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		
		// delete the old trending sites
		ContentProviderOperation.Builder op =
				ContentProviderOperation.newDelete(MarsContentProvider.CONTENT_URI_ALL_TRENDING_SITES);
		ops.add(op.build());
		
		// insert the new trending sites
		for(SiteInfoOverview siteInfoOverview : sites) {
			ContentValues values = new ContentValues();
			values.put(TrendingSitesTable.COLUMN_ADDRESS, siteInfoOverview.getAddress());
			values.put(TrendingSitesTable.COLUMN_DESC, siteInfoOverview.getDescription());
			values.put(TrendingSitesTable.COLUMN_LAT, siteInfoOverview.getLat());
			values.put(TrendingSitesTable.COLUMN_LON, siteInfoOverview.getLon());
			values.put(TrendingSitesTable.COLUMN_NAME, siteInfoOverview.getName());
			values.put(TrendingSitesTable.COLUMN_POSTER_IMAGE_CONTENT, siteInfoOverview.getPosterImageOverlayContent());
			values.put(TrendingSitesTable.COLUMN_POSTER_IMAGE_URL, siteInfoOverview.getPosterImageURL());
			values.put(TrendingSitesTable.COLUMN_SITE_ID, siteInfoOverview.getId());
			values.put(TrendingSitesTable.COLUMN_STATE, siteInfoOverview.getSiteState());
			values.put(TrendingSitesTable.COLUMN_NUM_AUGMENTED_IMAGES, siteInfoOverview.getNumAugmentedImages());	
			
			op = ContentProviderOperation.newInsert(MarsContentProvider.CONTENT_URI_ALL_TRENDING_SITES)
					.withValues(values);
			ops.add(op.build());	
		}
		
		mContentResolver.applyBatch(MarsContentProvider.AUTHORITY, ops);
		
		// update the augmented image table
		for(SiteInfoOverview siteInfoOverview : sites) {
			storeAugmentedImages(siteInfoOverview.getRecentlyAugmentedImages());
		}
	}
}