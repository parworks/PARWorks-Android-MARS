package com.parworks.mars.model.sync;

import android.accounts.Account;
import android.accounts.OperationCanceledException;
import android.app.Service;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.parworks.androidlibrary.response.SiteInfo;
import com.parworks.mars.model.db.TrendingSitesTable;
import com.parworks.mars.model.provider.MarsContentProvider;
import com.parworks.mars.model.provider.TrendingSitesContentProvider;
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

		//		List<SiteInfoOverview> trendingSites = User.getARSites().getTrendingSites();
		//		for(SiteInfoOverview site: trendingSites) {
		//			ContentValues cv = new ContentValues();
		//			cv.put(TrendingSitesTable.COLUMN_SITE_ID, site.getId());
		//			cv.put(TrendingSitesTable.COLUMN_DESC, site.getDescription());		
		//			context.getContentResolver().insert(TrendingSitesContentProvider.CONTENT_URI, cv);
		//		}

		String siteId = extras.getString("siteId");
		if (siteId != null) {
			
			Log.i(TAG, "performSync: " + authority + ", " + provider + ", " + syncResult);
			SiteInfo site = User.getARSites().getExisting(siteId).getSiteInfo();
			ContentValues cv = new ContentValues();
			cv.put(TrendingSitesTable.COLUMN_SITE_ID, site.getId());
			cv.put(TrendingSitesTable.COLUMN_DESC, site.getDescription());		
			//context.getContentResolver().insert(SitesContentProvider..CONTENT_URI, cv);
			context.getContentResolver().insert(MarsContentProvider.ALL_SITES_CONTENT_URI, cv);
			//context.getContentResolver().insert(TrendingSitesContentProvider.CONTENT_URI, cv);
		}
	}
}