package com.aawashcar.apigateway.model;

import java.sql.Timestamp;

public class MyPromotionModel extends PromotionModel {
	private String validatedTime;

	public String getValidatedTime() {
		return validatedTime;
	}

	public void setValidatedTime(String validatedTime) {
		this.validatedTime = validatedTime;
	}
}
