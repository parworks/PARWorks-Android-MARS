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

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ProgressBar;

import com.parworks.arviewer.MiniARViewer;
import com.parworks.mars.cache.BitmapCache;
import com.parworks.mars.cache.BitmapWorkerTask;
import com.parworks.mars.cache.BitmapWorkerTask.BitmapWorkerListener;
import com.parworks.mars.model.db.SiteInfoTable;

public class PosterImageManager {
		
	private final MiniARViewer miniARViewer;
	private final ProgressBar mSiteImageProgressBar;
	private final Activity mActivity;
	private final String mSiteId;
	private final ViewDimensionCalculator mViewDimensionCalculator;
	
	public static final String TAG = PosterImageManager.class.getName();
	
	public PosterImageManager(String siteId, MiniARViewer miniARView, ProgressBar siteImageProgressBar, Activity activity) {
		this.miniARViewer = miniARView;
		mSiteImageProgressBar = siteImageProgressBar;
		mActivity = activity;
		mSiteId = siteId;
		mViewDimensionCalculator = new ViewDimensionCalculator(mActivity);
	}	
	
	public void setSiteImage(Cursor data) {		
		String posterImageUrl = data.getString(
				data.getColumnIndex(SiteInfoTable.COLUMN_AUG_POSTER_IMAGE_URL));
		final String posterContent = data.getString(
				data.getColumnIndex(SiteInfoTable.COLUMN_AUG_POSTER_IMAGE_CONTENT));
		final int width = data.getInt(
				data.getColumnIndex(SiteInfoTable.COLUMN_AUG_POSTER_IMAGE_WIDTH));
		final int height = data.getInt(
				data.getColumnIndex(SiteInfoTable.COLUMN_AUG_POSTER_IMAGE_HEIGHT));
		
		int imageWidth = mViewDimensionCalculator.getScreenWidth();
		miniARViewer.setSize(imageWidth, (int) (imageWidth * 0.6));		
		
		if (posterImageUrl != null) {
			Bitmap posterImageBitmap = BitmapCache.get().getBitmap(
					BitmapCache.getImageKeyFromURL(posterImageUrl));
			if (posterImageBitmap == null) {
				new BitmapWorkerTask(posterImageUrl, new BitmapWorkerListener() {					
					@Override
					public void bitmapLoaded(Bitmap bitmap) {			
						miniARViewer.setImageBitmap(bitmap);
						miniARViewer.setOriginalSize(width, height);
						miniARViewer.setAugmentedData(posterContent);
						showSiteImageView();
					}
				}).execute();
			} else {	
				miniARViewer.setImageBitmap(posterImageBitmap);
				miniARViewer.setOriginalSize(width, height);
				miniARViewer.setAugmentedData(posterContent);
				showSiteImageView();
			}
		}
	}
	
	private void showSiteImageView() {
		miniARViewer.setVisibility(View.VISIBLE);
		mSiteImageProgressBar.setVisibility(View.INVISIBLE);
	}
}
