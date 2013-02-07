package com.parworks.mars.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

import com.parworks.mars.model.providers.AbstractMarsContentProvider;
import com.parworks.mars.model.providers.TrendingSitesContentProvider;

public class MarsAccountHelper {

	public static final String ACCOUNT_NAME = "MARS";
	public static final String ACCOUNT_TYPE = "com.parworks.mars.account";
	public static final String PROVIDER = "com.parworks.mars";

	private static final Account account = new Account(ACCOUNT_NAME, ACCOUNT_TYPE);

	public static void initAppAccount(Context contenxt) {

		AccountManager accMgr = AccountManager.get(contenxt);

		if (accMgr.addAccountExplicitly(account, null, null)) {
			ContentResolver.setIsSyncable(account, AbstractMarsContentProvider.AUTHORITY, 1);
			//			ContentResolver.setIsSyncable(account, SitesContentProvider.AUTHORITY, 1);
			ContentResolver.setSyncAutomatically(account, AbstractMarsContentProvider.AUTHORITY, true);
			//			ContentResolver.setSyncAutomatically(account, SitesContentProvider.AUTHORITY, true);
			//ContentResolver.addPeriodicSync(account, TrendingSitesContentProvider.AUTHORITY, new Bundle(), 20);			
		}
	}

	public static void sync() {
		ContentResolver.requestSync(account, TrendingSitesContentProvider.AUTHORITY, new Bundle());
	}
	
	public static void sync(String siteId) {
		Bundle bundle = new Bundle();
		bundle.putString("siteId", siteId);
		ContentResolver.requestSync(account, TrendingSitesContentProvider.AUTHORITY, bundle);
	}
}
