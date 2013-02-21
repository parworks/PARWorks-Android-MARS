package com.parworks.mars.view.trending;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ShingleBoard extends ImageView {
	
	private String displayName;
	private int numAugmentedImages;
	
	private Paint mTextPaint;

	public ShingleBoard(Context context) {
		super(context);
		initTextPaint();
	}

	public ShingleBoard(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initTextPaint();
	}

	public ShingleBoard(Context context, AttributeSet attrs) {
		super(context, attrs);
		initTextPaint();
	}
	
	private void initTextPaint() {
		mTextPaint = new Paint();
		mTextPaint.setColor(Color.WHITE);
		mTextPaint.setStyle(Style.FILL);
		mTextPaint.setTextSize(40);
		mTextPaint.setTextAlign(Align.CENTER);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {	
		super.onDraw(canvas);
		
		int width = this.getWidth();
		int height = this.getHeight();
		
		if (displayName != null && !TextUtils.isEmpty(displayName)) {			
			// draw site name			
			mTextPaint.setTextSize(40);
			canvas.drawText(displayName, width/2, height/2, mTextPaint);
			
			// draw site num augmented images
			mTextPaint.setTextSize(30);
			canvas.drawText(numAugmentedImages + " Augmented Images", width/2, height/2 + 40, mTextPaint);			
		}
	}
	
	public void updateText(String name, int num) {
		this.displayName = name;
		this.numAugmentedImages = num;
		this.invalidate();
	}
}
