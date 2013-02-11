package com.parworks.mars.view.siteexplorer;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parworks.mars.R;
import com.parworks.mars.model.db.SiteInfoTable;
import com.parworks.mars.model.sync.SyncHelper;
import com.parworks.mars.view.siteexplorer.AugmentedImagesLoader.AugmentedImagesLoaderListener;
import com.parworks.mars.view.siteexplorer.SiteInfoLoader.SiteInfoLoaderListener;

public class ExploreActivity extends FragmentActivity { 
	
	public static final String SITE_ID_ARGUMENT_KEY = "siteIdKey";
	public static final String TAG = ExploreActivity.class.getName();
	
	private String mSiteId;
	private static final int SITE_INFO_LOADER_ID = 0;
	private static final int AUGMENTED_IMAGES_LOADER_ID = 1;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_explore);
		
		mSiteId = getIntent().getStringExtra(SITE_ID_ARGUMENT_KEY);
		
		// sync the site
		SyncHelper.syncSite(mSiteId);
				
		SiteInfoLoader siteInfoLoader = new SiteInfoLoader(mSiteId, this, new SiteInfoLoaderListener() {
			
			@Override
			public void onSiteLoaded(Cursor siteData) {
				loadSiteInfoIntoUi(siteData);
				
			}
		});
		getSupportLoaderManager().initLoader(SITE_INFO_LOADER_ID, null, siteInfoLoader);
		
		AugmentedImagesLoader augmentedImagesLoader = new AugmentedImagesLoader(mSiteId, this, new AugmentedImagesLoaderListener() {
			
			@Override
			public void onImagesLoaded(Cursor data) {
				loadAugmentedImagesIntoUi(data);
				
			}
		});
		getSupportLoaderManager().initLoader(AUGMENTED_IMAGES_LOADER_ID, null, augmentedImagesLoader);
		
		
	}

	
	private void loadSiteInfoIntoUi(Cursor data) {
		if(data.getCount() == 0 ) {
			return;
		}
		//set site name
		String siteName = data.getString(data.getColumnIndex(SiteInfoTable.COLUMN_SITE_ID));
		TextView nameTextView = (TextView) findViewById(R.id.textViewSiteName);
		nameTextView.setText(siteName);
		
		//set site address
		String siteDesc = data.getString(data.getColumnIndex(SiteInfoTable.COLUMN_DESC));
		TextView addressTextView = (TextView) findViewById(R.id.textViewSiteAddress);
		addressTextView.setText(siteDesc);
		
		//let siteImageManager handle the SiteImageView
		ImageView siteImageView = (ImageView) findViewById(R.id.imageViewSiteImage);
		ProgressBar siteImageProgressBar = (ProgressBar) findViewById(R.id.progressBarSiteImage);
		SiteImageManager siteImageManager = new SiteImageManager(mSiteId, siteImageView, siteImageProgressBar, this);
		siteImageManager.setSiteImage(data);
		
		//let mapImageManager handle the map view
		ImageView mapImageView = (ImageView) findViewById(R.id.imageViewMap);
		ProgressBar mapProgressBar = (ProgressBar) findViewById(R.id.progressBarMapView);
		MapImageManager mapImageManager = new MapImageManager(mSiteId, mapImageView, mapProgressBar, this);
		mapImageManager.setMapView(data);
	}	

	private void loadAugmentedImagesIntoUi(Cursor data) {
		ProgressBar augmentedImagesProgressBar = (ProgressBar) findViewById(R.id.progressBarAugmentedPhotos);
		GridView augmentedImagesGridView = (GridView) findViewById(R.id.gridViewAugmentedPhotos);
		AugmentedImageViewManager augmentedImagesViewManager = new AugmentedImageViewManager(mSiteId, this, augmentedImagesProgressBar, augmentedImagesGridView);
		augmentedImagesViewManager.setAugmentedImages(data);
		
	}
}
