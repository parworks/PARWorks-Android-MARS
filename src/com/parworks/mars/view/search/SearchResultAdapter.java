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
package com.parworks.mars.view.search;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parworks.mars.R;
import com.parworks.mars.cache.BitmapCache;
import com.parworks.mars.cache.BitmapWorkerTask.BitmapWorkerListener;
import com.parworks.mars.view.siteexplorer.ViewDimensionCalculator;

public class SearchResultAdapter extends ArrayAdapter<SearchResultItem> {

	private ViewDimensionCalculator viewDimensionCalculator;
	
	public class ViewHolder {
		public TextView textView;
		public ImageView imageView;
	}
	
	public SearchResultAdapter(Context context) {
		super(context, 0);
		this.viewDimensionCalculator = new ViewDimensionCalculator((Activity) context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {		
		final ViewHolder viewHolder;
		final String siteId = this.getItem(position).getSiteId();		
		final String imageUrl = this.getItem(position).getPosterImageUrl();
		
		// create view
		if (convertView == null) {	  	
		    convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_result_list_row, null);
		    
		    TextView title = (TextView) convertView.findViewById(R.id.searchSiteName);
		    final ImageView imageView = (ImageView) convertView.findViewById(R.id.searchSitePosterImage);		    
		    
		    viewHolder = new ViewHolder();
		    viewHolder.textView = title;
		    viewHolder.imageView = imageView;
		    viewHolder.imageView.getLayoutParams().height = (int) (viewDimensionCalculator.getScreenWidth() * 0.6);
		    
		    convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		// display the title			
		viewHolder.textView.setText(siteId);

		// display poster image
		if (imageUrl != null && !TextUtils.isEmpty(imageUrl)) {
			if (!imageUrl.equals(viewHolder.imageView.getTag())) {
				BitmapCache.get().getBitmapAsync(imageUrl, new BitmapWorkerListener() {				
					@Override
					public void bitmapLoaded(Bitmap bitmap) {					
						viewHolder.imageView.setImageBitmap(bitmap);
						viewHolder.imageView.setTag(imageUrl);												
					}
				});			
			}
		} else {
			viewHolder.imageView.setImageBitmap(null);
			viewHolder.imageView.setBackgroundResource(R.drawable.img_missing_image);
			viewHolder.imageView.setTag(null);			
		}
		
		return convertView;
	}	

	public void updateRecord(String siteId, String posterImageUrl) {
		boolean isExist = false;
		for(int i = 0; i < this.getCount(); i++) {
			if (this.getItem(i).getSiteId().equals(siteId)) {
				isExist = true;
				if (posterImageUrl != null && !posterImageUrl.equals(this.getItem(i).getPosterImageUrl())) {
					this.getItem(i).setPosterImageUrl(posterImageUrl);
					this.notifyDataSetChanged();
				}				
			}
		}
			
		if (!isExist) {
			add(new SearchResultItem(siteId, posterImageUrl));
			this.notifyDataSetChanged();
		}
	}	
}
