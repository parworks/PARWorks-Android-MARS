package com.parworks.mars.view.siteexplorer;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;

public class AugmentedImagesLoader implements LoaderCallbacks<Cursor> {
	public interface AugmentedImagesLoaderListener {
		public void imagesLoaded(Cursor data);
	}
	
	
	private String mSiteId;
	private Context mContext;
	private AugmentedImagesLoaderListener mListener;
	
	public AugmentedImagesLoader(String siteId, Context context, AugmentedImagesLoaderListener listener) {
		mSiteId = siteId;
		mContext = context;
		mListener = listener;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// TODO Auto-generated method stub
		
	}

}
