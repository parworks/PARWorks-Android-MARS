package com.parworks.mars.view.siteexplorer;

import java.util.List;

import com.parworks.androidlibrary.response.SiteComment;
import com.parworks.mars.R;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommentsAdapter extends BaseAdapter {
	
	private final List<SiteComment> mComments;
	private final Context mContext;
	
	public CommentsAdapter(Context context, List<SiteComment> comments) {
		mComments = comments;
		mContext = context;
	}

	@Override
	public int getCount() {
		return mComments.size();
	}

	@Override
	public Object getItem(int position) {
		return mComments.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			LayoutInflater inflater = (LayoutInflater)mContext.getSystemService
				      (Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.comment_layout,parent,false);
		}
		SiteComment siteComment = mComments.get(position);
		
		TextView commentText = (TextView) convertView.findViewById(R.id.textViewCommentText);
		commentText.setText(siteComment.getComment());
		
		TextView commenterName = (TextView) convertView.findViewById(R.id.textViewCommenterName);
		commenterName.setText(siteComment.getUserName());
		
		return convertView;
		
		
	}

}
