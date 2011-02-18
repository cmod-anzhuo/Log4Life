package com.menethil.logmylife.bean;

/**
 * 基本信息消息体
 * 
 * @author menethil
 * 
 */
public class BaseInfo {
	private int id;
	private String imei;
	private String version;
	private String board;
	private long dateTime;

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getBoard() {
		return board;
	}

	public void setBoard(String board) {
		this.board = board;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setDateTime(long dateTime) {
		this.dateTime = dateTime;
	}

	public long getDateTime() {
		return dateTime;
	}

}
