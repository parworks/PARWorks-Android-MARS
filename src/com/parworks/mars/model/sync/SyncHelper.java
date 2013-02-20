package com.parworks.mars.model.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

import com.parworks.mars.model.provider.MarsContentProvider;

/**
 * Use {@link SyncHandler} instead
 * 
 * @author yusun
 */
@Deprecated
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
			ContentResolver.setIsSyncable(account, MarsContentProvider.AUTHORITY, 1);
			ContentResolver.setSyncAutomatically(account, MarsContentProvider.AUTHORITY, true);
		}
	}
	
	/**
	 * Trigger the sync for all the info that is not associated
	 * with a context, such as trending sites, suggested tags.
	 */
	public static void sync() {
		ContentResolver.requestSync(account, MarsContentProvider.AUTHORITY, new Bundle());
	}
}
