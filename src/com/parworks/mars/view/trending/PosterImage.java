package com.parworks.mars.view.trending;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class PosterImage extends Drawable {

	Bitmap bm;
	
	public PosterImage(Bitmap bitmap) {
		super();
		bm = bitmap;
	}

	@Override
	public void draw(Canvas canvas) {
		Paint mShadow = new Paint();
	    Rect rect = new Rect(0,0,bm.getWidth(), bm.getHeight());

	    mShadow.setAntiAlias(true);
	    mShadow.setShadowLayer(5.5f, 4.0f, 4.0f, Color.BLACK);

	    canvas.drawRect(rect, mShadow);
	    canvas.drawBitmap(bm, 0.0f, 0.0f, null);
	}

	@Override
	public int getOpacity() {	
		return 0;
	}

	@Override
	public void setAlpha(int arg0) {

	}

	@Override
	public void setColorFilter(ColorFilter arg0) {

	}
}
