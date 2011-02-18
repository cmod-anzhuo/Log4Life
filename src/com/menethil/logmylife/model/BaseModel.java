package com.menethil.logmylife.model;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class BaseModel {
	private static BaseModel baseModel;
	private ArrayList infos; // 包含请求的数据集合

	private BaseModel() {

	}

	/**
	 * 返回一个 BaseModel 实例
	 * 
	 * @return
	 */
	public static synchronized BaseModel getInstance() {
		if (baseModel == null) {
			baseModel = new BaseModel();
		}
		return baseModel;
	}

	public void addInfo(Object info) {
		infos.add(info);
	}

	public void removeInfo(Object info) {
		infos.remove(info);
	}

	public ArrayList getInfos() {
		return infos;
	}

	public void setInfos(ArrayList infos) {
		this.infos = infos;
	}
}
