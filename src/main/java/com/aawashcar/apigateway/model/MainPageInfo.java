package com.aawashcar.apigateway.model;

import java.sql.Timestamp;
import java.util.List;

public class MainPageInfo {
	private List<VehicleTypeModel> vehicleTypes;

	private List<VehicleCategoryModel> vehicleCategories;

	private String color;

	private UserModel user;

	private List<ServiceModel> services;

	private String bookedTime;

	private DefaultAddressModel defaultAddress;
	
	private String license;

	public List<VehicleTypeModel> getVehicleTypes() {
		return vehicleTypes;
	}

	public void setVehicleTypes(List<VehicleTypeModel> vehicleTypes) {
		this.vehicleTypes = vehicleTypes;
	}

	public List<VehicleCategoryModel> getVehicleCategories() {
		return vehicleCategories;
	}

	public void setVehicleCategories(List<VehicleCategoryModel> vehicleCategories) {
		this.vehicleCategories = vehicleCategories;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

	public List<ServiceModel> getServices() {
		return services;
	}

	public void setServices(List<ServiceModel> services) {
		this.services = services;
	}

	public String getBookedTime() {
		return bookedTime;
	}

	public void setBookedTime(String bookedTime) {
		this.bookedTime = bookedTime;
	}

	public DefaultAddressModel getDefaultAddress() {
		return defaultAddress;
	}

	public void setDefaultAddress(DefaultAddressModel defaultAddress) {
		this.defaultAddress = defaultAddress;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}
}
