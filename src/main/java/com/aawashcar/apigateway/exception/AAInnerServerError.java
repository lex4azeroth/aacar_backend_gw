package com.aawashcar.apigateway.exception;

public class AAInnerServerError extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5389170742459653469L;
	
	private String innerMessage;
	
	public AAInnerServerError(String message) {
		super(message);
	}
	
	public String getInnerMessage() {
		return innerMessage;
	}
}
