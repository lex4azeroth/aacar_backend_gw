package com.aawashcar.apigateway.util;

public class ServiceUtil {
	
	public static String[] getServiceIDs(String serviceIds) {
		return serviceIds.split(",");
	}

}
