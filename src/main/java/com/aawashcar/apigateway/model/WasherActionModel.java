package com.aawashcar.apigateway.model;

public class WasherActionModel {

	private String validId;
	private int orderId;
	private int remarkId;
	private String orderNumber;

	public String getValidId() {
		return validId;
	}

	public void setValidId(String validId) {
		this.validId = validId;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getRemarkId() {
		return remarkId;
	}

	public void setRemarkId(int remarkId) {
		this.remarkId = remarkId;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

}
