/*******************************************************************************
 * Copyright 2013 PAR Works, Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.parworks.mars.view.siteexplorer;

import com.parworks.mars.model.db.CommentsTable;
import com.parworks.mars.model.provider.MarsContentProvider;
import com.parworks.mars.utils.Utilities;

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
	public Loader<Cursor> onCreateLoader(int id, Bundle arg1) {
		Log.d(Utilities.DEBUG_TAG_SYNC, "Comments Loader - onCreateLoader : " + id);
		String [] projection = CommentsTable.ALL_COLUMNS;
		CursorLoader cursorLoader = new CursorLoader(mContext, MarsContentProvider.getCommentsUri(mSiteId),projection,null,null,null);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		Log.d(Utilities.DEBUG_TAG_SYNC,"Comments Loader - onLoadFinished");
		mListener.onCommentsLoaded(data);		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		Log.d(Utilities.DEBUG_TAG_SYNC, "Comments Loader - onLoaderReset");
		Log.d(TAG,"onLoaderReset -- UNIMPLEMENTED");		
	}

}
