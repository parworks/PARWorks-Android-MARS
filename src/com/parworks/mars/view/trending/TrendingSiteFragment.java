package com.parworks.mars.view.trending;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parworks.mars.R;

public class TrendingSiteFragment extends Fragment {
	
	private String siteId;
	private int numAugmentedImages;
	
	private int mColorRes = -1;
	
	public TrendingSiteFragment() { 
		this(null, 0, R.color.white);
	}
	
	public TrendingSiteFragment(String siteId, int numAugmentedImages, int colorRes) {
		this.siteId = siteId;
		this.numAugmentedImages = numAugmentedImages;
		mColorRes = colorRes;
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {		
		// check the purpose of the following line
		if (savedInstanceState != null) {
			mColorRes = savedInstanceState.getInt("mColorRes");
		}
				
		View v = inflater.inflate(R.layout.fragment_trending_site, null);
		
		TextView tv = (TextView) v.findViewById(R.id.trendingSiteId);
		tv.setText(siteId);
		TextView numTv = (TextView) v.findViewById(R.id.trendingSiteNum);
		numTv.setText(numAugmentedImages + " Augmented Photos");
		
		return v;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("mColorRes", mColorRes);
	}	
}
