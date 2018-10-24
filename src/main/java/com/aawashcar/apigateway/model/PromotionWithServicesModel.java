package com.aawashcar.apigateway.model;

import java.util.List;

public class PromotionWithServicesModel {

	private int id;
	private String name;
	private double price;
	private String description;
	private int duration;
	private List<PromotionService> services; 

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public List<PromotionService> getServices() {
		return services;
	}

	public void setServices(List<PromotionService> services) {
		this.services = services;
	}

}
