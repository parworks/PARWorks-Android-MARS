/*******************************************************************************
 * Copyright 2013 PAR Works, Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.parworks.mars.view.siteexplorer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parworks.androidlibrary.response.AugmentImageResultResponse;
import com.parworks.arviewer.ARViewerActivity;
import com.parworks.arviewer.utils.AugmentedDataUtils;
import com.parworks.arviewer.utils.ImageUtils;
import com.parworks.mars.R;
import com.parworks.mars.cache.BitmapCache;
import com.parworks.mars.cache.BitmapWorkerTask;
import com.parworks.mars.cache.BitmapWorkerTask.BitmapWorkerListener;
import com.parworks.mars.model.db.AugmentedImagesTable;
import com.parworks.mars.utils.JsonMapper;
import com.parworks.mars.utils.Utilities;

public class AugmentedImageViewManager {
	
	public static final String TAG = AugmentedImageViewManager.class.getName();
	
	private static Boolean placeHoldersAdded = false; 
	
	private final String mSiteId;
	private final LinearLayout mAugmentedImagesLayout;
	private final Context mContext;
	private final AugmentedImageAdapter mAdapter;
	private final List<Bitmap> mBitmaps;
	private final TextView mAugmentedImagesTotalTextView;
	private final Activity mActivity;
	
	private int AUGMENTED_IMAGE_HORIZONTAL_MARGINS;
	private int AUGMENTED_IMAGE_VERTICAL_MARGINS;
	private final static int PLACE_HOLDER_IMAGES_TOTAL = 5;
	
	// FIXME: The whole placeholder logic here needs to be replaced
	private static List<View> mPlaceHolderViews; 
	public static void clearPlaceHolders() {
		mPlaceHolderViews = new ArrayList<View>();
	}
	
	public AugmentedImageViewManager(String siteId, Activity activity, LinearLayout augmentedImagesLayout, TextView augmentedImagesTotalTextView) {		
		mSiteId = siteId;
		mActivity = activity;
		mContext = activity.getBaseContext();
		AUGMENTED_IMAGE_HORIZONTAL_MARGINS = Utilities.getDensityPixels(7, mContext); //pixels
		AUGMENTED_IMAGE_VERTICAL_MARGINS = Utilities.getDensityPixels(7, mContext);		
		mBitmaps = new ArrayList<Bitmap>();
		mAdapter = new AugmentedImageAdapter(mContext,mBitmaps);
		mAugmentedImagesLayout = augmentedImagesLayout;
		mAugmentedImagesTotalTextView = augmentedImagesTotalTextView;	
		addPlaceHolderImages();
	}
	
	public void setAugmentedImages(Cursor data) {
		// TODO limit this to be 20 or 10, otherwise it will run out of memory
		// if there are too many images
		// Or, the horizontal scroll view should cache property. That's the best
		// solution
		
		int imageLimit = data.getCount() < PLACE_HOLDER_IMAGES_TOTAL ? data.getCount() : PLACE_HOLDER_IMAGES_TOTAL;
		
		for(data.moveToFirst();data.getPosition() < imageLimit;data.moveToNext()) {
	    	final String url = data.getString(data.getColumnIndex(AugmentedImagesTable.COLUMN_GALLERY_SIZE_URL));	    	
	    	final String augmentedData = data.getString(data.getColumnIndex(AugmentedImagesTable.COLUMN_CONTENT));
	    	final String contentUrl = data.getString(data.getColumnIndex(AugmentedImagesTable.COLUMN_CONTENT_SIZE_URL));
			final int width = data.getInt(data.getColumnIndex(AugmentedImagesTable.COLUMN_WIDTH));
			final int height = data.getInt(data.getColumnIndex(AugmentedImagesTable.COLUMN_HIEGHT));
			final String imageId = data.getString(data.getColumnIndex(AugmentedImagesTable.COLUMN_IMAGE_ID));
			final int position = data.getPosition();
			
	    	if(url != null) {
	    		Log.d(TAG,"url is: " + url);
	    		Bitmap augmentedBitmap = BitmapCache.get().getBitmap(BitmapCache.getImageKeyFromURL(url));
	    		if(augmentedBitmap == null) {
					Log.d(TAG, "Bitmap not found in cache, start to download it.");
					new BitmapWorkerTask(url, new BitmapWorkerListener() {					
						@Override
						public void bitmapLoaded(Bitmap bitmap) {
							addBitmap(bitmap, imageId, contentUrl, augmentedData, width, height, position);							
						}
					}).execute();
	    		} else {
	    			addBitmap(augmentedBitmap, imageId, contentUrl, augmentedData, width, height, position);
	    		}	    		
	    	} else {
	    		continue;
	    	}
	    }
	    showAugmentedImagesView();
	    setAugmentedImagesTotalTextView(data.getCount());		
	}
	
	private void addPlaceHolderImages() {
		for(int i=0;i<PLACE_HOLDER_IMAGES_TOTAL;++i) {
			ImageView imageView = new ImageView(mContext);
			imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.img_missing_image_78x78));
			setAugmentedImageViewSize(imageView, i);
			mAugmentedImagesLayout.addView(imageView);
			mPlaceHolderViews.add(imageView);
		}
	}
	
	private void removePlaceHolderImages() {
		for(View placeHolder : mPlaceHolderViews) {
			mAugmentedImagesLayout.removeView(placeHolder);
		}		
	}
	
	private void setAugmentedImageViewSize(ImageView imageView, int position) {
//		int screenWidth = new ViewDimensionCalculator(mActivity).getScreenWidth();
//		int imageWidth = screenWidth /3;		
		int imageWidth = Utilities.getDensityPixels(78, mContext);
		int imageHeight = imageWidth;
		LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(imageWidth,imageHeight);
		if(position == (PLACE_HOLDER_IMAGES_TOTAL - 1))
			imageViewParams.setMargins(AUGMENTED_IMAGE_HORIZONTAL_MARGINS, AUGMENTED_IMAGE_VERTICAL_MARGINS, AUGMENTED_IMAGE_HORIZONTAL_MARGINS, AUGMENTED_IMAGE_VERTICAL_MARGINS);
		else
			imageViewParams.setMargins(AUGMENTED_IMAGE_HORIZONTAL_MARGINS, AUGMENTED_IMAGE_VERTICAL_MARGINS, 0, AUGMENTED_IMAGE_VERTICAL_MARGINS);
		imageView.setLayoutParams(imageViewParams);
		imageView.setScaleType(ScaleType.CENTER_CROP);
		imageView.setBackgroundResource(R.drawable.activity_explore_augmented_photos_border);
	}
	
	private void addBitmap(final Bitmap bitmap, final String imageId, final String contentUrl, 
			final String augmentedData, final int width, final int height, final int position) {
		removePlaceHolderImages();
		ImageView imageView = new ImageView(mContext);
		imageView.setImageBitmap(bitmap);
		setAugmentedImageViewSize(imageView, position);
		
		imageView.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {				
				Bitmap bitmap = BitmapCache.get().getBitmap(BitmapCache.getImageKeyFromURL(contentUrl));
				if (bitmap != null) {
					showARViewer(imageId, bitmap, augmentedData, width, height);
				} else {					
					new BitmapWorkerTask(contentUrl, new BitmapWorkerListener() {				
						@Override
						public void bitmapLoaded(Bitmap bitmap) {
							showARViewer(imageId, bitmap, augmentedData, width, height);
						}
					}).execute();
				}
			}
		});
		
		mAugmentedImagesLayout.addView(imageView);
	}
	
	private void showARViewer(String imageId, Bitmap bitmap, String augmentedData, int width, int height) {
		try {			
			final Intent intent = new Intent(mActivity, ARViewerActivity.class);
			intent.putExtra("site-id", mSiteId);
			intent.putExtra("image-id", imageId);
			intent.putExtra("file-path", ImageUtils.saveBitmapAsFile(bitmap, null));
			AugmentImageResultResponse data = JsonMapper.get().readValue(augmentedData, AugmentImageResultResponse.class);
			intent.putExtra("augmented-data", AugmentedDataUtils.convertAugmentResultResponse(imageId, data));
			intent.putExtra("original-size", width + "x" + height);
			mActivity.startActivity(intent);
		} catch (IOException e) {
			Log.e(TAG, "Failed to load the augmented image data", e);
		}
	}
	
	private void showAugmentedImagesView() {
		mAugmentedImagesLayout.setVisibility(View.VISIBLE);
	}
	
	private void setAugmentedImagesTotalTextView(int imagesTotal) {
		// TODO to enable the total number of augmented images,
		// we need more reliable image loading, rather than loading
		// all the image together at the same time
		
//		String text;
//		if(imagesTotal == 1) {
//			text = " augmented image";
//		} else {
//			text = " augmented images";
//		}
//		mAugmentedImagesTotalTextView.setText(imagesTotal + text);
//		mAugmentedImagesTotalTextView.setText("Recently augmented images");
	}
}
