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
package com.parworks.mars.view.intro;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;

import com.parworks.arcameraview.CaptureImageActivity;
import com.parworks.mars.R;
import com.parworks.mars.utils.ImageHelper;
import com.parworks.mars.utils.Utilities;
import com.parworks.mars.view.siteexplorer.ExploreActivity;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * The MARS intro activity.
 * 
 * @author graysonsharpe
 */
public class IntroActivity extends FragmentActivity {

	private static final String TAG = "IntroActivity";

	private static final int NUM_PAGES = 4;
	private ViewPager mPager;
	private PagerAdapter mPagerAdapter;
	private IntroActivity mContext;

	private CirclePageIndicator circlePageIndicator;
	
	private int currentPos = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);

		mContext = this;

		// Bind the title indicator to the adapter
		circlePageIndicator = (CirclePageIndicator) findViewById(R.id.pageIndicatorIntro);
		circlePageIndicator.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				currentPos = position;
			}
		});

		// Instantiate a ViewPager and a PagerAdapter.
		mPager = (ViewPager) findViewById(R.id.introViewPager);
		mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);
		mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {

			}
		});
		circlePageIndicator.setViewPager(mPager);
	}

	public void macbookButtonClicked(View v) {
		Intent intent = new Intent(IntroActivity.this,
				CaptureImageActivity.class);
		intent.putExtra(CaptureImageActivity.SITE_ID_KEY, "macbookkeyboard");
		intent.putExtra(CaptureImageActivity.IS_AUGMENT_ATTR, true);
		startActivity(intent);
	}

	public void dollarButtonClicked(View v) {
		Intent intent = new Intent(IntroActivity.this,
				CaptureImageActivity.class);
		intent.putExtra(CaptureImageActivity.SITE_ID_KEY, "Dollar1");
		intent.putExtra(CaptureImageActivity.IS_AUGMENT_ATTR, true);
		startActivity(intent);
	}

	public void doneButtonClicked(View v){
		finish();
	}
	
	/**
	 * A simple pager adapter that represents 5 {@link IntroPageFragment}
	 * objects, in sequence.
	 */
	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public Fragment getItem(int position) {

			if (position == 0) {
				IntroFirstPageFragment fragment = IntroFirstPageFragment
						.create(position, R.drawable.intro1);
				return fragment;
			} else if (position == 3) {
				IntroLastPageFragment fragment = IntroLastPageFragment.create(
						position, R.drawable.intro4);
				return fragment;
			} else {
				int resId = 0;
				if (position == 1)
					resId = R.drawable.intro2;
				else
					resId = R.drawable.intro3;
				IntroPageFragment fragment = IntroPageFragment.create(position,
						resId);
				return fragment;
			}
		}

		@Override
		public int getCount() {
			return NUM_PAGES;
		}
	}
	
	
}
