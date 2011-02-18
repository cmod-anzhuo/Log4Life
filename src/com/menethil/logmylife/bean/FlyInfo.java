package com.menethil.logmylife.bean;

public class FlyInfo {
	private String fromDrome;
	private String arriveDrome;
	private String company;
	private String startTime;
	private String arriveTime;
	private String airlineCode;
	private String mode;
	private String week;

	public String getFromDrome() {
		return fromDrome;
	}

	public void setFromDrome(String fromDrome) {
		this.fromDrome = fromDrome;
	}

	public String getArriveDrome() {
		return arriveDrome;
	}

	public void setArriveDrome(String arriveDrome) {
		this.arriveDrome = arriveDrome;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getArriveTime() {
		return arriveTime;
	}

	public void setArriveTime(String arriveTime) {
		this.arriveTime = arriveTime;
	}

	public String getAirlineCode() {
		return airlineCode;
	}

	public void setAirlineCode(String airlineCode) {
		this.airlineCode = airlineCode;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public String toString() {
		return fromDrome + "," + arriveDrome + "," + company + "," + startTime
				+ "," + airlineCode;
	}
}
