package com.menethil.logmylife.task.mark;

import java.util.LinkedHashMap;

public class FlyInfoTaskMark extends WebServiceTaskMark {
	private final String URL = "http://webservice.webxml.com.cn/webservices/DomesticAirline.asmx";
	private final String NAME_SPACE = "http://WebXml.com.cn/";
	private final String METHOD_NAME = "getDomesticAirlinesTime";

	/**
	 * 航班任务标记
	 * 
	 * @param params
	 *            请注意必须为 LinkedHashMap<String, Object> 参数列表
	 */
	public FlyInfoTaskMark(LinkedHashMap<String, Object> params) {
		super.setMethodName(METHOD_NAME);
		super.setNameSpace(NAME_SPACE);
		super.setUrl(URL);
		super.setParams(params);
	}
}
