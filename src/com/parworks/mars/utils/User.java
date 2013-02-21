package com.parworks.mars.utils;

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
}
