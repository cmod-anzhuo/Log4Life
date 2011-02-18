package com.menethil.logmylife.service;

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

public class WorkService implements IBaseService {
	@SuppressWarnings("unused")
	private Context context = null;

	public WorkService(Context appContext) {
		// TODO Auto-generated constructor stub
		context = appContext;
	}

	public Object getData(int page) {
		return null;
	}

	@SuppressWarnings("unchecked")
	public Object CallWebService(ATaskMark taskMark) {

		WebServiceTaskMark webServiceTaskMark = (WebServiceTaskMark) taskMark;

		String SOAP_ACTION = webServiceTaskMark.getNameSpace()
				+ webServiceTaskMark.getMethodName();
		SoapObject request = new SoapObject(webServiceTaskMark.getNameSpace(),
				webServiceTaskMark.getMethodName());
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);

		envelope.dotNet = true; // .net 支持

		if (webServiceTaskMark.getParams() != null
				&& !webServiceTaskMark.getParams().isEmpty()) {
			for (Iterator it = webServiceTaskMark.getParams().entrySet()
					.iterator(); it.hasNext();) {
				Map.Entry e = (Entry) it.next();
				request.addProperty(e.getKey().toString(), e.getValue());
			}
		}

		envelope.bodyOut = request;
		AndroidHttpTransport androidHttpTrandsport = new AndroidHttpTransport(
				webServiceTaskMark.getUrl());

		Object temp;

		try {
			androidHttpTrandsport.call(SOAP_ACTION, envelope);
			temp = envelope.getResponse();
		} catch (Exception ex) {
			temp = null;
		}
		return temp;
	}
}
