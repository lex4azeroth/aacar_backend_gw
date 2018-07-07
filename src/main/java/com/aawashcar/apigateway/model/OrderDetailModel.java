package com.aawashcar.apigateway.model;

import java.sql.Timestamp;
import java.util.List;

public class OrderDetailModel {
	private int orderId;
	private String orderNumber;
	private String serviceName;
	private Timestamp bookTime;
	private VehicleTypeModel vehicleType;
	private VehicleCategoryModel vehicleCategory;
	private String color;
	private String adress;
	private String status;
	private DefaultAddressModel defalutAddress;
	private String remarks;
	private List<MyCouponModel> coupons;
	private List<MyPromotionModel> promotions;

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

	public Timestamp getBookTime() {
		return bookTime;
	}

	public void setBookTime(Timestamp bookTime) {
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

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
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

	public List<MyPromotionModel> getPromotions() {
		return promotions;
	}

	public void setPromotions(List<MyPromotionModel> promotions) {
		this.promotions = promotions;
	}
}
