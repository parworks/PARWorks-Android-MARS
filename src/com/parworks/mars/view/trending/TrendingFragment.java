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

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.parworks.mars.MarsMenuFragment;
import com.parworks.mars.R;
import com.parworks.mars.model.db.TrendingSitesTable;
import com.parworks.mars.model.provider.MarsContentProvider;
import com.parworks.mars.view.siteexplorer.ImageViewManager;
import com.parworks.mars.view.siteexplorer.ImageViewManager.ImageLoadedListener;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * The Trending view is based on a ViewPager,
 * which contains a list of TrendingSiteFragment.
 * 
 * @author yusun
 */

public class TrendingFragment extends MarsMenuFragment implements LoaderCallbacks<Cursor> {	

	private static final String TAG = "TrendingFragment";
	private static final int TRENDING_SITES_LOADER_ID = 5;

	private ViewPager vp;
	private TrendingPagerAdapter vpAdapter;

	private CirclePageIndicator circlePageIndicator;
	private ImageView backgroundImageView1;
	private ImageView backgroundImageView2;
	/** Current page view position */
	private int currentPos = 0;
	private List<String> backgroundImageUrls;	

	public TrendingFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.fragment_trending, null);
		// init ViewPager
		vp = (ViewPager) v.findViewById(R.id.trendingViewPager);
		vp.setId(this.hashCode());	// this ID needs to be unique if you want the view to be refreshed after activity destroyed	
		// Bind the title indicator to the adapter
		circlePageIndicator = (CirclePageIndicator) v.findViewById(R.id.pageIndicatorTitle);	
		circlePageIndicator.setOnPageChangeListener(new OnPageChangeListener() {			
			@Override
			public void onPageScrollStateChanged(int arg0) { }

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { 
				if (position == currentPos) {
					// to the right
					showBackgroundImage(backgroundImageView1, position, 1 - positionOffset);
					showBackgroundImage(backgroundImageView2, position + 1, positionOffset);
				} else {
					// to the left
					showBackgroundImage(backgroundImageView1, position, 1 - positionOffset);
					showBackgroundImage(backgroundImageView2, position + 1, positionOffset);
				}
			}

			@Override
			public void onPageSelected(int position) {	
				switch (position) {
				case 0:
					((SlidingFragmentActivity) TrendingFragment.this.getActivity())
							.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
					break;
				default:
					((SlidingFragmentActivity) TrendingFragment.this.getActivity())
							.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
					break;
				}
				currentPos = position;
			}
		});

		// vp.setPageMargin(-60);

		// init background ImageViews
		backgroundImageView1 = (ImageView) v.findViewById(R.id.blurredBackground1);
		backgroundImageView2 = (ImageView) v.findViewById(R.id.blurredBackground2);

		// config SlidingMenu touch mode
		((SlidingFragmentActivity) this.getActivity()).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		return v;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);				
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		// init adapter	
		if (vpAdapter == null) {
			vpAdapter = new TrendingPagerAdapter(this.getActivity().getSupportFragmentManager());
		}
		// init Loader for TrendingSites
		this.getLoaderManager().initLoader(TRENDING_SITES_LOADER_ID, null, this);
	}

	@Override
	public void onResume() {
		super.onResume();
		ImageButton button = (ImageButton) ((SlidingFragmentActivity) this.getActivity())
				.getSupportActionBar().getCustomView().findViewById(R.id.rightBarButton);
		button.setBackgroundResource(R.drawable.ic_bar_item_intro);		
		if (currentPos != 0) {
			((SlidingFragmentActivity) this.getActivity())
					.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}
	
	public void rightBarButtonClicked(View v) {
		super.rightBarButtonClicked(v);
		Toast.makeText(((SlidingFragmentActivity) this.getActivity()), 
				"Insert Intro", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle arg1) {
		String[] projection = TrendingSitesTable.ALL_COLUMNS;
		CursorLoader cursorLoader = new CursorLoader(this.getActivity(), 
				MarsContentProvider.CONTENT_URI_ALL_TRENDING_SITES, projection, null, null, null);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (data.getCount() == 0) {
			return;
		}

		Log.d(TAG, "onLoadFinished and update sites fragment");
		// store all the background images
		backgroundImageUrls = new ArrayList<String>();
		// init new data content for the adapter
		ArrayList<TrendingSiteFragment> sitesFragments = new ArrayList<TrendingSiteFragment>(data.getCount());
		while(!data.isAfterLast()) {
			String siteId = data.getString(
					data.getColumnIndex(TrendingSitesTable.COLUMN_SITE_ID));
			String siteName = data.getString(
					data.getColumnIndex(TrendingSitesTable.COLUMN_NAME));
			int siteNum = data.getInt(
					data.getColumnIndex(TrendingSitesTable.COLUMN_NUM_AUGMENTED_IMAGES));
			String posterUrl = data.getString(
					data.getColumnIndex(TrendingSitesTable.COLUMN_AUG_POSTER_IMAGE_URL));
			String blurredUrl = data.getString(   // FIXME
					data.getColumnIndex(TrendingSitesTable.COLUMN_POSTER_BLURRED_IMAGE_URL));
			String posterContent = data.getString(
					data.getColumnIndex(TrendingSitesTable.COLUMN_AUG_POSTER_IMAGE_CONTENT));
			int width = data.getInt(
					data.getColumnIndex(TrendingSitesTable.COLUMN_AUG_POSTER_IMAGE_WIDTH));
			int height = data.getInt(
					data.getColumnIndex(TrendingSitesTable.COLUMN_AUG_POSTER_IMAGE_HEIGHT));

			// add fragment for site
			sitesFragments.add(new TrendingSiteFragment(siteId, siteName, siteNum, posterUrl, 
					posterContent, width, height));
	
			// add blurred image for background
			backgroundImageUrls.add(blurredUrl);
			data.moveToNext();
		}	

		// update vp adapter		
		vpAdapter.updateFragments(sitesFragments);	
		vp.setAdapter(vpAdapter);
		circlePageIndicator.setViewPager(vp);	
		vpAdapter.notifyDataSetChanged();

		if (currentPos < sitesFragments.size()) {
			circlePageIndicator.setCurrentItem(currentPos);
			vp.setCurrentItem(currentPos);
		} else {
			vp.setCurrentItem(0);
			circlePageIndicator.setCurrentItem(0);
			currentPos = 0;
		}		

		showBackgroundImage(backgroundImageView1, currentPos, 1);		
	}
	
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

	}

	@SuppressWarnings("deprecation")
	private void showBackgroundImage(final ImageView imageView, int position, final float scale) {
		if (position >= backgroundImageUrls.size()) {
			return;
		}

		final String imageUrl = backgroundImageUrls.get(position);
		// if the image view already displays the target image
		// only set the alpha value
		if (imageUrl != null && imageUrl.equals(imageView.getTag())) {
			imageView.setAlpha((int) (255 * scale));
		} else {
			// download and load the image first 
			// then setup the alpha value
			imageView.setTag(imageUrl);
			ImageViewManager imageViewManager = new ImageViewManager();
			if (imageUrl != null) {
				ImageLoadedListener listener = new ImageLoadedListener() {					
					@Override
					public void onImageLoaded(Bitmap bitmap) {
						imageView.setAlpha((int) (255 * scale));						
					}
				};
				imageViewManager.setImageView(imageUrl, imageView, listener);
			}
		}
	}

	/**
	 * Adapter for trending sites
	 */
	public class TrendingPagerAdapter extends FragmentPagerAdapter {

		private ArrayList<TrendingSiteFragment> mFragments;
		private FragmentManager fm;

		public TrendingPagerAdapter(FragmentManager fm) {
			super(fm);
			this.fm = fm;
		}

		@Override
		public int getCount() {
			if (mFragments == null) {
				return 0;
			}
			return mFragments.size();
		}

		@Override
		public Fragment getItem(int position) {
			return mFragments.get(position);
		}

		@Override
		public int getItemPosition(Object item) {
			if (mFragments == null) {
				return POSITION_NONE;
			}
			int position = mFragments.indexOf(item);
			if (position < 0) {
				position = POSITION_NONE;
			}
			return position;
		}

		public synchronized void updateFragments(ArrayList<TrendingSiteFragment> fragments) {
			if (mFragments != null) {
				FragmentTransaction ft = fm.beginTransaction();
				for (Fragment f : mFragments){
					ft.remove(f);
				}
				ft.commitAllowingStateLoss();
				mFragments.clear();
				ft = null;
				fm.executePendingTransactions();
			}
			
			mFragments = fragments;
			notifyDataSetChanged();
		}
	}

	@Override
	public void onDestroy() {		
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
}
