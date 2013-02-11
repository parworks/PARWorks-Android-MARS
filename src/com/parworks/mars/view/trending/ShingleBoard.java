package com.parworks.mars.view.trending;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ShingleBoard extends ImageView {
	
	private Context context;
	private String displayName;
	private int numAugmentedImages;
	
	private Paint mTextPaint;

	public ShingleBoard(Context context) {
		super(context);
		this.context = context;
		initTextPaint();
	}

	public ShingleBoard(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initTextPaint();
	}

	public ShingleBoard(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initTextPaint();
	}
	
	private void initTextPaint() {
		mTextPaint = new Paint();
		mTextPaint.setColor(Color.WHITE);
		mTextPaint.setStyle(Style.FILL);
		mTextPaint.setTextSize(40);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {	
		super.onDraw(canvas);
		if (displayName != null && !TextUtils.isEmpty(displayName)) {			
			// draw site name
			mTextPaint.setTextSize(40);
			canvas.drawText(displayName, 145, 140, mTextPaint);
			
			// draw site num augmented images
			mTextPaint.setTextSize(30);
			canvas.drawText(numAugmentedImages + " Augmented Images", 145, 180, mTextPaint);			
		}
	}
	
	public void updateText(String name, int num) {
		this.displayName = name;
		this.numAugmentedImages = num;
		this.invalidate();
	}
}
