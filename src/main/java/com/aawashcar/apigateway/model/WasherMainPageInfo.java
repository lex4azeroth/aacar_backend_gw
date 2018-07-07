package com.aawashcar.apigateway.model;

public class WasherMainPageInfo {
	
	private boolean isWasher;
	private WasherInfo washerInfo;
	private AssignedOrder assignedOrder;
	public boolean isWasher() {
		return isWasher;
	}
	public void setWasher(boolean isWasher) {
		this.isWasher = isWasher;
	}
	public WasherInfo getWasherInfo() {
		return washerInfo;
	}
	public void setWasherInfo(WasherInfo washerInfo) {
		this.washerInfo = washerInfo;
	}
	public AssignedOrder getAssignedOrder() {
		return assignedOrder;
	}
	public void setAssignedOrder(AssignedOrder assignedOrder) {
		this.assignedOrder = assignedOrder;
	}
}
