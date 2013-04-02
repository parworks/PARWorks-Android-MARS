/*******************************************************************************
 * Copyright 2013 PAR Works, Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.parworks.mars.view.siteexplorer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parworks.mars.R;
import com.parworks.mars.utils.User;

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
						
		mUserId = User.getUserId(mContext);
		mUserName = User.getUserName(mContext);
		
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
