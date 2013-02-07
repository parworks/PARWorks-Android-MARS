package com.parworks.mars.account;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthenticatationService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		return new AccountAuthenticator(this).getIBinder(); 
	}
}
