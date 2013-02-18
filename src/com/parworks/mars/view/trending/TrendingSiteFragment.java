package com.parworks.mars.view.trending;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.parworks.mars.R;
import com.parworks.mars.cache.BitmapCache;
import com.parworks.mars.cache.BitmapWorkerTask;
import com.parworks.mars.cache.BitmapWorkerTask.BitmapWorkerListener;
import com.parworks.mars.utils.ImageHelper;
import com.parworks.mars.view.siteexplorer.ExploreActivity;
import com.parworks.mars.view.siteexplorer.ImageViewManager;

public class TrendingSiteFragment extends Fragment implements OnClickListener {
	
	private String siteId;
	private int numAugmentedImages;
	private String posterImageUrl;	
	
	public TrendingSiteFragment() {
		super();
	}

	public TrendingSiteFragment(String siteId, int numAugmentedImages, String posterUrl) {
		this.siteId = siteId;
		this.numAugmentedImages = numAugmentedImages;
		this.posterImageUrl = posterUrl;
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {						
		RelativeLayout v = (RelativeLayout) inflater.inflate(R.layout.fragment_trending_site, null);
		
		// handle poster image	
		final ImageView imageView = (ImageView) v.findViewById(R.id.trendingSitePosterImage);
		ImageViewManager imageViewManager = new ImageViewManager();		
		int imageWidth = (int) (container.getWidth() * 0.8);
		imageView.getLayoutParams().width = imageWidth;
		imageView.getLayoutParams().height = imageWidth;
		
		if (posterImageUrl != null) {
			Bitmap posterImageBitmap = BitmapCache.get().getBitmap(
					BitmapCache.getImageKeyFromURL(posterImageUrl));
			if (posterImageBitmap == null) {
				new BitmapWorkerTask(posterImageUrl, new BitmapWorkerListener() {					
					@Override
					public void bitmapLoaded(Bitmap bitmap) {			
						//imageView.setImageDrawable(new PosterImage(bitmap));
						imageView.setImageBitmap(ImageHelper.getRoundedCornerBitmap(bitmap, 20));
						//imageView.setImageBitmap(bitmap);
					}
				}).execute();
			} else {	
				//imageView.setImageDrawable(new PosterImage(posterImageBitmap));
				imageView.setImageBitmap(ImageHelper.getRoundedCornerBitmap(posterImageBitmap, 20));
				//imageView.setImageBitmap(posterImageBitmap);
			}
		} else {
			imageView.setImageResource(R.drawable.img_missing_image);
		}
		
		// setup ShingleBoard
		ShingleBoard sb = (ShingleBoard) v.findViewById(R.id.shingleBoard);
		int boardWidth = (int) (container.getWidth() * 0.7);		
		sb.getLayoutParams().width = boardWidth;
		sb.updateText(siteId, numAugmentedImages);		
		
		// add click action
		imageView.setOnClickListener(this);
		sb.setOnClickListener(this);
		
		return v;
	}	
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onClick(View v) {
		Intent i = new Intent(this.getActivity(), ExploreActivity.class);
		i.putExtra(ExploreActivity.SITE_ID_ARGUMENT_KEY, siteId);
		startActivity(i);
	}	
}
