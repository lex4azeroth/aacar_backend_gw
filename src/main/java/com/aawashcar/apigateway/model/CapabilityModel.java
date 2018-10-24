package com.aawashcar.apigateway.model;

public class CapabilityModel extends BaseModel {

	private String serviceId;
	private String name;
	private String description;
	private String cleanningPart;
	private String timeConsuming;
	private String consumables;
	private String standard;
	private String iconUrl;
	private boolean isChoosen;
	private double price;
	private int categoryId;
	private String categoryName;
	private String categoryUrl;

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCleanningPart() {
		return cleanningPart;
	}

	public void setCleanningPart(String cleanningPart) {
		this.cleanningPart = cleanningPart;
	}

	public String getTimeConsuming() {
		return timeConsuming;
	}

	public void setTimeConsuming(String timeConsuming) {
		this.timeConsuming = timeConsuming;
	}

	public String getConsumables() {
		return consumables;
	}

	public void setConsumables(String consumables) {
		this.consumables = consumables;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public boolean isChoosen() {
		return isChoosen;
	}

	public void setChoosen(boolean isChoosen) {
		this.isChoosen = isChoosen;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryUrl() {
		return categoryUrl;
	}

	public void setCategoryUrl(String categoryUrl) {
		this.categoryUrl = categoryUrl;
	}
}
