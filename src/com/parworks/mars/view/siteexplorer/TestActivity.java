package com.parworks.mars.view.siteexplorer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class TestActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Intent intent = new Intent(this, ExploreActivity.class);
		intent.putExtra(ExploreActivity.SITE_ID_ARGUMENT_KEY, "");
		startActivity(intent);
	}

}
