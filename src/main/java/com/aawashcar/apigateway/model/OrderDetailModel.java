package com.aawashcar.apigateway.model;

import java.util.List;

public class OrderDetailModel {
	private int orderId;
	private String orderNumber;
	private String serviceName;
	private String serviceId;
	private String bookTime;
	private String completedTime;
	private String createdTime;
	private VehicleTypeModel vehicleType;
	private VehicleCategoryModel vehicleCategory;
	private String color;
	private String license;
	private String address;
	private String status;
	private int statusCode;
	private DefaultAddressModel defalutAddress;
	private String fullAddress;
	private String remarks;
	private List<MyCouponModel> coupons;
	private List<PromotionWithServicesModel> promotions;
	private double price;
	private double discountedPrice;
	private int operatorId;
	private LocationModel location;
	private String capabilityType;
	private String storeId;
	private String orderPhoneNumber;
	
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

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getBookTime() {
		return bookTime;
	}

	public void setBookTime(String bookTime) {
		this.bookTime = bookTime;
	}

	public VehicleTypeModel getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(VehicleTypeModel vehicleType) {
		this.vehicleType = vehicleType;
	}

	public VehicleCategoryModel getVehicleCategory() {
		return vehicleCategory;
	}

	public void setVehicleCategory(VehicleCategoryModel vehicleCategory) {
		this.vehicleCategory = vehicleCategory;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String adress) {
		this.address = adress;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public DefaultAddressModel getDefalutAddress() {
		return defalutAddress;
	}

	public void setDefalutAddress(DefaultAddressModel defalutAddress) {
		this.defalutAddress = defalutAddress;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public List<MyCouponModel> getCoupons() {
		return coupons;
	}

	public void setCoupons(List<MyCouponModel> coupons) {
		this.coupons = coupons;
	}

	public List<PromotionWithServicesModel> getPromotions() {
		return promotions;
	}

	public void setPromotions(List<PromotionWithServicesModel> promotions) {
		this.promotions = promotions;
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

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getCompletedTime() {
		return completedTime;
	}

	public void setCompletedTime(String completedTime) {
		this.completedTime = completedTime;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public int getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(int operatorId) {
		this.operatorId = operatorId;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getFullAddress() {
		return fullAddress;
	}

	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}

	public LocationModel getLocation() {
		return location;
	}

	public void setLocation(LocationModel location) {
		this.location = location;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getCapabilityType() {
		return capabilityType;
	}

	public void setCapabilityType(String capabilityType) {
		this.capabilityType = capabilityType;
	}

	public String getOrderPhoneNumber() {
		return orderPhoneNumber;
	}

	public void setOrderPhoneNumber(String orderPhoneNumber) {
		this.orderPhoneNumber = orderPhoneNumber;
	}
}
