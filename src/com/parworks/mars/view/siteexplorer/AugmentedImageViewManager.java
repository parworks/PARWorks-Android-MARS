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
	
	private static final String AUGMENTED_IMAGE_SIZE_COLUMN = AugmentedImagesTable.COLUMN_CONTENT_SIZE_URL;
	
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
		for(data.moveToFirst();!data.isLast();data.moveToNext()) {
			Log.d(TAG,"url is: " + data.getString(data.getColumnIndex(AUGMENTED_IMAGE_SIZE_COLUMN)));
			Log.d(TAG,"hihihhi");
		}
		
	}
	
	private void showAugmentedImagesGridView() {
		mAugmentedImagesGridView.setVisibility(View.VISIBLE);
		mAugmentedImagesProgressBar.setVisibility(View.INVISIBLE);
	}

}
