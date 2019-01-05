package com.aawashcar.apigateway.entity;

import java.sql.Timestamp;

public class Order {
	private int id;
	private Timestamp createdTime;
	private String orderNumber;
	private int userId;
	private String serviceId;
	private int vehicleId;
	private String detailLocation;
	private Timestamp bookTime;
	private Timestamp orderTime;
	private Timestamp completedTime;
	private int statusCode;
	private String status;
	private String remarks;
	private double price;
	private boolean invoiceStatus;
	private double discountedPrice;
//	private int provinceId;
//	private int countyId;
//	private int cityId;
//	private int districtId;
//	private int resiQuartId;
	private int locationId;
	private int couponId;
	private int promotionId;
	private int operatorId;
	private String capabilityType;
	private String storeId;
	private String orderPhoneNumber;
	
	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public int getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getDetailLocation() {
		return detailLocation;
	}

	public void setDetailLocation(String detailLocation) {
		this.detailLocation = detailLocation;
	}

	public Timestamp getBookTime() {
		return bookTime;
	}

	public void setBookTime(Timestamp bookTime) {
		this.bookTime = bookTime;
	}

	public Timestamp getCompletedTime() {
		return completedTime;
	}

	public void setCompletedTime(Timestamp completedTime) {
		this.completedTime = completedTime;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public boolean isInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(boolean invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public double getDiscountedPrice() {
		return discountedPrice;
	}

	public void setDiscountedPrice(double discountedPrice) {
		this.discountedPrice = discountedPrice;
	}

//	public int getProvinceId() {
//		return provinceId;
//	}
//
//	public void setProvinceId(int provinceId) {
//		this.provinceId = provinceId;
//	}
//
//	public int getCountyId() {
//		return countyId;
//	}
//
//	public void setCountyId(int countyId) {
//		this.countyId = countyId;
//	}
//
//	public int getCityId() {
//		return cityId;
//	}
//
//	public void setCityId(int cityId) {
//		this.cityId = cityId;
//	}
//
//	public int getDistrictId() {
//		return districtId;
//	}
//
//	public void setDistrictId(int districtId) {
//		this.districtId = districtId;
//	}
//
//	public int getResiQuartId() {
//		return resiQuartId;
//	}
//
//	public void setResiQuartId(int resiQuartId) {
//		this.resiQuartId = resiQuartId;
//	}

	public Timestamp getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Timestamp orderTime) {
		this.orderTime = orderTime;
	}

	public int getLocationId() {
		return locationId;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	public int getCouponId() {
		return couponId;
	}

	public void setCouponId(int couponId) {
		this.couponId = couponId;
	}

	public int getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(int promotionId) {
		this.promotionId = promotionId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public int getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(int operatorId) {
		this.operatorId = operatorId;
	}

	public String getCapabilityType() {
		return capabilityType;
	}

	public void setCapabilityType(String capabilityType) {
		this.capabilityType = capabilityType;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getOrderPhoneNumber() {
		return orderPhoneNumber;
	}

	public void setOrderPhoneNumber(String orderPhoneNumber) {
		this.orderPhoneNumber = orderPhoneNumber;
	}
}
