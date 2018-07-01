package com.aawashcar.apigateway.model;

public class CouponModel extends BaseModel {
	private String description;
	private int duration;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

}
