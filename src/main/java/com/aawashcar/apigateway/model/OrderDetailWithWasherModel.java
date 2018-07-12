package com.aawashcar.apigateway.model;

import com.aawashcar.apigateway.entity.Worker;

public class OrderDetailWithWasherModel extends OrderDetailModel {
	private Worker washerInfo;

	public Worker getWasherInfo() {
		return washerInfo;
	}

	public void setWasherInfo(Worker washerInfo) {
		this.washerInfo = washerInfo;
	}

}
