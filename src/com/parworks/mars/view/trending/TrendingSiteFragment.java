package com.parworks.mars.view.trending;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.parworks.mars.R;
import com.parworks.mars.view.siteexplorer.ImageViewManager;
import com.parworks.mars.view.siteexplorer.ImageViewManager.ImageLoadedListener;

public class TrendingSiteFragment extends Fragment {
	
	private String siteId;
	private int numAugmentedImages;
	private String posterImageUrl;	
	
	public TrendingSiteFragment(String siteId, int numAugmentedImages, String posterUrl) {
		this.siteId = siteId;
		this.numAugmentedImages = numAugmentedImages;
		this.posterImageUrl = posterUrl;
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {						
		View v = inflater.inflate(R.layout.fragment_trending_site, null);
		
		// handle poster image	
		ImageView imageView = (ImageView) v.findViewById(R.id.trendingSitePosterImage);
		ImageViewManager imageViewManager = new ImageViewManager();
		int imageWidth = (int) (container.getWidth() * 0.8);
		if (posterImageUrl != null) {
			imageViewManager.setImageView(posterImageUrl, imageWidth, imageView, new ImageLoadedListener() {			
				@Override
				public void onImageLoaded() {
					
				}
			});
		} else {
			imageView.setImageResource(R.drawable.img_missing_image);
		}
		
		// setup ShingleBoard
		ShingleBoard sb = (ShingleBoard) v.findViewById(R.id.shingleBoard);
		int boardWidth = (int) (container.getWidth() * 0.7);		
		sb.getLayoutParams().width = boardWidth;
		sb.updateText(siteId, numAugmentedImages);
		
		// display other site info
//		TextView tv = (TextView) v.findViewById(R.id.trendingSiteId);
//		tv.setText(siteId);
//		TextView numTv = (TextView) v.findViewById(R.id.trendingSiteNum);
//		numTv.setText(numAugmentedImages + " Augmented Photos");
		
		return v;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}	
}
