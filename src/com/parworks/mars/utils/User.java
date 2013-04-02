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
package com.parworks.mars.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.parworks.androidlibrary.ar.ARSites;

public class User {
	
	private static String API_KEY = "e6f5c802-de04-40e2-9b98-77c84abeca4e";
	private static String SECRET_KEY = "fa9be0a6-53c8-4bd9-9e2b-24033382ea39";
	
	private static ARSites arSites = new ARSites(API_KEY, SECRET_KEY);	
	
	public static String getApiKey() {
		return API_KEY;
	}
	
	public static String getSecretKey() {
		return SECRET_KEY;
	}	

	public static ARSites getARSites() {
		return arSites;
	}
	
	public static void setHasPerformedFirstLaunch(boolean launched, Context context){
		SharedPreferences.Editor editor = context.getSharedPreferences("FLAGS", 0).edit();
		editor.putBoolean("kDefaultsHasPerformedFirstLaunchKey", launched);
		editor.commit();
	}
	
	public static boolean hasPerformedFirstLaunch(Context context){
		return context.getSharedPreferences("FLAGS", 0).getBoolean("kDefaultsHasPerformedFirstLaunchKey", false);
	}
	
	public static void setUserId(String userId, Context context){
		SharedPreferences.Editor editor = context.getSharedPreferences("USER", 0).edit();
		editor.putString("userId", userId);
		editor.commit();
	}
	
	public static String getUserId(Context context){
		return context.getSharedPreferences("USER", 0).getString("userId", "");
	}
	
	public static void setUserName(String userName, Context context){
		SharedPreferences.Editor editor = context.getSharedPreferences("USER", 0).edit();
		editor.putString("userName", userName);
		editor.commit();
	}
	
	public static String getUserName(Context context){
		return context.getSharedPreferences("USER", 0).getString("userName", "");
	}
}
