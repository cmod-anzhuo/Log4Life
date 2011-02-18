package com.menethil.logmylife.util;

import java.util.ArrayList;

import org.ksoap2.serialization.SoapObject;

import com.menethil.logmylife.bean.FlyInfo;

public class TransSoapObject {

	@SuppressWarnings("unchecked")
	public static ArrayList getFlyInfo(SoapObject so) {
		SoapObject obj = (SoapObject) so.getProperty(1); // get the DataSet
		obj = (SoapObject) obj.getProperty(0);
		int count = obj.getPropertyCount(); // get Record Count

		if (count > 0) {
			ArrayList infos = new ArrayList();

			for (int i = 0; i < count; i++) {

				SoapObject obj2 = (SoapObject) obj.getProperty(i);

				FlyInfo flyInfo = new FlyInfo();

				flyInfo.setAirlineCode(obj2.getProperty("AirlineCode")
						.toString());
				flyInfo.setArriveDrome(obj2.getProperty("ArriveDrome")
						.toString());
				flyInfo
						.setArriveTime(obj2.getProperty("ArriveTime")
								.toString());
				flyInfo.setCompany(obj2.getProperty("Company").toString());
				flyInfo.setFromDrome(obj2.getProperty("StartDrome").toString());
				flyInfo.setMode(obj2.getProperty("Mode").toString());
				flyInfo.setStartTime(obj2.getProperty("StartTime").toString());
				flyInfo.setWeek(obj2.getProperty("Week").toString());

				infos.add(flyInfo);
			}

			return infos;
		}
		return null;
	}

	public static String getQQInfo(SoapObject so) {
		return so.toString();
	}

	@SuppressWarnings("unchecked")
	public static ArrayList getIPAddress(SoapObject so) {
		ArrayList list = new ArrayList();
		list.add(so.getProperty(0).toString());
		list.add(so.getProperty(1).toString());
		return list;
	}
}
