package com.aawashcar.apigateway.model;

import java.sql.Timestamp;

public class MyPromotionModel extends PromotionModel {
	private Timestamp validatedTime;

	public Timestamp getValidatedTime() {
		return validatedTime;
	}

	public void setValidatedTime(Timestamp validatedTime) {
		this.validatedTime = validatedTime;
	}
}
