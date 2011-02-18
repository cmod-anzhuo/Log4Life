package com.menethil.logmylife.bean;

import com.menethil.logmylife.bean.Type.SmsType;

/**
 * 短信消息体
 * 
 * @author menethil
 * 
 */
public class SmsInfo extends BaseInfo {
	private String phoneNumber;
	private String message;
	private SmsType type;
	private boolean read;

	public SmsInfo(String phoneNumber, String message, long dateTime,
			SmsType type, boolean read) {
		this.phoneNumber = phoneNumber;
		this.message = message;
		this.type = type;
		this.read = read;
		setDateTime(dateTime);
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public SmsType getType() {
		return type;
	}

	public void setType(SmsType type) {
		this.type = type;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

}
