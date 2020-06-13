package com.diyo.activity.desktop.util;

import java.util.IllegalFormatConversionException;
import java.util.MissingFormatArgumentException;

public class Log {
	public static final String TAG = "==VST==";
	private static final boolean DEBUG = true;

	public static void i(String msg, Object... args) {
		try {
			if (DEBUG)
				android.util.Log.i(TAG, String.format(msg, args));
		} catch (MissingFormatArgumentException e) {
			android.util.Log.e(TAG, "vitamio.Log", e);
			android.util.Log.i(TAG, msg);
		}catch (Exception e3){

        }
	}

	public static void d(String msg, Object... args) {
		try {
			if (DEBUG)
				android.util.Log.d(TAG, String.format(msg, args));
		} catch (MissingFormatArgumentException e) {
			android.util.Log.e(TAG, "vitamio.Log", e);
			android.util.Log.d(TAG, msg);
		}catch (Exception e3){

        }
	}

	public static void e(String msg, Object... args) {
		try {
			if (DEBUG) {
                	android.util.Log.d(TAG, String.format(msg, args));
            }
		} catch (MissingFormatArgumentException e) {
			android.util.Log.e(TAG, "vitamio.Log", e);
			android.util.Log.e(TAG, msg);
		}catch (IllegalFormatConversionException e2){

        }catch (Exception e3){

        }
	}
	public static void v(String msg, Object... args) {
		try {
			if (DEBUG)
				android.util.Log.v(TAG, String.format(msg, args));
		} catch (MissingFormatArgumentException e) {
			android.util.Log.v(TAG, "vitamio.Log", e);
			android.util.Log.v(TAG, msg);
		}catch (Exception e3){

        }
	}
//	public static void e(String msg, String args) {
//		try {
//				android.util.Log.e(TAG, msg + args);
//		} catch (MissingFormatArgumentException e) {
//			android.util.Log.e(TAG, "vitamio.Log", e);
//			android.util.Log.e(TAG, msg);
//		}
//	}

	public static void e(String msg, Throwable t) {
		android.util.Log.e(TAG, msg, t);
	}
}