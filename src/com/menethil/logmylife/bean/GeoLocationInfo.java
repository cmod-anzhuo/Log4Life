package com.menethil.logmylife.bean;

/**
 * 地理信息位置消息体
 * 
 * @author menethil
 * 
 */
public class GeoLocationInfo extends BaseInfo {
	private String location;

	public GeoLocationInfo(String location, long dateTime) {
		this.location = location;
		setDateTime(dateTime);
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
