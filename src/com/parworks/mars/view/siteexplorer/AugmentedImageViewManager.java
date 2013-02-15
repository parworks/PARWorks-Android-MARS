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
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AugmentedImageViewManager {
	public static final String TAG = AugmentedImageViewManager.class.getName();
	
	private final String mSiteId;
	private final ProgressBar mAugmentedImagesProgressBar;
	private final LinearLayout mAugmentedImagesLayout;
	private final Context mContext;
	private final AugmentedImageAdapter mAdapter;
	private final List<Bitmap> mBitmaps;
	private final TextView mAugmentedImagesTotalTextView;
	
	private final static int AUGMENTED_IMAGE_HORIZONTAL_MARGINS = 5; //pixels
	
	public AugmentedImageViewManager(String siteId, Context context, ProgressBar augmentedImagesProgressBar, LinearLayout augmentedImagesLayout, TextView augmentedImagesTotalTextView) {
		mSiteId = siteId;
		mContext = context;
		mAugmentedImagesProgressBar = augmentedImagesProgressBar;
		mBitmaps = new ArrayList<Bitmap>();
		mAdapter = new AugmentedImageAdapter(context,mBitmaps);
		mAugmentedImagesLayout = augmentedImagesLayout;
		mAugmentedImagesTotalTextView = augmentedImagesTotalTextView;
	}
	
	public void setAugmentedImages(Cursor data) {
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
	    showAugmentedImagesView();
	    setAugmentedImagesTotalTextView(data.getCount());
	    data.close();
		
	}
	
	private void addBitmap(Bitmap bitmap) {
		ImageView imageView = new ImageView(mContext);
		imageView.setImageBitmap(bitmap);
		LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		imageViewParams.setMargins(AUGMENTED_IMAGE_HORIZONTAL_MARGINS, 0, AUGMENTED_IMAGE_HORIZONTAL_MARGINS, 0);
		imageView.setLayoutParams(imageViewParams);
		mAugmentedImagesLayout.addView(imageView);
	}
	
	private void showAugmentedImagesView() {
		mAugmentedImagesLayout.setVisibility(View.VISIBLE);
		mAugmentedImagesProgressBar.setVisibility(View.INVISIBLE);
	}
	private void setAugmentedImagesTotalTextView(int imagesTotal) {
		String text;
		if(imagesTotal == 1) {
			text = " augmented image";
		} else {
			text = " augmented images";
		}
		mAugmentedImagesTotalTextView.setText(imagesTotal + text);
	}

}
