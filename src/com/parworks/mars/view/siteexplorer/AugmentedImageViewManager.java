package com.parworks.mars.view.siteexplorer;

import java.util.ArrayList;
import java.util.List;

import com.parworks.mars.cache.BitmapCache;
import com.parworks.mars.cache.BitmapWorkerTask;
import com.parworks.mars.cache.BitmapWorkerTask.BitmapWorkerListener;
import com.parworks.mars.model.db.AugmentedImagesTable;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ProgressBar;

public class AugmentedImageViewManager {
	public static final String TAG = AugmentedImageViewManager.class.getName();
	
	private final String mSiteId;
	private final ProgressBar mAugmentedImagesProgressBar;
	private final Gallery mAugmentedImagesGallery;
	private final Context mContext;
	private final AugmentedImageAdapter mAdapter;
	private final List<Bitmap> mBitmaps;
	
	public AugmentedImageViewManager(String siteId, Context context, ProgressBar augmentedImagesProgressBar, Gallery augmentedImagesGridView) {
		mSiteId = siteId;
		mContext = context;
		mAugmentedImagesGallery = augmentedImagesGridView;
		mAugmentedImagesProgressBar = augmentedImagesProgressBar;
		mBitmaps = new ArrayList<Bitmap>();
		mAdapter = new AugmentedImageAdapter(context,mBitmaps);
		mAugmentedImagesGallery.setAdapter(mAdapter);
		mAugmentedImagesGallery.setSpacing(5);
	}
	
	public void setAugmentedImages(Cursor data) {
		if(data.getCount() <= 0 ) {
			Log.d(TAG,"Augmented images cursor has no data. Count was: " + data.getCount() );
			return;
		}
		for(data.moveToFirst();!data.isAfterLast();data.moveToNext()) {
	    	String url = data.getString(data.getColumnIndex(AugmentedImagesTable.COLUMN_GALLERY_SIZE_URL));
	    	if(url != null) {
	    		Log.d(TAG,"url is: " + url);
	    		Bitmap augmentedBitmap = BitmapCache.get().getBitmap(BitmapCache.getImageKeyFromURL(url));
	    		if(augmentedBitmap == null) {
					Log.d(TAG, "Bitmap not found in cache, start to download it.");
					new BitmapWorkerTask(url, new BitmapWorkerListener() {					
						@Override
						public void bitmapLoaded(Bitmap bitmap) {
							addBitmap(bitmap);
							
						}
					}).execute();
	    		} else {
	    			addBitmap(augmentedBitmap);
	    		}
	    		
	    		
	    		
	    		
	    		
	    	} else {
	    		continue;
	    	}
	    }
	    data.close();
		
	}
	
	private void addBitmap(Bitmap bitmap) {
		Log.d(TAG,"Adding bitmap to gridview");
		mBitmaps.add(bitmap);
		mAdapter.notifyDataSetChanged();
		showAugmentedImagesGridView();
		if(mBitmaps.size() == 1) {
			mAugmentedImagesGallery.setSelection(1);
		}
	}
	
	private void showAugmentedImagesGridView() {
		mAugmentedImagesGallery.setVisibility(View.VISIBLE);
		mAugmentedImagesProgressBar.setVisibility(View.INVISIBLE);
	}

}
