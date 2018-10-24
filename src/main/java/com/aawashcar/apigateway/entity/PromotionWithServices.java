package com.aawashcar.apigateway.entity;

public class PromotionWithServices extends BaseInfoEntity {
	private boolean enabled;
	private double price;
	private int duration;
	private int serviceId;
	private int count;
	private int consumedCount;
	private int remainingCount;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getRemainingCount() {
		return remainingCount;
	}

	public void setRemainingCount(int remainingCount) {
		this.remainingCount = remainingCount;
	}

	public int getConsumedCount() {
		return consumedCount;
	}

	public void setConsumedCount(int consumedCount) {
		this.consumedCount = consumedCount;
	}

}
