package com.menethil.logmylife.tracker;

import org.ksoap2.serialization.SoapObject;
import com.cooler.schema.task.IResultReceiver;
import com.cooler.schema.task.OperateResult;
import com.cooler.schema.task.mark.ATaskMark;
import com.cooler.schema.task.tracker.AInvokeTracker;
import com.menethil.logmylife.task.mark.FlyInfoTaskMark;
import com.menethil.logmylife.task.mark.IPAdressTaskMark;
import com.menethil.logmylife.task.mark.QQOnlineTaskMark;
import com.menethil.logmylife.util.TransSoapObject;

public class WebServerTracker extends AInvokeTracker {

	public WebServerTracker(IResultReceiver resultReceiver) {
		super(resultReceiver);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String TAG() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void handleResult(OperateResult result) {
		// TODO Auto-generated method stub
		if (result.getResultData() != null) {
			ATaskMark task = result.getTaskMark();

			if (task instanceof FlyInfoTaskMark) {
				trackerResult = TransSoapObject.getFlyInfo((SoapObject) result
						.getResultData());
			} else if (task instanceof QQOnlineTaskMark) {
				trackerResult = result.getResultData();
			} else if (task instanceof IPAdressTaskMark) {
				trackerResult = TransSoapObject
						.getIPAddress((SoapObject) result.getResultData());
			}
		}
	}
}
