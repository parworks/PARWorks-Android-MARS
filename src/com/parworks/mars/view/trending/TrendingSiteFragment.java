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
package com.parworks.mars.view.trending;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.parworks.arviewer.MiniARViewer;
import com.parworks.mars.R;
import com.parworks.mars.cache.BitmapCache;
import com.parworks.mars.cache.BitmapWorkerTask;
import com.parworks.mars.cache.BitmapWorkerTask.BitmapWorkerListener;
import com.parworks.mars.utils.ImageHelper;
import com.parworks.mars.view.siteexplorer.ExploreActivity;
import com.parworks.mars.view.siteexplorer.ViewDimensionCalculator;

@SuppressLint("ValidFragment")
public class TrendingSiteFragment extends Fragment implements OnClickListener {
	
	private String siteId;
	private String displayName;
	private int numAugmentedImages;
	private String posterImageUrl;
	private String posterImageContent;
	private int posterOriWidth;
	private int posterOriHeight;
	
	public TrendingSiteFragment() {
		super();
	}

	public TrendingSiteFragment(String siteId, String displayName, 
			int numAugmentedImages, String posterUrl, String posterImageContent,
			int width, int height) {
		this.siteId = siteId;
		this.displayName = displayName;
		if (displayName == null || TextUtils.isEmpty(displayName)) {
			this.displayName = siteId;
		}
		this.numAugmentedImages = numAugmentedImages;
		this.posterImageUrl = posterUrl;
		this.posterImageContent = posterImageContent;
		this.posterOriWidth = width;
		this.posterOriHeight = height;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {		
		
		ViewDimensionCalculator caculator = new ViewDimensionCalculator(this.getActivity());
		RelativeLayout v = (RelativeLayout) inflater.inflate(R.layout.fragment_trending_site, null);
		// handle poster image
		final MiniARViewer miniARViewer = (MiniARViewer) v.findViewById(R.id.trendingSitePosterImageViewer);
		int imageWidth = (int) (caculator.getScreenWidth() * 0.8);
		miniARViewer.setSize(imageWidth, imageWidth);		
		if (posterImageUrl != null) {
			Bitmap posterImageBitmap = BitmapCache.get().getBitmap(
					BitmapCache.getImageKeyFromURL(posterImageUrl));
			if (posterImageBitmap == null) {
				new BitmapWorkerTask(posterImageUrl, new BitmapWorkerListener() {					
					@Override
					public void bitmapLoaded(Bitmap bitmap) {
						miniARViewer.setImageBitmap(ImageHelper.getRoundedCornerBitmap(bitmap, 10));
						miniARViewer.setOriginalSize(posterOriWidth, posterOriHeight);
						miniARViewer.setAugmentedData(posterImageContent);
					}
				}).execute();
			} else {	
				miniARViewer.setImageBitmap(ImageHelper.getRoundedCornerBitmap(posterImageBitmap, 10));
				miniARViewer.setOriginalSize(posterOriWidth, posterOriHeight);
				miniARViewer.setAugmentedData(posterImageContent);
			}
		}
		
		// setup ShingleBoard
		ShingleBoard sb = (ShingleBoard) v.findViewById(R.id.shingleBoard);
		int boardWidth = (int) (caculator.getScreenWidth() * 0.7);		
		sb.getLayoutParams().width = boardWidth;
		sb.updateText(displayName, numAugmentedImages);		
		
		// add click action
		miniARViewer.setOnClickListener(this);
		sb.setOnClickListener(this);

		return v;
	}	

	@Override
	public void onClick(View v) {
		Intent i = new Intent(this.getActivity(), ExploreActivity.class);
		i.putExtra(ExploreActivity.SITE_ID_ARGUMENT_KEY, siteId);
		startActivity(i);
	}	
}
