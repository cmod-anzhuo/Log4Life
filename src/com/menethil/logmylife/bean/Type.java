package com.menethil.logmylife.bean;

/**
 * 类别枚举
 * 
 * @author menethil
 * 
 */
public class Type {
	public enum CallType {
		Incoming, Outgoing, Missed
	}

	public enum SmsType {
		In, Out
	}

	public enum InfoType {
		Sms, GeoLocation, Call, Base
	}
}
