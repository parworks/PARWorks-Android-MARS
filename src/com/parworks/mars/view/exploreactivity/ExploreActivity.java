package com.parworks.mars.view.exploreactivity;

import com.parworks.mars.R;
import com.parworks.mars.model.databasetables.SiteInfoTable;
import com.parworks.mars.model.providers.SitesContentProvider;
import com.parworks.mars.model.syncadapters.TemporarySiteSyncMethods;
import com.parworks.mars.staticmaps.utils.ImageLoader;
import com.parworks.mars.utils.User;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ExploreActivity extends Activity implements LoaderCallbacks<Cursor>{
	
	private String mSiteId = "FirstSiteLatLon";
	
	public static final String TAG = ExploreActivity.class.getName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_explore);
		TemporarySiteSyncMethods.syncUserSites(this);
		
		getLoaderManager().initLoader(0, null, this);
		
		ImageView mapView = (ImageView) findViewById(R.id.imageViewMap);
		ImageLoader imageLoader = new ImageLoader(this);
		
		String url = "http://www.thefaeriesandangelsmagazine.com/resources/VEN-virtual-enterprise-network-business-opportunities-small-fish_id799929_size485.jpg?timestamp=1267988673507";
		imageLoader.DisplayImage(url, this, mapView);
	}
	
	

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle arg1) {
		String[] projection = SiteInfoTable.ALL_COLUMNS;
		CursorLoader cursorLoader = new CursorLoader(this, SitesContentProvider.getSiteUri(mSiteId),projection,null,null,null);
		Log.d(TAG,"onCreateLoader");
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		Log.d(TAG,"onLoadFinished");
		loadDataIntoUi(data);
		
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		Log.d(TAG,"onLoaderReset");
		
	}
	
	private void loadDataIntoUi(Cursor data) {
		if(data.getCount() == 0 ) {
			return;
		}
		String siteName = data.getString(data.getColumnIndex(SiteInfoTable.COLUMN_NAME));
		TextView nameTextView = (TextView) findViewById(R.id.textViewSiteName);
		nameTextView.setText(siteName);
	}

}
