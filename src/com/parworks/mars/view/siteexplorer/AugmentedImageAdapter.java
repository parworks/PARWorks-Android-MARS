package com.parworks.mars.view.siteexplorer;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class AugmentedImageAdapter extends BaseAdapter {
	
	private Context mContext;
	private List<Bitmap> mBitmaps;
	
	public AugmentedImageAdapter(Context context, List<Bitmap> bitmaps) {
		mContext = context;
		mBitmaps = bitmaps;
	}

	@Override
	public int getCount() {
		return mBitmaps.size();
	}

	@Override
	public Object getItem(int position) {
		return mBitmaps.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
//            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageBitmap(mBitmaps.get(position));
        return imageView;
	}

}
