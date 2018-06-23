package com.aawashcar.apigateway.entity;

import java.sql.Timestamp;

public class BaseEntity {
	private int id;
	private Timestamp createdTime;

	public int getId() {
		return id;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}
}
