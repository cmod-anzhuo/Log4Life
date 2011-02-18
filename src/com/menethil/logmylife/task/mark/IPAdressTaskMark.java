package com.menethil.logmylife.task.mark;

import java.util.LinkedHashMap;

public class IPAdressTaskMark extends WebServiceTaskMark {
	private static final String URL = "http://webservice.webxml.com.cn/WebServices/IpAddressSearchWebService.asmx";
	private static final String NAME_SPACE = "http://WebXml.com.cn/";
	private static final String METHOD_NAME = "getCountryCityByIp";
	private static final String METHODNAME_GEO = "getGeoIPContext";

	public IPAdressTaskMark(LinkedHashMap<String, Object> params, boolean geo) {
		if (!geo) {
			super.setMethodName(METHOD_NAME);
		} else {
			super.setMethodName(METHODNAME_GEO);
		}
		super.setNameSpace(NAME_SPACE);
		super.setUrl(URL);
		super.setParams(params);
	}
}
