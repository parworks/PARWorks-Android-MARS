package com.parworks.mars.view.search;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parworks.mars.R;
import com.parworks.mars.cache.BitmapCache;
import com.parworks.mars.cache.BitmapWorkerTask;
import com.parworks.mars.cache.BitmapWorkerTask.BitmapWorkerListener;
import com.parworks.mars.model.db.SiteInfoTable;
import com.parworks.mars.model.provider.MarsContentProvider;
import com.parworks.mars.view.siteexplorer.ExploreActivity;

public class SearchResultFragment extends Fragment implements LoaderCallbacks<Cursor> {

	private static final String TAG = "SearchResultFragment";
	private static final int SEARCH_SITE_INFO_LOADER_ID = 33;
	
	private List<String> siteIds;
	private SearchResultListAdapter adapter;

	public SearchResultFragment(List<String> siteIds) {
		this.siteIds = siteIds;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// init Loader for TrendingSites
		this.getLoaderManager().initLoader(SEARCH_SITE_INFO_LOADER_ID, null, this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_search_result, null);
		
		// initialize search result		
		final ListView lv = (ListView) v.findViewById(R.id.searchResultList);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				SearchResultItem item = (SearchResultItem) lv.getItemAtPosition(position);
				Intent i = new Intent(SearchResultFragment.this.getActivity(), ExploreActivity.class);
				i.putExtra(ExploreActivity.SITE_ID_ARGUMENT_KEY, item.siteId);
				startActivity(i);				
			}
		});
		adapter = new SearchResultListAdapter(this.getActivity());
		for(String id : siteIds) {
			adapter.add(new SearchResultItem(id, null));
		}
		lv.setAdapter(adapter);
		
		v.requestFocus();
		return v;
	}

	private class SearchResultItem {
		public String siteId;
		public String posterImageUrl;
		public SearchResultItem(String siteId, String posterImageUrl) {
			this.siteId = siteId;
			this.posterImageUrl = posterImageUrl;
		}
	}

	public class SearchResultListAdapter extends ArrayAdapter<SearchResultItem> {

		public SearchResultListAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_result_list_row, null);
			}
			
			// display poster image
			final ImageView imageView = (ImageView) convertView.findViewById(R.id.searchSitePosterImage);			
			String imageUrl = getItem(position).posterImageUrl;
			if (imageUrl != null && !TextUtils.isEmpty(imageUrl)) {
				Bitmap posterImageBitmap = BitmapCache.get().getBitmap(
						BitmapCache.getImageKeyFromURL(imageUrl));
				if (posterImageBitmap == null) {
					new BitmapWorkerTask(imageUrl, new BitmapWorkerListener() {					
						@Override
						public void bitmapLoaded(Bitmap bitmap) {			
							imageView.setImageBitmap(bitmap);
						}
					}).execute();
				} else {	
					imageView.setImageBitmap(posterImageBitmap);
				}
			}			
			
			TextView title = (TextView) convertView.findViewById(R.id.searchSiteName);
			title.setText(getItem(position).siteId);

			return convertView;
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		if (siteIds == null || siteIds.size() == 0) {
			return null;
		}
		
		String[] projection = SiteInfoTable.ALL_COLUMNS;		
		// construct the select args
		String selection = getSelection(siteIds.size());
		String[] selectionArgs = new String[siteIds.size()]; 
		for(int i = 0; i < siteIds.size(); i++) {
			selectionArgs[i] = siteIds.get(i);
		}
		// qeury all the siteIds
		CursorLoader cursorLoader = new CursorLoader(this.getActivity(), 
				MarsContentProvider.CONTENT_URI_ALL_SITES, projection, selection, selectionArgs, null);
		return cursorLoader;
	}
	
	private static String getSelection(int args) {
	    StringBuilder sb = new StringBuilder(SiteInfoTable.COLUMN_SITE_ID + " IN  (");
	    boolean first = true;
	    for (int i = 0; i < args; i ++) {
	        if (first) {
	            first = false;
	        } else {
	            sb.append(',');
	        }
	        sb.append('?');
	    }
	    sb.append(')');
	    return sb.toString();
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (data.getCount() == 0) {
			return;
		}
		Log.d(TAG, "Finished loading site info");
		adapter.clear();
		// init new data content for the adapter
		while(!data.isAfterLast()) {
			String siteId = data.getString(
					data.getColumnIndex(SiteInfoTable.COLUMN_SITE_ID));
			String posterUrl = data.getString(
					data.getColumnIndex(SiteInfoTable.COLUMN_POSTER_IMAGE_URL));

			adapter.add(new SearchResultItem(siteId, posterUrl));
			data.moveToNext();
		}	
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		
	}
}
