package com.aawashcar.apigateway.model;

import java.util.List;

public class CapabilityWithCategoryModel {
	private int categoryId;
	private String categoryName;
	private String categoryUrl;
	private List<CapabilityModel> capabilites;

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

	public List<CapabilityModel> getCapabilites() {
		return capabilites;
	}

	public void setCapabilites(List<CapabilityModel> capabilites) {
		this.capabilites = capabilites;
	}

}
