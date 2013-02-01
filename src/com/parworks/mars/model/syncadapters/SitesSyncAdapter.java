package com.parworks.mars.model.syncadapters;

import java.util.List;

import com.parworks.androidlibrary.ar.ARErrorListener;
import com.parworks.androidlibrary.ar.ARListener;
import com.parworks.androidlibrary.ar.ARSite;
import com.parworks.androidlibrary.ar.ARSites;
import com.parworks.androidlibrary.response.ImageOverlayInfo;
import com.parworks.androidlibrary.response.SiteInfo;
import com.parworks.mars.Mars;
import com.parworks.mars.model.providers.SitesContentHelper;
import com.parworks.mars.utils.User;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class SitesSyncAdapter extends AbstractThreadedSyncAdapter {
	
	private Context mContext;
	
	public static final String TAG = SitesSyncAdapter.class.getName();

	public SitesSyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
		mContext = context;
	}

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {
		
	}
	

}
