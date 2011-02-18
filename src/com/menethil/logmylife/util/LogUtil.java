package com.menethil.logmylife.util;

import android.util.Log;

public class LogUtil {

	// debug
	public static final boolean debug = true;

	// debug tracker
	public static final boolean DEBUG_TRACKER = true;
	// ���ܸ���
	public static final boolean DEBUG_PER = true;

	public static void i(String tag, String msg) {
		if (debug) {
			Log.i(tag, msg);
		}
	}

	public static void v(String tag, String msg) {
		if (debug) {
			Log.v(tag, msg);
		}
	}

	public static void w(String tag, String msg) {
		if (debug) {
			Log.w(tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (debug) {
			Log.e(tag, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (debug) {
			Log.d(tag, msg);
		}
	}

	public static void iop(String tag, String string) {
		// TODO Auto-generated method stub

	}

}
