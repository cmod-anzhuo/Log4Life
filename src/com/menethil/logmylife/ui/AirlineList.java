package com.menethil.logmylife.ui;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.cooler.schema.service.ActionException;
import com.cooler.schema.task.IResultReceiver;
import com.cooler.schema.task.mark.ATaskMark;
import com.menethil.logmylife.R;
import com.menethil.logmylife.bean.FlyInfo;
import com.menethil.logmylife.main.BaseContext;
import com.menethil.logmylife.task.WorkServiceWraper;
import com.menethil.logmylife.task.mark.FlyInfoTaskMark;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public class AirlineList extends ListActivity implements IResultReceiver {
	private ArrayList<FlyInfo> flyInfos = null;
	private MyAdapter myAdapter = null;
	private WorkServiceWraper service;
	ProgressDialog progressDialog = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		service = BaseContext.getInstance().getServiceWarper();
		myAdapter = new MyAdapter();
		setupListView(getIntent());
	}

	private void setupListView(Intent intent) {
		LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
		try {
			params.put("userID", "");
			params.put("theDate", intent.getStringExtra("theDate"));
			params.put("lastCity", intent.getStringExtra("lastCity"));
			params.put("startCity", intent.getStringExtra("startCity"));
		} catch (Exception ex) {
			ex.printStackTrace();
			finish();
		}

		final ATaskMark flyInfoTaskMark = new FlyInfoTaskMark(params);
		service.CallWebService(this, flyInfoTaskMark, null);

		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle(R.string.searching);
		progressDialog.setMessage(getString(R.string.pleanse_wait));
		progressDialog.setIndeterminate(true);
		progressDialog.setButton(getString(android.R.string.cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int i) {
						progressDialog.cancel();
					}
				});
		progressDialog
				.setOnCancelListener(new DialogInterface.OnCancelListener() {
					public void onCancel(DialogInterface dialog) {
						service.forceDiscardReceiveTask(flyInfoTaskMark);
						finish();
					}
				});
		progressDialog.show();
	}

	@SuppressWarnings("unchecked")
	public void receiveResult(ATaskMark arg0, ActionException arg1, Object arg2) {
		// TODO Auto-generated method stub
		progressDialog.dismiss();

		flyInfos = (ArrayList<FlyInfo>) arg2;
		setListAdapter(myAdapter);

		myAdapter.notifyDataSetChanged();
	}

	private class MyAdapter extends BaseAdapter implements ListAdapter {

		public int getCount() {
			return flyInfos.size();
		}

		public FlyInfo getItem(int arg0) {

			return flyInfos.get(arg0);
		}

		public long getItemId(int arg0) {
			return arg0;
		}

		public View getView(int arg0, View arg1, ViewGroup arg2) {

			if (arg1 == null) {
				LayoutInflater li = getLayoutInflater();
				arg1 = li.inflate(R.layout.list_item, null);
			}

			String timeString = String.format(getString(R.string.time_string),
					flyInfos.get(arg0).getStartTime(), flyInfos.get(arg0)
							.getArriveTime());
			String dromeString = String.format(
					getString(R.string.drome_string), flyInfos.get(arg0)
							.getFromDrome(), flyInfos.get(arg0)
							.getArriveDrome());
			String airlineString = flyInfos.get(arg0).getAirlineCode();
			String airModeString = String.format(
					getString(R.string.mode_string), flyInfos.get(arg0)
							.getMode());
			String airCompanyString = flyInfos.get(arg0).getCompany();

			TextView tvAirCode = (TextView) arg1.findViewById(R.id.AirCode);
			TextView tvAirDrome = (TextView) arg1.findViewById(R.id.AirDrome);
			TextView tvAirTime = (TextView) arg1.findViewById(R.id.AirTime);
			TextView tvAirMode = (TextView) arg1.findViewById(R.id.AirMode);
			TextView tvAirCompany = (TextView) arg1
					.findViewById(R.id.AirCompany);

			tvAirCode.setText(airlineString);
			tvAirDrome.setText(dromeString);
			tvAirTime.setText(timeString);
			tvAirMode.setText(airModeString);
			tvAirCompany.setText(airCompanyString);

			return arg1;
		}
	}
}