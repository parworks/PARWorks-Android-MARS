package com.parworks.mars.view.siteexplorer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Display;

public class ViewDimensionCalculator {
	
	private Activity mActivity;
	public ViewDimensionCalculator(Activity activity) {
		mActivity = activity;
	}
	public int getScreenWidth() {
		Display display = mActivity.getWindowManager().getDefaultDisplay();
//		Point size = new Point();
//		display.getSize(size);
//		int width = size.x;
		return display.getWidth();
	}
	
    public static int calculateHeightToMaintainAspectRatio(Integer width,Bitmap bitmap) {
		float startingWidth = bitmap.getWidth();
		float startingHeight = bitmap.getHeight();
		float widthHeightRatio = startingWidth/startingHeight;
		return Math.round(width / widthHeightRatio);
	}

}
