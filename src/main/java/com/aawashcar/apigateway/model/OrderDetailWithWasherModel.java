package com.aawashcar.apigateway.model;

import com.aawashcar.apigateway.entity.User;
import com.aawashcar.apigateway.entity.WashCarService;
import com.aawashcar.apigateway.entity.Worker;

public class OrderDetailWithWasherModel extends OrderDetailModel {
	private Worker washerInfo;
	private WashCarService washCarService;
	private User user;
	

	public Worker getWasherInfo() {
		return washerInfo;
	}

	public void setWasherInfo(Worker washerInfo) {
		this.washerInfo = washerInfo;
	}

	public WashCarService getWashCarService() {
		return washCarService;
	}

	public void setWashCarService(WashCarService washCarService) {
		this.washCarService = washCarService;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
