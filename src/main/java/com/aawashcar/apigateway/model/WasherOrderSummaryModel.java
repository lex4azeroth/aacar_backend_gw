package com.aawashcar.apigateway.model;

import java.sql.Timestamp;
import java.util.List;

public class WasherOrderSummaryModel {
	private int id;
	private int workerId;
	private Timestamp timestamp;
	private int orderId;
	private String orderNumber;
	private int status;
	private String detailLocation;
	private String locationRemarks;
	private List<String> services;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getWorkerId() {
		return workerId;
	}

	public void setWorkerId(int workerId) {
		this.workerId = workerId;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDetailLocation() {
		return detailLocation;
	}

	public void setDetailLocation(String detailLocation) {
		this.detailLocation = detailLocation;
	}

	public String getLocationRemarks() {
		return locationRemarks;
	}

	public void setLocationRemarks(String locationRemarks) {
		this.locationRemarks = locationRemarks;
	}

	public List<String> getServices() {
		return services;
	}

	public void setServices(List<String> services) {
		this.services = services;
	}
}
