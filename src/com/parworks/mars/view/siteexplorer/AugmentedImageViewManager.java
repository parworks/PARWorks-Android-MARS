package com.parworks.mars.view.siteexplorer;

import com.parworks.mars.model.db.AugmentedImagesTable;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;

public class AugmentedImageViewManager {
	public static final String TAG = AugmentedImageViewManager.class.getName();
	
	private final String mSiteId;
	private final ProgressBar mAugmentedImagesProgressBar;
	private final GridView mAugmentedImagesGridView;
	private final Context mContext;
	
	public AugmentedImageViewManager(String siteId, Context context, ProgressBar augmentedImagesProgressBar, GridView augmentedImagesGridView) {
		mSiteId = siteId;
		mContext = context;
		mAugmentedImagesGridView = augmentedImagesGridView;
		mAugmentedImagesProgressBar = augmentedImagesProgressBar;
	}
	
	public void setAugmentedImages(Cursor data) {
		if(data.getCount() <= 0 ) {
			Log.d(TAG,"Augmented images cursor has no data. Count was: " + data.getCount() );
			return;
		}
		
		data.moveToFirst();
	    while (!data.isAfterLast()) {
			Log.d(TAG,"url is: " + data.getString(data.getColumnIndex(AugmentedImagesTable.COLUMN_FULL_SIZE_URL)));
			Log.d(TAG,"hihihhi");
	        data.moveToNext();
	    }
	    data.close();
		
	}
	
	private void showAugmentedImagesGridView() {
		mAugmentedImagesGridView.setVisibility(View.VISIBLE);
		mAugmentedImagesProgressBar.setVisibility(View.INVISIBLE);
	}

}
