package com.parworks.mars.view.siteexplorer;

import java.util.ArrayList;
import java.util.List;

import com.parworks.androidlibrary.response.SiteComment;
import com.parworks.mars.R;
import com.parworks.mars.model.db.CommentsTable;
import com.parworks.mars.model.sync.SyncAdapterService;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class CommentsViewManager {
	public static final String TAG = CommentsViewManager.class.getName();
	
	private final String mSiteId;
	private final Context mContext;
	private final ProgressBar mCommentsProgressBar;
	private final LinearLayout mCommentsLayout;
	private final CommentsAdapter mCommentsAdapter;
	private final TextView mCommentsTotalTextView;
	private final List<SiteComment> mComments;
	
	public CommentsViewManager(String siteId, Context context, ProgressBar commentsProgressBar, LinearLayout commentsLayout,TextView commentsTotalTextView) {
		mSiteId = siteId;
		mContext = context;
		mCommentsProgressBar = commentsProgressBar;
		mCommentsLayout = commentsLayout;
		mComments = new ArrayList<SiteComment>();
		mCommentsAdapter = new CommentsAdapter(context,mComments);
		mCommentsTotalTextView = commentsTotalTextView;
	}
	public void setCommentsView(Cursor data) {
		Log.d(SyncAdapterService.TAG,"setCommentsView called: " + data.getCount());
		for(data.moveToFirst();!data.isAfterLast();data.moveToNext()) {
			String commentText = data.getString(data.getColumnIndex(CommentsTable.COLUMN_COMMENT));
			String userName = data.getString(data.getColumnIndex(CommentsTable.COLUMN_USER_NAME));
			String userId = data.getString(data.getColumnIndex(CommentsTable.COLUMN_USER_ID));
			String siteId = data.getString(data.getColumnIndex(CommentsTable.COLUMN_SITE_ID));
			long timeStamp = data.getLong(data.getColumnIndex(CommentsTable.COLUMN_TIMESTAMP));
			SiteComment comment = new SiteComment(siteId, timeStamp, userId, userName, commentText);
			addComment(comment);

			showCommentsView();
			setCommentTotalText(data.getCount());
		}
		data.close();
		
	}
	
	private void showCommentsView() {
		mCommentsProgressBar.setVisibility(View.INVISIBLE);
		mCommentsLayout.setVisibility(View.VISIBLE);
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
	private void addComment(SiteComment siteComment) {
		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService
			      (Context.LAYOUT_INFLATER_SERVICE);
		View commentView = inflater.inflate(R.layout.comment_layout,mCommentsLayout,false);
		
		TextView commentText = (TextView) commentView.findViewById(R.id.textViewCommentText);
		commentText.setText(siteComment.getComment());
		
		TextView commenterName = (TextView) commentView.findViewById(R.id.textViewCommenterName);
		commenterName.setText(siteComment.getUserName());
		mCommentsLayout.addView(commentView);
	}
	
	

}
