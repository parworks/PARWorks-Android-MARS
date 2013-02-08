package com.parworks.mars.view.siteexplorer;


import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parworks.mars.R;
import com.parworks.mars.cache.BitmapCache;
import com.parworks.mars.cache.BitmapWorkerTask;
import com.parworks.mars.cache.BitmapWorkerTask.BitmapWorkerListener;
import com.parworks.mars.model.db.SiteInfoTable;
import com.parworks.mars.model.provider.MarsContentProvider;
import com.parworks.mars.model.sync.SyncHelper;
import com.parworks.mars.utils.ImageLoader;
import com.parworks.mars.utils.ImageLoader.ImageLoaderListener;
import com.parworks.mars.view.siteexplorer.BaseImageRetreiver.BaseImageRetreiverListener;

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
		//ImageLoader imageLoader = new ImageLoader(this);
		
		//String url = "http://lstat.kuleuven.be/research/lsd/lsd2006/auditorium_small.jpg";
	//	imageLoader.DisplayImage(url, this, mapView);
		
		//final ImageView siteImageView = (ImageView) findViewById(R.id.imageViewSiteImage);
//		siteImageView.setScaleType(ScaleType.MATRIX);
//		BaseImageRetreiver baseImageRetreiver = new BaseImageRetreiver(this);
//		
//		int screenWidth = getDesiredImageViewWidth();
//		baseImageRetreiver.setImageViewToBaseImage(mSiteId, siteImageView, this,screenWidth, new ImageLoaderListener() {
//
//			@Override
//			public void imageLoaded() {
//				siteImageView.setVisibility(View.VISIBLE);
//				((ProgressBar)findViewById(R.id.progressBarSiteImage)).setVisibility(View.INVISIBLE);
//				
//			}
//			
//		});
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
	
	private void loadAndSetImageBitmap(String url, final ImageView imageView) {
		Bitmap posterImageBitmap = BitmapCache.get().getBitmap(
				BitmapCache.getImageKeyFromURL(url));
		if (posterImageBitmap == null) {
			Log.d(TAG, "Bitmap not found in cache, start to download it.");
			new BitmapWorkerTask(url, imageView, new BitmapWorkerListener() {
				
				@Override
				public void bitmapLoaded() {
					setCorrectImageViewSize(imageView);
					showSiteImageView();
					
				}
			}).execute();
		} else {
			imageView.setImageBitmap(posterImageBitmap);
			setCorrectImageViewSize(imageView);
			showSiteImageView();
		}
	}
	
	private void setCorrectImageViewSize(ImageView imageView) {
		Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
		int width = getDesiredImageViewWidth();
		bitmap = Bitmap.createScaledBitmap(bitmap, width, calculateHeight(width,bitmap), true);
		imageView.setImageBitmap(bitmap);
		
	}
	private void showSiteImageView() {
		ImageView imageView = (ImageView) findViewById(R.id.imageViewSiteImage);
		imageView.setVisibility(View.VISIBLE);
		((ProgressBar)findViewById(R.id.progressBarSiteImage)).setVisibility(View.INVISIBLE);
	}
	
    private int calculateHeight(Integer width,Bitmap bitmap) {
		float startingWidth = bitmap.getWidth();
		float startingHeight = bitmap.getHeight();
		float widthHeightRatio = startingWidth/startingHeight;
		return Math.round(width / widthHeightRatio);
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
		
		// Ready to show the image
		final ImageView imageView = (ImageView) findViewById(R.id.imageViewSiteImage);
		// retrieve the PosterImageURL
		String posterImageUrl = data.getString(data.getColumnIndex(SiteInfoTable.COLUMN_POSTER_IMAGE_URL));
		
		if(posterImageUrl == null ) {
			Log.d(TAG, "posterImageUrl was null. Using first base image.");
			BaseImageRetreiver.getFirstBaseImageUrl(mSiteId, new BaseImageRetreiverListener() {
				
				@Override
				public void firstBaseImageUrl(String url) {
					loadAndSetImageBitmap(url,imageView);
					
				}
			});
		} else {
			loadAndSetImageBitmap(posterImageUrl,imageView);
		}
	}
}
