package com.aawashcar.apigateway.model;

public class LatestOrder {
	private int id;
	private String orderNumber;
	private String serviceId;
	private double price; 
	private String storeId;
	private String capType;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getCapType() {
		return capType;
	}
	public void setCapType(String capType) {
		this.capType = capType;
	}

}
