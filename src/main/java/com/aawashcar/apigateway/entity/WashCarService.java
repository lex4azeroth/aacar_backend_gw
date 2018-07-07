package com.aawashcar.apigateway.entity;

public class WashCarService extends BaseInfoEntity {
	
	private boolean enabled;
	private String iconUrl;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
}
