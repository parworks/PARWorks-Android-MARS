package com.parworks.mars.view.search;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.parworks.mars.R;
import com.parworks.mars.cache.BitmapCache;
import com.parworks.mars.cache.BitmapWorkerTask;
import com.parworks.mars.cache.BitmapWorkerTask.BitmapWorkerListener;
import com.parworks.mars.model.db.SiteInfoTable;

public class SearchResultListAdapter extends SimpleCursorAdapter {

	public SearchResultListAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
	}

	@Override
	public void bindView(View v, Context context, Cursor cursor) {
		// display poster image		
		String imageUrl = cursor.getString(
				cursor.getColumnIndex(SiteInfoTable.COLUMN_POSTER_IMAGE_URL));
		final ImageView imageView = (ImageView) v.findViewById(R.id.searchSitePosterImage);

		if (imageUrl != null && !TextUtils.isEmpty(imageUrl)) {
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

		// display the title
		String siteId = cursor.getString(
				cursor.getColumnIndex(SiteInfoTable.COLUMN_SITE_ID));
		TextView title = (TextView) v.findViewById(R.id.searchSiteName);
		title.setText(siteId);
		
		// put siteId as the tag with the view
		v.setTag(siteId);
		
		System.out.println("YUSUN: Just bind view: " + siteId + " -> " + imageUrl);
	}
}
