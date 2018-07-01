package com.aawashcar.apigateway.entity;

public class Coupon extends BaseInfoEntity {
	private int couponType;
	private double value;
	private boolean enabled;
	private int duration;

	public int getCouponType() {
		return couponType;
	}

	public void setCouponType(int couponType) {
		this.couponType = couponType;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

}
