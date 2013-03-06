package com.parworks.mars;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {
	private long splashDelay = 1500;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		TimerTask task = new TimerTask() {
			@Override
			public void run() {				
				Intent intent = new Intent(SplashActivity.this,
						MarsMainActivity.class);
				startActivity(intent);
				finish();
			}
		};

		Timer timer = new Timer();
		timer.schedule(task, splashDelay);
	}
}