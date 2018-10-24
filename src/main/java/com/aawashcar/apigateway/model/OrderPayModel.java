package com.aawashcar.apigateway.model;

import java.math.BigDecimal;

public class OrderPayModel {
	private String validId;
	private double price;
	private double discountedPrice;
	private int vehicleType;
	private int vehicleCategory;
	private String color;
	private String serviceId;
	private String bookTime;
	private String orderTime;
	private String detailLocation;
	private String phoneNumber;
	private String license;
	private String addressRemark;
	private BigDecimal longitude;
	private BigDecimal latitude;
	private String capabilityType;
	private String storeId;

	public String getValidId() {
		return validId;
	}

	public void setValidId(String validId) {
		this.validId = validId;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getDiscountedPrice() {
		return discountedPrice;
	}

	public void setDiscountedPrice(double discountedPrice) {
		this.discountedPrice = discountedPrice;
	}

	public int getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(int vehicleType) {
		this.vehicleType = vehicleType;
	}

	public int getVehicleCategory() {
		return vehicleCategory;
	}

	public void setVehicleCategory(int vehicleCategory) {
		this.vehicleCategory = vehicleCategory;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getBookTime() {
		return bookTime;
	}

	public void setBookTime(String bookTime) {
		this.bookTime = bookTime;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getDetailLocation() {
		return detailLocation;
	}

	public void setDetailLocation(String detailLocation) {
		this.detailLocation = detailLocation;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getAddressRemark() {
		return addressRemark;
	}

	public void setAddressRemark(String addressRemark) {
		this.addressRemark = addressRemark;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
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

}
