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

import java.util.List;

import android.content.Context;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parworks.androidlibrary.response.SiteComment;
import com.parworks.mars.R;

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
		Linkify.addLinks(commentText, Linkify.ALL);
		
		TextView commenterName = (TextView) convertView.findViewById(R.id.textViewCommenterName);
		commenterName.setText(siteComment.getUserName());
						
		return convertView;
		
		
	}

}
