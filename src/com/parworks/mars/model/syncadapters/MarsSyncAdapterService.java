package com.parworks.mars.model.syncadapters;

import java.util.List;

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
import com.parworks.androidlibrary.response.SiteInfoOverview;
import com.parworks.mars.model.databasetables.TrendingSitesTable;
import com.parworks.mars.model.providers.TrendingSitesContentProvider;
import com.parworks.mars.utils.User;

public class MarsSyncAdapterService extends Service {

	private static final String TAG = "MarsSyncAdapterService";

	private static SyncAdapterImpl sSyncAdapter = null;
	private static ContentResolver mContentResolver = null;

	public MarsSyncAdapterService() {
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
				MarsSyncAdapterService.performSync(mContext, account, extras, authority, provider, syncResult);
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
			Log.i(TAG, "performSync: " + account.toString());
			Log.i(TAG, "performSync: " + authority + ", " + provider + ", " + syncResult);
			SiteInfo site = User.getARSites().getExisting(siteId).getSiteInfo();
			ContentValues cv = new ContentValues();
			cv.put(TrendingSitesTable.COLUMN_SITE_ID, site.getId());
			cv.put(TrendingSitesTable.COLUMN_DESC, site.getDescription());		
			context.getContentResolver().insert(TrendingSitesContentProvider.CONTENT_URI, cv);
		}
	}
}