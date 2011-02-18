package com.menethil.logmylife.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

import com.cooler.schema.task.mark.ATaskMark;
import com.menethil.logmylife.task.mark.WebServiceTaskMark;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

@SuppressWarnings("deprecation")
public class WorkService implements IBaseService {
	private Context context = null;

	public WorkService(Context appContext) {
		// TODO Auto-generated constructor stub
		context = appContext;
	}

	/**
	 * 获取位置，可以通过GPS或者NETWORK
	 * 
	 * @param count
	 * @return
	 */
	public HashMap<String, String> getLocation(int count) {
		final HashMap<String, String> rs = new HashMap<String, String>();

		final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		Location loc = null;
		String provider = LocationManager.GPS_PROVIDER;

		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
				&& !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			// GPS 和 网络定位均关闭
			rs.put("flag", "0");

			return rs;
		}

		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			provider = LocationManager.NETWORK_PROVIDER;
		}

		int i = 0;
		while (i++ < count) {
			loc = locationManager.getLastKnownLocation(provider);
			if (i <= 1) {
				locationManager.requestLocationUpdates(provider, 60000, 0, locationListener);
			}

			if (null != loc) {
				Long currentTime = System.currentTimeMillis();

				Long lastGpsTime = loc.getTime();

				if (provider == LocationManager.GPS_PROVIDER && currentTime - lastGpsTime > 5 * 60 * 1000) {
					loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					if (i <= 1) {
						locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 0,
								locationListener);
					}
				}
			} else {
				if (provider != LocationManager.NETWORK_PROVIDER) {
					loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					if (i <= 1) {
						locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 0,
								locationListener);
					}
				}
			}

			if (loc != null) {
				rs.put("lat", Integer.toString((int) (loc.getLatitude() * 1000000)));
				rs.put("lng", Integer.toString((int) (loc.getLongitude() * 1000000)));
				break;
			} else {
				// 无法获取位置信息
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				rs.put("flag", "1");
			}
		}
		return rs;
	}

	/**
	 * 调用网络服务
	 * 
	 * @param taskMark
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Object CallWebService(ATaskMark taskMark) {

		WebServiceTaskMark webServiceTaskMark = (WebServiceTaskMark) taskMark;

		String SOAP_ACTION = webServiceTaskMark.getNameSpace() + webServiceTaskMark.getMethodName();
		SoapObject request = new SoapObject(webServiceTaskMark.getNameSpace(), webServiceTaskMark.getMethodName());
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

		envelope.dotNet = true; // .net 支持

		if (webServiceTaskMark.getParams() != null && !webServiceTaskMark.getParams().isEmpty()) {
			for (Iterator it = webServiceTaskMark.getParams().entrySet().iterator(); it.hasNext();) {
				Map.Entry e = (Entry) it.next();
				request.addProperty(e.getKey().toString(), e.getValue());
			}
		}

		envelope.bodyOut = request;
		AndroidHttpTransport androidHttpTrandsport = new AndroidHttpTransport(webServiceTaskMark.getUrl());

		Object temp;

		try {
			androidHttpTrandsport.call(SOAP_ACTION, envelope);
			temp = envelope.getResponse();
		} catch (Exception ex) {
			temp = null;
		}
		return temp;
	}

	/**
	 * 位置监听器
	 */
	private static final LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			// 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
		}

		public void onProviderDisabled(String provider) {
			// Provider被disable时触发此函数，比如GPS被关闭
		}

		public void onProviderEnabled(String provider) {
			// Provider被enable时触发此函数，比如GPS被打开
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};
}
