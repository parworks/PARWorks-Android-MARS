package com.parworks.mars.view.search;

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
import com.parworks.mars.cache.BitmapWorkerTask;
import com.parworks.mars.cache.BitmapWorkerTask.BitmapWorkerListener;

public class SearchResultAdapter extends ArrayAdapter<SearchResultItem> {

	public SearchResultAdapter(Context context) {
		super(context, 0);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// create view
		if (convertView == null) {	  	
		    convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_result_list_row, null);			  	
		}
		
		// display the title
		String siteId = this.getItem(position).getSiteId();
		TextView title = (TextView) convertView.findViewById(R.id.searchSiteName);
		title.setText(siteId);

		// display poster image
		String imageUrl = this.getItem(position).getPosterImageUrl();
		final ImageView imageView = (ImageView) convertView.findViewById(R.id.searchSitePosterImage);
		if (imageUrl != null && !TextUtils.isEmpty(imageUrl)) {
			// if already set, do nothing
			if (!imageUrl.equals(imageView.getTag())) {
				imageView.setTag(imageUrl); // mark this image view has already been requested to load the bitmap
				Bitmap posterImageBitmap = BitmapCache.get().getBitmap(
						BitmapCache.getImageKeyFromURL(imageUrl));
				if (posterImageBitmap == null) {
					new BitmapWorkerTask(imageUrl, new BitmapWorkerListener() {					
						@Override
						public void bitmapLoaded(Bitmap bitmap) {			
							imageView.setImageBitmap(bitmap);
						}
					}).execute();
				} else {	
					imageView.setImageBitmap(posterImageBitmap);
				}
			}
		}
		
		// put siteId as the tag with the view
		convertView.setTag(siteId);
		return convertView;
	}

	public void updateRecord(String siteId, String posterImageUrl) {
		for(int i = 0; i < this.getCount(); i++) {
			if (this.getItem(i).getSiteId().equals(siteId)) {
				this.getItem(i).setPosterImageUrl(posterImageUrl);
			}
		}
	}
}
