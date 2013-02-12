package com.parworks.mars.view.trending;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

public class PosterImageView extends ImageView {

	public PosterImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public PosterImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PosterImageView(Context context) {
		super(context);
	}
	
	@Override  
    protected void onDraw(Canvas canvas) {
		Path clipPath = new Path();
	    float radius = 10.0f;
	    float padding = radius / 2;
	    int w = this.getWidth();
	    int h = this.getHeight();
	    clipPath.addRoundRect(new RectF(padding, padding, w - padding, h - padding), radius, radius, Path.Direction.CW);
	    canvas.clipPath(clipPath);
        super.onDraw(canvas);  
    }  
}
