package com.aawashcar.apigateway.util;

/**
 * 正数50内为需要继续处理状态： 支付 -> 10 ; 待派单 -> 20; 进行中 -> 30; 退款中 -> 50; 负数为完成情况状态: 已取消 -> -1; 已完成 -> -10; 0
 * -> 问题订单
 */
public enum OrderStatusCode {
								PAYED(10),
								TO_BE_DISPATCHED(20),
								IN_PROGRESS(30),
								REFOUNDING(50),
								CANCELLED(-1),
								DONE(-10),
								ISSUE(0);

	private final int status;

	private OrderStatusCode(int status) {
		this.status = status;
	}

	public int getStatus() {
		return this.status;
	}

	public static String getStatusName(int status) {
		switch (status) {
			case 10:
				return OrderStatusCode.PAYED.name();
			case 20:
				return OrderStatusCode.TO_BE_DISPATCHED.name();
			case 30:
				return OrderStatusCode.IN_PROGRESS.name();
			case 50:
				return OrderStatusCode.REFOUNDING.name();
			case -1:
				return OrderStatusCode.CANCELLED.name();
			case -10:
				return OrderStatusCode.DONE.name();
			case 0:
			default:
				return OrderStatusCode.ISSUE.name();
		}
	}
}
