package com.menethil.logmylife.task.mark;

import java.util.LinkedHashMap;

public class QQOnlineTaskMark extends WebServiceTaskMark {
	private final String URL = "http://webservice.webxml.com.cn/webservices/qqOnlineWebService.asmx";
	private final String NAME_SPACE = "http://WebXml.com.cn/";
	private final String METHOD_NAME = "qqCheckOnline";

	public QQOnlineTaskMark(LinkedHashMap<String, Object> params) {
		super.setMethodName(METHOD_NAME);
		super.setNameSpace(NAME_SPACE);
		super.setUrl(URL);
		super.setParams(params);
	}
}
