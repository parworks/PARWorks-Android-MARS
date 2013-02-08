package com.parworks.mars.view.siteexplorer;


import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parworks.mars.R;
import com.parworks.mars.model.db.SiteInfoTable;
import com.parworks.mars.model.provider.MarsContentProvider;
import com.parworks.mars.model.sync.SyncHelper;
import com.parworks.mars.utils.ImageLoader;
import com.parworks.mars.utils.ImageLoader.ImageLoaderListener;

public class ExploreActivity extends FragmentActivity implements LoaderCallbacks<Cursor>{
	
	private String mSiteId;
	
	public static final String SITE_ID_ARGUMENT_KEY = "siteIdKey";
	
	public static final String TAG = ExploreActivity.class.getName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_explore);
	//	TemporarySiteSyncMethods.syncUserSites(this);
		
		mSiteId = getIntent().getStringExtra(SITE_ID_ARGUMENT_KEY);
		
		// sync the site
		SyncHelper.syncSite(mSiteId);
		
		getSupportLoaderManager().initLoader(0, null, this);
		
		ImageView mapView = (ImageView) findViewById(R.id.imageViewMap);
		ImageLoader imageLoader = new ImageLoader(this);
		
		//String url = "http://lstat.kuleuven.be/research/lsd/lsd2006/auditorium_small.jpg";
	//	imageLoader.DisplayImage(url, this, mapView);
		
		final ImageView siteImageView = (ImageView) findViewById(R.id.imageViewSiteImage);
//		siteImageView.setScaleType(ScaleType.MATRIX);
		BaseImageRetreiver baseImageRetreiver = new BaseImageRetreiver(this);
		
		int screenWidth = getDesiredImageViewWidth();
		baseImageRetreiver.setImageViewToBaseImage(mSiteId, siteImageView, this,screenWidth, new ImageLoaderListener() {

			@Override
			public void imageLoaded() {
				siteImageView.setVisibility(View.VISIBLE);
				((ProgressBar)findViewById(R.id.progressBarSiteImage)).setVisibility(View.INVISIBLE);
				
			}
			
		});
	}
	
	private int getDesiredImageViewWidth() {
		Display display = getWindowManager().getDefaultDisplay();
//		Point size = new Point();
//		display.getSize(size);
//		int width = size.x;
		return display.getWidth();
	}
	
	

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle arg1) {
		String[] projection = SiteInfoTable.ALL_COLUMNS;
		CursorLoader cursorLoader = new CursorLoader(this, MarsContentProvider.getSiteUri(mSiteId),projection,null,null,null);
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
		String siteDesc = data.getString(data.getColumnIndex(SiteInfoTable.COLUMN_DESC));
		TextView addressTextView = (TextView) findViewById(R.id.textViewSiteAddress);
		addressTextView.setText(siteDesc);
	}

}
