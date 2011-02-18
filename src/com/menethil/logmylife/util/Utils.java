package com.menethil.logmylife.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

public class Utils {
	private static final String TAG = "Utils";

	private static SimpleDateFormat DateFormat = new SimpleDateFormat(
			"yyyy-MM-dd");

	public static String getFormatDateString(int year, int month, int day) {
		try {
			Date mDate = new Date();
			mDate.setYear(year);
			mDate.setMonth(month + 1);
			mDate.setDate(day);

			return DateFormat.format(mDate);
		} catch (Exception ex) {
			Log.e(TAG, ex.getMessage());
		}
		return null;
	}
}
