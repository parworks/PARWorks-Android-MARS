package com.parworks.mars.view.siteexplorer;

import com.parworks.mars.R;
import com.parworks.mars.model.db.SiteInfoTable;
import com.parworks.mars.view.siteexplorer.BaseImageRetreiver.BaseImageRetreiverListener;
import com.parworks.mars.view.siteexplorer.ImageViewManager.ImageLoadedListener;

import android.app.Activity;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class PosterImageManager {
	
	private final ImageView mSiteImageView;
	private final ProgressBar mSiteImageProgressBar;
	private final Activity mActivity;
	private final String mSiteId;
	
	public static final String TAG = PosterImageManager.class.getName();
	
	public PosterImageManager(String siteId, ImageView siteImageView, ProgressBar siteImageProgressBar, Activity activity) {
		mSiteImageView = siteImageView;
		mSiteImageProgressBar = siteImageProgressBar;
		mActivity = activity;
		mSiteId = siteId;
	}
	
	
	public void setSiteImage(Cursor data) {
		String posterImageUrl = data.getString(data.getColumnIndex(SiteInfoTable.COLUMN_POSTER_IMAGE_URL));
		
		final ImageViewManager imageViewManager = new ImageViewManager();
		
		final ImageLoadedListener imageLoadedListener = new ImageLoadedListener() {
			
			@Override
			public void onImageLoaded() {
				showSiteImageView();
				
			}
		};
		
		ViewDimensionCalculator viewDimensionCalculator = new ViewDimensionCalculator(mActivity);
		final int width = viewDimensionCalculator.getScreenWidth();
		
		if(posterImageUrl != null ) {
			imageViewManager.setImageView(posterImageUrl,  mSiteImageView, imageLoadedListener);
		} else {
			imageViewManager.setImageView(posterImageUrl, mSiteImageView, imageLoadedListener);
			Log.d(TAG, "posterImageUrl was null. Using first base mSiteImageView.");
			BaseImageRetreiver.getFirstBaseImageUrl(mSiteId, new BaseImageRetreiverListener() {
				
				@Override
				public void firstBaseImageUrl(String url) {
					imageViewManager.setImageView(url, mSiteImageView, imageLoadedListener);
					
				}
			});
		}
	}
	
	private void showSiteImageView() {
		mSiteImageView.setVisibility(View.VISIBLE);
		mSiteImageProgressBar.setVisibility(View.INVISIBLE);
	}

}
