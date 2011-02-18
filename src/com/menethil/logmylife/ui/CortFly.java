package com.menethil.logmylife.ui;

import java.util.Calendar;
import com.menethil.logmylife.R;
import com.menethil.logmylife.util.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.DatePicker.OnDateChangedListener;

public class CortFly extends Activity {
	private static final String PREFS_NAME = "cort_fly";

	private static final String PREFS_START_DROME = "startCity";

	private static final String PREFS_ARRIVE_DROME = "lastCity";

	Button btnGet, btnexit;

	EditText editStartCity, editLastCity;

	DatePicker dpDate;

	String mStartCity;

	String mLastCity;

	String mTheDate;

	Calendar c;

	SharedPreferences sp;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_flyinfo);

		dpDate = (DatePicker) findViewById(R.id.DatePicker01);
		btnGet = (Button) findViewById(R.id.BtnLogin);
		btnexit = (Button) findViewById(R.id.BtnExit);
		editStartCity = (EditText) findViewById(R.id.EditTextUser);
		editLastCity = (EditText) findViewById(R.id.EditTextPassWord);

		sp = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		editStartCity.setText(sp.getString(PREFS_START_DROME, ""));
		editLastCity.setText(sp.getString(PREFS_ARRIVE_DROME, ""));

		c = Calendar.getInstance();
		dpDate.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
				.get(Calendar.DAY_OF_MONTH), new OnDateChangedListener() {
			@Override
			public void onDateChanged(DatePicker view, int year,
					int monthOfYear, int dayOfMonth) {
				// TODO Auto-generated method stub
				mTheDate = Utils.getFormatDateString(year, monthOfYear,
						dayOfMonth);
			}
		});

		btnexit.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		btnGet.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mStartCity = editStartCity.getText().toString();
				mLastCity = editLastCity.getText().toString();

				ResponseOnClickLogin(mStartCity, mLastCity, mTheDate);
			}
		});
	}

	public void ResponseOnClickLogin(String startCity, String lastCity,
			String theDate) {
		Editor editor = sp.edit();
		editor.putString(PREFS_START_DROME, mStartCity);
		editor.putString(PREFS_ARRIVE_DROME, mLastCity);
		editor.commit();

		Intent intent = new Intent();

		intent.setClass(this, AirlineList.class);
		intent.putExtra("theDate", theDate);
		intent.putExtra("lastCity", lastCity);
		intent.putExtra("startCity", startCity);

		startActivity(intent);
		finish();
	}
}