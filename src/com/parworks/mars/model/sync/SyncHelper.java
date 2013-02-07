package com.parworks.mars.model.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

import com.parworks.mars.model.provider.SitesContentProvider;

public class SyncHelper {

	public static final String ACCOUNT_NAME = "MARS";
	public static final String ACCOUNT_TYPE = "com.parworks.mars.account";

	private static final Account account = new Account(ACCOUNT_NAME, ACCOUNT_TYPE);

	public static void initAppAccount(Context contenxt) {
		AccountManager accMgr = AccountManager.get(contenxt);
		// once the account is added to Android, the following 
		// call will not succeed anymore
		if (accMgr.addAccountExplicitly(account, null, null)) {
			// config sync service
			ContentResolver.setIsSyncable(account, SitesContentProvider.AUTHORITY, 1);
			ContentResolver.setSyncAutomatically(account, SitesContentProvider.AUTHORITY, true);
		}
	}
	
	public static void syncSiteInfo(String siteId) {
		
	}	
	
	public static void syncSite(String siteId) {
		Bundle bundle = new Bundle();
		bundle.putString("siteId", siteId);
		ContentResolver.requestSync(account, SitesContentProvider.AUTHORITY, bundle);
	}
}
