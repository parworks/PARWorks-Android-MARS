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
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;

public class PosterImageManager {
	
	private final ImageView mSiteImageView;
	private final ProgressBar mSiteImageProgressBar;
	private final Activity mActivity;
	private final String mSiteId;
	private final ViewDimensionCalculator mViewDimensionCalculator;
	
	public static final String TAG = PosterImageManager.class.getName();
	
	public PosterImageManager(String siteId, ImageView siteImageView, ProgressBar siteImageProgressBar, Activity activity) {
		mSiteImageView = siteImageView;
		mSiteImageView.setAdjustViewBounds(true);
		mSiteImageView.setScaleType(ScaleType.CENTER_CROP);
		mSiteImageProgressBar = siteImageProgressBar;
		mActivity = activity;
		mSiteId = siteId;
		mViewDimensionCalculator = new ViewDimensionCalculator(mActivity);
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
		
		if(posterImageUrl != null ) {
			imageViewManager.setImageView(posterImageUrl,  mSiteImageView, imageLoadedListener);
			setPosterImageSize();
		} else {
			imageViewManager.setImageView(posterImageUrl, mSiteImageView, imageLoadedListener);
			Log.d(TAG, "posterImageUrl was null. Using first base mSiteImageView.");
			BaseImageRetreiver.getFirstBaseImageUrl(mSiteId, new BaseImageRetreiverListener() {
				
				@Override
				public void firstBaseImageUrl(String url) {
					imageViewManager.setImageView(url, mSiteImageView, imageLoadedListener);
					setPosterImageSize();
					
				}
			});
		}
	}
	
	private void showSiteImageView() {
		mSiteImageView.setVisibility(View.VISIBLE);
		mSiteImageProgressBar.setVisibility(View.INVISIBLE);
	}
	private void setPosterImageSize() {
		mSiteImageView.getLayoutParams().width = mViewDimensionCalculator.getScreenWidth();
		mSiteImageView.getLayoutParams().height = mViewDimensionCalculator.getScreenWidth()/2;
	}

}
