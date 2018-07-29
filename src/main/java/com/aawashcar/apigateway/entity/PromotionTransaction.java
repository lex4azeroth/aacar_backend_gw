package com.aawashcar.apigateway.entity;

import java.sql.Timestamp;

public class PromotionTransaction extends BaseEntity {
	private int promotionId;
	private int userId;
	private double price;
	private boolean enabled;
	private boolean paied;
	private Timestamp modifiedTime;

	public int getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(int promotionId) {
		this.promotionId = promotionId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isPaied() {
		return paied;
	}

	public void setPaied(boolean paied) {
		this.paied = paied;
	}

	public Timestamp getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Timestamp modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

}
