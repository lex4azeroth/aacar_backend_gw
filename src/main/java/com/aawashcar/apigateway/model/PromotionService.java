package com.aawashcar.apigateway.model;

public class PromotionService {

	private int serviceId;
	private int count;
	private int consumedCount;
	private int remainingCount;
	private String serviceName;
	private String serviceCode;

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

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public int getConsumedCount() {
		return consumedCount;
	}

	public void setConsumedCount(int consumedCount) {
		this.consumedCount = consumedCount;
	}

	public int getRemainingCount() {
		return remainingCount;
	}

	public void setRemainingCount(int remainingCount) {
		this.remainingCount = remainingCount;
	}

}
