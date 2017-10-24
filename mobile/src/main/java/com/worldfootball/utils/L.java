package com.worldfootball.utils;

import android.util.Log;

public class L {
	public static final String TAG = "TAG";

	public static void i(String message) {
		Log.i(TAG, message);
	}

	public static void i(Class c, String message) {
		Log.i(TAG, "{" + c.getSimpleName() + "} : " + message);
	}

	public static void e(String message) {
		Log.e(TAG, message);
	}

	public static void e(Class c, String message) {
		Log.e(TAG, "{" + c.getSimpleName() + "} : " + message);
	}

	public static void d(String message) {
		Log.d(TAG, message);
	}

	public static void d(Class c, String message) {
		Log.d(TAG, "{" + c.getSimpleName() + "} : " + message);
	}
}
