package com.parworks.mars.view.siteexplorer;


import com.parworks.mars.R;
import com.parworks.mars.model.db.SiteInfoTable;
import com.parworks.mars.view.siteexplorer.ImageViewManager.ImageLoadedListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class MapImageManager {

	private final ImageView mMapImageView;
	private final ImageView mMapShadowImageView;
	private final ProgressBar mMapImageProgressBar;
	private final Context mContext;
	private final Activity mActivity;
	private final String mSiteId;
	private ViewDimensionCalculator mViewDimensionCalculator;
	
	public static final String TAG = MapImageManager.class.getName();
	
	public MapImageManager(String siteId, ImageView mapImageView, ImageView mapShadowImageView, ProgressBar mapImageProgressBar, Activity activity) {
		mMapImageView = mapImageView;
		mMapShadowImageView = mapShadowImageView;
		mMapImageView.setAdjustViewBounds(true);
		mMapImageView.setScaleType(ScaleType.CENTER_CROP);
		mMapImageProgressBar = mapImageProgressBar;
		mActivity = activity;
		mContext = activity.getBaseContext();
		mSiteId = siteId;
		mViewDimensionCalculator = new ViewDimensionCalculator(mActivity);

	}
	
	private void showMapView() {
		mMapImageView.setVisibility(View.VISIBLE);
		mMapShadowImageView.setVisibility(View.VISIBLE);
		mMapImageProgressBar.setVisibility(View.INVISIBLE);
		
	}
	private void disableMapView() {
		mMapImageView.setVisibility(View.GONE);
		mMapShadowImageView.setVisibility(View.GONE);
		mMapImageProgressBar.setVisibility(View.GONE);
	}
	private void setMapViewSize() {
		mMapImageView.getLayoutParams().width = mViewDimensionCalculator.getScreenWidth();
		mMapImageView.getLayoutParams().height = mViewDimensionCalculator.getScreenWidth()/3;
		
		mMapShadowImageView.getLayoutParams().width = mMapImageView.getLayoutParams().width;
		mMapShadowImageView.getLayoutParams().height = mMapImageView.getLayoutParams().height;
	}

	public void setMapView(Cursor data) {
		final String lat = data.getString(data.getColumnIndex(SiteInfoTable.COLUMN_LAT));
		final String lon = data.getString(data.getColumnIndex(SiteInfoTable.COLUMN_LON));
		if(lat == null || lon == null) {
			disableMapView();
			return;
		}
		if(lat.equals("0.0") && lon.equals("0.0")) {
			disableMapView();
			return;
		}
		final int width = mViewDimensionCalculator.getScreenWidth();
		final int height = width/2;
		
		String mapUrl = StaticGoogleMaps.getMapUrl(lat, lon, width, height);
		ImageViewManager imageViewManager = new ImageViewManager();		
		
		if(mapUrl != null) {
			imageViewManager.setImageView(mapUrl, mMapImageView, new ImageLoadedListener() {
				
				@Override
				public void onImageLoaded(Bitmap bitmap) {
//					mMapImageView.setBackground(new BitmapDrawable(mActivity.getBaseContext().getResources(),bitmap));
//					mMapImageView.setImageDrawable(mActivity.getBaseContext().getResources().getDrawable(R.drawable.activity_explore_map_border));
					setMapViewSize();
					showMapView();
//					createMapGradient(bitmap);
					mMapShadowImageView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							try {
								double latDouble = Double.parseDouble(lat);
								double lonDouble = Double.parseDouble(lon);
								String mapsUri = String.format("geo:%f,%f?q=%f,%f", latDouble, lonDouble,latDouble,lonDouble);
								Intent mapsIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(mapsUri));
								mapsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								mContext.startActivity(mapsIntent);
							} catch(Exception e) {
								Log.e(TAG,e.getMessage());
							}
							
						}
					});
					
				}
			});
			
		}  else {
			Log.e(TAG, "mapURL was null.");
		}
		
	}
	
//	protected void createMapGradient(Bitmap bitmap) {
//		Bitmap gradientBitmap = getGradientBitmap(bitmap);
//		mMapImageView.setImageBitmap(gradientBitmap);
//		
//	}
//
//	private Bitmap getGradientBitmap(Bitmap bitmap) {
////		    Canvas bigCanvas = new Canvas(bitmap);
//		    Bitmap b = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
//		    Canvas c = new Canvas(b);
//		    c.drawColor(Color.TRANSPARENT);
//		    LinearGradient grad = new LinearGradient(bitmap.getWidth()/2, 0, bitmap.getWidth()/2, 20, Color.WHITE, 0X00FFFFFF, TileMode.CLAMP);
//		    Paint p = new Paint();
//		    p.setStyle(Paint.Style.FILL);
//		    p.setShader(grad);
//		    c.drawRect(0, 0, bitmap.getWidth(), 20, p);
//		    bigCanvas.drawBitmap(bitmap, 0, 0, null);
//		    bigCanvas.drawBitmap(b,0,0, null);
//		    return bitmap;
//	}
	
}
