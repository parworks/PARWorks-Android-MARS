package com.parworks.mars.view.trending;

import com.parworks.mars.utils.Utilities;

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
	
	private Context mContext;
	
	private String displayName;
	private int numAugmentedImages;
	
	private Paint mTextPaint;

	public ShingleBoard(Context context) {
		super(context);
		mContext = context;
		initTextPaint();
	}

	public ShingleBoard(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initTextPaint();
	}

	public ShingleBoard(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initTextPaint();
	}
	
	private void initTextPaint() {
		mTextPaint = new Paint();
		mTextPaint.setColor(Color.WHITE);
		mTextPaint.setStyle(Style.FILL);
		mTextPaint.setTextSize(Utilities.getDensityPixels(21, mContext));
		mTextPaint.setTextAlign(Align.CENTER);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {	
		super.onDraw(canvas);
		
		int width = this.getWidth();
		int height = this.getHeight();
		
		if (displayName != null && !TextUtils.isEmpty(displayName)) {			
			// draw site name			
			mTextPaint.setTextSize(Utilities.getDensityPixels(21, mContext));
			canvas.drawText(displayName, width/2, height/2 + Utilities.getDensityPixels(17, mContext), mTextPaint);
			
			// draw site num augmented images
			mTextPaint.setTextSize(Utilities.getDensityPixels(12, mContext));			
//			mTextPaint.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "Avenir Book.ttf"));
			canvas.drawText(numAugmentedImages + " Augmented Images", width/2, height/2 + Utilities.getDensityPixels(33, mContext), mTextPaint);			
		}
	}
	
	public void updateText(String name, int num) {
		this.displayName = name;
		this.numAugmentedImages = num;
		this.invalidate();
	}
}
