package com.aawashcar.apigateway.model;

public class WechatPayResponseModel extends WechatPayResponse {
	private String timeStamp;

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

}
