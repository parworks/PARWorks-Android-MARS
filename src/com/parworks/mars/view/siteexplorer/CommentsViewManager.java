package com.parworks.mars.view.siteexplorer;

import java.util.ArrayList;
import java.util.List;

import com.parworks.androidlibrary.response.SiteComment;
import com.parworks.mars.model.db.CommentsTable;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CommentsViewManager {
	public static final String TAG = CommentsViewManager.class.getName();
	
	private final String mSiteId;
	private final Context mContext;
	private final ProgressBar mCommentsProgressBar;
	private final ListView mCommentsListView;
	private final CommentsAdapter mCommentsAdapter;
	private final TextView mCommentsTotalTextView;
	private final List<SiteComment> mComments;
	
	public CommentsViewManager(String siteId, Context context, ProgressBar commentsProgressBar, ListView commentsListView,TextView commentsTotalTextView) {
		mSiteId = siteId;
		mContext = context;
		mCommentsProgressBar = commentsProgressBar;
		mCommentsListView = commentsListView;
		mComments = new ArrayList<SiteComment>();
		mCommentsAdapter = new CommentsAdapter(context,mComments);
		mCommentsListView.setAdapter(mCommentsAdapter);
		mCommentsTotalTextView = commentsTotalTextView;
	}
	public void setCommentsView(Cursor data) {
		for(data.moveToFirst();!data.isAfterLast();data.moveToNext()) {
			String commentText = data.getString(data.getColumnIndex(CommentsTable.COLUMN_COMMENT));
			String userName = data.getString(data.getColumnIndex(CommentsTable.COLUMN_USER_NAME));
			String userId = data.getString(data.getColumnIndex(CommentsTable.COLUMN_USER_ID));
			String siteId = data.getString(data.getColumnIndex(CommentsTable.COLUMN_SITE_ID));
			long timeStamp = data.getLong(data.getColumnIndex(CommentsTable.COLUMN_TIMESTAMP));
			SiteComment comment = new SiteComment(siteId, timeStamp, userId, userName, commentText);
			mComments.add(comment);
		}
		mCommentsAdapter.notifyDataSetChanged();
		showCommentsView();
		setCommentTotalText(data.getCount());
		data.close();
		
	}
	
	private void showCommentsView() {
		mCommentsProgressBar.setVisibility(View.INVISIBLE);
		mCommentsListView.setVisibility(View.VISIBLE);
	}
	private void setCommentTotalText(int commentTotal) {
		String commentTotalText;
		if(commentTotal == 1) {
			commentTotalText = " comment";
		} else {
			commentTotalText = " comments";
		}
		mCommentsTotalTextView.setText(commentTotal + commentTotalText);
	}
	
	

}
