package com.parworks.mars.view.trending;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.parworks.mars.R;
import com.parworks.mars.view.siteexplorer.ImageViewManager;

public class TrendingSiteFragment extends Fragment {
	
	private String siteId;
	private int numAugmentedImages;
	private String posterImageUrl;	
	private String posterBlurredImageUrl;
	
	private ImageView backgroundImageView;
	
	public TrendingSiteFragment(String siteId, int numAugmentedImages, String posterUrl, String blurUrl) {
		this.siteId = siteId;
		this.numAugmentedImages = numAugmentedImages;
		this.posterImageUrl = posterUrl;
		this.posterBlurredImageUrl = blurUrl;
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {						
		RelativeLayout v = (RelativeLayout) inflater.inflate(R.layout.fragment_trending_site, null);
		
		// handle poster image	
		ImageView imageView = (ImageView) v.findViewById(R.id.trendingSitePosterImage);
		ImageViewManager imageViewManager = new ImageViewManager();		
		int imageWidth = (int) (container.getWidth() * 0.8);
		imageView.getLayoutParams().width = imageWidth;
		imageView.getLayoutParams().height = imageWidth;
		
		if (posterImageUrl != null) {
			imageViewManager.setImageView(posterImageUrl, ImageViewManager.IGNORE_WIDTH, imageView, null);
		} else {
			imageView.setImageResource(R.drawable.img_missing_image);
		}
		
		// setup ShingleBoard
		ShingleBoard sb = (ShingleBoard) v.findViewById(R.id.shingleBoard);
		int boardWidth = (int) (container.getWidth() * 0.7);		
		sb.getLayoutParams().width = boardWidth;
		sb.updateText(siteId, numAugmentedImages);		
		
		return v;
	}	
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}	
}
