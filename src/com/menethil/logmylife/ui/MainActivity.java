package com.menethil.logmylife.ui;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.cooler.schema.service.ActionException;
import com.cooler.schema.task.IResultReceiver;
import com.cooler.schema.task.mark.ATaskMark;
import com.menethil.logmylife.R;
import com.menethil.logmylife.main.BaseContext;
import com.menethil.logmylife.main.MarkableHandler;
import com.menethil.logmylife.task.mark.IPAdressTaskMark;
import com.menethil.logmylife.task.mark.QQOnlineTaskMark;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.widget.Toast;

public class MainActivity extends PreferenceActivity implements IResultReceiver {
	private BaseContext baseContext = null;
	private Preference mAirline;
	private Preference mQQOnline;
	private Preference mIPAddress;
	ProgressDialog progressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.layout.mainframe);

		baseContext = BaseContext.getInstance();
		baseContext.initBaseContext(this);
		baseContext.registerSubHandler(new ResultHandler(hashCode()));

		mAirline = (Preference) findPreference("airline");
		mQQOnline = (Preference) findPreference("qq_online");
		mIPAddress = (Preference) findPreference("ip_address");

		mQQOnline.setOnPreferenceChangeListener(mOnPreferenceChangeListener);
		mIPAddress.setOnPreferenceChangeListener(mOnPreferenceChangeListener);
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		// TODO Auto-generated method stub
		if (preference == mAirline) {
			Intent intent = new Intent();
			intent.setClass(this, CortFly.class);
			startActivity(intent);
		}
		return true;
	}

	private OnPreferenceChangeListener mOnPreferenceChangeListener = new OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			// TODO Auto-generated method stub
			progressDialog = new ProgressDialog(MainActivity.this);
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

						}
					});
			progressDialog.show();

			if (preference == mQQOnline) {
				quaryQQOnline((String) newValue);
				return true;
			} else if (preference == mIPAddress) {
				quaryIPAddress((String) newValue);
				return true;
			}
			return false;
		}
	};

	private class ResultHandler extends MarkableHandler {

		public ResultHandler(int messageMark) {
			super(messageMark);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			String message = null;

			if (isFinishing()) {
				return;
			}

			switch (msg.what) {
			case BaseContext.M_QQONLINE:
				if ("E".equals(msg.obj.toString())) {
					message = "所查询的QQ号码不存在!";
				}

				if ("Y".equals(msg.obj.toString())) {
					message = "QQ当前在线!";
				} else {
					message = "QQ离线状态!";
				}

				if (message != null) {
					Toast.makeText(BaseContext.getInstance().getAppContext(),
							message, 3).show();
				}
				break;
			case BaseContext.M_IPADDRESS:
				message = msg.obj.toString();
				Toast.makeText(BaseContext.getInstance().getAppContext(),
						message, 3).show();
				break;
			default:
				break;
			}
		}
	}

	private void quaryQQOnline(String newValue) {
		LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("qqCode", newValue);
		ATaskMark qqTaskMark = new QQOnlineTaskMark(params);
		BaseContext.getInstance().getServiceWarper().CallWebService(this,
				qqTaskMark, null);
	}

	private void quaryIPAddress(String newValue) {
		LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("theIpAddress", newValue);
		ATaskMark ipTaskMark = new IPAdressTaskMark(params, false);
		BaseContext.getInstance().getServiceWarper().CallWebService(this,
				ipTaskMark, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void receiveResult(ATaskMark taskMark, ActionException exception,
			Object trackerResult) {
		// TODO Auto-generated method stub
		progressDialog.dismiss();

		Message message = Message.obtain();
		if (taskMark instanceof IPAdressTaskMark) {
			message.what = BaseContext.M_IPADDRESS;
			message.obj = ((ArrayList) trackerResult).get(0) + "\r\n"
					+ ((ArrayList) trackerResult).get(1);
		} else if (taskMark instanceof QQOnlineTaskMark) {
			message.what = BaseContext.M_QQONLINE;
			message.obj = trackerResult.toString();
		}

		BaseContext.getInstance().handleMarketMessage(message);
	}
}
