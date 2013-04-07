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
package com.parworks.mars;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainMenuListFragment extends Fragment {
	
	private MarsMainActivity parentActivity;
	private ListView lv;
	private MainMenuListAdapter adapter;
	// store the currently selected menu item
	private View currentedSelectedView;
	// store if this is first time start
	private boolean firstTimeStartup = true;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// store the activity reference
		parentActivity = (MarsMainActivity)(this.getActivity());
		
		// init menu view
		View view = inflater.inflate(R.layout.sliding_menu, null);
		
		// init the main menu options with an adapter
		lv = (ListView) view.findViewById(R.id.menu_list);
		adapter = new MainMenuListAdapter(getActivity());
		adapter.add(new MainMenuItem(getString(R.string.menu_trending), 
				R.drawable.ic_menu_trending));
		adapter.add(new MainMenuItem(getString(R.string.menu_nearby), 
				R.drawable.ic_menu_nearby));
		adapter.add(new MainMenuItem(getString(R.string.menu_search), 
				R.drawable.ic_menu_search));
		adapter.add(new MainMenuItem(getString(R.string.menu_technology), 
				R.drawable.ic_menu_technology));		
		lv.setAdapter(adapter);
		
		// config menu item click control
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {								
				switch (position) {
				case 0:
					parentActivity.switchContent(MarsMainActivity.FRAGMENT_TRENDING);
					break;
				case 1:
					parentActivity.switchContent(MarsMainActivity.FRAGMENT_NEARBY);
					break;
				case 2:
					parentActivity.switchContent(MarsMainActivity.FRAGMENT_SEARCH);
					break;
				case 3:
					parentActivity.switchContent(MarsMainActivity.FRAGMENT_TECHNOLOGY);
					break;
				}
				
				if (firstTimeStartup) {// first time  highlight first row
					currentedSelectedView = lv.getChildAt(0);
			    }
			    firstTimeStartup = false; 
				// make the right highlight for the background color
				if (currentedSelectedView != null) {
					currentedSelectedView.setBackgroundResource(R.color.menu_background_color);
				}
				view.setBackgroundResource(R.color.menu_background_select_color);				
				currentedSelectedView = view;
			}
		});

		return view;
	}	

	private class MainMenuItem {
		public String tag;
		public int iconRes;
		public MainMenuItem(String tag, int iconRes) {
			this.tag = tag; 
			this.iconRes = iconRes;
		}
	}

	public class MainMenuListAdapter extends ArrayAdapter<MainMenuItem> {

		public MainMenuListAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.sliding_menu_list_row, null);
			}
			
			if (firstTimeStartup && position == 0) {
				convertView.setBackgroundResource(R.color.menu_background_select_color);
	        }
			
			ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
			icon.setImageResource(getItem(position).iconRes);
			TextView title = (TextView) convertView.findViewById(R.id.row_title);
			title.setText(getItem(position).tag);
			
			return convertView;
		}
	}
}
