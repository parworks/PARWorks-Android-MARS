package com.parworks.mars.view.siteexplorer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.parworks.mars.R;

public class LeaveCommentActivity extends Activity {

	private Context mContext;
	
	private EditText mCommentEditText;
	
	private String mUserId;
	private String mUserName;
	
	private String mSiteId;
	
	private AddCommentManager mAddCommentManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leave_comment);
		mContext = this;
						
		mUserId = getIntent().getStringExtra("userId");
		mUserName = getIntent().getStringExtra("userName");
		
		mSiteId = getIntent().getStringExtra("siteId");
		
		mCommentEditText = (EditText)findViewById(R.id.comment_edittext);
		
		mAddCommentManager = new AddCommentManager(this, mSiteId);
	}
	
	@Override
	protected void onResume() {	
		super.onResume();
		
	}

	public void commentCancelClicked(View v) {
		finish();
	}

	public void commentPostClicked(View v) {
		if(mCommentEditText.getEditableText().toString().length() > 0){
			mAddCommentManager.addComment(mUserId, mUserName, mCommentEditText.getEditableText().toString());
			finish();
		}
		else
			Toast.makeText(mContext, "Please enter a comment to post.", Toast.LENGTH_SHORT).show();
	}

}
