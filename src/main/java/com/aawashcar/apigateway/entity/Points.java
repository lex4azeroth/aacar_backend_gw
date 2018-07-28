package com.aawashcar.apigateway.entity;

import java.sql.Timestamp;

public class Points extends BaseEntity {

	private int userId;
	private double userPoints;
	private double exchangeRate;
	private Timestamp lastModifiedTime;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public double getUserPoints() {
		return userPoints;
	}

	public void setUserPoints(double userPoints) {
		this.userPoints = userPoints;
	}

	public double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public Timestamp getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(Timestamp lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}
}
