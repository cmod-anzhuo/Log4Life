package com.menethil.logmylife.task.mark;

import java.util.LinkedHashMap;

import com.cooler.schema.task.mark.ATaskMark;

public class WebServiceTaskMark extends ATaskMark {
	private String url;
	private String nameSpace;
	private String methodName;
	private LinkedHashMap<String, Object> params;

	@SuppressWarnings("unused")
	private int hashCode;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public void setParams(LinkedHashMap<String, Object> params) {
		this.params = params;
	}

	public LinkedHashMap<String, Object> getParams() {
		return params;
	}
}
