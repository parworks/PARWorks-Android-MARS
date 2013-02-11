package com.parworks.mars.view.siteexplorer;

import com.parworks.mars.model.db.AugmentedImagesTable;
import com.parworks.mars.model.provider.MarsContentProvider;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

public class AugmentedImagesLoader implements LoaderCallbacks<Cursor> {
	public interface AugmentedImagesLoaderListener {
		public void onImagesLoaded(Cursor data);
	}
	
	
	private String mSiteId;
	private Context mContext;
	private AugmentedImagesLoaderListener mListener;
	
	public static final String TAG = AugmentedImagesLoader.class.getName();
	
	public AugmentedImagesLoader(String siteId, Context context, AugmentedImagesLoaderListener listener) {
		mSiteId = siteId;
		mContext = context;
		mListener = listener;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		String[] projection = AugmentedImagesTable.ALL_COLUMNS;
		CursorLoader cursorLoader = new CursorLoader(mContext, MarsContentProvider.getSiteAugmentedImagesUri(mSiteId),projection,null,null,null);
		Log.d(TAG,"onCreateLoader");
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		Log.d(TAG,"onLoadFinished");
		mListener.onImagesLoaded(data);
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		Log.d(TAG,"onLoaderReset -- UNIMPLEMENTED");
		
	}

}
