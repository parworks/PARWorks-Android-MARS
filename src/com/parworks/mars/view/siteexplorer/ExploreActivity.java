package com.parworks.mars.view.siteexplorer;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.parworks.mars.R;
import com.parworks.mars.model.db.SiteInfoTable;
import com.parworks.mars.model.sync.SyncHelper;
import com.parworks.mars.view.siteexplorer.AugmentedImagesLoader.AugmentedImagesLoaderListener;
import com.parworks.mars.view.siteexplorer.SiteInfoLoader.SiteInfoLoaderListener;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public class ExploreActivity extends SherlockFragmentActivity { 
	
	public static final String SITE_ID_ARGUMENT_KEY = "siteIdKey";
	public static final String TAG = ExploreActivity.class.getName();
	
	private String mSiteId;
	private static final int SITE_INFO_LOADER_ID = 0;
	private static final int AUGMENTED_IMAGES_LOADER_ID = 1;
	
	private View mLayoutView;
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLayoutView = getLayoutInflater().inflate(R.layout.activity_explore, null);
		setContentView(mLayoutView);
		
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
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		}
		return super.onOptionsItemSelected(item);
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
		PosterImageManager siteImageManager = new PosterImageManager(mSiteId, siteImageView, siteImageProgressBar, this);
		siteImageManager.setSiteImage(data);
		
		//let mapImageManager handle the map view
		ImageView mapImageView = (ImageView) findViewById(R.id.imageViewMap);
		ProgressBar mapProgressBar = (ProgressBar) findViewById(R.id.progressBarMapView);
		MapImageManager mapImageManager = new MapImageManager(mSiteId, mapImageView, mapProgressBar, this);
		mapImageManager.setMapView(data);
	}	

	private void loadAugmentedImagesIntoUi(Cursor data) {
		ProgressBar augmentedImagesProgressBar = (ProgressBar) findViewById(R.id.progressBarAugmentedPhotos);
//		Gallery augmentedImagesGallery = (Gallery) findViewById(R.id.galleryAugmentedPhotos);
		LinearLayout augmentedImagesLayout = (LinearLayout) findViewById(R.id.linearLayoutAugmentedImagesLayout);
		AugmentedImageViewManager augmentedImagesViewManager = new AugmentedImageViewManager(mSiteId, this, augmentedImagesProgressBar, augmentedImagesLayout);
		augmentedImagesViewManager.setAugmentedImages(data);
		
	}
}
