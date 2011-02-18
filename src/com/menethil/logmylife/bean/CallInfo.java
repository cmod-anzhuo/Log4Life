package com.menethil.logmylife.bean;

import com.menethil.logmylife.bean.Type.CallType;

/**
 * 来电记录消息体
 * 
 * @author menethil
 * 
 */
public class CallInfo extends BaseInfo {
	private String phoneNumber;
	private String myNumber;
	private long duration;
	private CallType type;

	public CallInfo() {

	}

	public CallInfo(String phoneNumber, String myNumber, long dateTime,
			long duration, CallType type) {
		this.phoneNumber = phoneNumber;
		this.myNumber = myNumber;
		this.duration = duration;
		this.type = type;
		setDateTime(dateTime);
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getMyNumber() {
		return myNumber;
	}

	public void setMyNumber(String myNumber) {
		this.myNumber = myNumber;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public CallType getType() {
		return type;
	}

	public void setType(CallType type) {
		this.type = type;
	}

}
