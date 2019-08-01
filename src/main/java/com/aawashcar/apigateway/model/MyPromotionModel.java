package com.aawashcar.apigateway.model;

import java.sql.Timestamp;

public class MyPromotionModel extends PromotionModel {
	private String validatedTime;
	private int serviceId;
	private int categoryId;

	public String getValidatedTime() {
		return validatedTime;
	}

	public void setValidatedTime(String validatedTime) {
		this.validatedTime = validatedTime;
	}

	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
}
