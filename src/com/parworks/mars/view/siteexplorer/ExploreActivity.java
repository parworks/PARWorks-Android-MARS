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
package com.parworks.mars.view.siteexplorer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.parworks.arcameraview.CaptureImageActivity;
import com.parworks.arviewer.MiniARViewer;
import com.parworks.mars.R;
import com.parworks.mars.model.db.SiteInfoTable;
import com.parworks.mars.model.sync.SyncHandler;
import com.parworks.mars.utils.Utilities;
import com.parworks.mars.view.siteexplorer.AugmentedImagesLoader.AugmentedImagesLoaderListener;
import com.parworks.mars.view.siteexplorer.CommentsLoader.CommentsLoaderListener;
import com.parworks.mars.view.siteexplorer.SiteInfoLoader.SiteInfoLoaderListener;

public class ExploreActivity extends SherlockFragmentActivity {

	public static final String SITE_ID_ARGUMENT_KEY = "siteIdKey";
	public static final String TAG = ExploreActivity.class.getName();

	private String mSiteId;
	private static final int SITE_INFO_LOADER_ID = 0;
	private static final int AUGMENTED_IMAGES_LOADER_ID = 1;
	private static final int COMMENTS_LOADER_ID = 2;

	private View mLayoutView;
	private AddCommentManager mAddCommentManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLayoutView = getLayoutInflater().inflate(R.layout.activity_explore,
				null);
		setContentView(mLayoutView);
		
		AugmentedImageViewManager.clearPlaceHolders();

		mSiteId = getIntent().getStringExtra(SITE_ID_ARGUMENT_KEY);

		SiteInfoLoader siteInfoLoader = new SiteInfoLoader(mSiteId, this,
				new SiteInfoLoaderListener() {
					@Override
					public void onSiteLoaded(Cursor siteData) {
						loadSiteInfoIntoUi(siteData);
					}
				});
		getSupportLoaderManager().initLoader(SITE_INFO_LOADER_ID, null,
				siteInfoLoader);

		AugmentedImagesLoader augmentedImagesLoader = new AugmentedImagesLoader(
				mSiteId, this, new AugmentedImagesLoaderListener() {
					@Override
					public void onImagesLoaded(Cursor data) {
						loadAugmentedImagesIntoUi(data);
					}
				});
		getSupportLoaderManager().initLoader(AUGMENTED_IMAGES_LOADER_ID, null,
				augmentedImagesLoader);

		CommentsLoader commentsLoader = new CommentsLoader(mSiteId, this,
				new CommentsLoaderListener() {
					@Override
					public void onCommentsLoaded(Cursor data) {
						loadCommentsIntoUi(data);
					}
				});
		getSupportLoaderManager().initLoader(COMMENTS_LOADER_ID, null,
				commentsLoader);

		// getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		// getSupportActionBar().setTitle(mSiteId);
		// getSupportActionBar().setHomeButtonEnabled(true);
		// getSupportActionBar().setDisplayShowHomeEnabled(true);

		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);

		LayoutInflater inflator = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflator.inflate(R.layout.actionbar_main, null);

		getSupportActionBar().setCustomView(v);

		ImageButton leftBarButton = (ImageButton) getSupportActionBar()
				.getCustomView().findViewById(R.id.leftBarButton);
		leftBarButton.setBackgroundResource(R.drawable.ic_bar_item_back);

		ImageButton rightBarButton = (ImageButton) getSupportActionBar()
				.getCustomView().findViewById(R.id.rightBarButton);
		rightBarButton.setBackgroundResource(R.drawable.try_it_now);

		TextView barTitle = (TextView) getSupportActionBar().getCustomView()
				.findViewById(R.id.barTitle);
		barTitle.setVisibility(View.GONE);
		
		mAddCommentManager = new AddCommentManager(this, mSiteId);
		Button addCommentButton = (Button) findViewById(R.id.buttonAddComment);
		addCommentButton.setOnClickListener(mAddCommentManager);

		// sync the site
		SyncHandler.syncSiteInfo(mSiteId, true);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mAddCommentManager.facebookOnActivityResult(requestCode, resultCode,
				data);
	}

	public void leftBarButtonClicked(View v) {
		onBackPressed();
	}

	public void rightBarButtonClicked(View v) {
		Intent intent = new Intent(ExploreActivity.this,
				CaptureImageActivity.class);
		intent.putExtra(CaptureImageActivity.SITE_ID_KEY, mSiteId);
		intent.putExtra(CaptureImageActivity.IS_AUGMENT_ATTR, true);
		startActivity(intent);
	}

	private void loadSiteInfoIntoUi(Cursor data) {
		if (data.getCount() == 0) {
			return;
		}
		// set site name
		String siteName = data.getString(data
				.getColumnIndex(SiteInfoTable.COLUMN_NAME));
		if (siteName == null) {
			siteName = data.getString(data
					.getColumnIndex(SiteInfoTable.COLUMN_SITE_ID));
		} else if (TextUtils.isEmpty(siteName)) {
			siteName = data.getString(data
					.getColumnIndex(SiteInfoTable.COLUMN_SITE_ID));
		}
		TextView nameTextView = (TextView) findViewById(R.id.textViewSiteName);
		nameTextView.setText(siteName);

//		TextView barTitle = (TextView) getSupportActionBar().getCustomView()
//				.findViewById(R.id.barTitle);
//		barTitle.setText(siteName);

		// set site address
		String siteAddress = data.getString(data
				.getColumnIndex(SiteInfoTable.COLUMN_ADDRESS));
		TextView addressTextView = (TextView) findViewById(R.id.textViewSiteAddress);
		if (siteAddress == null) {
			addressTextView.setText("No address available");
		} else {
			addressTextView.setText(siteAddress);
			Linkify.addLinks(addressTextView, Linkify.ALL);
		}

		// set site description
		String siteDesc = data.getString(data
				.getColumnIndex(SiteInfoTable.COLUMN_DESC));
		TextView descriptionTextView = (TextView) findViewById(R.id.textViewSiteDescription);
		descriptionTextView.setText(siteDesc);
		Linkify.addLinks(descriptionTextView, Linkify.ALL);

		// let siteImageManager handle the SiteImageView
		MiniARViewer miniARView = (MiniARViewer) findViewById(R.id.siteExplorerPosterImageView);
		ProgressBar siteImageProgressBar = (ProgressBar) findViewById(R.id.progressBarSiteImage);
		PosterImageManager siteImageManager = new PosterImageManager(mSiteId,
				miniARView, siteImageProgressBar, this);
		siteImageManager.setSiteImage(data);

		// let mapImageManager handle the map view
		ImageView mapImageView = (ImageView) findViewById(R.id.imageViewMap);
		ImageView mapShadowImageView = (ImageView) findViewById(R.id.imageViewMapShadow);
		ProgressBar mapProgressBar = (ProgressBar) findViewById(R.id.progressBarMapView);
		MapImageManager mapImageManager = new MapImageManager(mSiteId,
				mapImageView, mapShadowImageView, mapProgressBar, this);
		mapImageManager.setMapView(data);
	}

	private void loadAugmentedImagesIntoUi(Cursor data) {
		Log.d(Utilities.DEBUG_TAG_SYNC, "Explore Activity - loadAugmentedImages");
		LinearLayout augmentedImagesLayout = (LinearLayout) findViewById(R.id.linearLayoutAugmentedImagesLayout);
		TextView augmentedImagesTotalTextView = (TextView) findViewById(R.id.textViewAugmentedPhotoTotal);
		AugmentedImageViewManager augmentedImagesViewManager = new AugmentedImageViewManager(
				mSiteId, this, augmentedImagesLayout, augmentedImagesTotalTextView);
		augmentedImagesViewManager.setAugmentedImages(data);
	}

	private void loadCommentsIntoUi(Cursor data) {
		Log.d(Utilities.DEBUG_TAG_SYNC, "Explore Activity - loadCommentsIntoUi");
		ProgressBar commentsProgressBar = (ProgressBar) findViewById(R.id.progressBarComments);
		LinearLayout commentsLayout = (LinearLayout) findViewById(R.id.linearLayoutComments);
		TextView commentsTotalTextView = (TextView) findViewById(R.id.textViewCommentTotal);
		CommentsViewManager commentsViewManager = new CommentsViewManager(
				mSiteId, this, commentsProgressBar, commentsLayout,
				commentsTotalTextView);
		commentsViewManager.setCommentsView(data);
	}
}
