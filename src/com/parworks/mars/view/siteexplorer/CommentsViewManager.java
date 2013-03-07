package com.parworks.mars.view.siteexplorer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
import com.parworks.androidlibrary.response.SiteComment;
import com.parworks.mars.R;
import com.parworks.mars.model.db.CommentsTable;
import com.parworks.mars.utils.Utilities;

public class CommentsViewManager {
	public static final String TAG = CommentsViewManager.class.getName();
	
	private final String mSiteId;
	private final Context mContext;
	private final ProgressBar mCommentsProgressBar;
	private final LinearLayout mCommentsLayout;
	private final CommentsAdapter mCommentsAdapter;
	private final TextView mCommentsTotalTextView;
	private final List<SiteComment> mComments;
	
	private int mCommentCount;
	
	//TODO make this a function of screen size
	private static final int PROFILE_PICTURE_WIDTH = 100;
	private static final int PROFILE_PICTURE_HEIGHT = 100;
	
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
		Log.d(Utilities.DEBUG_TAG_SYNC,"setCommentsView called: " + data.getCount());
		mCommentsTotalTextView.setText("Comments");
		mCommentCount = data.getCount();
		mCommentsLayout.removeAllViews();
		
		if(mCommentCount > 0)
			showCommentsView();		
		else
			hideCommentsView();
		
		for(data.moveToFirst();!data.isAfterLast();data.moveToNext()) {
			String commentText = data.getString(data.getColumnIndex(CommentsTable.COLUMN_COMMENT));
			String userName = data.getString(data.getColumnIndex(CommentsTable.COLUMN_USER_NAME));
			String userId = data.getString(data.getColumnIndex(CommentsTable.COLUMN_USER_ID));
			String siteId = data.getString(data.getColumnIndex(CommentsTable.COLUMN_SITE_ID));
			long timeStamp = data.getLong(data.getColumnIndex(CommentsTable.COLUMN_TIMESTAMP));
			SiteComment comment = new SiteComment(siteId, timeStamp, userId, userName, commentText);
			addComment(comment, data.getPosition());

//			showCommentsView();
//			mCommentsTotalTextView.setText("Comments");
//			setCommentTotalText(data.getCount());
		}
		
	}
	
	private void showCommentsView() {
		mCommentsProgressBar.setVisibility(View.INVISIBLE);
		mCommentsLayout.setVisibility(View.VISIBLE);
	}
	
	private void hideCommentsView() {		
		mCommentsProgressBar.setVisibility(View.INVISIBLE);
		mCommentsLayout.setVisibility(View.GONE);
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
	
	private void addComment(SiteComment siteComment, int position) {
		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService
			      (Context.LAYOUT_INFLATER_SERVICE);
		View commentView = inflater.inflate(R.layout.comment_layout,mCommentsLayout,false);
		
		TextView commentText = (TextView) commentView.findViewById(R.id.textViewCommentText);
		commentText.setText(siteComment.getComment());
		Linkify.addLinks(commentText, Linkify.ALL);
		
		TextView commenterName = (TextView) commentView.findViewById(R.id.textViewCommenterName);
		commenterName.setText(siteComment.getUserName());
		
		TextView commentTimestamp = (TextView) commentView.findViewById(R.id.textViewCommentTimestamp);
		commentTimestamp.setText(DateFormat.getDateTimeInstance(
	            DateFormat.MEDIUM, DateFormat.SHORT).format(new Date(siteComment.getTimeStamp())));
		
		ProfilePictureView profilePicture = (ProfilePictureView) commentView.findViewById(R.id.profilePictureViewComment);
		setProfilePictureSize(Utilities.getDensityPixels(26, mContext), profilePicture);
		Log.d(TAG,"Setting profile picture id " + siteComment.getUserId());
		profilePicture.setProfileId(siteComment.getUserId());
		
		ImageView bottomLineImageView = (ImageView) commentView.findViewById(R.id.comment_layout_bottomline);
		if(position == (mCommentCount - 1))
			bottomLineImageView.setVisibility(View.VISIBLE);
		else
			bottomLineImageView.setVisibility(View.GONE);						
		
		mCommentsLayout.addView(commentView);
	}	
	
    private void setProfilePictureSize(int i, ProfilePictureView profilePic) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(i,i);
        profilePic.setLayoutParams(params);
    }
}
