package com.parworks.mars.account;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.parworks.mars.Test;

public class AccountAuthenticator extends AbstractAccountAuthenticator {
	
	private Context mContext;

	public AccountAuthenticator(Context context) {
		super(context);
		mContext = context;
	}
	   	
	@Override
	public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, 
			String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
System.out.println("GOD EXEC");
		final Bundle result;  
		final Intent intent;  

		intent = new Intent(this.mContext, Test.class);
		//intent.setAction("com.example.testaccountmanager.LOGIN");
		//intent.putExtra(Constants.AUTHTOKEN_TYPE, authTokenType);  
		intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);  

		result = new Bundle();  
		result.putParcelable(AccountManager.KEY_INTENT, intent);  

		return result;
	}

	@Override
	public Bundle confirmCredentials(AccountAuthenticatorResponse arg0,
			Account arg1, Bundle arg2) throws NetworkErrorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle editProperties(AccountAuthenticatorResponse arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle getAuthToken(AccountAuthenticatorResponse arg0, Account arg1,
			String arg2, Bundle arg3) throws NetworkErrorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAuthTokenLabel(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle hasFeatures(AccountAuthenticatorResponse arg0, Account arg1,
			String[] arg2) throws NetworkErrorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle updateCredentials(AccountAuthenticatorResponse arg0,
			Account arg1, String arg2, Bundle arg3)
					throws NetworkErrorException {
		// TODO Auto-generated method stub
		return null;
	}
}
