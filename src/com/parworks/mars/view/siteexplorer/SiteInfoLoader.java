package com.parworks.mars.view.siteexplorer;

import com.parworks.mars.model.db.SiteInfoTable;
import com.parworks.mars.model.provider.MarsContentProvider;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

public class SiteInfoLoader implements LoaderCallbacks<Cursor> {
	public interface SiteInfoLoaderListener {
		public void onSiteLoaded(Cursor siteData);
	}
	
	private final SiteInfoLoaderListener mListener;
	private final String mSiteId;
	private final Context mContext;
	
	public static final String TAG = SiteInfoLoader.class.getName();
	
	public SiteInfoLoader(String siteId, Context context, SiteInfoLoaderListener listener) {
		mSiteId = siteId;
		mListener = listener;
		mContext = context;
	}
	
	
	

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		String[] projection = SiteInfoTable.ALL_COLUMNS;
		CursorLoader cursorLoader = new CursorLoader(mContext, MarsContentProvider.getSiteUri(mSiteId),projection,null,null,null);
		Log.d(TAG,"onCreateLoader");
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		Log.d(TAG,"onLoadFinished");
		mListener.onSiteLoaded(data);
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		Log.d(TAG,"onLoaderReset");		
		
	}

}
