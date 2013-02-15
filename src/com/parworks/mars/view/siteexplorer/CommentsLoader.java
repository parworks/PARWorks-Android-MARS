package com.parworks.mars.view.siteexplorer;

import com.parworks.mars.model.db.CommentsTable;
import com.parworks.mars.model.provider.MarsContentProvider;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

public class CommentsLoader implements LoaderCallbacks<Cursor>{
	public interface CommentsLoaderListener {
		public void onCommentsLoaded(Cursor data);
	}
	
	private String mSiteId;
	private Context mContext;
	private CommentsLoaderListener mListener;
	
	public static final String TAG = CommentsLoader.class.getName();
	
	public CommentsLoader(String siteId, Context context, CommentsLoaderListener listener) {
		mSiteId = siteId;
		mContext = context;
		mListener = listener;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		String [] project = CommentsTable.ALL_COLUMNS;
		CursorLoader cursorLoader = new CursorLoader(mContext, MarsContentProvider.getCommentsUri(mSiteId),project,null,null,null);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor data) {
		mListener.onCommentsLoaded(data);
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		Log.d(TAG,"onLoaderReset -- UNIMPLEMENTED");
		
	}

}
